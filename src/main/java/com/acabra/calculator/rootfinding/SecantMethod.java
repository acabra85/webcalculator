package com.acabra.calculator.rootfinding;

import com.acabra.calculator.function.RealFunction;

/**
 * Implementation described in the book Numerical Analysis Burden-Faires 9th Ed. Pg 72
 * @author Agustin
 */
public class SecantMethod extends IterativeRootFindingAlgorithm {

    public SecantMethod(RealFunction rf1, double[] params) {
        super(rf1, params);
    }

    public SecantMethod(RealFunction realFunction, double[] doubleArray, double tolerance) {
        super(realFunction, doubleArray, tolerance);
    }

    public IterativeRootFindingResult iterateMethod(int numIterations) {
        int actualIterations = 0;
        while (actualIterations < numIterations && !toleranceReached()) {
            this.parameters = applyMethod(parameters);
            actualIterations++;
        }
        return new IterativeRootFindingResult(parameters[1], parameters[0], actualIterations);
    }

    protected double[] applyMethod(double[] params) {
        double p0 = params[0];
        double p1 = params[1];

        double q0 = f(p0);
        double q1 = f(p1);
        double p = p1 - q1 * ((p1 - p0) / (q1 - q0));

        double[] rtnP = new double[3];
        rtnP[0] = p1;
        rtnP[1] = p;
        return rtnP;
    }

    private double f(double val) {
        return realFunction.apply(val);
    }

    @Override
    public boolean toleranceReached() {
        return Math.abs(parameters[1] - parameters[0]) < tolerance;
    }

}
