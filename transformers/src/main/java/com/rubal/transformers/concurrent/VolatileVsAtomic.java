package com.rubal.transformers.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class VolatileVsAtomic {
    static class UnsafeCounter {
        private volatile int counter;

        public UnsafeCounter() {
            this.counter = 0;
        }

        synchronized int getValue() {
            return counter;
        }
        synchronized void  increment() {
            counter++;
        }
    }


    public static void main(String[] args) {
        UnsafeCounter unsafeCounter = new UnsafeCounter();
        AtomicInteger a = new AtomicInteger();
        String s = new String("ssds");
        Thread t = new Thread(()->{
            while(unsafeCounter.getValue()< 100) {
                unsafeCounter.increment();
                System.out.println(Thread.currentThread().getName()+"-"+ unsafeCounter.getValue());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t1 = new Thread(()->{
            while(unsafeCounter.getValue()< 100) {
                unsafeCounter.increment();
                System.out.println(Thread.currentThread().getName()+"-"+ unsafeCounter.getValue());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t3 = new Thread(()->{
            while(unsafeCounter.getValue()< 100) {
                unsafeCounter.increment();
                System.out.println(Thread.currentThread().getName()+"-"+ unsafeCounter.getValue());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.setName("T1");
        t1.setName("T2");
        t3.setName("T3");

        t.start();
        t1.start();
        t3.start();
    }
}
