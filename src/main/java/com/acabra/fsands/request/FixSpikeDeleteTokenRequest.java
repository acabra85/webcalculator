package com.acabra.fsands.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FixSpikeDeleteTokenRequest {

    private final String tokenToDelete;
    private final String userToken;

    @JsonCreator
    public FixSpikeDeleteTokenRequest(@JsonProperty("tokenToDelete") String tokenToDelete,
                                   @JsonProperty("userToken")String userToken) {
        this.tokenToDelete = tokenToDelete;
        this.userToken = userToken;
    }
}
