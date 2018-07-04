package com.tictactoe.common;

import java.util.ArrayList;
import java.util.List;

public class RoomBean {
    private String name;
    private int maxPlayersNumber;
    private int gridSize;
    private int targetScore;
    private List<PlayerBean> playerBeans = new ArrayList<>();//vsechny hraci v skutecnym pokoji
    private GameBean gameBean;

    //vrace jmeno
    public String getName() {
        return name;
    }
    //nastavi jmeno
    public void setName(String name) {
        this.name = name;
    }
    // vrace vsech hracu
    public int getMaxPlayersNumber() {
        return maxPlayersNumber;
    }
    //nastavi maximalni pocet hracu
    public void setMaxPlayersNumber(int maxPlayersNumber) {
        this.maxPlayersNumber = maxPlayersNumber;
    }
    //vrace velikost pole
    public int getGridSize() {
        return gridSize;
    }
    //nastavi velikost pole
    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }
    //vrace score
    public int getTargetScore() {
        return targetScore;
    }
    //nastavi score
    public void setTargetScore(int targetScore) {
        this.targetScore = targetScore;
    }
    //vrace hracu pouzitim Json
    public List<PlayerBean> getPlayerBeans() {
        return playerBeans;
    }
    //vrace data s game pomoci Json
    public GameBean getGameBean() {
        return gameBean;
    }
}
