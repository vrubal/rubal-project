package com.rubal.lld.tictactoe.game;

import com.rubal.lld.tictactoe.player.Player;

import java.util.Queue;

/**
 * @author
 * @created
 * @project rubal-project
 */
public class GameBoard {
    char [][]currState;
    Queue<Player> queue;

    private boolean validateMode(int i, int j){
        return false;
    }
    public void makeMove(Player player, int i, int j){

    }
    public boolean checkWinner(Player player){
        return false;
    }
    public void display(){

    }
}
