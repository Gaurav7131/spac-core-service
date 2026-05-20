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

    private boolean isIpoComplete = false;

    @Override
    public void startSpacLifecycle(Spac spac) {
        // Step 1: Save initial SPAC to DB
        Spac savedSpac = activities.registerNewSpac(spac);

        // Step 2: Pause workflow execution indefinitely until the IPO signal is
        // received
        Workflow.await(() -> isIpoComplete);

        // Step 3: IPO happened! Update the DB
        activities.updateSpacStage(savedSpac.getId(), "IPO_COMPLETE");
    }

    @Override
    public void signalIpoComplete() {
        this.isIpoComplete = true;
    }
}