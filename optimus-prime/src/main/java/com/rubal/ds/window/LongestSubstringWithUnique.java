package com.rubal.ds.window;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class LongestSubstringWithUnique {

    public static int longestSubstringWithUniqueChar(String s) {
        int l=0, r=0, n=s.length();
        Map<Character, Integer> map = new HashMap<>();
        int max = -1;
        while(r<n){
            if(map.containsKey(s.charAt(r))){
                l=Math.max(l, map.get(s.charAt(r))+1);
            }
            map.put(s.charAt(r), r);
            max = Math.max(max, r-l+1);
            r++;
        }
        return max;
    }

    public static void main(String[] args) {
        String s = "abcdabcde";
        System.out.println(longestSubstringWithUniqueChar(s));
    }
}
