package com.tictactoe.client;


//Session - zpuste. kdyz hrac stisne tlacitko play
public class Session {
    private String mark;//znak x,o,#,$

    private String currentRoom;

    //nastavi skutecny pokoj
    public void setCurrentRoom(String currentRoom) {
        this.currentRoom = currentRoom;
    }
    //vrace skutecny pokoj
    public String getCurrentRoom() {
        return currentRoom;
    }
    //vrace symbol
    public String getMark() {
        return mark;
    }
    //nastavi symbol
    public void setMark(String mark) {
        this.mark = mark;
    }
}
