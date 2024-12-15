package com.rubal.ds.window;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 * You are visiting a farm that has a single row of fruit trees arranged from left to right.
 * The trees are represented by an integer array of arr[], where arr[i]  is the type of fruit the ith tree produces.
 * You want to collect as much fruit as possible. However, the owner has some strict rules that you must follow :
 *
 * You only have two baskets, and each basket can only hold a single type of fruit. There is no limit on the amount of fruit each basket can hold.
 * Starting from any tree of your choice, you must pick exactly one fruit from every tree (including the start tree) while moving to the right. The picked fruits must fit in one of the baskets.
 * Once you reach a tree with fruit that cannot fit in your baskets, you must stop.
 * Given the integer array of fruits, return the maximum number of fruits you can pick.
 */
public class FruitIntoBasket {
    public static int totalFruits(Integer[] arr) {
        int k=2;
        Map<Integer, Integer> map = new HashMap<>();
        int l=0,r=0,max=-1;
        while(r<arr.length){
            map.put(arr[r], map.getOrDefault(arr[r],0)+1);
            if(map.size()>k){
                map.put(arr[l], map.get(arr[l])-1);
                if(map.get(arr[l])==0)
                    map.remove(arr[l]);
                l++;
            }
            max = Math.max(max, r-l+1);
            r++;
        }
        return max;
    }

    public static void main(String[] args) {

    }
}
