package com.rubal.transformers.jdk;

import java.util.Objects;
import java.util.function.Predicate;

@FunctionalInterface
interface NullCheckPredicate<T> extends Predicate<T> {
    default void identify(){

    }

    boolean test(T t );
}
@FunctionalInterface
interface NullCheckPredicate2<T> extends NullCheckPredicate<T> {

    default void identify(String s){

    }
    boolean test(T t );
}
public class TestPredicate {


    public static void main(String[] args) {
        String s = null;
        NullCheckPredicate checkPredicate = Objects::isNull;
        System.out.println(checkPredicate.test(s));
    }
}
