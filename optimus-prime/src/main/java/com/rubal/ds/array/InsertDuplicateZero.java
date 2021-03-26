package com.rubal.ds.array;

import java.util.Arrays;

public class InsertDuplicateZero {


    public  static void shift(int[] arr, int start){
        int prev=arr[start];
        for (int i = start; i <arr.length-1; i++) {
            int tmp = arr[i+1];
            arr[i]=prev;
            prev=tmp;
        }

    }
    public static void duplicateZeros(int[] arr) {
        /*int prev=0;
        for(int i=0;i<arr.length-1;i++){

            if(arr[i]==0 && arr[i+1]!=0){
                shift(arr, i+1);
                arr[++i]=0;
            }
        }*/
        int count = 0;
        for(int i : arr) {
            if(i == 0) {
                count++;
            }
        }
        int len = arr.length;
        for(int i = len - 1; i >= 0; i--) {
            int temp = i + count;
            if(temp < len) {
                arr[temp] = arr[i];
            }
            if(arr[i] == 0) {
                count--;
                temp = i + count;
                if(temp < len) {
                    arr[temp] = arr[i];
                }
            }
        }

    }

    public static void main(String[] args) {
        int[]arr= new int[]{1,0,2,3,0,4,5,0};
        duplicateZeros(arr);
        Arrays.stream(arr).forEach(System.out::println);
    }
}
