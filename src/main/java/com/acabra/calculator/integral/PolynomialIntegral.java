package com.acabra.calculator.integral;

import java.util.Optional;

/**
 * Created by Agustin on 9/30/2016.
 */
public class PolynomialIntegral extends IntegrableFunction {

    private final int order;
    private final boolean[] exponents;
    private final double[] coefficients;

    public PolynomialIntegral(double lowerbound, double upperbound, int order,
                              boolean[] exponents, double[] coefficients) {
        super(lowerbound, upperbound, IntegralFunctionType.POLYNOMIAL.getLabel());
        this.order = order;
        this.exponents = exponents;
        this.coefficients = coefficients;
    }

    public PolynomialIntegral(int lowerbound, int upperbound, int order, boolean[] exponents, double[] coefficients, Optional<Double> result) {
        super(lowerbound, upperbound, result.get(), IntegralFunctionType.POLYNOMIAL.getLabel());
        this.order = order;
        this.exponents = exponents;
        this.coefficients = coefficients;
    }

    @Override
    protected double solve() {
        if (null == result) {
            result = executeIntegration(false);
        }
        return result;
    }

    @Override
    protected double solveAreaUnderTheGraph() {
        if (areaUnderTheGraph == null) {
            areaUnderTheGraph = executeIntegration(true);
        }
        return areaUnderTheGraph;
    }

    private double executeIntegration(boolean area) {
        double total = 0.0;
        for(int i = 0; i < order; i++) {
            if (exponents[i] && coefficients[i] != 0) {
                if (area) {
                    total +=  evaluateIntegral(upperBound, coefficients[i], i + 1, true) +
                            evaluateIntegral(lowerBound, coefficients[i], i + 1, true);
                } else {
                    total +=  evaluateIntegral(upperBound, coefficients[i], i + 1, false) -
                            evaluateIntegral(lowerBound, coefficients[i], i + 1, false);
                }
            }
        }
        return total;
    }

    private double evaluateIntegral(double limit, double coefficient, int newExponent, boolean area) {
        double orderValue = Math.pow(limit, newExponent) * coefficient / newExponent;
        return area ? Math.abs(orderValue) : orderValue;
    }
}
