package com.rubal.ds.dp;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class LongestCommonSubString {
    public static int longestCommonSubString(String x, String y, int m, int n, int count){

        if(m==0||n==0)
            return count;

        if(x.charAt(n-1)==y.charAt(m-1)){
            count = longestCommonSubString(x,y,m-1, n-1, count+1);
        }else{
            count = Math.max(count,Math.max(longestCommonSubString(x,y,m, n-1, 0),longestCommonSubString(x,y,m-1, n, 0)));
        }
        return count;
    }
    public static int longestCommonTopDown(String x, String y, int m, int n, int count){
        int max = 0;
       int dp[][] = new int[m+1][n+1];
        for (int i = 1; i < m + 1; i++) {
            for (int j = 1; j < n + 1; j++) {
                if(x.charAt(i-1)==y.charAt(j-1)) {
                    dp[i][j] = Math.max(dp[i][j], 1 + dp[i - 1][j - 1]);
                    max = Math.max(dp[i][j],max);
                }
                else
                    dp[i][j] = max;
            }
        }
        return dp[m][n];
    }
    public static void main(String[] args) {
        String s1="abcfgt";
        String s2="abcsdt";
        System.out.println(longestCommonSubString(s1,s2,6,6,0));
        System.out.println(longestCommonTopDown(s1,s2,6,6,0));

    }
}
