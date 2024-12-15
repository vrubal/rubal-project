package com.rubal.ds.window;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 *
 * Given two strings s and t of lengths m and n respectively, return the minimum window
 * substring
 *  of s such that every character in t (including duplicates) is included in the window.
 *  If there is no such substring, return the empty string "".
 *
 * Input: s = "ADOBECODEBANC", t = "ABC"
 * Output: "BANC"
 * map{A=1,B=1,C=1} cnt=0 l=0 r=0
 * map{A=0 B=1, C=1} cnt=1 l=0 r=1
 * map{A=0,B=1,C=1,D=-1} cnt=1 l=0 r=2
 * map{A=0,B=1,C=1,D=-1, O=-1} cnt=1 l=0 r=3
 * map{A=0,B=0,C=1,D=-1, O=-1} cnt=2, l=0, r=4
 * map{A=0,B=0,C=1,D=-1, O=-1} cnt=2  l=0 r=5
 * map{A=0,B=0,C=0,D=-1, O=-1, E=-1} cnt=3  l=0 r=6 res="ADOBEC"
 * map{A=1,B=0,C=0,D=-1, O=-1, E=-1} cnt=2  l=1 r=7 res="ADOBEC"
 */
public class MinimumWindowSubstring {
    public static String minWindow(String s, String t) {
        int l=0,r=0,cnt=0,min=Integer.MAX_VALUE;
        String result = "";
        Map<Character, Integer> map = new HashMap<>();
        //t.chars().forEach(e->map.put((char)e, map.getOrDefault(e,0)+1));
        for(char c: t.toCharArray())
            map.put(c, map.getOrDefault(c,0)+1);

        while(r<s.length()){
            if(map.containsKey(s.charAt(r)) && map.get(s.charAt(r)) >=0 ) {
                cnt++;
            }
            map.put(s.charAt(r), map.getOrDefault(s.charAt(r), 0) - 1);
            while(cnt==t.length()){
                if(min > r-l+1) {
                    min = r - l + 1;
                    result = s.substring(l, r+1);
                }
                map.put(s.charAt(l), map.get(s.charAt(l))+1);
                if(map.get(s.charAt(l))>0) {
                    cnt--;
                }
                l++;
            }
            r++;
        }
        return result;
    }

    public static void main(String[] args) {
        String s = "aa";
        String t = "aa";
        System.out.println(minWindow(s,t));
    }
}
