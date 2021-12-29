package com.acabra.mmind.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder(setterPrefix = "with")
@Getter
public class MMindTokenInfoDTO {
    private final String token;
    private final boolean isAdmin;
    private final Long roomNumber;
    private final String expiresAfter;
    private final boolean expired;

    @JsonCreator
    public MMindTokenInfoDTO(@JsonProperty("token") String token,
                             @JsonProperty("isAdmin") boolean isAdmin,
                             @JsonProperty("roomNumber") long roomNumber,
                             @JsonProperty("expiresAfter") String expiresAfter,
                             @JsonProperty("expired") boolean expired) {
        this.token = token;
        this.isAdmin = isAdmin;
        this.roomNumber = roomNumber;
        this.expiresAfter = expiresAfter;
        this.expired = expired;
    }

    @JsonProperty("isAdmin")
    public boolean isAdmin() {
        return isAdmin;
    }
}
