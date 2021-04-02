package com.rubal.transformers.concurrent;

import java.util.ArrayList;
import java.util.List;

class Producer<T>{
    private List<T> list;
    private int limit;
    Producer(List<T> list, int limit){
        this.list = list;
        this.limit = limit;
    }

    public void produce(T t){
        try {
            synchronized (list) {
                while (true) {

                    if (list.size() == limit) {
                        list.wait();
                        Thread.sleep(1000);

                    }
                    System.out.println(Thread.currentThread() + " Produced: " + t);
                    list.add(t);
                    list.notifyAll();
                }
            }
        }catch (InterruptedException e){
        }
    }
}

class Consumer<T>{
    private List<T> list;
    private int limit;
    Consumer(List<T> list, int limit){
        this.list = list;
        this.limit = limit;
    }

    public T consume(){
        T e = null;
            synchronized (list) {
                try {
                    while(true) {
                        if (list.size() == 0) {
                            list.wait();
                            Thread.sleep(1000);
                        }
                        e = list.remove(0);
                        System.out.println(Thread.currentThread()+" Consumed: "+e);
                        list.notifyAll();
                    }
                } catch (InterruptedException ie) {

                }
            }
        return e;
    }

}
public class WaitNotifyDemo {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        Producer<Integer> p = new Producer<>(list, 10);
        Consumer<Integer> c = new Consumer<>(list, 10);

        Runnable r1 = ()->{
            p.produce(1);
        };

        Runnable r2 = ()->{
          c.consume();
        };

        Thread t1 = new Thread(r1, "Producer");
        Thread t2 = new Thread(r2, "Consumer");

        t1.start();t2.start();


    }


}
