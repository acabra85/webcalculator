package com.acabra.calculator.rootfinding;

/**
 * Class to represent the result of an IterativeRootFinding process
 * Created by Agustin on 11/2/2016.
 */
public class IterativeRootFindingResult {
    private final int iterations;
    private final double approximatedRoot;
    private final double b;

    public IterativeRootFindingResult(double approximatedRoot, double b, int iterations) {
        this.approximatedRoot = approximatedRoot;
        this.b = b;
        this.iterations = iterations;
        System.out.println(String.format("p: %.5f, iterations : %d", approximatedRoot, iterations));
    }

    public int getIterations() {
        return iterations;
    }

    public double getApproximatedRoot() {
        return approximatedRoot;
    }

    public double getB() {
        return b;
    }

    @Override
    public String toString() {
        return "IterativeRootFindingResult{" +
                "iterations=" + iterations +
                ", approximatedRoot=" + approximatedRoot +
                ", b=" + b +
                '}';
    }
}
