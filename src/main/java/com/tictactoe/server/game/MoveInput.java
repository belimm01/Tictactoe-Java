package com.tictactoe.server.game;

import com.tictactoe.common.MoveBean;

public interface MoveInput {
    public MoveBean getNextMove(String player);
}

