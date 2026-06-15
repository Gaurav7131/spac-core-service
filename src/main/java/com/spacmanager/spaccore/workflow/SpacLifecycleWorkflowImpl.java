package com.spacmanager.spaccore.workflow;

import com.spacmanager.spaccore.activity.SpacActivity;
import com.spacmanager.spaccore.entity.Spac;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class SpacLifecycleWorkflowImpl implements SpacLifecycleWorkflow {

    private final ActivityOptions options = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(10))
            .build();

    private final SpacActivity activities = Workflow.newActivityStub(SpacActivity.class, options);

    // state flag for the state machine
    private boolean isIpoComplete = false;
    private String voteDecision = null;// Approve or Reject

    @Override
    public void startSpacLifecycle(Spac spac) {
        // Step 1: Save initial SPAC to DB
        Spac savedSpac = activities.registerNewSpac(spac);

        /*
         * Step 2: Pause workflow execution indefinitely until the IPO signal is
         * received
         * // Workflow.await(() -> isIpoComplete);
         * 
         * 
         * 
         * // Step 3: IPO happened! Update the DB
         * activities.updateSpacStage(savedSpac.getId(), "IPO_COMPLETE");
         */

        // Step 2: IPO with 30-day Timeout (SLA Enforcement)
        boolean ipoReceived = Workflow.await(Duration.ofDays(30), () -> isIpoComplete);

        if (!ipoReceived) {
            activities.processRefund(savedSpac.getId(), "IPO Timeout: Signal not received within 30 days");
            return;
        }

        activities.updateSpacStage(savedSpac.getId(), "IPO_COMPLETE");

        // Step 3 & 4: Voting with 7-day Timeout (Saga Pattern)
        boolean voteReceived = Workflow.await(Duration.ofDays(7), () -> voteDecision != null);

        System.out.println("DEBUG: Current voteDecision state: " + this.voteDecision);

        if (!voteReceived || "REJECT".equals(voteDecision)) {
            System.out.println("DEBUG: Entering REFUND path");
            activities.processRefund(savedSpac.getId(), "Vote Rejected or Timeout");
        } else {
            System.out.println("DEBUG: Entering MERGER path"); // If this doesn't show, the vote signal didn't work
            activities.executeMerger(savedSpac.getId());
            activities.updateSpacStage(savedSpac.getId(), "MERGED");
        }
    }

    @Override
    public void signalIpoComplete() {
        this.isIpoComplete = true;
    }

    @Override
    public void signalStakeHolderVote(String decision) {
        this.voteDecision = decision.toUpperCase();
    }
}