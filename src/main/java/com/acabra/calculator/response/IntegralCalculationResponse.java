package com.acabra.calculator.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Agustin on 10/5/2016.
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class IntegralCalculationResponse extends CalculationResponse {

    private final Double accuracy;
    private final double integralResult;

    @JsonCreator
    public IntegralCalculationResponse(@JsonProperty("id") long id,
                                       @JsonProperty("failure") boolean failure,
                                       @JsonProperty("expression") String expression,
                                       @JsonProperty("result") double result,
                                       @JsonProperty("integralResult") double integralResult,
                                       @JsonProperty("responseTime") long responseTime,
                                       @JsonProperty("description") String description) {
        super(id, failure, expression, result +"", responseTime, description);
        this.integralResult = integralResult;
        this.accuracy = result!=integralResult ? calculateAccuracy(result, integralResult) : 100;
    }

    private static double calculateAccuracy(double approx, double real) {
        return 100.0 - Math.abs(approx - real) * 100.0 / real;
    }

    @JsonProperty("accuracy")
    public synchronized double getAccuracy() {
        return accuracy;
    }

    public double getIntegralResult() {
        return integralResult;
    }
}
