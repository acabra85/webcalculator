package com.acabra.calculator.rootfinding;

import com.acabra.calculator.function.RealFunction;

/**
 * Implementation described in the book Numerical Analysis Burden-Faires 9th Ed. Pg 68
 * @author Agustin
 */
public class NewtonMethod extends IterativeRootFindingAlgorithm {

    public NewtonMethod(RealFunction realFunction, double[] params) {
        super(realFunction, params);
    }

    public NewtonMethod(RealFunction realFunction, double[] doubleArray, double tolerance) {
        super(realFunction, doubleArray, tolerance);
    }

    private double fd(double p0) {
        return realFunction.calculateDerivative(p0);
    }

    private double f(double p0) {
        return realFunction.evaluate(p0);
    }

    public IterativeRootFindingResult iterateMethod(int numIterations) {
        int actualIterations = 0;
        while (actualIterations < numIterations && !toleranceReached()) {
            this.parameters = applyMethod(parameters);
            actualIterations++;
        }
        return new IterativeRootFindingResult(parameters[0], Double.NaN, actualIterations);
    }

    @Override
    public boolean toleranceReached() {
        return Math.abs(realFunction.evaluate(this.parameters[0])) < tolerance;
    }

    protected double[] applyMethod(double[] params) {
        double p0 = params[0];
        double[] rtnP = new double[3];
        rtnP[0] = p0 - (f(p0) / fd(p0));
        rtnP[1] = p0;
        return rtnP;
    }
}
