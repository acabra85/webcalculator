package com.acabra.mmind.response;

import com.acabra.calculator.response.SimpleResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MMindDeleteTokenResponse extends SimpleResponse {
    private final String message;
    @Builder(setterPrefix = "with")
    protected MMindDeleteTokenResponse(long id, boolean failure, String message) {
        super(id, failure);
        this.message = message;
    }
}
