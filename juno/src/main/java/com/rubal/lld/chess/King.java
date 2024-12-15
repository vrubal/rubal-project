package com.rubal.lld.chess;

/**
 * @author
 * @created
 * @project rubal-project
 */
public class King extends Piece{
    boolean castling;

    public King(int x, int y, PieceType pieceType) {
        super(x, y, pieceType);
    }

    @Override
    public boolean canMove(int newSpot) {
        return false;
    }
}
