package com.acabra.calculator.rootfinding;

import com.acabra.calculator.function.RealFunction;
import com.acabra.calculator.util.WebCalculatorConstants;

public abstract class IterativeRootFindingAlgorithm {
    protected final RealFunction realFunction;
    protected double[] parameters;
    protected final double tolerance;

    public IterativeRootFindingAlgorithm(RealFunction realFunction, double[] params) {
        this.realFunction = realFunction;
        this.parameters = params.clone();
        this.tolerance = WebCalculatorConstants.ACCURACY_EPSILON;
    }

    public IterativeRootFindingAlgorithm(RealFunction realFunction, double[] parameters, double tolerance) {
        this.realFunction = realFunction;
        this.parameters = parameters.clone();
        this.tolerance = tolerance;
    }

    public RealFunction getFunction() {
        return realFunction;
    }

    public double[] getParameters() {
        return this.parameters.clone();
    }

    public IterativeRootFindingResult iterateMethod(int numIterations) {
        int i;
        for (i = 0; i < numIterations && !toleranceReached(); i++) {
            parameters = applyMethod(parameters);
        }
        return new IterativeRootFindingResult(parameters[0], parameters[1], i);
    }

    protected abstract double[] applyMethod(double[] params);

    public abstract boolean toleranceReached();
}
