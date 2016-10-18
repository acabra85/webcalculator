package com.acabra.calculator.integral.function;

import com.acabra.calculator.integral.approx.NumericalMethodApproximationType;
import com.acabra.calculator.integral.input.IntegrableFunctionInputParameters;

import java.util.NoSuchElementException;

/**
 * Created by Agustin on 9/30/2016.
 */
public class FunctionFactory {

    private static final String FORMAT_LIMITS_ERROR_MSG = "invalid bounds[%s, %s], not in function's %s domain";

    private static void validIntegralExponentialParameters(IntegrableFunctionInputParameters parameters) {
        if (!FExponential.inFunctionsDomain(parameters.getLowerLimit())
                || !FExponential.inFunctionsDomain(parameters.getUpperLimit())) {
            throw new UnsupportedOperationException(String.format(FORMAT_LIMITS_ERROR_MSG, parameters.getLowerLimit(), parameters.getUpperLimit(), IntegrableFunctionType.EXPONENTIAL.getLabel()));
        }
    }

    private static void validIntegralPolynomialParameters(IntegrableFunctionInputParameters parameters) {
        if (!FPolynomial.inFunctionsDomain(parameters.getLowerLimit())
                || !FPolynomial.inFunctionsDomain(parameters.getUpperLimit())) {
            throw new UnsupportedOperationException(String.format(FORMAT_LIMITS_ERROR_MSG, parameters.getLowerLimit(), parameters.getUpperLimit(),
                    IntegrableFunctionType.POLYNOMIAL.getLabel()));
        }
    }

    private static void validIntegralLogarithmicParameters(IntegrableFunctionInputParameters parameters) {
        if (!FLogarithmic.inFunctionsDomain(parameters.getLowerLimit())
                || !FLogarithmic.inFunctionsDomain(parameters.getUpperLimit())) {
            throw new UnsupportedOperationException(String.format(FORMAT_LIMITS_ERROR_MSG, parameters.getLowerLimit(), parameters.getUpperLimit(),
                    IntegrableFunctionType.LOGARITHMIC.getLabel()));
        }
    }

    private static IntegrableFunction createExponentialIntegrableFunction(IntegrableFunctionInputParameters parameters) {
        validIntegralExponentialParameters(parameters);
        return new FExponential(parameters.getLowerLimit(), parameters.getUpperLimit(), parameters.getIntegrationResult().orElse(null), parameters.getApproximation().orElse(null));
    }

    private static void validIntegralInverseParameters(IntegrableFunctionInputParameters parameters) {
        if (!FInverse.inFunctionsDomain(parameters.getLowerLimit()) ||
                !FInverse.inFunctionsDomain(parameters.getUpperLimit())) {
            throw new UnsupportedOperationException(String.format(FORMAT_LIMITS_ERROR_MSG, parameters.getLowerLimit(), parameters.getUpperLimit(),
                    IntegrableFunctionType.INVERSE.getLabel()));
        }
    }

    private static IntegrableFunction createLogarithmicIntegralFunction(IntegrableFunctionInputParameters parameters) {
        validIntegralLogarithmicParameters(parameters);
        return new FLogarithmic(parameters.getLowerLimit(), parameters.getUpperLimit(), parameters.getIntegrationResult(), parameters.getApproximation());
    }

    private static IntegrableFunction createPolynomialIntegralFunction(IntegrableFunctionInputParameters parameters) {
        validIntegralPolynomialParameters(parameters);
        return new FPolynomial(parameters.getLowerLimit(), parameters.getUpperLimit(), parameters.getCoefficients(),
                parameters.getIntegrationResult(), parameters.getApproximation());
    }

    private static IntegrableFunction createInverseIntegralFunction(IntegrableFunctionInputParameters parameters) {
        validIntegralInverseParameters(parameters);
        return new FInverse(parameters.getLowerLimit(), parameters.getUpperLimit(), parameters.getIntegrationResult().orElse(null), parameters.getApproximation().orElse(null));
    }

    public static IntegrableFunction createIntegralFunction(IntegrableFunctionType functionType, IntegrableFunctionInputParameters parameters) {
        switch (functionType) {
            case EXPONENTIAL:
                return createExponentialIntegrableFunction(parameters);
            case POLYNOMIAL:
                return createPolynomialIntegralFunction(parameters);
            case LOGARITHMIC:
                return createLogarithmicIntegralFunction(parameters);
            case INVERSE:
                return createInverseIntegralFunction(parameters);
            default:
                throw new NoSuchElementException("function not defined in the scope of the calculator");
        }
    }

    public static IntegrableFunctionType evaluateFunctionType(int functionId) {
        return IntegrableFunctionType.provideType(functionId);
    }

    public static NumericalMethodApproximationType evaluateApproximationMethodType(int approximationMethodId) {
        return NumericalMethodApproximationType.provideType(approximationMethodId);
    }
}
