package com.rubal.lld.snakeladder;

import java.util.Random;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class Dice {
    private int limit;

    private Random random;
    public Dice(int limit, Random random) {
        this.limit = limit;
        this.random = random;
    }

    public int roll(){
        return this.random.nextInt(limit);
    }
}
