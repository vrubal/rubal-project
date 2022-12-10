package com.rubal.ds.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class OddEvenPrinter {

    public static void main(String[] args) throws Exception {
        List<Integer> numbers = new ArrayList<>();
        //int i = 0;
        ReentrantLock lock = new ReentrantLock();
        Condition oddCondition = lock.newCondition();
        Condition evenCondition = lock.newCondition();
        Thread odd = new Thread(()->{
            int i=0;
            while(true){
                try {
                    synchronized (numbers){
                        while(numbers.size()%2 == 0 ) {
                            numbers.wait();
                            Thread.sleep(1000);
                        }
                        System.out.println("odd:"+numbers.size());
                        numbers.add(i++);
                        numbers.notify();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        Thread even = new Thread(()->{
            int i=0;
            while(true){
                try {
                    synchronized (numbers){
                        while(numbers.size()%2 != 0 ) {
                            numbers.wait();
                            Thread.sleep(1000);
                        }
                        System.out.println("Even:"+numbers.size());
                        numbers.add(i++);
                        numbers.notify();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        odd.start();
        even.start();
    }
}
