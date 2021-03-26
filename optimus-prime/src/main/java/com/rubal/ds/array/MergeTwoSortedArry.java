package com.rubal.ds.array;

import java.util.Arrays;

public class MergeTwoSortedArry {
    public static int[] merge(int[] nums1, int m, int[] nums2, int n) {
        int[] result = new int[m+n+2];
        int i=0,j=0,k=0;
        while(i<=m && j<=n){
            if(nums1[i] < nums2[j]){
                result[k++]=nums1[i++];
            }else{
                result[k++]=nums2[j++];
            }
        }
        while(i<=m){
            result[k++]=nums1[i++];
        }
        while(j<=n){
            result[k++]=nums2[j++];
        }
        return result;
    }

    public static void main(String[] args) {
        int[] arr1=new int[]{1,1,2,0,0,0};
        int[] arr2=new int[]{2,3,6};

        int[] merge = merge(arr1, 2, arr2, 2);
        Arrays.stream(merge).forEach(System.out::println);

    }

}
