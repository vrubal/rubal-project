package com.rubal.lld.taskscheduler;

/**
 * @author vrinal
 * @created
 * @project rubal-project
 */
public class RecurringTask extends ScheduledTask{
    private final long executionTime;
    private final long interval;

    public RecurringTask(ExecutionContext context, long executionTime, long interval, int priority) {
        super(context, priority);
        this.executionTime = executionTime;
        this.interval = interval;
    }

    @Override
    public long getNextExecutionTime() {
        return executionTime;
    }

    @Override
    public boolean isRecurring() {
        return true;
    }

    @Override
    public ScheduledTask nextScheduledTask() {
        return new RecurringTask(context, executionTime + interval, interval ,1);
    }
}
