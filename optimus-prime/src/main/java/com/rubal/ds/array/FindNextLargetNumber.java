package com.rubal.ds.array;

public class FindNextLargetNumber {


    public static int nextLargestNumber(int [] n){

        int k=n.length-1;
        int i=0;
        int[] result = new int[n.length];
        int prev = n[k];
        while(i<n.length){
            if(i==0){
                result[k--]=-1;
            }else{
                if(prev < n[k]){

                }
            }
        }
        return 0;
    }
}
