package com.rubal.transformers.concurrent;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class OddEvenPrinter {

    public static void main(String[] args) throws InterruptedException {
        final AtomicInteger count = new AtomicInteger(1);

        // Each queue acts as a "token" — a thread runs only when it receives the token.
        SynchronousQueue<Boolean> oddToken = new SynchronousQueue<>();
        SynchronousQueue<Boolean> evenToken = new SynchronousQueue<>();
        SynchronousQueue<Boolean> zeroToken = new SynchronousQueue<>();

        Runnable or = () -> {
            try {
                while (true) {
                    oddToken.take();          // wait for token
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + ":" + count.get());
                    count.getAndIncrement();
                    zeroToken.put(true);      // hand token to zero
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable er = () -> {
            try {
                while (true) {
                    evenToken.take();         // wait for token
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + ":" + count.get());
                    count.getAndIncrement();
                    zeroToken.put(true);      // hand token to zero
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable zr = () -> {
            try {
                while (true) {
                    zeroToken.take();         // wait for token
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + ":" + 0);
                    if (count.get() % 2 != 0)
                        oddToken.put(true);   // hand token to odd
                    else
                        evenToken.put(true);  // hand token to even
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Thread t = new Thread(er, "even");
        Thread t2 = new Thread(or, "odd");
        Thread t3 = new Thread(zr, "zero");

        t.start();
        t2.start();
        t3.start();

        // Kick off: odd goes first
        oddToken.put(true);

        t.join();
        t2.join();
        t3.join();
    }
}
