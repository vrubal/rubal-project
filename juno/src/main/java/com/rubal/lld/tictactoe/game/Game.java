package com.rubal.lld.tictactoe.game;

import com.rubal.lld.tictactoe.player.Player;

import java.util.Queue;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class Game {
    int gameId;
    GameBoard board;

    public void start(){
        while(true){
            board.display();
            Player p = board.queue.poll();
            int i = System.in.read();
            int j = System.in.read();
        }
    }
}
