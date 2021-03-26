package com.rubal.ds.thread;

import java.util.ArrayList;
import java.util.List;

class Producer {

    private int capacity;
    private List<Integer> list;

    public Producer(List<Integer> list, int capacity) {
        this.capacity = capacity;
        this.list = list;
    }

    public void produce(List list) throws InterruptedException {
        while (true) {

        synchronized (list) {
                while (list.size() == capacity) {
                    wait();
                }

                list.add(1);
                notify();
            }
        }
    }
};

class Consumer {
    private int capacity;
    private List<Integer> list;

    public Consumer(List<Integer> list, int capacity) {
        this.capacity = capacity;
        this.list = list;
    }
    public void consume(List list, int capacity) throws InterruptedException {

        while (true) {

        synchronized(list) {
                while (list.size() == 0) {
                    wait();
                }

                list.remove(0);
                notify();
            }
        }

    }
};

public class Test{

    public static void main(String[] args){
        List<Integer> list = new ArrayList<>();
        Producer producer = new Producer(list, 100);
        Consumer consumer = new Consumer(list,100);

        Thread t1 = new Thread(() -> {
            try{
                producer.produce(list);
            }catch(Exception e){
                System.out.println("Error occureed in producer thread"+e);
            }

        });

        Thread t2 = new Thread(() -> {
            try{
                consumer.consume(list, 100);
            }catch(Exception e){
                System.out.println("Error occureed in producer thread"+e);
            }

        });

        t1.start();
        t2.start();
    }
}
