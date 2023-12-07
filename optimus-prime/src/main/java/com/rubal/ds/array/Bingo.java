package com.rubal.ds.array;

import java.util.HashSet;
import java.util.Set;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class Bingo {
    public static void main(String[] args) {
        String[][]ticket = new String[][]{
                {"","11","21","32","","","62","","82"},
                {"1","","23","39","","57","","74",""},
                {"5","17","","","42","","66","79",""}
        };
        boolean[]vis = new boolean[91];
        String[] input = new String[]{
              "83","11","9","60","5","45","82","42","8","79","12"
        };
        Set<String> even = new HashSet<>();
        Set<String> odd = new HashSet<>();
        Set<String> corner = new HashSet<>();
        Set<String> duckling = new HashSet<>();
        corner.add("11");
        corner.add("82");
        corner.add("79");
        corner.add("5");
        String elem = null;
        int eCount=0, oCount=0, dCount=0, cCount=0;

        for(int i=0;i<3;i++){
            for (int j = 0; j < 9; j++) {
                int val = Integer.parseInt(ticket[i][j]);
                vis[val] = true;
                if(val%2==0)
                    eCount++;
                else if(val%2==1)
                    oCount++;
            }

            if(i==0){
                for(int j=0;j<9;j++){

                }
            }
            if(i==2){
                for(int j=0;j<9;j++){

                }
            }
        }
        for (int i = 0; i < input.length; i++) {
            int val = Integer.parseInt(input[i]);
            if(vis[val]==false)
                continue;
            if(val%2==0)
                eCount--;
            else if(val%2==1)
                oCount--;

            if(eCount==0){

            }
            if(oCount==0){

            }
            if(dCount==0){

            }
            if(cCount==0){

            }

        }


    }
}
