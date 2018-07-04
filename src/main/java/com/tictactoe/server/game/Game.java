package com.tictactoe.server.game;

import com.tictactoe.common.GameBean;
import com.tictactoe.common.PlayerBean;

import java.util.List;
import java.util.stream.Collectors;

public class Game extends GameBean {
    protected final static String BLANK = "";
    private final static int point = 1;

    protected final List<Player> players;


    public Game(int size, int targetScore, List<Player> players) {
        this.targetScore = targetScore;
        this.gridSize = size;
        this.grid = new String[size][size];
        this.players = players;
        for (int i = 0; i < this.gridSize; ++i) {
            for (int j = 0; j < this.gridSize; ++j) {
                this.grid[i][j] = BLANK;
            }
        }

        this.movesLeft = this.gridSize * this.gridSize;
        this.winner = null;
    }


    protected boolean makeMove(String player, int row, int col) {
        if (!this.isValidMove(row, col) || !BLANK.equals(this.grid[row][col])) {
            return false;
        }

        this.grid[row][col] = player;
        --this.movesLeft;
        for (Player p : players) {
            if (p.getMark().equals(player)) {
                int[] score = p.getScore();
                score[row] += point;
                score[this.gridSize + col] += point;
                if (row == col) score[2 * this.gridSize] += point;
                if (this.gridSize - 1 - col == row) score[2 * this.gridSize + 1] += point;
            }
        }
        return true;
    }

    protected GameStatus calcGameStatus() {
        if (0 == this.movesLeft) {
            return GameStatus.DRAW;
        }

        for (Player p : players) {

            for (int aScore : p.getScore()) {
                if (this.targetScore == aScore) {
                    this.winner = p.getMark();
                    return GameStatus.VICTORY;
                }
            }
        }
        return GameStatus.CONTINUE;
    }

    private boolean isValidMove(int row, int col) {
        return (0 <= row && row < this.gridSize) &&
                (0 <= col && col < this.gridSize);
    }

    protected String printGrid() {
        StringBuilder res = new StringBuilder();
        res.append("--------\n");
        for (int row = 0; row < this.gridSize; ++row) {
            for (int col = 0; col < this.gridSize; ++col) {
                if (BLANK.equals(this.grid[row][col])) res.append("  ");
                else res.append(this.grid[row][col]).append(" ");
            }
            res.append("\n");
        }
        res.append("--------\n");
        return res.toString();
    }


    @Override
    public List<PlayerBean> getPlayerBeans() {
        return players.stream().map((p) -> {
            PlayerBean player = new PlayerBean();
            player.setMark(p.getMark());
            player.setScore(p.getScore());
            return player;
        }).collect(Collectors.toList());
    }
}
