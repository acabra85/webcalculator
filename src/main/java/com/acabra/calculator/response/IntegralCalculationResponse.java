package com.acabra.calculator.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Agustin on 10/5/2016.
 */
public class IntegralCalculationResponse extends CalculationResponse {

    private Double accuracy;
    private double integralResult;

    public IntegralCalculationResponse() {}

    public IntegralCalculationResponse(long id, String expression, double approximation, double integralResult, long responseTime, String description) {
        super(id, expression, approximation, responseTime, description);
        this.integralResult = integralResult;
    }

    private static double calculateAccuracy(double approx, double real) {
        return 100.0 - Math.abs(approx - real) * 100.0 / real;
    }

    @JsonProperty("accuracy")
    public synchronized double getAccuracy() {
        if (null == accuracy) {
            this.accuracy = result!=integralResult ? calculateAccuracy(result, integralResult) : 100;
        }
        return accuracy;
    }

    @JsonProperty("integralResult")
    public double getIntegralResult() {
        return integralResult;
    }
}
