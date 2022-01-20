package com.rubal.ds.leetcode;

import java.util.Arrays;

public class NTimeComputer {

    public static boolean isValid(int n, int max, int k){
        if(n*k <=max){
            return true;
        }
        return false;
    }
    public static void main(String[] args) {
        int [] bat = new int[]{10,10,3,5};
        int sum = Arrays.stream(bat).sum();
        int low=0, high = sum;

        while((low+1)<high){
            int mid = low+(high-low)/2;
            if(isValid(2, sum, mid)){
                low=mid;
            }else{
                high=mid;
            }
        }
        System.out.println(low);
    }
}
