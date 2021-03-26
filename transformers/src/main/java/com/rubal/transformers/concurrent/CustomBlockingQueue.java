package com.rubal.transformers.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CustomBlockingQueue<T>{
    private final List<T> arr = new ArrayList<>();
    private int limit = 1;
    private ReentrantLock lock = new ReentrantLock();
    private Condition addCond = lock.newCondition();
    private Condition removeCond = lock.newCondition();


    public void add(T elem){

        if(elem==null) return;
        lock.lock();
        try {
            while (arr.size() == limit) {
                addCond.await();
                Thread.sleep(1000);
            }

            System.out.println(Thread.currentThread() + " Adding element: "+elem);
            arr.add(elem);
            removeCond.signal();

        }catch (InterruptedException e){
            //lock.unlock();
        }finally {
            lock.unlock();
        }

    }

    public T remove(){
        T t = null;
        lock.lock();
        try{
            while(arr.size() == 0){
                removeCond.await();
                Thread.sleep(1000);
            }
            t = arr.remove(0);
            System.out.println(Thread.currentThread()+" Removed element: "+t);
            addCond.signal();
        }catch (InterruptedException e){
            //lock.unlock();
        }finally {
            lock.unlock();
        }
        return t;
    }

    public static void main(String[] args) {
        CustomBlockingQueue<Integer> q = new CustomBlockingQueue<>();
        Runnable r1 = ()->{
            while(true) {
                q.add(1);
            }
        };
        Runnable r2 = ()->{
            while(true) {
                q.remove();
            }
        };

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();t2.start();
    }

}
