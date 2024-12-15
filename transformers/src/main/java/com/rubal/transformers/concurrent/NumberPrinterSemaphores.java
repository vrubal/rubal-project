package com.rubal.transformers.concurrent;

import java.util.concurrent.Semaphore;

public class NumberPrinterSemaphores {
    static int counter = 0;
    static Semaphore semaphore = new Semaphore(1, true);

    static class OddEvenNumberPrinterThread implements Runnable {
        @Override
        public void run() {
            while (counter < 99) {
                try {
                    synchronized (this) {
                        semaphore.acquire();
                        counter++;
                        String tName = Thread.currentThread().getName();
                        System.out.println(tName + " --> " + counter);
                        semaphore.release();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public static void main(String[] args) throws Exception {
            Thread t1 = new Thread(new OddEvenNumberPrinterThread());
            Thread t2 = new Thread(new OddEvenNumberPrinterThread());
            t1.start();
            t2.start();
        }
    }
}
