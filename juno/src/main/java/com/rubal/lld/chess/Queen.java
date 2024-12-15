package com.rubal.lld.chess;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class Queen extends  Piece{

    public Queen(int x, int y, PieceType pieceType){
        super(x,y,pieceType);
    }

    @Override
    public boolean canMove(int newSpot) {
        return false;
    }
}
