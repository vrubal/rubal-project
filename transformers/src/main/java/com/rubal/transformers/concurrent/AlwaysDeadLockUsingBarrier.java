package com.rubal.transformers.concurrent;

import lombok.Setter;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

@Setter
class Task1{
    String r1;
    String r2;
    CyclicBarrier barrier;
    Task1(String r1, String r2){
        this.r1 = r1;
        this.r2 = r2;
    }

    public void methodOne() throws InterruptedException, BrokenBarrierException {
        synchronized (r1){
            System.out.println(Thread.currentThread()+" acquired lock on resource1");
            System.out.println(Thread.currentThread()+" waiting for barrier condition");
            barrier.await();
            System.out.println(Thread.currentThread()+" Barrier broken");

            synchronized (r2){
                System.out.println(Thread.currentThread()+" acquired lock on resource2");
            }

        }
    }

}
@Setter
class Task2{
    String r1;
    String r2;
    CyclicBarrier barrier;
    Task2(String r1, String r2){
        this.r1 = r1;
        this.r2 = r2;
    }

    public void methodOne() throws BrokenBarrierException, InterruptedException {
        synchronized (r2){
            System.out.println(Thread.currentThread()+" acquired lock on resource2");
            System.out.println(Thread.currentThread()+" waiting for barrier condition");
            barrier.await();
            System.out.println(Thread.currentThread()+" Barrier broken");

            synchronized (r1){
                System.out.println(Thread.currentThread()+" acquired lock on resource1");
            }
        }
    }

}



public class AlwaysDeadLockUsingBarrier {

    public static void main(String[] args) {
        String r1="r1";
        String r2="r2";
        CyclicBarrier latch = new CyclicBarrier(2);
        Task1 task1 = new Task1(r1,r2);
        Task2 task2 = new Task2(r1,r2);
        task1.setBarrier(latch);
        task2.setBarrier(latch);
        Runnable tr1 = () -> {

            try {
                task1.methodOne();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Runnable tr2 = () -> {

            try {
                task2.methodOne();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread t1 = new Thread(tr1, "Thread1");
        Thread t2 = new Thread(tr2, "Thread2");

        t1.start();
        t2.start();




    }
}
