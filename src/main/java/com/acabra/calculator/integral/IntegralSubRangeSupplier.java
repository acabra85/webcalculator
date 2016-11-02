package com.acabra.calculator.integral;

import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralFunction;
import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralFunctionFactory;
import com.acabra.calculator.integral.definiteintegral.IntegrableFunctionType;
import com.acabra.calculator.integral.input.IntegrableFunctionInputParameters;
import com.acabra.calculator.integral.input.IntegrableFunctionInputParametersBuilder;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by Agustin on 9/29/2016.
 */
public class IntegralSubRangeSupplier implements Supplier {

    private final IntegrableFunctionType functionType;
    private final List<Double> coefficients;
    private final SubRangeSupplier rangeSupplier;

    /**
     *
     * @param lowerLimit lower integral bound must be equal or lower than upperLimit
     * @param upperLimit upper integral bound must be greater or equal than lowerLimit
     * @param repeatedCalculations amount of repeatedCalculations to obtain the integral must be > 0
     * @param functionType the type of the integral function to create
     */
    public IntegralSubRangeSupplier(IntegrableFunctionType functionType, double lowerLimit, double upperLimit, List<Double> coefficients, int repeatedCalculations) {
        this.functionType = functionType;
        this.coefficients = coefficients;
        rangeSupplier = new SubRangeSupplier(lowerLimit, upperLimit, repeatedCalculations);
    }

    public boolean hasMoreSubRanges() {
        return rangeSupplier.hasMoreSubRanges();
    }

    @Override
    public DefiniteIntegralFunction get() {
        Interval interval = rangeSupplier.get();
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(interval.getLowerLimit())
                .withUpperLimit(interval.getUpperLimit())
                .withCoefficients(coefficients)
                .build();
        return DefiniteIntegralFunctionFactory.createIntegralFunction(functionType, parameters);
    }
}
