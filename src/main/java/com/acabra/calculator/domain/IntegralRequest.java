package com.acabra.calculator.domain;

import java.util.List;

/**
 * Created by Agustin on 9/30/2016.
 */
public class IntegralRequest {

    private final double lowerLimit;
    private final double upperLimit;
    private final int repeatedCalculations;
    private final int numThreads;
    private final int approximationMethodId;
    private final int functionId;
    private final boolean areaInscribed;
    private List<Double> coefficients;

    public IntegralRequest(double lowerLimit, double upperLimit, int repeatedCalculations, int numThreads, int functionId, int approximationMethodId, boolean areaInscribed,
                           List<Double> coefficients) {
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.repeatedCalculations = repeatedCalculations;
        this.numThreads = numThreads;
        this.functionId = functionId;
        this.areaInscribed = areaInscribed;
        this.approximationMethodId = approximationMethodId;
        this.coefficients = coefficients;
    }

    public int getApproximationMethodId() {
        return approximationMethodId;
    }

    public double getLowerLimit() {
        return lowerLimit;
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    public int getRepeatedCalculations() {
        return repeatedCalculations;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public int getFunctionId() {
        return functionId;
    }

    public boolean isAreaInscribed() {
        return areaInscribed;
    }

    public List<Double> getCoefficients() {
        return coefficients;
    }
}
