package com.spacmanager.spaccore.workflow;

import com.spacmanager.spaccore.entity.Spac;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface SpacLifecycleWorkflow {
    @WorkflowMethod // Entry
    void startSpacLifecycle(Spac spac);

    @SignalMethod // reacts to external signal must have void return type
    void signalIpoComplete();
}