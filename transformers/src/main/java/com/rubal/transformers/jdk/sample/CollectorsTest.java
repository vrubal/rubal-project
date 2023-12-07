package com.rubal.transformers.jdk.sample;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class CollectorsTest {
    public static void main(String[] args) {
        String s = "indiaone";
        Optional<Map.Entry<String, Long>> collect = Arrays.stream(s.split(""))
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(e -> e.getValue() > 1)
                //.map(Map.Entry::getKey)
                .findFirst();
        System.out.println(collect.get().getKey());
        String[]arr= new String[]{"a","ss","dsdsd","a","ere"};
        Integer integer = Arrays.stream(arr).map(String::length).max(Comparator.comparingInt(o -> o)).get();
        System.out.println(integer);
    }
}
