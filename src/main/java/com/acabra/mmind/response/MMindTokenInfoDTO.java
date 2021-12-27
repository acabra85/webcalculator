package com.acabra.mmind.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder(setterPrefix = "with")
@Getter
public class MMindTokenInfoDTO {
    private final String token;
    private final String expiresAfter;

    @JsonCreator
    public MMindTokenInfoDTO(@JsonProperty("token") String token, @JsonProperty("expiresAfter") String expiresAfter) {
        this.token = token;
        this.expiresAfter = expiresAfter;
    }
}
