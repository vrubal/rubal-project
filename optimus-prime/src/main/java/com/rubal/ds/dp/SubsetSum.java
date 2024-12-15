package com.rubal.ds.dp;

import java.util.Arrays;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class SubsetSum {

    public static int subSetsumRec(int sum, int currIdx, int num[]){
        if(sum==0)
            return 1;
        if(currIdx >= num.length)
            return 0;
        if(sum < 0)
            return 0;
        return subSetsumRec(sum-num[currIdx], currIdx+1, num) +
                    subSetsumRec(sum, currIdx+1, num);

    }
    public static void main(String[] args) {
        int W = 4;
        int []wt = new int[]{3, 5 ,6, 7};
        int value[] = new int[]{2, 4, 6};
        Arrays.stream(wt).max().getAsInt();
        System.out.println(subSetsumRec(9, 0, wt));
    }
}
