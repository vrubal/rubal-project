package com.rubal.change.lld.snakeladder;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class GameLauncher {
    public static void main(String[] args) {
        Board board = buildBoard(8);
        Set<Player> players = buildPlayers();
        Dice dice = new Dice(6, new Random());
        SnakeLadderGame game = new SnakeLadderGame(board, players, dice);
        game.play();
    }
    static Board buildBoard(int boardSize){
        Map<CellType, List<Cell>> map = new HashMap<>();
        LadderCell ldc1 = new LadderCell(3, 10);
        LadderCell ldc2 = new LadderCell(6, 35);
        LadderCell ldc3 = new LadderCell(16, 45);
        LadderCell ldc4 = new LadderCell(34, 63);
        SnakeCell snakeCell1 = new SnakeCell(42, 12);
        SnakeCell snakeCell3 = new SnakeCell(52, 23);
        SnakeCell snakeCell4 = new SnakeCell(62, 19);
        SnakeCell snakeCell2 = new SnakeCell(35, 8);
        map.put(CellType.LADDER, List.of(ldc1, ldc2,ldc3, ldc4));
        map.put(CellType.SNAKE, List.of(snakeCell1, snakeCell2,snakeCell3,snakeCell4));
        Board board = new Board(boardSize, map);
        return board;
    }

    static Set<Player> buildPlayers(){
        Player player1 = new Player(1, "Vijay",0);
        Player player2 = new Player(2, "Shivay",0);
        Player player3 = new Player(3, "Rubal",0);
        Player player4 = new Player(4, "Shambhavi",0);
        return Set.of(player1, player2, player4, player3);
    }
}
