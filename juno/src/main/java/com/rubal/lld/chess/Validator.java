package com.rubal.lld.chess;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public interface Validator {
     boolean validateMove(Player player, Piece oldSpot, Piece newSpot);
}
