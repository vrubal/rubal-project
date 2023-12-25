package com.rubal.lld.taskscheduler;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public interface TaskStore<T extends ScheduledTask> {

    T peek();

    T poll();

    void add(T task);

    boolean remove(T task);

    boolean isEmpty();
}
