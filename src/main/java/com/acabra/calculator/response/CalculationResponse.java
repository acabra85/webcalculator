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
    protected final String approximation;
    private final String description;
    private final long responseTime;

    @JsonCreator
    public CalculationResponse(@JsonProperty("id") long id,
                               @JsonProperty("failure") boolean failure,
                               @JsonProperty("expression") String expression,
                               @JsonProperty("result") String result,
                               @JsonProperty("responseTime") long responseTime,
                               @JsonProperty("description") String description) {
        super(id, failure);
        this.expression = expression;
        this.approximation = result;
        this.description = description;
        this.responseTime = responseTime;
    }

    public String getExpression() {
        return expression;
    }

    public String getApproximation() {
        return approximation;
    }

    public String getDescription() {
        return description;
    }

    public long getResponseTime() {
        return responseTime;
    }

}
