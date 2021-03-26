package com.rubal.ds.array;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CountDuplicates {

    public static Map<Character, Integer> pringDuplicateCounts(char[] arr){
        Map<Character, Integer> counts = new LinkedHashMap<>();
        for (int i = 0; i < arr.length; i++) {
            if(counts.containsKey(arr[i])){
                counts.put(arr[i], counts.get(arr[i])+1);
            }else{
                counts.put(arr[i], 1);
            }
        }
        return counts;
    }

    public static void main(String[] args) {
        System.out.println(pringDuplicateCounts(new char[]{'e','a','a','a','b','b','b','c'}));
    }

}
