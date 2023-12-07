package com.rubal.transformers;

import com.rubal.transformers.jdk.Person;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class CustomCollector implements Collector<Person, List<Person>, List<Person>> {

    public static CustomCollector toList(){
        return new CustomCollector();
    }
    @Override
    public Supplier<List<Person>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<Person>, Person> accumulator() {
        return ((people, person) -> people.add(person));
    }

    @Override
    public BinaryOperator<List<Person>> combiner() {
        return (people, people2) -> {
            people.addAll(people2);
            return people;
        };
    }

    @Override
    public Function<List<Person>, List<Person>> finisher() {
        return Collections::unmodifiableList;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.singleton(Characteristics.UNORDERED);
    }

    public static void main(String[] args) {
        List<Person> persons = new ArrayList();
        persons.add(new Person("Vijay",158, "Pune"));
        persons.add(new Person("Vijay",158, "Pune"));
        persons.add(new Person("Vijay",158, "Pune"));
        persons.add(new Person("Vijay",158, "Pune"));

        Map<String, Long> collect = persons.stream().collect(Collectors.groupingBy(Person::getCity, Collectors.counting()));
    }
}
