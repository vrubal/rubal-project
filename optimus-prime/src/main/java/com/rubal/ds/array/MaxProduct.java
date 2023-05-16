package com.rubal.ds.array;

/**
 * @author vubal
 * @created
 * @project rubal-project
 */
public class MaxProduct {
    public static void main(String[] args) {
        int arr [] = new int[]{6 , -3, -10, 0 ,1};

        int res = arr[0];
        int max = res;
        int min = res;
        for(int i=1;i<arr.length;i++){
            if(arr[i] < 0){
                int t = max;
                max = min;
                min = t;
            }
            max = Math.max(arr[i], max*arr[i]);
            min = Math.min(arr[i], min*arr[i]);
            res = Math.max(res, max);
        }
        System.out.println("max:"+res);
    }
}
