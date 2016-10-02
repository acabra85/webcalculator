package com.acabra.calculator.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Agustin on 9/27/2016.
 */
public class CalculationResponse extends SimpleResponse {

    private final String expression;
    private final double result;
    private final String description;

    public CalculationResponse(long id, String expression, double result, String description) {
        super(id);
        this.expression = expression;
        this.result = result;
        this.description = description;
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
}
