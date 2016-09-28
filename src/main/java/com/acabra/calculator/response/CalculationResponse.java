package com.acabra.calculator.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Agustin on 9/27/2016.
 */
public class CalculationResponse extends SimpleResponse {

    private final String expression;
    private final String result;

    public CalculationResponse(long id, String expression, String result) {
        super(id);
        this.expression = expression;
        this.result = result;
    }

    @JsonProperty("expression")
    public String getExpression() {
        return expression;
    }

    @JsonProperty("result")
    public String getResult() {
        return result;
    }
}
