package com.rubal.lld.taskscheduler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class SchedulingApp {
    private final List<Thread> threads;
    private final TaskStore<ScheduledTask> taskStore;

    public SchedulingApp(ExecutorConfig executorConfig, TaskStore<ScheduledTask> taskStore) {
        this.threads = new ArrayList<>();
        this.taskStore = taskStore;
        for (int i = 0; i < executorConfig.getNumThreads(); i++) {
            Thread thread = new Thread(new TaskRunner(taskStore));
            threads.add(thread);
        }
    }
    public void start(){
        threads.forEach(e->e.start());
    }

    public void stop() {
        threads.forEach(t -> t.interrupt());
    }

    public static void main(String[] args) {
        PriorityBlockingQueueTaskStore store = new PriorityBlockingQueueTaskStore(Comparator.comparingInt(a -> a.priority), 5);
        ExecutorConfig executorConfig = new ExecutorConfig(5);
        SchedulingApp app = new SchedulingApp(executorConfig, store);

    }
}