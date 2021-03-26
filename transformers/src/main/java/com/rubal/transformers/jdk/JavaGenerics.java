package com.rubal.transformers.jdk;

import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@ToString
class Test<A,B,C,D,E>{
    private A a;
    private B b;
    private C c;
    private D d;
    private E e;
    public Test(A a, B b, C c, D d, E e){
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;

    }
}
public class   JavaGenerics {

    public static void printL(List<? extends  Number> nums){
        nums.forEach(System.out::println);
        for (Number num : nums) {

        }
    }
    public static void printS(List<? super  String> nums){
        nums.forEach(System.out::println);
    }
    public static void main(String[] args) {

        new Test(1,2,3,4,5);
        System.out.println(new Test(1,2,3,4,5));
        /*printL(Arrays.asList(1,2,3,4,5));
        printS(Arrays.asList(1,2,3,4,5));*/

    }


}
