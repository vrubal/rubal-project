package com.rubal.ds.array;

import java.util.*;

public class EmployeeFreeTime {
    public static void main(String[] args) {
        List<List<Integer>> slots = new ArrayList<>();
        //[(1, 4, 5), (1, 1, 5), (2, 2, 6), (3, 8, 9), (3, 2, 4)]
        slots.add(List.of(4,5));
        slots.add(List.of(1,5));
        slots.add(List.of(2,6));
        slots.add(List.of(8,9));
        slots.add(List.of(2,4));
        /*
            --
            ---
                 --
                -------
          prevStart = 3  4
          end   = 5  10

         */
        Collections.sort(slots, Comparator.comparingInt(a -> a.get(0)));
        List<List<Integer>>res = new ArrayList();
        System.out.println(slots);
        int end   = slots.get(0).get(1);
        for(int i=1;i<slots.size();i++){
            if(end < slots.get(i).get(0)){
                res.add(List.of(end, slots.get(i).get(0)));
            }
            end = Math.max(end, slots.get(i).get(1));
        }
        System.out.println(res);
    }
}
