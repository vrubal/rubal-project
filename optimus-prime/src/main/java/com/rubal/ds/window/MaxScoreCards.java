package com.rubal.ds.window;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 * There are several cards arranged in a row, and each card has an associated number of points.
 * The points are given in the integer array cardPoints.
 *
 * In one step, you can take one card from the beginning or from the end of the row.
 * You have to take exactly k cards.
 *
 * Your score is the sum of the points of the cards you have taken.
 *
 * Given the integer array cardPoints and the integer k, return the maximum score you can obtain.
 */
public class MaxScoreCards {

    public static int maxScoreCards(int []arr, int k){
        int l=0, r=0, max=0;
        int lsum=0, rsum=0;
        for(int i=0;i<k;i++)
            lsum+=arr[i];
        max=Math.max(max, lsum+rsum);
        for (int i = k-1; i >=0; i--) {
            lsum-=arr[i];
            rsum+=arr[arr.length-k+i];
            max=Math.max(max, lsum+rsum);
        }
        return max;
    }
    public static void main(String[] args) {
        var arr = new int[]{1,2,3,4,5,6,1};
        int result = maxScoreCards(arr, 3);
        System.out.println(result);
    }
}
