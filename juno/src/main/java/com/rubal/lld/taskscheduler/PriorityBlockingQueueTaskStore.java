package com.rubal.lld.taskscheduler;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author
 * @created
 * @project rubal-project
 */
public class PriorityBlockingQueueTaskStore implements TaskStore<ScheduledTask> {

    private final PriorityBlockingQueue<ScheduledTask> taskQueue;
    private final Set<ScheduledTask> tasks;

    public PriorityBlockingQueueTaskStore(Comparator<ScheduledTask> comparator, Integer queueSize) {
        this.taskQueue = new PriorityBlockingQueue<>(queueSize, comparator);
        this.tasks = new HashSet<>();
    }

    @Override
    public void add(ScheduledTask task) {
        taskQueue.offer(task);
    }

    @Override
    public ScheduledTask poll() {
        return taskQueue.poll();
    }

    @Override
    public ScheduledTask peek() {
        return taskQueue.peek();
    }

    @Override
    public boolean remove(ScheduledTask task) {
        if (tasks.contains(task)) {
            taskQueue.remove(task);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized boolean isEmpty() {
        return taskQueue.isEmpty();
    }
}
