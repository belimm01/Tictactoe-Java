package com.tictactoe.common;

public class MoveBean {

    private int row;
    private int col;

    public MoveBean() {

    }
    //nastavi sloupcy a radky
    public MoveBean(int row, int col) {
        this.row = row;
        this.col = col;
    }
    //vrati radky
    public int getRow() {
        return row;
    }
    //nastavi radky
    public void setRow(int row) {
        this.row = row;
    }
    //vrace sloupcy
    public int getCol() {
        return col;
    }
    //nastavi sloupcy
    public void setCol(int col) {
        this.col = col;
    }
}

