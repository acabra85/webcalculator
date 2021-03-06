package com.acabra.calculator.integral.definiteintegral;

import com.acabra.calculator.function.CosineFunction;
import com.acabra.calculator.function.ExponentialFunction;
import com.acabra.calculator.function.InverseFunction;
import com.acabra.calculator.function.LogarithmicFunction;
import com.acabra.calculator.function.PolynomialFunction;
import com.acabra.calculator.function.SineFunction;
import com.acabra.calculator.integral.approx.NumericalMethodAreaApproximationType;
import com.acabra.calculator.integral.input.IntegrableFunctionInputParameters;
import java.util.NoSuchElementException;

/**
 * Created by Agustin on 9/30/2016.
 */
public class DefiniteIntegralFunctionFactory {

    private static final String FORMAT_LIMITS_ERROR_MSG = "invalid bounds[%s, %s], not in function's %s DOMAIN";

    private static void validIntegralExponentialParameters(IntegrableFunctionInputParameters parameters) {
        if (!ExponentialFunction.DOMAIN.belongsDomain(parameters.getLowerLimit())
                || !ExponentialFunction.DOMAIN.belongsDomain(parameters.getUpperLimit())) {
            throw new UnsupportedOperationException(String.format(FORMAT_LIMITS_ERROR_MSG, parameters.getLowerLimit(), parameters.getUpperLimit(), IntegrableFunctionType.EXPONENTIAL.getLabel()));
        }
    }

    private static void validIntegralPolynomialParameters(IntegrableFunctionInputParameters parameters) {
        if (!PolynomialFunction.DOMAIN.belongsDomain(parameters.getLowerLimit())
                || !PolynomialFunction.DOMAIN.belongsDomain(parameters.getUpperLimit()) ||
                 !parameters.getCoefficients().stream().anyMatch(v -> Double.isFinite(v) || Double.isNaN(v))) {
            throw new UnsupportedOperationException(String.format(FORMAT_LIMITS_ERROR_MSG, parameters.getLowerLimit(), parameters.getUpperLimit(),
                    IntegrableFunctionType.POLYNOMIAL.getLabel()));
        }
    }

    private static void validIntegralLogarithmicParameters(IntegrableFunctionInputParameters parameters) {
        if (!LogarithmicFunction.DOMAIN.belongsDomain(parameters.getLowerLimit())
                || !LogarithmicFunction.DOMAIN.belongsDomain(parameters.getUpperLimit())) {
            throw new UnsupportedOperationException(String.format(FORMAT_LIMITS_ERROR_MSG, parameters.getLowerLimit(), parameters.getUpperLimit(),
                    IntegrableFunctionType.LOGARITHMIC.getLabel()));
        }
    }

    private static void validIntegralInverseParameters(IntegrableFunctionInputParameters parameters) {
        if (!InverseFunction.DOMAIN.belongsDomain(parameters.getLowerLimit()) ||
                !InverseFunction.DOMAIN.belongsDomain(parameters.getUpperLimit())) {
            throw new UnsupportedOperationException(String.format(FORMAT_LIMITS_ERROR_MSG, parameters.getLowerLimit(), parameters.getUpperLimit(),
                    IntegrableFunctionType.INVERSE.getLabel()));
        }
    }
    private static void validIntegralSineParameters(IntegrableFunctionInputParameters parameters) {
        if (!SineFunction.DOMAIN.belongsDomain(parameters.getLowerLimit())
                || !SineFunction.DOMAIN.belongsDomain(parameters.getUpperLimit())
        ) {
            throw new UnsupportedOperationException(String.format(FORMAT_LIMITS_ERROR_MSG, parameters.getLowerLimit(), parameters.getUpperLimit(),
                    IntegrableFunctionType.SINE.getLabel()));
        }
    }

    private static void validIntegralCosineParameters(IntegrableFunctionInputParameters parameters) {
        if (!CosineFunction.DOMAIN.belongsDomain(parameters.getLowerLimit())
                || !CosineFunction.DOMAIN.belongsDomain(parameters.getUpperLimit())
        ) {
            throw new UnsupportedOperationException(String.format(FORMAT_LIMITS_ERROR_MSG, parameters.getLowerLimit(), parameters.getUpperLimit(),
                    IntegrableFunctionType.COS.getLabel()));
        }
    }

    private static DefiniteIntegralFunction createExponentialIntegrableFunction(IntegrableFunctionInputParameters parameters) {
        validIntegralExponentialParameters(parameters);
        return new DefiniteIntegralExponential(parameters.getLowerLimit(), parameters.getUpperLimit(), parameters.getIntegrationResult(), parameters.getApproximation());
    }

    private static DefiniteIntegralFunction createLogarithmicIntegralFunction(IntegrableFunctionInputParameters parameters) {
        validIntegralLogarithmicParameters(parameters);
        return new DefiniteIntegralLogarithmic(parameters.getLowerLimit(), parameters.getUpperLimit(), parameters.getIntegrationResult(), parameters.getApproximation());
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

    private static DefiniteIntegralFunction createSineIntegralFunction(IntegrableFunctionInputParameters parameters) {
        validIntegralSineParameters(parameters);
        return null;
    }

    private static DefiniteIntegralFunction createCosineIntegralFunction(IntegrableFunctionInputParameters parameters) {
        validIntegralCosineParameters(parameters);
        return null;
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
            case COS:
                return createCosineIntegralFunction(parameters);
            case SINE:
                return createSineIntegralFunction(parameters);
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
