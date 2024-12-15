package com.rubal.transformers.jdk;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public interface MyFunctionalInterface {
    default <T> T test1(T t){
        return null;
    }
    <T> T test(T t);
    String test(String s);

}
