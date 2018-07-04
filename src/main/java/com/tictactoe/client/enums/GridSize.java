package com.tictactoe.client.enums;

//Velikost pole , score (kolik musi dobyt hrac znaku)
public enum GridSize {
    F3X3(3, 3), F6X6(6, 4), F9X9(9, 4);
    //F3X3(velikost pole, score)

    private String label;
    private int size;//velikost
    private int targetScore;//score

    //konstruktor
    private GridSize(int size, int targetScore) {
        this.size = size;
        this.targetScore = targetScore;
        this.label = size + "x" + size;
    }

    public int getSize() {
        return size;
    }

    public int getTargetScore() {
        return targetScore;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
