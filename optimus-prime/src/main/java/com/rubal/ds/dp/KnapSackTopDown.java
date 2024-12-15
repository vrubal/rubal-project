package com.rubal.ds.dp;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class KnapSackTopDown {
    public static void main(String[] args) {
        int W = 15;
        int []wt = new int[]{4, 5 ,6};
        int value[] = new int[]{2, 4, 6};
        int n= 3;
        int dp [][] = new int[4][16];
        for(int i=0; i<4;i++){
            dp[i][0] = 0;
        }
        for(int i=0; i<16;i++){
            dp[0][i] = 0;
        }
        for(int i=1;i<4;i++){
            for (int j=1;j<16;j++){
                if(wt[i-1] <= j) {
                    dp[i][j] = Math.max(value[i - 1] + dp[i - 1][j - wt[i - 1]],
                            dp[i - 1][j]);
                }else{
                    dp[i][j] = dp[i-1][j];
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 16; j++) {
                System.out.print(dp[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println(dp[3][15]);
    }
}
