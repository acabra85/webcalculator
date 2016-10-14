package com.acabra.calculator.integral.input;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Agustin on 10/9/2016.
 */
public class IntegrableFunctionInputParameters {

    private final double lowerBound;
    private final double upperBound;
    private final Optional<List<Double>> coefficients;
    private final Optional<Double> approximation;
    private final Optional<Double> integrationResult;

    public IntegrableFunctionInputParameters(double lowerBound, double upperBound, Optional<List<Double>>  coefficients, Optional<Double> approximation, Optional<Double> integrationResult) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.coefficients = coefficients == null ? Optional.of(Collections.emptyList()) : coefficients;
        this.approximation = approximation;
        this.integrationResult = integrationResult;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public List<Double> getCoefficients() {
        return coefficients.orElse(Collections.emptyList());
    }

    public Optional<Double> getApproximation() {
        return approximation;
    }

    public Optional<Double> getIntegrationResult() {
        return integrationResult;
    }
}
