package com.rubal.ds.array;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class TrappingWater {
    public static int measureWater(int[] nums){
        int left=0,lMax=0,right=nums.length-1,rMax=0,sum=0;
        while(left<=right) {
            if(lMax < rMax) {
                if (nums[left] > lMax) {
                    lMax = nums[left];
                } else {
                    sum += (lMax - nums[left]);
                }
                left++;
            }else {
                if (nums[right] > rMax) {
                    rMax = nums[right];
                } else {
                    sum += (rMax - nums[right]);
                }
                right--;
            }
        }
        return sum;

    }
    public static void main(String[] args) {
        int arr[] = new int[]{4,2,0,6,2,0,3,5};
        System.out.println(measureWater(arr));
    }
}
