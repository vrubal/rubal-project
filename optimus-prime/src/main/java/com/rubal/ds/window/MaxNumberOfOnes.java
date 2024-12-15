package com.rubal.ds.window;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 * Given a binary array nums and an integer k,
 * return the maximum number of consecutive 1's in the array if you can flip at most k 0's.
 */
public class MaxNumberOfOnes {
    public static int maxNumberOfOnes(int arr[], int k){
        int l=0, r=0, n= arr.length;
        int maxLen = 0;
        int zeros = 0;
        while(r<n){
            if(arr[r]==0)
                zeros++;
            while(zeros>k){
                if(arr[l]==0)
                    zeros--;
                l++;
            }
            maxLen = Math.max(maxLen, r-l+1);
            r++;
        }
        return maxLen;
    }

    public static void main(String[] args) {
        int arr[] = {1,1,1,0,0,0,1,1,1,1,0};
        int k=1;
        var result = maxNumberOfOnes(arr, k);
        System.out.println(result);
    }
}
