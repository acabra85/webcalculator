package com.acabra.calculator.integral;

import com.acabra.calculator.integral.approx.NumericalMethodApproximationType;
import com.acabra.calculator.integral.input.IntegrableFunctionInputParameters;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Created by Agustin on 9/30/2016.
 */
public class IntegralFunctionFactory {

    private static void validIntegralLimits(double lowerbound, double upperbound) {
        if (lowerbound > upperbound) {
            throw new InputMismatchException("invalid integral range");
        }
    }

    private static IntegrableFunction createExponentialIntegrableFunction(IntegrableFunctionInputParameters parameters) {
        return new ExponentialIntegral(parameters.getLowerBound(), parameters.getUpperBound(), parameters.getIntegrationResult().orElse(null), parameters.getApproximation().orElse(null));
    }

    private static IntegrableFunction createPolynomialIntegralFunction(IntegrableFunctionInputParameters parameters) {
        return new PolynomialIntegral(parameters.getLowerBound(), parameters.getUpperBound(), parameters.getCoefficients(),
                parameters.getIntegrationResult(), parameters.getApproximation());
    }

    public static IntegrableFunction createIntegralFunction(IntegralFunctionType functionType, IntegrableFunctionInputParameters parameters) {
        validIntegralLimits(parameters.getLowerBound(), parameters.getUpperBound());
        switch (functionType) {
            case EXPONENTIAL:
                return createExponentialIntegrableFunction(parameters);
            case POLYNOMIAL:
                return createPolynomialIntegralFunction(parameters);
            default:
                throw new NoSuchElementException("function not defined in the scope of the calculator");
        }
    }

    public static IntegralFunctionType evaluateFunctionType(int functionId) {
        return IntegralFunctionType.provideType(functionId);
    }

    public static NumericalMethodApproximationType evaluateApproximationMethodType(int approximationMethodId) {
        return NumericalMethodApproximationType.provideType(approximationMethodId);
    }
}
