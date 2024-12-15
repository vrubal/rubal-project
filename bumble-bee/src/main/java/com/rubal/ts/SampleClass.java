package com.rubal.ts;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class SampleClass {


    public static void main(String[] args) {
        int [] a = new int[]{2,4,5,25,625};
        Set<Integer> set = new HashSet<>();
        Arrays.stream(a).forEach(set::add);
        int max = Integer.MIN_VALUE;
        for(int i=0;i<a.length;i++){
            int count = 0;
            int num = a[i];
            while(set.contains(num)){
                count++;
                num=num*num;
            }
            max=Math.max(max,count);
        }
        System.out.println(max);
    }
}
