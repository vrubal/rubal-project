package com.rubal.ds.sorting;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class Frequency {
    public static int count(String s , int idx){
        char pre = s.charAt(idx);
        int l=idx, r=idx+1;
        int count = 0;
        while(l>=0 && s.charAt(l)==pre){
            count++;
            l--;
        }
        while(r<s.length() && s.charAt(r)==pre){
            count++;
            r++;
        }
        return count;
    }
    public static void main(String[] args) {
        String s = "abbbbcccd";
        char c = '3';
        int l=0, h=s.length()-1;
        while(l<=h){
            int mid = l+(h-l)/2;
            if(s.charAt(mid)==c){
                System.out.println(count(s, mid));
                break;
            }
            if(s.charAt(mid)<c){
                l=mid+1;
            }else
                h=mid-1;
        }
    }
}
