package com.rubal.ds.matrix;

import java.util.Arrays;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class SPiralMatrix {
        public static int[][] generateMatrix(int n) {
            int rowStart = 0;
            int rowEnd = n-1;
            int colStart = 0;
            int colEnd = n-1;
            int num = 1;
            int lim = n*n;

            int arr[][] = new int[n][n];
            while(n<=lim){
                for(int i=colStart; i<=colEnd; i++){
                    arr[rowStart][i] = num++;
                }
                rowStart++;
                for(int i=rowStart; i<=rowEnd; i++){
                    arr[i][colEnd] = num ++;
                }
                colEnd--;
                for(int i=colEnd; i>=colStart;i--){
                    arr[rowEnd][i] = num ++;
                }
                rowEnd--;
                for(int i=rowEnd; i>=rowStart;i--){
                    arr[i][colStart] = num ++;
                }
                colStart++;

            }
            return arr;
        }

    public static void main(String[] args) {
        generateMatrix(3);
    }
}
