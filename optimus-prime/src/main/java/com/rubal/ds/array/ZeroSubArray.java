package com.rubal.ds.array;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class ZeroSubArray {

    public static long findSubarray(long[] arr ,int n) {
        Map<Long,Integer> map = new HashMap<>();
        int i=-1;
        long sum=0;
        long count=0;
        map.put((long)sum,1);
        while(i<n-1)
        {
            i++;
            sum += arr[i];
            if(map.containsKey(sum)){
                count += map.get(sum);
                map.put(sum,map.get(sum)+1);
            }
            else{
                map.put(sum, 1);
            }
        }
        return count;
    }

    public static void main(String[] args) {
        findSubarray(new long[]{6,-1,-3,4,-2,2,4,6,-12,-7}, 10);
    }
}
