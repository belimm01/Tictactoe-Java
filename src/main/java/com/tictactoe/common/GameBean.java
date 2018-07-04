package com.tictactoe.common;

import java.util.ArrayList;
import java.util.List;

public class GameBean {
    //protected podporuje dedicnost
    protected int gridSize;//velikost pole
    protected String[][] grid;//pole 

    protected String winner = null;
    protected int targetScore;
    protected int movesLeft = 0;


    public void setPlayerBeans(List<PlayerBean> playerBeans) {
        this.playerBeans = playerBeans;//nastavi hrace
    }

    private List<PlayerBean> playerBeans = new ArrayList<>();//vsechny hraci jsou tady

    //vrace velikost pole
    public int getGridSize() {
        return gridSize;
    }
    //nastavi velikost pole
    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }
    //vrati pole
    public String[][] getGrid() {
        return grid;
    }
    //nastavi pole
    public void setGrid(String[][] grid) {
        this.grid = grid;
    }
    //vrace viteze
    public String getWinner() {
        return winner;
    }
    //nastavi viteze
    public void setWinner(String winner) {
        this.winner = winner;
    }
    //vrace score
    public int getTargetScore() {
        return targetScore;
    }
    //nastavi score
    public void setTargetScore(int targetScore) {
        this.targetScore = targetScore;
    }
    //vrace tah
    public int getMovesLeft() {
        return movesLeft;
    }
    //nastavi tah
    public void setMovesLeft(int movesLeft) {
        this.movesLeft = movesLeft;
    }
   //vrace hracu pomoci Json
    public List<PlayerBean> getPlayerBeans() {
        return playerBeans;
    }

}
