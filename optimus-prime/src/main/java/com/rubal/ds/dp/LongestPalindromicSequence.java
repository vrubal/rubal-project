package com.rubal.ds.dp;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class LongestPalindromicSequence {
    public static int findLongestPalindromicSeq(int l, int r, String s, int mem[][]){
        if(l==r) return 1;
        if(l>r) return 0;
        if(mem[l][r] !=0 ) return mem[l][r];
        return mem[l][r] = s.charAt(l)==s.charAt(r) ? 2 + findLongestPalindromicSeq(l+1,r-1, s,mem) :
                Math.max(findLongestPalindromicSeq(l+1,r, s,mem),
                        findLongestPalindromicSeq(l,r-1, s,mem));

    }

    public static void main(String[] args) {
        String s = "ABABABAS";
        int dp[][] = new int[s.length()][s.length()];
        int l = findLongestPalindromicSeq(0, s.length()-1, s, dp);
        System.out.println("length"+l);
        for (int i = 0; i <s.length(); i++) {
            for (int j = 0; j < s.length(); j++) {
                System.out.print(dp[i][j]+" ");
            }
            System.out.println();
        }
     }
}
