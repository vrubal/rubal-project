package com.rubal.ds.leetcode;


/**
 * @author vpremlal
 * @created
 * @project rubal-project
 * 30,40,60,70,80
 * 30,40 --- 40+30  [30,60,70,80] [40]
 * 80,70 --- 80+40  [30,60,40] [70,80]
 * 30 60 --- 60 + 30 [30,40] [70,80,60]
 * 30,40 --- 40
 *
 *
 *
 *
 */
public class OptimumRiverCrossingCost {
    public static int findMinimumRiverCrossingCost(int cost[]){
        int totalCost = 0;
        for (int i = cost.length-1; i >= 1; i-=2) {
            if (i == 1) {
                totalCost += Math.max(cost[1] , cost[0]);
            } else
                totalCost += Math.min(cost[i] + cost[i - 1] + 2 * cost[0],
                        cost[i] + 2 * cost[1]+cost[0]);

        }

        return totalCost;
    }
    public static void main(String[] args) {
        int arr[] = new int[]{30,40,60,70, 80};
        System.out.println(findMinimumRiverCrossingCost(arr));
    }
}
