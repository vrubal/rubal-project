package com.rubal.ds.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class PrimeNumbersInRange {
    List<Integer> findAllPrimesSieve(int num) {
        List<Integer> list = new ArrayList<>();
        boolean prime[] = new boolean[num + 1];
        Arrays.fill(prime, true);
        for (int i = 2; i * i <= num; i++) {
            if (prime[i]) {
                for (int p = i * i; p <= num; p += i)
                    prime[p] = false;
            }
        }
        for (int i = 2; i <= num; i++) {
            if (prime[i])
                list.add(i);
        }
        return list;
    }

    List<Integer> findAllPrimes(int num) {
        PrimeNumber primeNumber = new PrimeNumber();
        List<Integer> list = new ArrayList<>();
        for (int i = 2; i <= num; i++) {
            if (primeNumber.isPrime(i))
                list.add(i);
        }
        return list;
    }
}
