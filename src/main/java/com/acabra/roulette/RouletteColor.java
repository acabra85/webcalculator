package com.acabra.roulette;

public enum RouletteColor {
    GREEN(0),
    RED(1),
    BLACK(2);

    private final int id;

    public int getId() {
        return id;
    }

    RouletteColor(int id) {
        this.id = id;
    }
}
