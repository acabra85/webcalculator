package com.acabra.fsands.response;

import com.acabra.calculator.response.SimpleResponse;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FixSpikeRestartResponse extends SimpleResponse {

    private final String action;
    private final String secret;

    @Builder(setterPrefix = "with")
    @JsonCreator
    protected FixSpikeRestartResponse(@JsonProperty("id")long id, @JsonProperty("failure") boolean failure,
                                   @JsonProperty("action") String action,
                                   @JsonProperty("secret") String secret
    ) {
        super(id, failure);
        this.action = action;
        this.secret = secret;
    }
}
