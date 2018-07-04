package com.tictactoe.common;

public enum MarkBean {
    m1("X"), m2("O"), m3("#"), m4("$");//nastavim znaku, pomoci enum

    private String mark;

    //nastavi znak
    MarkBean(String mark) {
        this.mark = mark;
    }
    //vrati znak
    public String getMark() {
        return mark;
    }
}
