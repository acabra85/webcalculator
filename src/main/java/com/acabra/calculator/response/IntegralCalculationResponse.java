package com.acabra.calculator.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Agustin on 10/5/2016.
 */
public class IntegralCalculationResponse extends CalculationResponse {

    private final double accuracy;
    private final double integralResult;

    public IntegralCalculationResponse(long id, String expression, double approximation, double integralResult, long responseTime, String description) {
        super(id, expression, approximation, responseTime, description);
        this.integralResult = integralResult;
        this.accuracy = 100.0 - calculateAccuracy(approximation, integralResult);
    }

    private static double calculateAccuracy(double aprox, double real) {
        return Math.abs(aprox - real) * 100.0 / real;
    }

    @JsonProperty("accuracy")
    public double getAccuracy() {
        return accuracy;
    }

    @JsonProperty("integralResult")
    public double getIntegralResult() {
        return integralResult;
    }
}
