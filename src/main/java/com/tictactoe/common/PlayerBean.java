package com.tictactoe.common;

public class PlayerBean {
    protected String mark;
    protected int[] score;
    //vrace score
    public int[] getScore() {
        return score;
    }
    //nastavi score
    public void setScore(int[] score) {
        this.score = score;
    }
    //vrace znak
    public String getMark() {
        return mark;
    }
    //nastavi znak
    public void setMark(String mark) {
        this.mark = mark;
    }
}
