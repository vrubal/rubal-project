package com.rubal.transformers.jdk.sample;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
interface I1 {
    default void display(){
        System.out.println("I1");
    }
}
interface I2 {
    default void display(){
        System.out.println("I2");
    }
}

public class TestDefaultMethod implements I1, I2{


    @Override
    public void display() {
        I1.super.display();
        I2.super.display();
    }

    public static void main(String[] args) {
        TestDefaultMethod t = new TestDefaultMethod();
        t.display();
    }
}
