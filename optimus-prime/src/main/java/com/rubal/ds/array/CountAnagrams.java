package com.rubal.ds.array;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class CountAnagrams {
    static int search(String pat, String txt) {
        int sum =0;
        for(char a: pat.toCharArray()){
            sum+=a;
        }
        int tSum = 0, count = 0, len =0;
        for(int i=0;i<txt.length();i++){
            if(tSum == sum && len == pat.length())
                count++;
            tSum += txt.charAt(i);
            len++;
            if(tSum>sum && len > pat.length()){
                tSum-=txt.charAt(i-pat.length());
                len--;
            }
        }
        if(tSum==sum && len == pat.length())
            count++;
        return count;
    }
    public static void main(String[] args) {
        System.out.println(search("po", "zbcoejuvpvaboyg"));
    }
}
