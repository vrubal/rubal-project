package com.rubal.lld.chess;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public abstract class Piece {
    int x;
    int y;
    boolean alive;
    PieceType type;

    Piece(int x, int y, PieceType pieceType){
        this.type = pieceType;
        this.x=x;
        this.y=y;
        this.alive=true;
    }
    public void setAlive(boolean alive){
        this.alive = alive;
    }

    public abstract boolean canMove(int newSpot);
}
