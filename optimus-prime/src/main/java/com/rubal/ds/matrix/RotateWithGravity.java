package com.rubal.ds.matrix;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class RotateWithGravity {
    public static void arrangeColumn(char[][] arr, int row) {
        int start = 0, end = arr[0].length-1;
        int last = end, low = start;
        while(end >= 0){
            if(arr[row][end] == '*'){
                last = end-1 ;
            }else if(arr[row][end] == '.' && arr[row][last] == '#'){
                last--;
            } else if(arr[row][end] == '#' && end < last && arr[row][last]=='.'){
                arr[row][end] = '.';
                arr[row][last] = '#';
                last --;
            }
            end --;
        }
    }
    public static void main(String[] args) {
        char [][] arr = new char[][] {{'#', '#', '*', '.'}};
        arrangeColumn(arr, 0);
    }
}
