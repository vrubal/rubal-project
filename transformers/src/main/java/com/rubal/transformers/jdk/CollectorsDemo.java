package com.rubal.transformers.jdk;

import com.sun.source.tree.Tree;

import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class CollectorsDemo {
    public static void main(String[] args) {

        System.out.println("List to another collection Example");
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        TreeSet<Integer> collect = list.stream().map(e -> e * 2)
                .collect(Collectors.toCollection(TreeSet::new));
        collect.forEach(System.out::println);
        System.out.println("========================");

        System.out.println("Count the number of odd numbers");
        Long counting = collect.stream().filter(e -> e % 2 != 0).collect(Collectors.counting());
        System.out.println("TOtal:"+counting);
        System.out.println("========================");

        System.out.println("Find min by using the minBy comparator");
        Integer minBy = list.stream().collect(Collectors.minBy(Comparator.naturalOrder())).get();
        System.out.println("Min:"+minBy);
        System.out.println("========================");

        System.out.println("Find max by using the minBy comparator");

        Integer maxBy = list.stream()
                .collect(Collectors.maxBy(Comparator.naturalOrder())).get();
        //list.stream().collect()
        System.out.println("Max:"+maxBy);
        System.out.println("========================");

        System.out.println("PartitionBy Example (partition list based on the partition factor)");
        List<String> strings = Arrays.asList("a","alpha","beta","gamma");
        Map<Boolean, List<String>> partitionBy = strings
                .stream()
                .collect(Collectors.partitioningBy(x -> x.length() % 2==0));

        System.out.println(partitionBy);
        System.out.println("========================");

        System.out.println("ToMap Example (map element based on the criteria)");
        List<String> strings1 = Arrays.asList("alpha","beta","gamma");
        Map<String,Integer> map = strings1
                .stream()
                .collect(Collectors
                        .toMap(Function.identity(),String::length));
        System.out.println(map);
        System.out.println("========================");

        System.out.println("GroupBY Example (group element based on the criteria)");
        List<String> strings2 = Arrays.asList("alpha","beta","gamma");
        Map<Integer, List<String>> collect1 = strings
                .stream()
                .collect(Collectors.groupingBy(e->e.length()));
        Map<String, Integer> collect2 = strings
                .stream()
                .collect(Collectors.toMap(Function.identity(), String::length));
        System.out.println(collect2);

        Map<String, Integer> collect3 = collect2.entrySet().stream().filter(e -> e.getValue() % 2 == 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        System.out.println("GroupingBy Example");
        System.out.println(collect3);
        System.out.println("========================");
        Map<Object, List<Integer>> result =  Arrays.stream(new int[]{1,2,1,2,3,3,4,5,6})
                .boxed()
                .collect(Collectors.groupingBy(e->e%2==0));
        System.out.println(result);

        System.out.println("========================");
        System.out.println("Count duplicate Example");

        ArrayList<Integer> numbersList
                = new ArrayList<>(Arrays.asList(1, 1, 2, 3, 3, 3, 4, 5, 6, 6, 6, 7, 8));

        numbersList.stream().sorted(Comparator.naturalOrder());
        Map<Integer, Integer> elementCountMap = numbersList.stream()
                .collect(Collectors.toMap(Function.identity(), v -> 1, Integer::sum))
                .entrySet().stream().filter(e->e.getValue()==  1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        System.out.println(elementCountMap);
        Map<Integer, Long> elementCountMap2 = numbersList.stream().
                collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println(elementCountMap2);

        System.out.println("========================");
        System.out.println("Drop While example");

        List<Integer> collect4 = numbersList.stream().dropWhile(e -> e.intValue()!=3).collect(Collectors.toList());
        System.out.println(collect4);
        System.out.println("========================");
        System.out.println("Take While example");
        List<Integer> collect5 = numbersList.stream().takeWhile(e -> e.intValue()!=3).collect(Collectors.toList());
        System.out.println(collect5);

        System.out.println("========================");
        System.out.println("Reduce example");
        System.out.println(Stream.of(1,2,3).reduce(0,(a,b)->a+b));

        /*Map<String, Optional<Employee>> collect =
                employeeList.stream().collect(
                        Collectors.groupingBy(
                                Employee::getDept,
                                Collectors.reducing(BinaryOperator.maxBy(bySalary))
                        )
                );
        */
        List<Person> employees = new ArrayList<>();
        employees.add(new Person("name", 6, "Pune" ));
        employees.add(new Person("name", 8, "Pune" ));
        employees.add(new Person("name", 7, "Mumbai" ));
        employees.add(new Person("name", 6, "Mumbai" ));
        employees.add(new Person("name", 9, "Mumbai" ));
        Comparator<Employee> byHeight = Comparator.comparing(Employee::getHeight);
        Map<String, Integer> sumByCity = employees.stream()
                .collect(Collectors.groupingBy(Person::getCity,
                        Collectors.summingInt(Person::getHeight)));

        System.out.println("Sum of all heieght in CITY");
        System.out.println(sumByCity);

        Map<String, Optional<Person>> maxHeightByCity = employees.stream()
                .collect(Collectors.groupingBy(Person::getCity,
                        Collectors.maxBy(Comparator.comparing(Person::getHeight))));
        System.out.println("Max Height by city");
        System.out.println(maxHeightByCity);

        Map<String, Double> averageHeightByCity = employees.stream().collect(Collectors.groupingBy(Person::getCity, Collectors.averagingInt(Person::getHeight)));
        System.out.println("AverageHeight By City");
        System.out.println(averageHeightByCity);
        Map<String, Long> countByCity = employees
                .stream()
                .collect(Collectors.groupingBy(Person::getCity, Collectors.counting()));
        System.out.println("Count By City");
        System.out.println(countByCity);
    }

}
