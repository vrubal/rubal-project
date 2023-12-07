package com.rubal.change.lld.snakeladder;

import org.springframework.core.SpringVersion;

import java.util.*;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class SnakeLadderGame {
    private Board board;
    private Set<Player> playerList;

    private Dice dice;

    public SnakeLadderGame(Board board, Set<Player> playerList, Dice dice) {
        this.board = board;
        this.playerList = playerList;
        this.dice = dice;
    }

    public void play() {
        try{
        int winPos = board.size* board.size;
        Queue<Player> q = new LinkedList<>();
        for(Player e: playerList)
            q.add(e);
        while(true) {
            int newPos = 0;
            Player player = q.remove();
            int num = dice.roll();
            Cell cell = getCell(num+ player.currPos);
            //System.out.println("Player " + player.name + " current position:" + player.currPos);
            if (cell != null)
                newPos = CellProcessorFactory.get(cell.cellType).process(cell, player.currPos+num);

            else {
                newPos = player.currPos + num;
            }
            //System.out.println("Player " + player.name + " new position:" + newPos);
            System.out.println(player.name+" Curr Pos:"+player.currPos+ " Dice:"+num+" newPos:"+newPos);

            if (newPos == winPos) {
                System.out.println("Player " + player.name + " has won");
                break;
            }
            if(newPos<winPos) {
                player.currPos = newPos;
            }
            q.add(player);
            Thread.sleep(1000);
        }
        }catch (Exception e){
            System.out.println("Exception");
        }
    }

    Cell getCell(int num){
        Optional<Cell> ladderCell = board.cells.get(CellType.LADDER).stream().filter(e -> e.start == num).findAny();
        if(ladderCell.isPresent())
            return ladderCell.get();
        Optional<Cell> snakeCell = board.cells.get(CellType.SNAKE).stream().filter(e -> e.start == num).findAny();
        return snakeCell.orElse(null);
    }
}
