package com.rubal.ds.leetcode;
/*

P   A   H   N
A P L S I I G
Y   I   R

 */
public class ZigZagPrint {
    public static String convert(String s, int numRows) {
        if (numRows == 1) return s;

        StringBuilder ret = new StringBuilder();
        int n = s.length();
        int cycleLen = 2 * numRows - 2;

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j + i < n; j += cycleLen) {
                System.out.println(j+i);
                ret.append(s.charAt(j + i));
                if (i != 0 && i != numRows - 1 && j + cycleLen - i < n)
                    ret.append(s.charAt(j + cycleLen - i));
            }
        }
        return ret.toString();
    }

    public static void main(String[] args) {
        String input = "PAYPALISHIRING";
        System.out.println(convert(input, 3));
    }
}
