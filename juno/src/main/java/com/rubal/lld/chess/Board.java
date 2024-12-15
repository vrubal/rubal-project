package com.rubal.lld.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class Board {
    Map<Integer, Piece> pieceMap;
    int blackKing;
    int whiteKing;
    Queue<Player> players;
    List<Validator> list;

    boolean validateMove(Player player, int oldSpot, int newSpot){
        Piece pieceAtEnd = pieceMap.get(newSpot);
        Piece pieceAtStart = pieceMap.get(oldSpot);
        for (Validator validator:list){
            if(!validator.validateMove(player, pieceAtStart, pieceAtEnd)){
                return false;
            }
        }
        return pieceAtStart.canMove(newSpot);
    }
    void move(int oldSpot, int newSpot){
        Piece pieceAtEnd = pieceMap.get(newSpot);
        Piece pieceAtStart = pieceMap.get(oldSpot);
        if(pieceAtEnd!=null){
            pieceAtEnd.alive=false;
        }
        updatePieceLocation(pieceAtStart, newSpot);
        pieceMap.put(newSpot, pieceAtStart);
    }

    private void updatePieceLocation(Piece piece, int newSpot) {
        int oldSpot = (piece.x/8)*(piece.y%8);
        pieceMap.remove(oldSpot);
        pieceMap.put(newSpot, piece);
        piece.x= newSpot/8;
        piece.y= newSpot %8;
    }

}
