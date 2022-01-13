package com.acabra.fsands.response;

import com.acabra.calculator.response.SimpleResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FixSpikeDeleteTokenResponse extends SimpleResponse {
    private final String message;
    @Builder(setterPrefix = "with")
    protected FixSpikeDeleteTokenResponse(long id, boolean failure, String message) {
        super(id, failure);
        this.message = message;
    }
}
