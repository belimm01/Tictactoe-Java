package com.tictactoe.server.game;

import com.tictactoe.common.PlayerBean;


public class Player extends PlayerBean {

    //tady vytvarim player
    public Player(String mark, int gridSize) {
        this.mark = mark;
        this.score = new int[gridSize * 2 + 2];
        for (int i = 0; i < gridSize * 2 + 2; ++i) {
            this.score[i] = 0;
        }
    }

}