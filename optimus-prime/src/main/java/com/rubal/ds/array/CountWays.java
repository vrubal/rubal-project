package com.rubal.ds.array;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class CountWays {
    public static void main(String[] args) {
            int n=94;
            int Sum = 336;
            long start = (long) Math.pow(10,n-1);
            long end = (long) (Math.pow(10,n)-1);
            int count = 0;
            while(start < end){

                int currSum = 0;
                long s = start;
                while(s!=0){
                    currSum += s%10;
                    s=s/10;
                }
                if(currSum == Sum)
                    count++;

                start++;
            }
        System.out.println(count);;


    }
}
