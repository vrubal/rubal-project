package com.rubal.ds.array;

/**
 * @author
 * @created
 * @project rubal-project
 */
public class PeakElement {
    public static int peakElement(int[] arr,int n) {
        int l = 2, h = n - 2;
        if (n == 1)
            return 0;
        if (arr[1] < arr[0])
            return 0;
        if (arr[n - 2] < arr[n - 1])
            return n - 1;
        while (l <= h) {
            int mid = (l + h) / 2;
            if (arr[mid] > arr[mid + 1] && arr[mid] > arr[mid - 1]) {
                System.out.println(mid);
                return mid;
            }
            if (arr[mid] > arr[mid - 1])
                l = mid + 1;
            else {
                h = mid - 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        peakElement(new int[]{6,4,15,19,9}, 5);
    }
}
