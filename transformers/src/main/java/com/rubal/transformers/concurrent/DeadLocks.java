package com.rubal.transformers.concurrent;


public class DeadLocks {
    public static void main(String[] args) throws InterruptedException {

        String lock1 = "lock1";
        String lock2 = "lock2";
        Runnable Task1 = ()->{

            synchronized (lock1){
                try {
                    System.out.println(Thread.currentThread() + " holding lock1");
                    //Thread.sleep(1000);
                    synchronized (lock2){
                        System.out.println(Thread.currentThread() + " holding lock2");
                    }
                }
                catch(RuntimeException e){
                    System.out.println("Exception "+e);
                }
            }
        };

        Runnable Task2 = ()->{

            synchronized (lock2){
                try {
                    System.out.println(Thread.currentThread() + " holding lock2");
                    //Thread.sleep(1000);
                    synchronized (lock1){
                        System.out.println(Thread.currentThread() + " holding lock1");
                    }
                }
                catch(RuntimeException e){
                    System.out.println("Exception "+e);
                }
            }
        };

        Thread t = new Thread(Task1, "Thread1");
        Thread t2 = new Thread(Task2, "Thread2");

        t.start();
        t2.start();
        t.join();
        t2.join();
    }

}
