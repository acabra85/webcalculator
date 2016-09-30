package com.acabra.calculator.integral;

/**
 * Created by Agustin on 9/30/2016.
 */
public class PolynomialIntegral extends IntegrableFunction {

    private int order;
    private boolean[] exponents;
    private double[] factors;

    public PolynomialIntegral(double lowerbound, double upperbound) {
        super(lowerbound, upperbound, IntegralFunctionType.POLYNOMIAL.getLabel());
    }

    @Override
    protected double solve() {
        return 0;
    }
}
