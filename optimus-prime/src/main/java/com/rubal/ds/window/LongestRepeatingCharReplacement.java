package com.rubal.ds.window;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 * You are given a string s and an integer k.
 * You can choose any character of the string and change it to any other uppercase English character.
 * You can perform this operation at most k times.
 *
 * Return the length of the longest substring containing the same letter you can get after
 * performing the above operations.
 * AABABBA
 *
 */
public class LongestRepeatingCharReplacement {

    static int lengthOfLongestSubstr(String s, int k){
        int l=0,r=0,max=-1, maxFreq=0;
        Map<Character, Integer> map = new HashMap<>();
        while(r<s.length()){
            int freq = map.getOrDefault(s.charAt(r),0)+1;
            map.put(s.charAt(r), freq);
            maxFreq = Math.max(maxFreq, freq);
            if((r-l+1)-maxFreq <= k){
                max = Math.max(max, r-l+1);
            }else{
                map.put(s.charAt(l), map.get(s.charAt(l))-1);
                l++;
            }
            r++;
        }
        return max;
    }
    public static void main(String[] args) {
        String s = "AAABAC";
        int result = lengthOfLongestSubstr(s, 1);
        System.out.println(result);
    }
}
