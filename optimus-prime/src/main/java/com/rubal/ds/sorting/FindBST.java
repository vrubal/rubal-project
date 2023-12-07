package com.rubal.ds.sorting;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class FindBST {

    public static int find(int N, int low, int high, int[]arr){
        while(low<high) {
            int mid = (low+high)/2;
            if ((arr[mid - 1] != arr[mid]) && ((arr[mid + 1] != arr[mid]))) {
                return mid;
            }
            else if((arr[mid]==arr[mid+1]) || (arr[mid]==arr[mid-1])){
                low=mid+1;
            }else
                high=mid-1;
        }
        return -1;
    }

    public static void main(String[] args) {
        Pattern.matches(".*","a");
        Map<Integer, Integer> m = new HashMap();
        for(Map.Entry e: m.entrySet()){

        }
        int[]arr=new int[]{2,2,4,4,5,6,6};
        int i = find(arr.length, 0, arr.length , arr);
        System.out.println(arr[i]);
    }
}
