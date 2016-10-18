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
    private final Double integralResult;

    @JsonCreator
    public IntegralCalculationResponse(@JsonProperty("id") long id,
                                       @JsonProperty("failure") boolean failure,
                                       @JsonProperty("expression") String expression,
                                       @JsonProperty("result") Double result,
                                       @JsonProperty("integralResult") Double integralResult,
                                       @JsonProperty("responseTime") long responseTime,
                                       @JsonProperty("description") String description) {
        super(id, failure, expression, result == null ? "0" : result.toString(), responseTime, description);
        this.integralResult = integralResult;
        this.accuracy = calculateAccuracy(result, integralResult);
    }

    private static double calculateAccuracy(Double approx, Double real) {
        if (null == approx || null == real) {
            return 0.0;
        }
        double distance = Math.abs(real - approx);
        if (distance > Math.abs(real)) {
            return 0.0;
        } else {
            return 100.0 - (distance * 100.0 / Math.abs(real));
        }
    }

    @JsonProperty("accuracy")
    public double getAccuracy() {
        return accuracy;
    }

    public double getIntegralResult() {
        return integralResult;
    }
}
