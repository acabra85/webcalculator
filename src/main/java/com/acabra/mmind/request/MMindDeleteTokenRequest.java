package com.acabra.mmind.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MMindDeleteTokenRequest {

    private final String tokenToDelete;
    private final String userToken;

    @JsonCreator
    public MMindDeleteTokenRequest(@JsonProperty("tokenToDelete") String tokenToDelete,
                                   @JsonProperty("userToken")String userToken) {
        this.tokenToDelete = tokenToDelete;
        this.userToken = userToken;
    }
}
