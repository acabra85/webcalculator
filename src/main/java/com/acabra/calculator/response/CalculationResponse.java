package com.acabra.calculator.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Agustin on 9/27/2016.
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class CalculationResponse extends SimpleResponse {

    private final String expression;
    protected final double result;
    private final String description;
    private final long responseTime;

    @JsonCreator
    public CalculationResponse(@JsonProperty("id") long id,
                               @JsonProperty("expression") String expression,
                               @JsonProperty("result") double result,
                               @JsonProperty("responseTime") long responseTime,
                               @JsonProperty("description") String description) {
        super(id);
        this.expression = expression;
        this.result = result;
        this.description = description;
        this.responseTime = responseTime;
    }

    public String getExpression() {
        return expression;
    }

    public double getResult() {
        return result;
    }

    public String getDescription() {
        return description;
    }

    public long getResponseTime() {
        return responseTime;
    }
}
