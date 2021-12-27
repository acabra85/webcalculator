package com.acabra.mmind.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder(setterPrefix = "with")
@Getter
public class MMindSystemStatusRoomDTO {
    private final long number;
    private final MMindTokenInfoDTO hostToken;
    private final MMindTokenInfoDTO guestToken;
    private final String expiresAfter;

    @JsonCreator
    public MMindSystemStatusRoomDTO(@JsonProperty("number") long number,
                                    @JsonProperty("hostToken") MMindTokenInfoDTO hostToken,
                                    @JsonProperty("guestToken") MMindTokenInfoDTO guestToken,
                                    @JsonProperty("expiresAfter") String expiresAfter) {
        this.number = number;
        this.hostToken = hostToken;
        this.guestToken = guestToken;
        this.expiresAfter = expiresAfter;
    }
}
