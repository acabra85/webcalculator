package com.acabra.mmind.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MMindRestartRequest {
    private final String secret;
    private final String token;

    public MMindRestartRequest(@JsonProperty("secret") String secret, @JsonProperty("token") String token) {
        this.secret = secret;
        this.token = token;
    }
}
