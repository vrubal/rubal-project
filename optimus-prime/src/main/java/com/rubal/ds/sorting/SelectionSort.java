package com.rubal.ds.sorting;

import java.util.Arrays;

public class SelectionSort {

    // Find smallest element in the array
    // Place the smallest element at i
    // Where i with start with 0, increment i
    // Repeat above steps till all elements sorted
    public static void sort(int arr[]){
        for (int i = 0; i < arr.length-1; i++) {
            int minIdx=i;
            for (int j = i+1; j < arr.length; j++) {
                if(arr[j]<arr[minIdx]){
                    minIdx=j;
                }
            }
            int temp=arr[i];
            arr[i]=arr[minIdx];
            arr[minIdx]=temp;

        }
    }

    public static void sortRec(int arr[], int n){
         int minIdx=n;
            for (int j = n+1; j < arr.length; j++) {
                if(arr[j]<arr[minIdx]){
                    minIdx=j;
                }
            }
            int temp=arr[n];
            arr[n]=arr[minIdx];
            arr[minIdx]=temp;
            if(n<arr.length-1)
                sortRec(arr, n+1);
    }

    public static void main(String[] args) {
        int[]arr=new int[]{9,8,7,6,5,4,3,2,1};
        sort(arr);
        Arrays.stream(arr).forEach(System.out::println);
        int[]arr1=new int[]{2,4,1,6,9,3,5};
        sortRec(arr1,0);
        Arrays.stream(arr1).forEach(System.out::println);
    }
}
