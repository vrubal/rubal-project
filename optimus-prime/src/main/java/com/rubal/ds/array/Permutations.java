package com.rubal.ds.array;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class Permutations {
    public static void permutate(List<List<Long>> l, List<Long> t, long nums[]){

        if(t.size()==nums.length){
            l.add(new ArrayList<>(t));
            return;
        }
        for(int i=0;i<nums.length;i++){
            if(t.contains(nums[i]))
                continue;
            t.add(nums[i]);
            permutate(l,t,nums);
            t.remove(t.size()-1);
        }
    }
    public static void main(String []args )
    {
        int n = 4;
        long[] arr = new long [(int)n];
        List<Long> t = new ArrayList<>();
        List<List<Long>> f = new ArrayList<>();
        for(int i=0;i<n; i++)
            arr[i] = i+1;

        permutate(f, t, arr);
        System.out.println(f);
        System.out.println(f.size());
    }
}
