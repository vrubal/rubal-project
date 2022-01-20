package com.rubal.ds.leetcode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PrintAllCombination {

    public static void printAllComb(int []nums, Set<List<Integer>> list, List<Integer> tList, int l, int start){
        if(tList.size()==6
            ) {
                list.add(new ArrayList<>(tList));
        }else{
            for(int i=start;i<nums.length;i++){
                tList.add(nums[i]);
                printAllComb(nums, list, tList,l, i+1);
                tList.remove(tList.size()-1);
            }
        }




    }
    public static void main(String[] args) {
        int [] arr = new int[]{-1,0,1,2,-1,-4};
        Set<List<Integer>> finalList = new HashSet<>();
        List<Integer> tList = new ArrayList<>();
        printAllComb(arr, finalList, tList, 6, 0);
        finalList.forEach(System.out::println);
    }
}
