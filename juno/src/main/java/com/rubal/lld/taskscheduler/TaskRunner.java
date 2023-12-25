package com.rubal.lld.taskscheduler;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class TaskRunner implements Runnable{
    private final TaskStore<ScheduledTask> taskStore;
    private boolean running;

    public TaskRunner(TaskStore<ScheduledTask> taskStore) {
        this.taskStore = taskStore;
    }

    public void run() {
        while (running) {
            ScheduledTask scheduledTask = taskStore.poll();
            if (scheduledTask == null) {
                break;
            }
            scheduledTask.execute();
            if (scheduledTask.isRecurring()) {
                taskStore.add(scheduledTask.nextScheduledTask());
            }
        }
    }

    public void stop() {
        this.running = false;
    }
}
