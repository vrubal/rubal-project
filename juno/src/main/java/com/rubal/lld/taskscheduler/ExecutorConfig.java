package com.rubal.lld.taskscheduler;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class ExecutorConfig {
    private int numThreads;

    public ExecutorConfig(int numThreads) {
        this.numThreads = numThreads;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }
}
