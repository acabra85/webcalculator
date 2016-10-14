package com.acabra.calculator.integral;

import com.acabra.calculator.integral.approx.NumericalMethodApproximationType;
import com.acabra.calculator.integral.input.IntegrableFunctionInputParameters;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;

/**
 * Created by Agustin on 9/30/2016.
 */
public class IntegralFunctionFactory {

    private static final String FORMAT_LIMITS_ERROR_MSG = "invalid bounds[%s, %s], not in function's %s domain";

    private static void validIntegralExponentialParameters(IntegrableFunctionInputParameters parameters) {
        if (!FExponential.inFunctionsDomain(parameters.getLowerBound())
                || !FExponential.inFunctionsDomain(parameters.getUpperBound())) {
            throw new UnsupportedOperationException(String.format(FORMAT_LIMITS_ERROR_MSG, parameters.getLowerBound(), parameters.getUpperBound(), IntegralFunctionType.EXPONENTIAL.getLabel()));
        }
    }

    private static void validIntegralPolynomialParameters(IntegrableFunctionInputParameters parameters) {
        if (!FPolynomial.inFunctionsDomain(parameters.getLowerBound())
                || !FPolynomial.inFunctionsDomain(parameters.getUpperBound())) {
            throw new UnsupportedOperationException(String.format(FORMAT_LIMITS_ERROR_MSG, parameters.getLowerBound(), parameters.getUpperBound(),
                    IntegralFunctionType.POLYNOMIAL.getLabel()));
        }
    }

    private static void validIntegralLogarithmicParameters(IntegrableFunctionInputParameters parameters) {
        if (!FLogarithmic.inFunctionsDomain(parameters.getLowerBound())
                || !FLogarithmic.inFunctionsDomain(parameters.getUpperBound())) {
            throw new UnsupportedOperationException(String.format(FORMAT_LIMITS_ERROR_MSG, parameters.getLowerBound(), parameters.getUpperBound(),
                    IntegralFunctionType.LOGARITHMIC.getLabel()));
        }
    }

    private static IntegrableFunction createExponentialIntegrableFunction(IntegrableFunctionInputParameters parameters) {
        validIntegralExponentialParameters(parameters);
        return new FExponential(parameters.getLowerBound(), parameters.getUpperBound(), parameters.getIntegrationResult().orElse(null), parameters.getApproximation().orElse(null));
    }

    private static IntegrableFunction createLogarithmicIntegralFunction(IntegrableFunctionInputParameters parameters) {
        validIntegralLogarithmicParameters(parameters);
        return new FLogarithmic(parameters.getLowerBound(), parameters.getUpperBound(), parameters.getIntegrationResult(), parameters.getApproximation());
    }

    private static IntegrableFunction createPolynomialIntegralFunction(IntegrableFunctionInputParameters parameters) {
        validIntegralPolynomialParameters(parameters);
        return new FPolynomial(parameters.getLowerBound(), parameters.getUpperBound(), parameters.getCoefficients(),
                parameters.getIntegrationResult(), parameters.getApproximation());
    }

    public static IntegrableFunction createIntegralFunction(IntegralFunctionType functionType, IntegrableFunctionInputParameters parameters) {
        switch (functionType) {
            case EXPONENTIAL:
                return createExponentialIntegrableFunction(parameters);
            case POLYNOMIAL:
                return createPolynomialIntegralFunction(parameters);
            case LOGARITHMIC:
                return createLogarithmicIntegralFunction(parameters);
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
