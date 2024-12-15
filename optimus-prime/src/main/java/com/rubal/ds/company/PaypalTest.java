package com.rubal.ds.company;

import java.util.*;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class PaypalTest {

    public static void main(String[] args) {
        int timeLimit = 4;
        int[][] payments = new int[][]{{1,2},{25,65},{1,2},{4,5}};
        int[] timestamps = new int[]{1, 2, 3, 4};
        String[] result = new String[4];
        Map<String, List<Integer>> map = new HashMap<>();
        Arrays.stream(payments).forEach(e-> System.out.println("S:"+e[0]+"R:"+e[1]));
        for (int i = 0; i < timestamps.length; i++) {
            String id = "S:"+payments[i][0]+"R:"+payments[i][1];
            if(map.containsKey(id)){
                map.get(id).add(i);
            }else{
                ArrayList<Integer> list = new ArrayList<>();
                list.add(i);
                map.put(id, list);
            }
        }
        for (Map.Entry<String, List<Integer>> e: map.entrySet()){
            int prevTimeStamp = -1;
            for (Integer t:e.getValue()) {
                if(prevTimeStamp == -1 || (timestamps[t] - prevTimeStamp) > timeLimit){
                    result[t] = "false";
                } else {
                    result[t] = "true";
                }
                prevTimeStamp = timestamps[t];
            }
        }
        System.out.println(Arrays.asList(result));
    }
}
