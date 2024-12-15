package com.rubal.transformers.jdk;

import com.rubal.ds.thread.Test;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestStreams {
    static String field="test";
    static String local="test";

    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(1, 2, 2, 4, 4, 1);
        int multi=10;
        Stream<Integer> integerStream = nums.parallelStream();
        Supplier<Stream<Integer>> supplier = ()->nums.stream();
        System.out.println("count:"+supplier.get().count());
        System.out.println("distinct:"+supplier.get().distinct().count());
        System.out.println("List:"+supplier.get().collect(Collectors.toList()));
        System.out.println("All match:"+supplier.get().allMatch(t->t<10));
        System.out.println("Find any:"+supplier.get().findAny().get());
        System.out.println("Find First:"+supplier.get().findFirst().get());
        System.out.println("Reduce:"+supplier.get().reduce((t1, t2)-> t1+t2).get());
        System.out.println("Map:"+supplier.get().map(integer -> integer*multi).collect(Collectors.toList()));
        //multi=11;
        System.out.println("NoneMatch:"+supplier.get().noneMatch(integer -> integer>3));


        //ParallelStream
        System.out.println("Parallel Stream");
        List<Integer> numrs = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        numrs.stream().skip(8).forEach((t)->{System.out.println(t +"Thread:"+Thread.currentThread());});
        //numrs.stream().filter(Optional::isPresent).findFirst()

        //spliterator
        System.out.println("Spliteratoer");
        List<Integer> numSplit= Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        //nums.stream().flatMap();
        Map<Integer, Integer> map = new HashMap<>();
        //map.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Comparator.naturalOrder());

    }



}
