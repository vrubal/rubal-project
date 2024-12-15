package com.rubal.ds.thread;


import java.util.HashSet;
import java.util.Set;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class RaceCondition {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        set.add("Key");

        Runnable r = ()->{
            synchronized (set) {
                if (set.contains("Key")) {
                    var key = set.remove("Key");
                    if (!key)
                        System.out.println(Thread.currentThread() + " : Key is not present");
                    else
                        System.out.println(Thread.currentThread() + " : Key is present");
                } else
                    System.out.println(Thread.currentThread() + " : Key is not present");
            }
        };

        Thread t1 = new Thread(r);
        t1.setName("T1");
        Thread t2 = new Thread(r);
        t2.setName("T2");

        t1.start();
        t2.start();
    }
}
