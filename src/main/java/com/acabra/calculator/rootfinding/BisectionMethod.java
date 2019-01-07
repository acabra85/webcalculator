package com.acabra.calculator.rootfinding;

import com.acabra.calculator.function.RealFunction;

/**
 * Implementation described in the book Numerical Analysis Burden-Faires 9th Ed. Pg 49
 * @author Agustin
 */
public class BisectionMethod extends IterativeRootFindingAlgorithm {

    public BisectionMethod(RealFunction realFunction, double[] params) {
        super(realFunction, params);
    }

    public BisectionMethod(RealFunction realFunction, double[] doubleArray, double tolerance) {
        super(realFunction, doubleArray, tolerance);
    }

    public IterativeRootFindingResult iterateMethod(int numIterations) {
        int actualIterations = 0;
        do {
            this.parameters = applyMethod(parameters);
            actualIterations++;
        } while (actualIterations < numIterations && !toleranceReached());
        return new IterativeRootFindingResult(parameters[2], Double.NaN, actualIterations);
    }

    protected double[] applyMethod(double[] params) {
        double a = params[0];
        double b = params[1];
        double fa = f(a);
        double middlePoint = (b - a) / 2.0;
        double p = a + middlePoint;
        double fp = f(p);
        //System.out.println("fa:"+fa+" fb:"+fb+ " fc:" + fc);
        if (fa * fp > 0) {
            a = p;
        } else {
            b = p;
        }
        //System.out.println("a:"+a+" b:"+b+ " c:" + c);
        double[] rtnP = new double[3];
        rtnP[0] = a;
        rtnP[1] = b;
        rtnP[2] = p;
        return rtnP;
    }

    private double f(double a) {
        return realFunction.apply(a);
    }

    @Override
    public boolean toleranceReached() {
        return Math.abs(f(this.parameters[2])) < tolerance;
    }
}
