package com.tictactoe.client.enums;

//Nastavim pocet hracu v jedne hre
public enum PlayersNumber {
    P2("Two", 2), P3("Three", 3), P4("Four", 4);
    //P2( , pocet hracu)
    private String label;
    private int count;//pocet hracu

    private PlayersNumber(String label, int targetScore) {
        this.label = label;
        this.count = targetScore;
    }
    //vrace pocet hracu
    public int getCount() {
        return count;
    }
    //convertuje label v retezec
    @Override
    public String toString() {
        return label;
    }
}
