package com.acabra.mmind.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class MMindDeleteRoomRequest {
    private final String token;
    private final long roomNumber;

    @JsonCreator
    public MMindDeleteRoomRequest(@JsonProperty("token") @NonNull String token,
                                  @JsonProperty("roomNumber") long roomNumber) {
        this.token = token;
        this.roomNumber = roomNumber;
    }
}
