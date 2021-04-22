package com.rubal.ds.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PascalTriangle {

    public static void printPascalTriangle(int num){
        int arr [] = new int[num+1];
        arr[0]=1;
        for(int i=1;i<=num;i++){
            for(int j=i;j>0;j--){
                arr[j]=arr[j]+arr[j-1];
            }
        }

        List<Integer> list = new ArrayList<Integer>();
        Arrays.stream(arr).forEach(list::add);
    }
    public static void main(String[] args) {

        /*printPascalTriangle(0);
        System.out.println("");
        printPascalTriangle(1);
        System.out.println("");*/
        printPascalTriangle(10);
    }
}
