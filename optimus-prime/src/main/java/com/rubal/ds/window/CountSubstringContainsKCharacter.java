package com.rubal.ds.window;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class CountSubstringContainsKCharacter {

    public static int countSubstring(String s, int k){
        int l=0, r=0, count=0;
        Map<Character, Integer> map = new HashMap<>();
        map.put('a',-1);
        map.put('b',-1);
        map.put('c',-1);
        while(r<s.length()){
            map.put(s.charAt(r), r);
            Integer minIdx = map.values().stream().min(Comparator.comparingInt(o -> o)).get();
            if(minIdx > -1){
                count+=(1+minIdx);
            }
            r++;
        }
        return count;
    }
    public static void main(String[] args) {
        String s= "bbacba";
        int result = countSubstring(s, 3);
        System.out.println(result);
    }
}
/*
    bbacba
    map{b=0} l=0 r=1 count=0
    map{b=1} l=0 r=2 count=2
    map{b=1, a=2} l=0,r=3 count=2
    map{b=1,a=2,c=3} l=0,r=4 count=1

 */