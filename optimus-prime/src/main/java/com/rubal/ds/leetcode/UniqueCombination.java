package com.rubal.ds.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UniqueCombination {
    public static void allCombination(int[]nums, List<List<Integer>> l, List<Integer>t, int[] u){
        //Arrays.stream(((int[])"".toCharArray())).

        if(t.size()==nums.length){
            l.add(new ArrayList<>(t));
        }else{
            for(int i=0; i<nums.length;i++){
                if(u[i]==1)continue;
                if(i>0 && nums[i-1]==nums[i] && u[i-1]==0) continue;
                t.add(nums[i]);
                u[i]=1;
                allCombination(nums,l,t,u);
                t.remove(t.size()-1);
                u[i]=0;
            }
        }


    }

    public static void main(String[] args) {
        int[] nums = new int[]{1,1,2};
        List<Integer> t = new ArrayList();
        List<List<Integer>> l = new ArrayList();
        allCombination(nums, l,t, new int[nums.length]);
        System.out.println(l);
    }
}
