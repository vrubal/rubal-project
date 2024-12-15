package com.rubal.ds.array;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class SortColors {
    public static void main(String[] args) {
        int[] a = new int[]{2,0,2,1,1,0};
        int zero=0, one=0, two = a.length-1;
        while(one<two){
            if(a[one] == 0){
                int t = a[zero];
                a[zero] = a[one];
                a[one] = t;
                one++;
                zero++;
            }else if(a[one] == 1){
                one++;
            }else if(a[one] == 2){
                int t = a[two];
                a[two] = a[one];
                a[one] = t;
                //one--;
                two--;
            }
        }


    }
}
