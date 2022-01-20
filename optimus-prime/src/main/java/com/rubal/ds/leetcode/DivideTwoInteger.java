package com.rubal.ds.leetcode;

import java.util.Date;

public class DivideTwoInteger {
    public static int divide(int dividend, int divisor){
        int count = 0;
        int num = Math.abs(divisor);
        while(num<=Math.abs(dividend)){
            num+=Math.abs(divisor);
            count++;
            //System.out.println(count);
        }
        return ((dividend > 0 && divisor > 0)||(dividend < 0 && divisor < 0))  ? count : count*-1;
    }

    public static void main(String[] args) {
        System.out.println("StartTime"+new Date());
        System.out.println(divide(Integer.MAX_VALUE, 4));
        System.out.println("EndTime"+new Date());
    }
}
