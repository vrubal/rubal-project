package com.rubal.ds.sorting;

import java.util.Arrays;

public class BubbleSort {

    // Swap if element is greater then next element
    // Repeat steps till all elements sorted
    public static void sort(int arr[]){
        int cnt=0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length-1-i; j++) {
                if(arr[j]>arr[j+1]){
                    int tmp = arr[j];
                    arr[j]=arr[j+1];
                    arr[j+1]=tmp;
                }
                cnt++;
            }
        }
        System.out.println(cnt);
    }

    public static void sortRec(int arr[], int n){
        for (int j = 0; j < arr.length-1-n; j++) {
            if(arr[j]>arr[j+1]){
                int tmp = arr[j];
                arr[j]=arr[j+1];
                arr[j+1]=tmp;
            }
        }
        if(n< arr.length)
            sortRec(arr, n+1);
    }
    public static void main(String[] args) {
        int arr[]=new int[]{10,9,8,7,6,5,4,3,2,1};
        sort(arr);
        Arrays.stream(arr).forEach(System.out::println);
        System.out.println("---------------------------");
        int arr1[]=new int[]{10,9,8,7,6,5,4,3,2,1};
        sortRec(arr1,0);
        Arrays.stream(arr1).forEach(System.out::println);
    }
}
