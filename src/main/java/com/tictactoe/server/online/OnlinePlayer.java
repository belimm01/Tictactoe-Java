package com.tictactoe.server.online;

import com.tictactoe.server.game.Player;

import java.net.Socket;

public class OnlinePlayer extends Player {
    private Socket socket;

    //konstruktor, pripoji se ve room se svym znakem
    public OnlinePlayer(Socket socket, String mark, int gridSize) {
        super(mark, gridSize);
        this.socket = socket;
    }

    //vrace socket
    public Socket getSocket() {
        return socket;
    }
}
