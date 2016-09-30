package com.acabra.calculator.integral;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Created by Agustin on 9/30/2016.
 */
public class IntegralFunctionFactory {

    public static IntegrableFunction createIntegralFunction(IntegralFunctionType functionType, double lowerbound, double upperbound, Optional<Double> result) {
        switch (functionType) {
            case EXPONENTIAL:
                return result.isPresent() ? new ExponentialIntegral(lowerbound, upperbound, result.get()) : new ExponentialIntegral(lowerbound, upperbound);
            default:
                throw new NoSuchElementException("function not defined in the scope of the calculator");
        }
    }

    public static IntegralFunctionType evaluateFunctionType(int functionId) {
        return IntegralFunctionType.provideType(functionId);
    }

    public static IntegrableFunction createPolynomialIntegralFunction(int lowerbound, int upperbound, int order, boolean[] exponents, double[] coefficients, Optional<Double> result) {
        return !result.isPresent() ? new PolynomialIntegral(lowerbound, upperbound, order, exponents, coefficients) : new PolynomialIntegral(lowerbound, upperbound, order, exponents, coefficients, result);
    }
}
