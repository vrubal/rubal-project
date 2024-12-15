package com.rubal.ds.math;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class PrimeNumber {

    //16/6 --
    //
    public boolean isPrime(int num){
        if(num==2)
            return true;
        if(num%2==0)
            return false;
        for(int i=3;i<=Math.sqrt(num);i+=2){
            if(num%i == 0)
                return false;
        }
        return true;
    }
    public boolean isPrimeWithFor(int num){
        //Math.sqrt(num)
        if(num==2||num%2==0)
            return false;
        for(int i=3;i<num/2;i+=2){
            if(num%i == 0)
                return false;
        }
        return true;
    }
}
