package com.rubal.ds.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathFindingMatrix {
    public static void print(int[][] mat){
        for(int i=0;i<mat.length; i++){
            for(int j=0;j<mat[0].length;j++){
                System.out.print(mat[i][j]+" ");
            }
            System.out.println("");
        }
    }
    public static int countPath(int m, int n, int[][] mat){
        System.out.println("m:"+m+"n:"+n);
        if(m<0 || n<0){
            return 0;
        }
        if(mat[m][n] > 1){
            return mat[m][n];
        }
        if(m==0 && n==0){
            System.out.println("---------");
            print(mat);
            System.out.println("---------");
            return 1;
        }else{
            mat[m][n]=countPath(m-1, n, mat)+countPath(m,n-1,mat);
            return mat[m][n];
        }
    }


    public static void main(String[] args) {
        int m=30, n=30;
        List<Integer> l = new ArrayList<>();
         countPath(m-1, n-1, new int[m][n]);

    }




}
