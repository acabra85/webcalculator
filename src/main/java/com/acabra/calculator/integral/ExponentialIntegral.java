package com.acabra.calculator.integral;

/**
 * Created by Agustin on 9/29/2016.
 */
public class ExponentialIntegral extends IntegrableFunction {

    public ExponentialIntegral(double lowerBound, double upperBound) {
        super(lowerBound, upperBound, IntegralFunctionType.EXPONENTIAL.getLabel());
    }

    public ExponentialIntegral(double lowerBound, double upperBound, double result) {
        super(lowerBound, upperBound, result, IntegralFunctionType.EXPONENTIAL.getLabel());
    }

    @Override
    public double solve() {
        if (null == result) {
            logger.info("solving in -> " + Thread.currentThread().getName());
            result = Math.exp(upperBound) - Math.exp(lowerBound);
        }
        return result;
    }
}
