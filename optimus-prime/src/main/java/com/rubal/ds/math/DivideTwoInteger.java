package com.rubal.ds.math;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class DivideTwoInteger {
    public static int divide(int dividend, int divisor) {
        int count = 0;
        int num = Math.abs(divisor);
        while(num<=Math.abs(dividend)){
            num+=Math.abs(divisor);
            count++;
        }
        return (dividend<0 && divisor<0)  ? count : (dividend<0 || divisor<0)? count*-1 : count;
    }

    public static void main(String[] args) {
        System.out.println(divide(-2147483648, -1));
    }
}
