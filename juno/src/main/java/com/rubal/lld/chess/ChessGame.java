package com.rubal.lld.chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Queue;

public class ChessGame {

    private GameStatus gameStatus;
    private Board board;
    void buildPlayer(){

    }
    Board buildBoard(){
        Board board = new Board();
        board.list = List.of(new CheckValidator(), new MoveValidator());
        return board;
    }
    void play()throws IOException {
        Queue<Player> players = board.players;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Piece whiteKing = board.pieceMap.get(5);
        Piece blackKing = board.pieceMap.get(59);

        while(gameStatus == GameStatus.IN_PROGRESS){
            Player player = players.poll();
            boolean validMove = false;
            int start=0;
            int end=0;
            
            while(!validMove) {
                System.out.println("Enter start:");
                start = Integer.parseInt(br.readLine());
                System.out.println("Enter start:");
                end = Integer.parseInt(br.readLine());
                validMove=board.validateMove(player, start, end);
            }
            board.move(start, end);
            if(!whiteKing.alive || !blackKing.alive){
                System.out.println("Player:"+player.name+" has won");
                gameStatus = GameStatus.FINISHED;
            }
            players.add(player);
        }
    }
}
