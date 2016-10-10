package com.acabra.calculator.domain;

/**
 * Created by Agustin on 10/8/2016.
 */
public class IntegralRequestBuilder {

    private double lowerBound;
    private double upperBound;
    private int repeatedCalculations;
    private int numThreads;
    private int functionId;
    private int approximationMethodId;
    private boolean areaInscribed;

    public IntegralRequestBuilder withLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
        return this;
    }

    public IntegralRequestBuilder withUpperBound(double upperBound) {
        this.upperBound = upperBound;
        return this;
    }

    public IntegralRequestBuilder withRepeatedCalculations(int repeatedCalculations) {
        this.repeatedCalculations = repeatedCalculations;
        return this;
    }

    public IntegralRequestBuilder withNumThreads(int numThreads) {
        this.numThreads = numThreads;
        return this;
    }

    public IntegralRequestBuilder withFunctionId(int functionId) {
        this.functionId = functionId;
        return this;
    }

    public IntegralRequestBuilder withApproximationMethodId(int methodId) {
        this.approximationMethodId = methodId;
        return this;
    }

    public IntegralRequestBuilder withAreaInscribed(boolean areaInscribed) {
        this.areaInscribed = areaInscribed;
        return this;
    }

    public IntegralRequest build() {
        return new IntegralRequest(lowerBound, upperBound, repeatedCalculations, numThreads, functionId, approximationMethodId, areaInscribed);
    }
}