package com.rubal.ds.leetcode;

import java.util.Arrays;
import java.util.stream.Stream;

public class BrainPower {

    public static long mostPoints(int[][] q) {
        long maxPoint[] = new long[q.length+1];
        int maxIdx=0;
        for(int i=q.length-1; i>=0; i--){
            maxIdx=i+q[i][1]+1;
            if(maxIdx>=q.length){
                maxPoint[i]=q[i][0];
            }else{
                maxPoint[i]=q[i][0]+maxPoint[maxIdx];
            }
            maxPoint[i]=Math.max(maxPoint[i],maxPoint[i+1]);
        }
        return maxPoint[0];
    }
    public static void main(String[] args) {
        int[][] q= new int[][]{{21,5},{92,3},{74,2},{39,4},{58,2},{5,5},{49,4},{65,3}};
        System.out.println(mostPoints(q));
    }
}
