package com.acabra.calculator.integral.approx;

import com.acabra.calculator.integral.IntegralSubRangeSupplier;
import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralFunction;
import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralFunctionFactory;
import com.acabra.calculator.integral.definiteintegral.IntegrableFunctionType;
import com.acabra.calculator.integral.input.IntegrableFunctionInputParametersBuilder;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * Created by Agustin on 10/17/2016.
 */
public abstract class AreaApproximatorNumericalMethod {

    protected final double lowerLimit;
    protected final double upperLimit;
    protected final IntegrableFunctionType functionType;
    protected final List<Double> coefficients;

    public AreaApproximatorNumericalMethod(double lowerLimit, double upperLimit, List<Double> coefficients, IntegrableFunctionType functionType) {
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.coefficients = Collections.unmodifiableList(coefficients);
        this.functionType = functionType;
    }

    protected DefiniteIntegralFunction provideIntegralFunctionWithApproximation(Double approximation) {
        return DefiniteIntegralFunctionFactory.createIntegralFunction(functionType, new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerLimit)
                .withUpperLimit(upperLimit)
                .withCoefficients(coefficients)
                .withApproximation(approximation)
                .build());
    }

    protected IntegralSubRangeSupplier createRangeSupplier(int repeatedCalculations) {
        return new IntegralSubRangeSupplier(functionType, lowerLimit, upperLimit, coefficients, repeatedCalculations);
    }

    protected abstract CompletableFuture<DefiniteIntegralFunction> approximate(final int repeatedCalculations, final ExecutorService executor);
    protected abstract Double applyRule(final DefiniteIntegralFunction function);
}
