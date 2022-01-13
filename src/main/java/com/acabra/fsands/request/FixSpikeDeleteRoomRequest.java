package com.acabra.fsands.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class FixSpikeDeleteRoomRequest {
    private final String token;
    private final long roomNumber;

    @JsonCreator
    public FixSpikeDeleteRoomRequest(@JsonProperty("token") @NonNull String token,
                                  @JsonProperty("roomNumber") long roomNumber) {
        this.token = token;
        this.roomNumber = roomNumber;
    }
}
