package com.rubal.ds.thread;

import javax.swing.plaf.IconUIResource;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author vribal
 * @created
 * @project rubal-project
 */
public class OddEvenZero {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition zero = lock.newCondition();
        Condition odd = lock.newCondition();
        Condition even = lock.newCondition();

        Thread evenT = new Thread(()->{
            int count=0;
            while(count<100){
                try {
                    lock.lock();
                    System.out.println("even:"+count);
                    count+=2;
                    zero.signal();
                    if(count<100)
                        even.await();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }

        });
        Thread zeroT = new Thread(()->{
            int count = 0;
            while(count<100){
                try {
                    lock.lock();
                    System.out.println("0");
                    if (count % 2 == 0)
                        even.signal();
                    else
                        odd.signal();
                    count++;
                    if(count<100)
                        zero.await();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }
        });
        Thread oddT = new Thread(()->{
            int count = 1;
            while(count<100){
                try {
                    lock.lock();
                    System.out.println("odd:"+count);
                    count+=2;
                    zero.signal();
                    if(count<100)
                        odd.await();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }
        });
        zeroT.start();
        evenT.start();
        oddT.start();
    }


}
