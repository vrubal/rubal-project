package com.rubal.lld.chess;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class MoveValidator implements Validator {
    @Override
    public boolean validateMove(Player player, Piece pieceAtStart, Piece pieceAtEnd) {
        if(pieceAtEnd != null && (pieceAtEnd.type == pieceAtStart.type)) {
            return false;
        }
        return true;
    }
}
