package com.rubal.lld.chess;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class Rook extends Piece{
    Rook(int x, int y, PieceType pieceType) {
        super(x, y, pieceType);
    }

    @Override
    public boolean canMove(int newSpot) {
        return false;
    }
}
