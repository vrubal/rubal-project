package com.rubal.ds.company;

import java.util.*;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */

/*
Find the missing numbers between 0 to n
input : 8,0,6,4
output :1,2,3,5,7
 */
public class FindMissingNumbers {

    public long[] printFirstNegativeInteger(long A[], int N, int K){

        int i=0, j=0, idx=0;
        long res[] = new long[N-K+1];
        Queue<Integer> q = new LinkedList<>();


        while(j<N){
            if(A[j]<0)
                q.add(j);
            if(j-i+1==K ){
                if(!q.isEmpty() && q.poll() >= i && q.poll() <= j)
                    res[idx++] = A[q.remove()];
                i++;
            }
            j++;
        }
        return res;

    }
    public static List<Integer> findMissingNumbers(int[] arr){
        List<Integer> missingNumbers = new ArrayList<>();
        Set<Integer> availableNumbers = new HashSet<>();
        if(arr.length>0);
        int max = Arrays.stream(arr).max().getAsInt();
        int min = Arrays.stream(arr).min().getAsInt();
        int idx = 0;
        Arrays.stream(arr).forEach(value -> availableNumbers.add(value));
        for(int i=min;i<=max;i++){
            if(!availableNumbers.contains(i)){
                missingNumbers.add(i);
            }
        }
        return missingNumbers;
    }

    public static void main(String[] args) {
        System.out.println(findMissingNumbers(new int[]{8,0,6,4}));
    }
}
