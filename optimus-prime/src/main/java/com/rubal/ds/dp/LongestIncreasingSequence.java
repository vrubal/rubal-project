package com.rubal.ds.dp;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class LongestIncreasingSequence {

    public static int LIS(int arr[], int idx, int prev, int [][]dp){
        if(idx == arr.length)
            return 0;
        if(dp[idx][prev] != -1)
            return dp[idx][prev];
        int exclude = LIS(arr, idx+1, prev,dp);
        int include = 0;
        if(prev == 0 || arr[idx] > arr[prev-1])
            include = 1+ LIS(arr, idx+1, idx,dp);
        return dp[idx][prev] = Math.max(include, exclude);
    }
    public static void main(String[] args) {
        //int [] arr = new int[]{1,4,2,3,1,7,8};
        //int [] arr = new int[]{1,8,5,6,4,9};
        int [] arr = new int[]{9,1,2,3,1,5};
        int dp[][] = new int[arr.length+1][arr.length+1];
        for (int i = 0; i < arr.length+1; i++) {
            for (int j = 0; j < arr.length+1; j++) {
                dp[i][j] = -1;
            }
        }
        int mx = LIS(arr, 0, 0, dp);
        System.out.println(mx);
    }
}
