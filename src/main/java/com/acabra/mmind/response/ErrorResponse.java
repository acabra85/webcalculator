package com.acabra.mmind.response;

import com.acabra.calculator.response.SimpleResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse extends SimpleResponse {

    private final String error;

    @Builder(setterPrefix = "with")
    protected ErrorResponse(@JsonProperty("id") long id, @JsonProperty("error") String error) {
        super(id, true);
        this.error = error;
    }
}
