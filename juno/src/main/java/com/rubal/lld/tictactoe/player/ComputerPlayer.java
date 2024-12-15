package com.rubal.lld.tictactoe.player;

import com.rubal.lld.tictactoe.User;

/**
 * @author
 * @created
 * @project rubal-project
 */
public class ComputerPlayer extends Player{
    private User user;

    public ComputerPlayer(char aChar, User user) {
        super(aChar);
        this.user = user;
    }

    @Override
    int playNextTurn(int[][] currState) {
        return 0;
    }
}
