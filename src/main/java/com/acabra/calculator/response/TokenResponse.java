package com.acabra.calculator.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Agustin on 9/27/2016.
 */
public class TokenResponse extends SimpleResponse {

    private final String token;

    public TokenResponse(long id, String token) {
        super(id);
        this.token = token;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }
}
