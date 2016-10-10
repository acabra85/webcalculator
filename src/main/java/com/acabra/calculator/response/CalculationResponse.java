package com.acabra.calculator.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Agustin on 9/27/2016.
 */
public class CalculationResponse extends SimpleResponse {

    private String expression;
    protected double result;
    private String description;
    private long responseTime;

    public CalculationResponse() {
    }

    public CalculationResponse(long id, String expression, double result, long responseTime, String description) {
        this.id = id;
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
