package com.rubal.ds.dp;

import java.util.Arrays;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class FibonaciWithDP {
    // 0 1 1 2 3 5 8 13 21
    public static long fiboNacci(long n, long dp[]){
        if(n<=2)
            return n;
        if(dp[(int)n]!=-1) {
            System.out.println("Returning memoized fib of:"+n);
            return dp[(int)n];
        };
        return dp[(int)n] = fiboNacci(n-1, dp)+fiboNacci(n-2, dp);
    }
    public static void main(String[] args) {
        long n = 3;
        long [] arr = new long[(int)(n+1)];
        Arrays.fill(arr, -1);
        arr[0] = 0;
        arr[1] = 1;
        //arr[2] = 2;
        System.out.println(fiboNacci(n,arr));
        //Arrays.stream(arr).forEach(System.out::println);
    }
}
