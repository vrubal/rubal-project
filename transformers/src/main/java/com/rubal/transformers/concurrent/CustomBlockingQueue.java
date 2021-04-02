package com.rubal.transformers.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class CustomBlockingQueue<T>{
    private final List<T> arr;
    private int limit;
    private ReentrantLock lock = new ReentrantLock();
    private Condition addCond = lock.newCondition();
    private Condition removeCond = lock.newCondition();

    public CustomBlockingQueue(List<T> arr, int limit){
        this.arr = arr;
        this.limit = limit;
    }
    public void add(T elem){

        if(elem==null) return;
        lock.lock();
        try {
            while (arr.size() == limit) {
                addCond.await();
                Thread.sleep(1000);
            }
            log.info(Thread.currentThread() + " Adding element: "+elem);
            arr.add(elem);
            removeCond.signal();
        }catch (InterruptedException e){
            log.error("Exception occurred", e);
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
            log.info(Thread.currentThread()+" Removed element: "+t);
            addCond.signal();
        }catch (InterruptedException e){
            log.error("Exception occurred", e);
        }finally {
            lock.unlock();
        }
        return t;
    }

    public static void main(String[] args) {
        CustomBlockingQueue<Integer> q = new CustomBlockingQueue<>(new ArrayList<>(), 10);
        Random random = new Random();
        Runnable r1 = ()->{
            while(true) {
                q.add(random.nextInt(100));
            }
        };
        Runnable r2 = ()->{
            while(true) {
                q.remove();
            }
        };

        Thread t1 = new Thread(r1,"Producer");
        Thread t2 = new Thread(r2,"Consumer");
        t1.start();t2.start();
    }

}
