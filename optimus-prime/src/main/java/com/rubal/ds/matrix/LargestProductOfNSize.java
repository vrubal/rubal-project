package com.rubal.ds.matrix;

public class LargestProductOfNSize {






    public static long  findMaxProduct(int i, int j, int k, long[][] matrix){
        int cL = matrix[0].length;
        int rL = matrix.length;
        long maxProduct=0;
        long product = 1;
        //find max product in right-side
        for(int a=j, count=k; count>=1 && a<cL && k<=cL ; a++, count--){
            product*=matrix[i][a];
        }
        if(product !=0 ){
            maxProduct = checkIfMaximum(maxProduct, product);
        }
        //find max product in left-side
        for(int a=j, count=k; count>=1 && a>=0 && k<=cL ; a--, count--){
            product*=matrix[i][a];
        }
        if(product !=0 ){
            maxProduct = checkIfMaximum(maxProduct, product);
        }
        //find max product in up-side
        for(int b=i, count=k; count>=1 && b>=0 && k<=rL ; b--, count--){
            product*=matrix[b][j];
        }
        if(product !=0 ){
            maxProduct = checkIfMaximum(maxProduct, product);
        }

        //find max product in down-side
        for(int b=i, count=k; count>=1 && b<rL && k<=rL ; b++, count--){
            product*=matrix[b][j];
        }
        if(product !=0 ){
            maxProduct = checkIfMaximum(maxProduct, product);
        }

        return maxProduct;

    }

    public static long checkIfMaximum(long maxProduct, long product) {

        if(maxProduct < product){
            maxProduct = product;
        }
        return maxProduct;
    }


    public static void main(String[] args) {
        long[][] matrix = new long[][]{{1,2,3,4},{1,2,3,4},{1,2,3,4},{1,2,3,4}};
        long max = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                long product = findMaxProduct(i,j,3, matrix);
                if( max < product){
                    max = product;
                }
            }
        }

        System.out.println(max);

    }
}
