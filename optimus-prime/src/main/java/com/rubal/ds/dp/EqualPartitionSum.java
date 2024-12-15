package com.rubal.ds.dp;

import java.util.Arrays;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class EqualPartitionSum {
    public static boolean subSetsumRec(int sum, int currIdx, int num[]){
        if(sum==0)
            return true;
        if(currIdx >= num.length)
            return false;
        if(sum < 0)
            return false;
        return subSetsumRec(sum-num[currIdx], currIdx+1, num) ||
                subSetsumRec(sum, currIdx+1, num);

    }
    public static boolean subSetsumDPTopDown(int nums[]){
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if ((sum & 1) == 1) {
            return false;
        }
        sum /= 2;
        int n = nums.length;
        boolean[][] dp = new boolean[n+1][sum+1];
        for (int i = 0; i < dp.length; i++) {
            Arrays.fill(dp[i], false);
        }
        dp[0][0] = true;
        for (int i = 1; i < n+1; i++) {
            dp[i][0] = true;
        }
        for (int j = 1; j < sum+1; j++) {
            dp[0][j] = false;
        }
        for (int i = 1; i < n+1; i++) {
            for (int j = 1; j < sum+1; j++) {
                dp[i][j] = dp[i-1][j];
                if (j >= nums[i-1]) {
                    dp[i][j] = (dp[i][j] || dp[i-1][j-nums[i-1]]);
                }
            }
        }

        for (int i = 0; i < n+1; i++) {
            for (int j = 0; j < sum + 1; j++) {
                System.out.print(dp[i][j] + " ");
            }
            System.out.println("");
        }
        return dp[n][sum];
    }
    public static void main(String[] args) {
        int arr[]  = new int[]{4,6,6,8};
        int sum = Arrays.stream(arr).sum();
        //System.out.println(subSetsumRec(sum/2, 0, arr));
        System.out.println(subSetsumDPTopDown( arr));

    }
}
