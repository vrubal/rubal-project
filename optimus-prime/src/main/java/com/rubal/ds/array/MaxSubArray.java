package com.rubal.ds.array;

import java.util.ArrayList;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class MaxSubArray {

       static ArrayList<Integer> findSubarray(int a[], int n) {
            int start =0, end=0, s=0, max = Integer.MIN_VALUE, sum=0;
            int maxEnd=0,maxStart=0;
            ArrayList<Integer> res = new ArrayList<>();
            for(int i=0;i<n;i++){
                if( a[i] < 0){
                    sum=0;
                    s = i+1;
                }else{
                    sum+=a[i];
                    System.out.println("sum:"+sum);
                    if(sum>max){
                        max = sum;
                        start = s;
                        end = i;
                        //if(end-start > maxEnd-maxStart){
                        /*System.out.println("sum:"+sum);
                        System.out.print("end:"+end);
                        System.out.println(" start:"+start);
                        System.out.print("maxEnd:"+maxEnd);
                        System.out.println(" maxStart:"+maxStart);*/
                            maxStart=start;
                            maxEnd=end;
                        //}

                    }
                    if(sum==max && i-s > maxEnd-maxStart){
                        maxStart=start;
                        maxEnd=end;
                    }
                }

            }
       /* System.out.println("sum:"+sum);
        System.out.println("start:"+start);
        System.out.println("end:"+end);*/

            for(int i=maxStart;i<=maxEnd;i++){
                res.add(a[i]);
            }
            return res;

        }

    public static void main(String[] args) {
        //int arr[]=new int[]{117,359,190,291,231,440,25,37,441,178,95,353,318,478,387,347,110,68,128,409,17,441,245,81,108,88,393,250,233,161,383,295,311,77,-90,162,392,385,37,110,181,202,68,364,180,354,30,355,443,361,218,48,248,256,406,389,177,496,243,162,448,458,357,483,89,128,149,187};
        int arr[]=new int[]{2,-1,1,1};
        findSubarray(arr, arr.length);
    }

}
