package com.rubal.ds.sorting;

import java.util.Arrays;

public class MergeSort {

    public static void merge(int[]left, int[]right, int arr[], int ls, int rs){
        System.out.println("Before-----------------------------"+arr.length);
        Arrays.stream(left).forEach(System.out::println);
        Arrays.stream(right).forEach(System.out::println);
        System.out.println("Before-----------------------------");
        int i=0,l=0,r=0;
        while(l<ls && r<rs){
            if(left[l]<right[r]){
                arr[i++]=left[l++];
            }else{
                arr[i++]= right[r++];
            }
        }
        while (l<ls){
            arr[i++]=left[l++];
        }
        while(r<rs){
            arr[i++]= right[r++];
        }
        System.out.println("After-----------------------------");
            Arrays.stream(arr).forEach(System.out::println);
        System.out.println("After-----------------------------");
    }
    public static void sort(int arr[], int len){
        if(len<2)
            return;
        int mid=len/2;
        int[] left = new int[mid];
        int[] right = new int[len-mid];
        int k=0;
        for (int i = 0; i < len; i++) {
            if(i<mid){
                left[i]=arr[i];
            }else{
                right[k]=arr[i];
                k++;
            }
        }
        sort(left, mid);
        sort(right, len-mid);
        merge(left, right, arr, mid, len-mid);
    }


    public static void main(String[] args) {
        int[]arr=new int[]{12,1,10,50,5,15,45};
        sort(arr, arr.length);
        Arrays.stream(arr).forEach(System.out::println);

    }
}
