package com.rubal.ds.array;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FindEvenLenghtNums {
    public static int findNumbers(int[] nums) {
        return (int) Arrays.stream(nums).filter(t->(String.valueOf(t).length()%2 == 0 )).count();

    }

    public static void main(String[] args) {

        System.out.println(findNumbers(new int[]{11,2,3,4,5}));
    }
}
