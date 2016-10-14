package com.acabra.calculator.domain;

import java.util.Collection;
import java.util.List;

/**
 * Created by Agustin on 9/30/2016.
 */
public class IntegralRequest {

    private final double lowerBound;
    private final double upperBound;
    private final int repeatedCalculations;
    private final int numThreads;
    private final int approximationMethodId;
    private final int functionId;
    private final boolean areaInscribed;
    private List<Double> coefficients;

    public IntegralRequest(double lowerBound, double upperBound, int repeatedCalculations, int numThreads, int functionId, int approximationMethodId, boolean areaInscribed,
                           List<Double> coefficients) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
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

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
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
