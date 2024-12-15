package com.rubal.ds.array;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class SumOfMiddleElements {

    public static int sumOfMiddlePair(int[]a, int[]b) {
        int n = a.length;
        int cnt = 0;
        int i = 0;
        int j = 0;
        int m1 = 0;
        int m2 = 0;
        while (cnt <= n) {
            /*if(i==n){
                m1=m2;
                m2=b[0];
                break;
            }else if(j==n){
                m1=m2;
                m2=a[0];
                break;
            }*/
            if (a[i] <= b[j]) {
                m1=m2;
                m2=a[i];
                i++;
            } else {
                m1=m2;
                m2=b[j];
                j++;
            }
            cnt++;
        }
        return (m1 + m2) / 2;
    }
    public static int sumOfMiddlePair2(int[]a, int[]b) {
        int n = a.length;
        int  i=0, j=0;
        int m1=-1, m2=-1;
        int cnt = n;
        while(cnt>=0){
            if(i==n){
                m1=a[i-1];
                m2=b[0];
                break;
            }
            if(j==n){
                m2=b[j-1];
                m1=b[0];
                break;
            }
            if(a[i] <= b[j]){
                m1 = a[i];
                i++;
            }else{
                m2=b[j];
                j++;
            }
            cnt--;
        }
         return (m1+m2);
    }
    public static void main(String[] args) {
        //1 2 3 4 5 6 7 8
        //1 1 1 1 2 4 6 8
        //1 1 1 1 2 2 3 4
        //2 2 2 2 3 3 5 6
        //2 2 2 2 3 4 5 5 6 8
        //5 5 7 9 10 10 10 12 13 16 16 17 18 18 20 22 24 26 27 29 29 29 29
        int ar1[] = {5,10,12,16,17,18,22,24,27,29,29,29};
        int ar2[] = {5,7,9,10,10,13,16,18,20,22,26,29};
        //Arrays.sort(ar1, Comparator.comparingInt((int a,int b) ->{ return a>b;}));
        System.out.println(sumOfMiddlePair2(ar1, ar2));
        //System.out.println(sumOfMiddlePair(ar1, ar2));

    }
}
