package com.acabra.calculator.integral;

import java.util.Optional;

/**
 * Created by Agustin on 9/30/2016.
 */
public class PolynomialIntegral extends IntegrableFunction {

    private final int order;
    private final double[] coefficients;

    public PolynomialIntegral(double lowerbound, double upperbound, int order, double[] coefficients) {
        super(lowerbound, upperbound, IntegralFunctionType.POLYNOMIAL.getLabel());
        this.order = order;
        this.coefficients = coefficients;
    }

    public PolynomialIntegral(int lowerbound, int upperbound, int order, double[] coefficients, Optional<Double> result) {
        super(lowerbound, upperbound, result.get(), IntegralFunctionType.POLYNOMIAL.getLabel());
        this.order = order;
        this.coefficients = coefficients;
    }

    @Override
    protected double evaluate(double domainPoint) {
        double total = 0.0;
        for(int exponent = 0; exponent < order; exponent++) {
            if (coefficients[exponent] != 0) {
                total +=  Math.pow(domainPoint, exponent) * coefficients[exponent];
            }
        }
        return total;
    }

    @Override
    protected double solveIntegralWithRiemannSequences(boolean inscribedRectangle) {
        if (sequenceRiemannRectangle == null) {
            sequenceRiemannRectangle = calculateRiemannSequenceRectangleArea(inscribedRectangle);
        }
        return sequenceRiemannRectangle;
    }

    protected Double executeIntegration() {
        double total = 0.0;
        for(int i = 0; i < order; i++) {
            if (coefficients[i] != 0) {
                total +=  evaluateIntegral(upperBound, coefficients[i], i + 1) -
                        evaluateIntegral(lowerBound, coefficients[i], i + 1);
            }
        }
        return total;
    }

    private double evaluateIntegral(double limit, double coefficient, int newExponent) {
        return Math.pow(limit, newExponent) * coefficient / newExponent;
    }
}
