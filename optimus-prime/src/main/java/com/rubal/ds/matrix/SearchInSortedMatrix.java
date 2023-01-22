package com.rubal.ds.matrix;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class SearchInSortedMatrix {

    public static final boolean search(int[][]mat, int target){
        int k =3;
        int row = -1;
        int m = mat.length;
        int n = mat[0].length;

        int s = 0, e = m*n-1;
        while(s<=e){
            int mid = (s+e)/2;
            //System.out.println("mid"+mid);
            //System.out.println("mid/n: "+mid/n +" mid%n: "+mid%n);
            if(mat[mid/n][mid%n] == target) {
                return true;
            }
            if(mat[mid/n][mid%n] < target) {
                s = mid + 1;
            }
            else
                e=mid-1;
        }

        return false;
    }
    public static final boolean search2(int[][]mat, int target){
        int k =3;
        int row = -1;
        int m = mat.length;
        int n = mat[0].length;
        int i=0, j=n-1;
        while(i<m && j>=0) {
            if (mat[i][j] == target)
                return true;
            if (mat[i][j] < target)
                i++;
            else
                j--;
        }
        return false;
    }
    public static void main(String[] args) {
        int [][] mat = new int[][]{
                {1,2,3,4},
                {5,6,7,8},
                {9,10,11,12},
                {13,14,15,16}
        };
        System.out.println(search(mat, 10));
        System.out.println(search2(mat, 10));

    }
}
