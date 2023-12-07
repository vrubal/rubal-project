package com.rubal.ds.array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */

/*
Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.

You may assume that each input would have exactly one solution, and you may not use the same element twice.

You can return the answer in any order.

Example 1:

Input: nums = [2,7,11,15], target = 9

Output: [0,1]

Explanation: Because nums[0] + nums[1] == 9, we return [0, 1].



Example 2:

Input: nums = [3,2,4], target = 6

Output: [1,2]



Example 3:

Input: nums = [3,3], target = 6

Output: [0,1]

 // Retail banking
 // LeetCode --
 // IAM
 // Terraform/ Kafka
 // Auth Security
 // Resilience --

// Explain solution before starting to code8/
 */
public class FindPair {

    public static List<Integer> findPair(int [] arr, int target){
        Map<Integer, Integer> map = new HashMap <>();
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i <arr.length; i++) {
            if(map.containsKey(target-arr[i])){
                result.add(map.get(target-arr[i]));
                result.add(i);
                break;
            }else{
                map.put(arr[i], i);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(findPair(new int[]{3,2,4}, 6));
        System.out.println(findPair(new int[]{3,2,4}, 7));
    }

}
