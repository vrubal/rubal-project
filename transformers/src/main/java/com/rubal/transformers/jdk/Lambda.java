package com.rubal.transformers.jdk;

interface TestLambda1{
    void test();
    default void test2(int q){
        System.out.println("Interface1 Default" +q);
    };
}
@FunctionalInterface
interface TestLambda2{
    void test();
    default void test2(int q){
        System.out.println("Interface2 Default" +q);
    };
}
class TestLambdaClass implements TestLambda1, TestLambda2{
    @Override
    public void test() {

    }

    @Override
    public void test2(int q) {
        TestLambda1.super.test2(q);
    }

}
public class Lambda {
    public static void main(String[] args) {
        TestLambda2 t = ()->System.out.print("asas");
        t.test2(12);

        TestLambda1 t1 = ()->System.out.print("asas");
        t1.test2(12);

        TestLambdaClass c = new TestLambdaClass();
        c.test2(34);

        TestLambda1 c1 = new TestLambdaClass();
        c1.test2(34);

        TestLambda2 c2 = new TestLambdaClass();
        c2.test2(34);
    }
}
