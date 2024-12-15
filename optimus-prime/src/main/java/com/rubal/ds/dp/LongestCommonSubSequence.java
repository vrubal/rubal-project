package com.rubal.ds.dp;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class LongestCommonSubSequence {
    public static int LCS(String x, String y, int n, int m){
        if(n==0||m==0)
            return 0;
        if(x.charAt(n-1)==y.charAt(m-1))
            return 1+LCS(x,y,n-1,m-1);
        else
            return Math.max(LCS(x,y,n-1,m), LCS(x,y,n,m-1));
    }

    public static int LCSTopDown(String x, String y, int n, int m){
        int dp[][] = new int[n+1][m+1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if(x.charAt(i-1)==y.charAt(j-1))
                    dp[i][j] = 1+dp[i-1][j-1];
                else
                    dp[i][j]=Math.max(dp[i-1][j],dp[i][j-1]);
            }
        }
        return dp[n][m];
    }

    public static void main(String[] args) {
        String s1 = "abcdefh";
        String s2 = "abclgfh";
        System.out.println(LCS(s1,s2, 7,7));
        System.out.println(LCSTopDown(s1,s2, 7,7));
    }
}
