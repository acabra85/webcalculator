package com.acabra.calculator.integral;

import org.apache.log4j.Logger;

/**
 * Created by Agustin on 9/29/2016.
 */
public abstract class IntegrableFunction {

    protected double lowerBound;
    protected double upperBound;
    private String label;
    private static final String STRING_REPRESENTATION_FORMAT = "integral{%s}[%.2f, %.2f]";
    protected Double result;
    protected Double areaUnderTheGraph;
    protected static final Logger logger = Logger.getLogger(IntegrableFunction.class);

    public IntegrableFunction(double lowerBound, double upperBound, String label) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.label = label;
        this.result = null;
    }

    public IntegrableFunction(double lowerBound, double upperBound, double result, String label) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.result = result;
        this.label = label;
    }

    protected abstract double solve();

    protected abstract double solveAreaUnderTheGraph();

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public double getResult() {
        return solve();
    }

    public Double getAreaUnderTheGraph() {
        return solveAreaUnderTheGraph();
    }

    @Override
    public String toString() {
        return String.format(STRING_REPRESENTATION_FORMAT, label, lowerBound, upperBound);
    }
}
