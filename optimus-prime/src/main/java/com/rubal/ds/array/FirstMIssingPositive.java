package com.rubal.ds.array;

/**
 * 8788146724
 * @author vrubal
 * @created
 * @project rubal-project
 *
 *  0 1 2 3 4 5
 *  6 -3 6 -2 -1 4
 *  3 0 2 1 4 -1
 * -3 0 -2 -1 -4
 */
public class FirstMIssingPositive {
    public static void swap(int nums[], int i, int j){
        int t = nums[i];
        nums[i] = nums[j];
        nums[j] = t;
    }
    public static int firstMissing(int [] nums, int n){
        for(int i = 0; i < n; ++ i)
            while(nums[i] > 0 && nums[i] <= n
                    && nums[nums[i] - 1] != nums[i])
                swap(nums, i, nums[i] - 1);

        for(int i = 0; i < n; ++ i)
            if(nums[i] != i + 1)
                return i + 1;

        return n + 1;
    }
    public static int firstMissingPositive(int nums[], int n)
    {
        int pIdx=0, nIdx=nums.length;
        while(pIdx<nIdx){
            if(nums[pIdx]<=0){
                int t = nums[pIdx];
                nums[pIdx] = nums[--nIdx];
                nums[nIdx] = t;
            }
            else
                pIdx++;
        }
        //nIdx--;
        for(int i=0;i<nIdx;i++){
            if(Math.abs(nums[i])<=nIdx
                    && nums[Math.abs(nums[i])-1]>0){
                nums[Math.abs(nums[i])-1]*=-1;
            }
        }
        for(int i=0;i<nIdx;i++){
            if(nums[i]>0){
                i+=1;
                return i;
            }
        }
        return pIdx+1;
    }

    public static void main(String[] args) {
        int [] arr = new int[]{3,4,-1,1};
        System.out.println(firstMissing(arr, arr.length));
    }
}
