package com.rubal.lld.chess;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class CheckValidator implements Validator{
    @Override
    public boolean validateMove(Player player, Piece oldSpot, Piece newSpot) {
        return !player.check;
    }
}
