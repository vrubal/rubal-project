package com.rubal.lld.tictactoe.player;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public abstract class Player {
    char aChar;

    public Player(char aChar) {
        this.aChar = aChar;
    }

    abstract int playNextTurn(int[][] currState);
}
