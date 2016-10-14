package com.acabra.calculator.integral.input;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Agustin on 10/9/2016.
 */
public class IntegrableFunctionInputParametersBuilder {

    private double lowerBound;
    private double upperBound;
    private List<Double> coefficients;
    private Double approximation;
    private Double integrationResult;

    public IntegrableFunctionInputParametersBuilder() {
        approximation = null;
        integrationResult = null;
    }

    public IntegrableFunctionInputParametersBuilder withLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
        return this;
    }

    public IntegrableFunctionInputParametersBuilder withUpperBound(double upperBound) {
        this.upperBound = upperBound;
        return this;
    }

    public IntegrableFunctionInputParametersBuilder withCoefficients(List<Double> coefficients) {
        this.coefficients = coefficients;
        return this;
    }

    public IntegrableFunctionInputParametersBuilder withApproximation(Double approximation) {
        this.approximation = approximation;
        return this;
    }

    public IntegrableFunctionInputParametersBuilder withIntegrationResult(Double integrationResult) {
        this.integrationResult = integrationResult;
        return this;
    }

    public IntegrableFunctionInputParameters build(){
        return new IntegrableFunctionInputParameters(this.lowerBound, this.upperBound,
                this.coefficients == null ? Optional.of(Collections.emptyList()) : Optional.of(this.coefficients),
                this.approximation == null ? Optional.empty() : Optional.of(this.approximation),
                this.integrationResult == null ? Optional.empty() : Optional.of(this.integrationResult));
    }
}