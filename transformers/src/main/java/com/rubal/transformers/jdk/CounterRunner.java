package com.rubal.transformers.jdk;
import java.util.concurrent.atomic.AtomicInteger;
class Counter implements Runnable{

    private volatile int count;
    Counter(int count){
        this.count = count;
    }


    //@SneakyThrows
    @Override
    public void run() {
        while(true){
            count++;
            System.out.println(Thread.currentThread()+" count:"+count);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
class AtomicCounter implements Runnable{

    private volatile AtomicInteger count;
    AtomicCounter(AtomicInteger count){
        this.count = count;
    }


    //@SneakyThrows
    @Override
    public void run() {
        while(true){
            count.getAndIncrement();
            System.out.println(Thread.currentThread()+" count:"+count.get());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class CounterRunner {

    public static volatile AtomicInteger atomicCount = new AtomicInteger();
    public static volatile int count = 0;
    public static void main(String[] args) {


       AtomicCounter c = new AtomicCounter(atomicCount);
        Thread t = new Thread(c, "Atomic-1:");
        t.start();

        AtomicCounter c1 = new AtomicCounter(atomicCount);
        Thread t1 = new Thread(c1, "Atomic-2:");
        t1.start();


        Counter cc = new Counter(count);
        Thread tt = new Thread(cc, "NonAtomic-1:");
        tt.start();

        Counter cc1 = new Counter(count);
        Thread tt1 = new Thread(cc1, "NonAtomic-2:");
        tt1.start();
    }
}
