package com.rubal.ds.array;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class RotateArray {
    public static void rotate(int[] nums, int k) {
        int n = nums.length;
        for(int i=k;k>=1;k--){
            int t = nums[n-i];
            int start = k-i;
            for(int j=start; j<n-1-start;j++){
                int hold = nums[j];
                nums[j] = t;
                t = hold;
            }
        }
    }

    public static void main(String[] args) {
        int [] n = new int[]{1,2,3,4,5,6,7};
        rotate(n, 3);
    }
}
