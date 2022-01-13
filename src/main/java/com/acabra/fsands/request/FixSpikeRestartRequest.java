package com.acabra.fsands.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FixSpikeRestartRequest {
    private final String secret;
    private final String token;

    public FixSpikeRestartRequest(@JsonProperty("secret") String secret, @JsonProperty("token") String token) {
        this.secret = secret;
        this.token = token;
    }
}
