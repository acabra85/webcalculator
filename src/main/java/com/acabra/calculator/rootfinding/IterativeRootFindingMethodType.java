package com.acabra.calculator.rootfinding;

/**
 * Created by Agustin on 11/2/2016.
 */
public enum IterativeRootFindingMethodType {
    NEWTON(0), SECANT(1), BISECANT(2);

    private final int id;

    IterativeRootFindingMethodType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
