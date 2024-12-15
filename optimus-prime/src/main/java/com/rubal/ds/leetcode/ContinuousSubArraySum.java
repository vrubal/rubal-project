package com.rubal.ds.leetcode;

import java.util.HashMap;
import java.util.Map;

public class ContinuousSubArraySum {

    public static boolean check(){
        int nums[]=new int[]{23,2,4,6,7};
        int k=6;
        Map<Integer, Integer> map = new HashMap(){{put(0,-1);}};;
        int runningSum = 0;
        for (int i=0;i<nums.length;i++) {
            System.out.println(map);
            runningSum += nums[i];
            if (k != 0) runningSum %= k;
            Integer prev = map.get(runningSum);
            if (prev != null) {
                if (i - prev > 1)
                    return true;
            }
            else map.put(runningSum, i);
        }
        return false;
    }
    public static void main(String[] args) {
        System.out.println(check());
    }

}
