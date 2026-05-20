package com.spacmanager.spaccore.controller;

import com.spacmanager.spaccore.activity.SpacActivityImpl;
import com.spacmanager.spaccore.config.TemporalConfig;
import com.spacmanager.spaccore.entity.Spac;
import com.spacmanager.spaccore.workflow.SpacLifecycleWorkflow;
import com.spacmanager.spaccore.workflow.SpacLifecycleWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/spacs")
public class SpacController {

    @Autowired
    private WorkflowClient workflowClient;

    @Autowired
    private WorkerFactory workerFactory;

    @Autowired
    private SpacActivityImpl spacActivity;

    // Start the Temporal Worker when Spring Boot starts
    @PostConstruct
    public void startWorker() {
        Worker worker = workerFactory.newWorker(TemporalConfig.SPAC_TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(SpacLifecycleWorkflowImpl.class);
        worker.registerActivitiesImplementations(spacActivity);
        workerFactory.start();
    }

    // REST API Endpoint: Initializes the SPAC and starts the workflow
    @PostMapping("/init")
    public ResponseEntity<String> initializeSpac(@RequestBody Spac spac) {
        String workflowId = "SPAC-" + spac.getTickerSymbol();

        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(TemporalConfig.SPAC_TASK_QUEUE)
                .setWorkflowId(workflowId)
                .build();

        SpacLifecycleWorkflow workflow = workflowClient.newWorkflowStub(SpacLifecycleWorkflow.class, options);

        // Start asynchronously
        WorkflowClient.start(workflow::startSpacLifecycle, spac);

        return ResponseEntity.ok("Started Temporal Workflow for: " + workflowId);
    }

    // REST API Endpoint: Sends a signal to the running workflow
    @PostMapping("/{ticker}/ipo-complete")
    public ResponseEntity<String> completeIpo(@PathVariable String ticker) {
        String workflowId = "SPAC-" + ticker;
        SpacLifecycleWorkflow workflow = workflowClient.newWorkflowStub(SpacLifecycleWorkflow.class, workflowId);

        workflow.signalIpoComplete();
        return ResponseEntity.ok("IPO Signal sent to Temporal for " + ticker);
    }
}