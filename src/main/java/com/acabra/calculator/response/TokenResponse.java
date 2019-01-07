package com.acabra.calculator.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Agustin on 9/27/2016.
 */
@JsonInclude
public class TokenResponse extends SimpleResponse {

    private static final long serialVersionUID = -8933813372767652274L;
    private final String token;

    @JsonCreator
    public TokenResponse(@JsonProperty("id") long id,
                         @JsonProperty("token")String token) {
        super(id, false);
        this.token = token;
    }


    public String getToken() {
        return token;
    }
}
