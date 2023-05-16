package com.rubal.transformers.concurrent;


import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class OddEvenPrinter {

    public static void main(String[] args) {
        final AtomicInteger count = new AtomicInteger(1);
        ReentrantLock lock = new ReentrantLock();
        Condition even = lock.newCondition();
        Condition odd = lock.newCondition();
        Condition zero = lock.newCondition();

        Runnable er = () -> {
            try {
                lock.lock();
                while (true) {
                    while (count.get() % 2 != 0) {
                        even.await();
                        Thread.sleep(1000);

                    }
                    System.out.println(Thread.currentThread().getName()+":"+ count.get());
                    count.getAndIncrement();
                    zero.signal();
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        };
        Runnable or = () -> {
            try {
                lock.lock();
                while (true) {
                    while (count.get() % 2 == 0) {
                        odd.await();
                        Thread.sleep(1000);
                    }
                    System.out.println(Thread.currentThread().getName()+":"+ count.get());
                    count.getAndIncrement();
                    zero.signal();
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        };

        Runnable zr = () -> {
            try {
                lock.lock();
                while (true) {
                    zero.await();
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName()+":"+ 0);
                    if(count.get()%2==0)
                        even.signal();
                    else
                        odd.signal();
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        };

        Thread t = new Thread(er,"even");
        Thread t2 = new Thread(or, "odd");
        Thread t3 = new Thread(zr, "zero");

        t.start();
        t3.start();
        t2.start();
    }

}
