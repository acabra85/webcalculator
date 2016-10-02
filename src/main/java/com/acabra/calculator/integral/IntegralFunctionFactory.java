package com.acabra.calculator.integral;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Created by Agustin on 9/30/2016.
 */
public class IntegralFunctionFactory {

    public static IntegrableFunction createIntegralFunction(IntegralFunctionType functionType, double lowerbound, double upperbound, Optional<Double> result) {
        validIntegralLimits(lowerbound, upperbound);
        switch (functionType) {
            case EXPONENTIAL:
                return result.isPresent() ? new ExponentialIntegral(lowerbound, upperbound, result.get()) : new ExponentialIntegral(lowerbound, upperbound);
            default:
                throw new NoSuchElementException("function not defined in the scope of the calculator");
        }
    }

    public static IntegrableFunction createFullySolvedIntegralFunction(IntegralFunctionType functionType, double lowerBound, double upperBound, double result, double sequenceRiemannRectangleAreaSum) {
        validIntegralLimits(lowerBound, upperBound);
        switch (functionType) {
            case EXPONENTIAL:
                return new ExponentialIntegral(lowerBound, upperBound, result, sequenceRiemannRectangleAreaSum);
            default:
                throw new NoSuchElementException("function not defined in the scope of the calculator");
        }
    }

    public static IntegrableFunction createPolynomialIntegralFunction(int lowerbound, int upperbound, int order, double[] coefficients, Optional<Double> result) {
        validIntegralLimits(lowerbound, upperbound);
        return !result.isPresent() ? new PolynomialIntegral(lowerbound, upperbound, order, coefficients) : new PolynomialIntegral(lowerbound, upperbound, order, coefficients, result);
    }

    public static IntegralFunctionType evaluateFunctionType(int functionId) {
        return IntegralFunctionType.provideType(functionId);
    }

    private static void validIntegralLimits(double lowerbound, double upperbound) {
        if (lowerbound > upperbound) {
            throw new InputMismatchException("invalid integral range");
        }
    }
}
