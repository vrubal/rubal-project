package com.rubal.lld.taskscheduler;

import java.util.Comparator;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public abstract class ScheduledTask implements Comparable {
    public final ExecutionContext context;
    public final int priority;
    public ScheduledTask (ExecutionContext context, int priority) {
        this.context = context;
        this.priority = priority;
    }

    abstract boolean isRecurring();

    void execute() {
        context.execute();
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    abstract ScheduledTask nextScheduledTask();

    abstract long getNextExecutionTime();
}
