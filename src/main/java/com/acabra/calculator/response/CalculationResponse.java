package com.acabra.calculator.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Agustin on 9/27/2016.
 */
public class CalculationResponse extends SimpleResponse {

    private final String expression;
    private final double result;
    private final String description;
    private final long responseTime;

    public CalculationResponse(long id, String expression, double result, long responseTime, String description) {
        super(id);
        this.expression = expression;
        this.result = result;
        this.description = description;
        this.responseTime = responseTime;
    }

    @JsonProperty("expression")
    public String getExpression() {
        return expression;
    }

    @JsonProperty("result")
    public double getResult() {
        return result;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("responseTime")
    public long getResponseTime() {
        return responseTime;
    }
}
