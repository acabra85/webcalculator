package com.acabra.calculator.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Agustin on 9/27/2016.
 */
public class TokenResponse extends SimpleResponse {

    private String token;

    public TokenResponse() {}

    public TokenResponse(long id, String token) {
        this.id = id;
        this.token = token;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }
}
