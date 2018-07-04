package com.tictactoe.server.online;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tictactoe.common.GameBean;
import com.tictactoe.common.MarkBean;
import com.tictactoe.common.PlayerBean;
import com.tictactoe.common.RoomBean;
import com.tictactoe.server.game.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Room extends RoomBean {
    private final Set<String> availableMarks = new LinkedHashSet<>(
            Arrays.stream(MarkBean.values()).map(MarkBean::getMark).collect(Collectors.toList()));//vypise vsechny sumboly do kolekce

    @JsonIgnore//ignorovat pri serializace 
    private Game game;

    @JsonIgnore
    private List<OnlinePlayer> players = new LinkedList<>();


    public void startGame() {
        game = new Game(getName(),getGridSize(), getTargetScore(), (List<Player>) (List) players);//nastartuju hru s nejakou velisti
    }

    public Game getGame() {
        return game;
    }
    //konvertuje game v gameBean (Bean) bean -> json
    @Override
    public GameBean getGameBean() {
        if (game == null)
            return null;
        GameBean gameBean = new GameBean();
        gameBean.setGridSize(game.getGridSize());
        gameBean.setGrid(game.getGrid());
        gameBean.setWinner(game.getWinner());
        gameBean.setTargetScore(game.getTargetScore());
        gameBean.setMovesLeft(game.getMovesLeft());
        gameBean.setPlayerBeans(game.getPlayerBeans());
        return gameBean;
    }
    //vrace vsech hracu
    public List<OnlinePlayer> getPlayers() {
        return players;
    }
    //vrace vsech hracu (Bean)
    @Override
    public List<PlayerBean> getPlayerBeans() {
        return players.stream().map((p) -> {
            PlayerBean player = new PlayerBean();
            player.setMark(p.getMark());
            player.setScore(p.getScore());
            return player;
        }).collect(Collectors.toList());
    }
    //vygeneruje dostupny znak
    public String generateAvailableMark() {
        for (Player player : players) {
            availableMarks.removeIf(p -> p.equals(player.getMark()));
        }
        Iterator<String> it = availableMarks.iterator();
        String s = it.next();
        it.remove();
        return s;
    }
    //udelat pristup k znaku
    public void enableMark(String mark) {
        availableMarks.add(mark);
    }
}
