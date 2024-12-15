package com.rubal.ds.window;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class LongestSubStringAtmostKCharacter {
    public static int longestSubString(String s, int k){
        int l=0,r=0,max=-1;
        Map<Character, Integer> map = new HashMap<>();
        while(r<s.length()){
            map.put(s.charAt(r), map.getOrDefault(s.charAt(r),0)+1);
            if(map.size()>k){
                map.put(s.charAt(l), map.get(s.charAt(l))-1);
                if(map.get(s.charAt(l))==0)
                    map.remove(s.charAt(l));
                l++;
            }
            max = Math.max(max, r-l+1);
            r++;
        }
        return max;
    }
    public static void main(String[] args) {
        String s = "aaaabbcccd";
        int result = longestSubString(s, 2);
        System.out.println(result);
    }
}
/*
    map{a->1} l=0,r=0 max=1
    map{a=2} l=0,r=1 max=2
    map{a=3} l=0,r=2 max=3
    map{a=4} l=0,r=3 max=4

 */