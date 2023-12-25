package com.rubal.lld.taskscheduler;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class OneTimeTask extends ScheduledTask {
    private final long executionTime;

    public OneTimeTask(ExecutionContext context, long executionTime, int priority) {
        super(context, priority);
        this.executionTime = executionTime;
    }

    @Override
    public long getNextExecutionTime() {
        return executionTime;
    }

    @Override
    public boolean isRecurring() {
        return false;
    }

    @Override
    public ScheduledTask nextScheduledTask() {
        return null;
    }
}