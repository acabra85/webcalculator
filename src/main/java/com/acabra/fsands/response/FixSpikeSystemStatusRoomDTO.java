package com.acabra.fsands.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder(setterPrefix = "with")
@Getter
public class FixSpikeSystemStatusRoomDTO {
    private final long number;
    private final FixSpikeTokenInfoDTO hostToken;
    private final FixSpikeTokenInfoDTO guestToken;
    private final String expiresAfter;
    private final boolean expired;

    @JsonCreator
    public FixSpikeSystemStatusRoomDTO(@JsonProperty("number") long number,
                                    @JsonProperty("hostToken") FixSpikeTokenInfoDTO hostToken,
                                    @JsonProperty("guestToken") FixSpikeTokenInfoDTO guestToken,
                                    @JsonProperty("expiresAfter") String expiresAfter,
                                    @JsonProperty("expired") boolean expired) {
        this.number = number;
        this.hostToken = hostToken;
        this.guestToken = guestToken;
        this.expiresAfter = expiresAfter;
        this.expired = expired;
    }
}
