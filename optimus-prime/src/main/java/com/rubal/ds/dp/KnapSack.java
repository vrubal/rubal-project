package com.rubal.ds.dp;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class KnapSack {
    static int knapSackRec(int W, int wt[], int val[], int n, int [][] dp)
    {
        if(W==0 || n==0)
            return 0;
        if(dp[n][W] != -1)
            return dp[n][W];

        if(W >= wt[n-1])
            return dp [n][W] = Math.max(val[n-1]+knapSackRec(W-wt[n-1], wt, val, n-1, dp),
                    knapSackRec(W, wt, val, n-1,dp));
        else{
            return dp[n][W] = knapSackRec(W, wt, val, n-1,dp);
        }
    }
    static int knapSack(int W, int wt[], int val[], int n) {
        int dp[][] = new int[n+1][W+1];
        for(int i=0;i<n+1; i++){
            for(int j=0;j<W+1;j++){
                dp[i][j] = -1;
            }
        }
        knapSackRec(W, wt, val, n, dp);
        for(int i=0;i<n+1; i++){
            for(int j=0;j<W+1;j++){
                System.out.print(dp[i][j] +" ");
            }
            System.out.println();
        }
        return dp[n][W];

    }
    public static void main(String[] args) {
        int W = 4;
        int []wt = new int[]{4, 5 ,6};
        int value[] = new int[]{2, 4, 6};
        System.out.println(knapSack(W, wt, value, 3));

    }
}
/*
        0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
      0 0 0 0 0 0 0 0 0 0 0  0  0  0  0  0  0
  4 2 1 0 0 0 0 2 2 2 2 2 2  2  2  2  2  2  2
  5 4 2 0 0 0 0 0 4 4 4 4 6  6  6  6  6  6  6
  6 6 3 0 0 0 0 0 0 6 6 6 6  8  8  8  8  8  12




 */