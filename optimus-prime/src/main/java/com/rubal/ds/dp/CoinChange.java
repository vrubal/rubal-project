package com.rubal.ds.dp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class CoinChange {
    static int min = Integer.MAX_VALUE;
    public static long count(int coins[], int N, int sum) {
        if (sum == 0)
            return 1;

        if (sum < 0)
            return 0;

        if (N <= 0)
            return 0;

        return count(coins, N, sum - coins[N - 1]);
    }
    public static void allPossibleWays(int amount, int[] coins, List<Integer> tList, List<List<Integer>> finalList) {
        if(amount == 0 && tList.size() < min){
            List<Integer> t = new ArrayList<>();
            t.addAll(tList);
            finalList.add(t);
            min = t.size();
        }
        for (int i = 0; i < coins.length; i++) {
            if(amount-coins[i] >= 0) {
                tList.add(coins[i]);
                allPossibleWays(amount-coins[i], coins, tList, finalList);
                tList.remove(tList.size()-1);
            }
        }
    }
    public static int changeHelper(int amount, int[] coins, int currentIndex, int[][] dp) {

        if(amount == 0) return 1;
        if (amount < 0 || currentIndex >= coins.length) return 0;
        if(dp[currentIndex][amount] == -1) {

            dp[currentIndex][amount] = changeHelper(amount - coins[currentIndex], coins, currentIndex, dp)
                    + changeHelper(amount, coins, currentIndex + 1, dp);
        }
        return dp[currentIndex][amount];
    }
    public static void main(String[] args) {
        int arr[] = new int[]{1,2,3};
        int n = 5;
        int[][] dp = new int[arr.length+1][n+1];
        /*for (int i = 0; i < arr.length+1; i++) {
            for (int j = 0; j <n+1; j++) {
                dp[i][j] = -1;
            }
        }

        System.out.println(changeHelper(n, arr, 0, dp));
        for (int i = 0; i < arr.length+1; i++) {
            for (int j = 0; j <n; j++) {
                System.out.println(dp[i][j]);
            }
        }*/
        List<List<Integer>> list = new ArrayList<>();
        allPossibleWays(n, arr, new ArrayList<>(),list);
        list.stream().forEach(System.out::println);

    }
}
