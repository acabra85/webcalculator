package com.acabra.calculator.integral.definiteintegral;

import com.acabra.calculator.integral.approx.NumericalMethodAreaApproximationType;
import com.acabra.calculator.integral.input.IntegrableFunctionInputParameters;

import java.util.NoSuchElementException;

/**
 * Created by Agustin on 9/30/2016.
 */
public class DefiniteIntegralFunctionFactory {

    private static final String FORMAT_LIMITS_ERROR_MSG = "invalid bounds[%s, %s], not in function's %s domain";

    private static void validIntegralExponentialParameters(IntegrableFunctionInputParameters parameters) {
        if (!DefiniteIntegralExponential.inFunctionsDomain(parameters.getLowerLimit())
                || !DefiniteIntegralExponential.inFunctionsDomain(parameters.getUpperLimit())) {
            throw new UnsupportedOperationException(String.format(FORMAT_LIMITS_ERROR_MSG, parameters.getLowerLimit(), parameters.getUpperLimit(), IntegrableFunctionType.EXPONENTIAL.getLabel()));
        }
    }

    private static void validIntegralPolynomialParameters(IntegrableFunctionInputParameters parameters) {
        if (!DefiniteIntegralPolynomial.inFunctionsDomain(parameters.getLowerLimit())
                || !DefiniteIntegralPolynomial.inFunctionsDomain(parameters.getUpperLimit()) ||
                 !parameters.getCoefficients().stream().anyMatch(v -> Double.isFinite(v) || Double.isNaN(v))) {
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

    private static DefiniteIntegralFunction createExponentialIntegrableFunction(IntegrableFunctionInputParameters parameters) {
        validIntegralExponentialParameters(parameters);
        return new DefiniteIntegralExponential(parameters.getLowerLimit(), parameters.getUpperLimit(), parameters.getIntegrationResult(), parameters.getApproximation());
    }

    private static void validIntegralInverseParameters(IntegrableFunctionInputParameters parameters) {
        if (!DefiniteIntegralInverse.inFunctionsDomain(parameters.getLowerLimit()) ||
                !DefiniteIntegralInverse.inFunctionsDomain(parameters.getUpperLimit())) {
            throw new UnsupportedOperationException(String.format(FORMAT_LIMITS_ERROR_MSG, parameters.getLowerLimit(), parameters.getUpperLimit(),
                    IntegrableFunctionType.INVERSE.getLabel()));
        }
    }

    private static DefiniteIntegralFunction createLogarithmicIntegralFunction(IntegrableFunctionInputParameters parameters) {
        validIntegralLogarithmicParameters(parameters);
        return new FLogarithmic(parameters.getLowerLimit(), parameters.getUpperLimit(), parameters.getIntegrationResult(), parameters.getApproximation());
    }

    private static DefiniteIntegralFunction createPolynomialIntegralFunction(IntegrableFunctionInputParameters parameters) {
        validIntegralPolynomialParameters(parameters);
        return new DefiniteIntegralPolynomial(parameters.getLowerLimit(), parameters.getUpperLimit(), parameters.getCoefficients(),
                parameters.getIntegrationResult(), parameters.getApproximation());
    }

    private static DefiniteIntegralFunction createInverseIntegralFunction(IntegrableFunctionInputParameters parameters) {
        validIntegralInverseParameters(parameters);
        return new DefiniteIntegralInverse(parameters.getLowerLimit(), parameters.getUpperLimit(), parameters.getIntegrationResult(), parameters.getApproximation());
    }

    public static DefiniteIntegralFunction createIntegralFunction(IntegrableFunctionType functionType, IntegrableFunctionInputParameters parameters) {
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

    public static NumericalMethodAreaApproximationType evaluateApproximationMethodType(int approximationMethodId) {
        return NumericalMethodAreaApproximationType.provideType(approximationMethodId);
    }
}
