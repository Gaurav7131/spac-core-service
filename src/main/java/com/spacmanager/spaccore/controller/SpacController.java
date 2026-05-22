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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/spacs")
public class SpacController {

    @Autowired
    private WorkflowClient workflowClient;

    @Autowired
    private WorkerFactory workerFactory;

    @Autowired
    private SpacActivityImpl spacActivity;

    // Inject Kafka Template to send messages to the Simulation Engine
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostConstruct
    public void startWorker() {
        Worker worker = workerFactory.newWorker(TemporalConfig.SPAC_TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(SpacLifecycleWorkflowImpl.class);
        worker.registerActivitiesImplementations(spacActivity);
        workerFactory.start();
    }

    @GetMapping
    public List<String> getSpacs() {
        return List.of(
                "SPAC 1: Tech Acquisition Corp",
                "SPAC 2: Green Energy Partners",
                "STATUS: API Gateway Routing is SUCCESSFUL!");
    }

    @PostMapping("/init")
    public ResponseEntity<String> initializeSpac(@RequestBody Spac spac) {
        String workflowId = "SPAC-" + spac.getTickerSymbol();

        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(TemporalConfig.SPAC_TASK_QUEUE)
                .setWorkflowId(workflowId)
                .build();

        SpacLifecycleWorkflow workflow = workflowClient.newWorkflowStub(SpacLifecycleWorkflow.class, options);
        WorkflowClient.start(workflow::startSpacLifecycle, spac);

        return ResponseEntity.ok("Started Temporal Workflow for: " + workflowId);
    }

    // NEW ENDPOINT: Triggers the background simulation engine
    @PostMapping("/trigger-simulation/{ticker}")
    public ResponseEntity<String> triggerSimulation(@PathVariable String ticker) {
        // Publishes the message to the "spac-simulations" Kafka topic
        kafkaTemplate.send("spac-simulations", "Calculate dilution for: " + ticker);
        return ResponseEntity.ok("Simulation job submitted for " + ticker);
    }

    @PostMapping("/{ticker}/ipo-complete")
    public ResponseEntity<String> completeIpo(@PathVariable String ticker) {
        String workflowId = "SPAC-" + ticker;
        SpacLifecycleWorkflow workflow = workflowClient.newWorkflowStub(SpacLifecycleWorkflow.class, workflowId);
        workflow.signalIpoComplete();
        return ResponseEntity.ok("IPO Signal sent to Temporal for " + ticker);
    }
}