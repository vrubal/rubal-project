package com.rubal.transformers.jdk;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class SpliteratorTest {
    public static void main(String[] args) {
        Stream<Double> doubleStream = Stream.generate(Math::random).limit(100);
        //doubleStream.forEach(System.out::println);
        int n = 0;
        List<Double> collect = doubleStream.collect(Collectors.toList());
        while(collect.spliterator().tryAdvance(
                System.out::println
        )){
            n++;
        }
        System.out.println("count"+n);
    }
}
