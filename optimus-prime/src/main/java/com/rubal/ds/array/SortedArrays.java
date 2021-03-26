package com.rubal.ds.array;

import java.util.Arrays;

public class SortedArrays {


    public static int[] sortedSquares(int[] nums) {

        int[]result=new int[nums.length];
        int start = 0;
        int end = nums.length-1;
        for(int i=nums.length-1;i>=0;i--){

            if(nums[start]*nums[start] < nums[end]*nums[end]){
                result[i]=nums[end]*nums[end];
                end--;

            }else{
                result[i]=nums[start]*nums[start];
                start++;
            }
        }
        return result;

    }

    public static void main(String[] args) {
        int result[]= sortedSquares(new int[]{-4,-1,0,3,10});
        Arrays.stream(result).forEach(System.out::println);
    }
}
