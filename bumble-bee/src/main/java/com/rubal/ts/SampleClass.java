package com.rubal.ts;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class SampleClass {


    public static void main(String[] args) {
        int [] a = new int[]{2,4,5,25,625};
        Set<Integer> set = new HashSet<>();
        List<Integer> collect = Stream.of(1, 2, 3, 4).filter(e->e%2==0).
                collect(Collectors.toList());
        List<Integer> collect2 = Stream.of(1, 2, 3, 4).filter(e->e%2==0).toList();
        System.out.println(collect);
        System.out.println(collect2);


    }
}
