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
            case POLYNOMIAL:
                return new PolynomialIntegral(lowerbound, upperbound);
            default:
                throw new NoSuchElementException("function not defined in the scope of the calculator");
        }
    }

    public static IntegralFunctionType evaluateFunctionType(int functionId) {
        return IntegralFunctionType.provideType(functionId);
    }
}
