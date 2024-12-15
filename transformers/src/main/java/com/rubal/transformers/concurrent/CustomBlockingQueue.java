package com.rubal.transformers.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class CustomBlockingQueue<T>{
    private final List<T> arr;
    private int limit;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    public CustomBlockingQueue(List<T> arr, int limit){
        this.arr = arr;
        this.limit = limit;
    }
    public void add(T elem){
        if(elem==null) return;
        writeLock.lock();
        try {
            if (arr.size()==10)
                return;
            log.info(Thread.currentThread() + " Adding element: "+elem);
            arr.add(elem);
        }catch (Exception e){
            log.error("Exception occurred", e);
        }finally {
            writeLock.unlock();
        }

    }
    public T get(){
        T t = null;
        readLock.lock();
        try{
            /*while(arr.size() == 0){
                Thread.sleep(1000);
            }*/
            t = arr.get(0);
            log.info(Thread.currentThread()+" Get element: "+t);
        }catch (Exception e){
            log.error("Exception occurred", e);
        }finally {
            readLock.unlock();
        }
        return t;
    }
    public int size(){
        readLock.lock();
        try{
            log.info(Thread.currentThread()+" Get size: {}", arr.size());
            return arr.size();

        }catch (Exception e){
            log.error("Exception occurred", e);
        }finally {
            readLock.unlock();
        }
        throw new RuntimeException();
    }
    public T remove(){
        T t = null;
        writeLock.lock();
        try{
            /*while(arr.size() == 0){
                Thread.sleep(1000);
            }*/
            t = arr.remove(0);
            log.info(Thread.currentThread()+" Removed element: "+t);
        }catch (Exception e){
            log.error("Exception occurred", e);
        }finally {
            writeLock.unlock();
        }
        return t;
    }

    public static void main(String[] args) {
        CustomBlockingQueue<Integer> q = new CustomBlockingQueue<>(new ArrayList<>(), 10);
        Random random = new Random();
        Runnable r1 = ()->{
            try {
                while(true) {
                    if(q.size()>10)
                        break;
                    q.add(random.nextInt(100));
                    Thread.sleep(1000);

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        };
        Runnable r2 = ()->{
            try {
                while (true) {
                    q.size();
                    Thread.sleep(1000);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        };
        Arrays.stream(new int[]{1,2,3,4}).map(operand -> operand*10).forEach(System.out::println);
        Thread t10 = new Thread(r1,"Producer0");
        Thread t11 = new Thread(r1,"Producer1");
        Thread t12 = new Thread(r1,"Producer2");
        Thread t20 = new Thread(r2,"Consumer0");
        Thread t21 = new Thread(r2,"Consumer1");
        Thread t22 = new Thread(r2,"Consumer2");
        t10.start();
        t11.start();
        t12.start();
        t20.start();
        t21.start();
        t22.start();
    }

}
