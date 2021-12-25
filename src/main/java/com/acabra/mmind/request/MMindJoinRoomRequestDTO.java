package com.acabra.mmind.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class MMindJoinRoomRequestDTO {
    @NonNull final String playerName;
    @NonNull final String password;
    @NonNull final String secret;
    @NonNull final long roomNumber;

    @JsonCreator
    public MMindJoinRoomRequestDTO(@JsonProperty("playerName") String playerName,
                                   @JsonProperty("password") String password,
                                   @JsonProperty("secret") String secret,
                                   @JsonProperty("roomNumber") long roomNumber) {
        this.playerName = playerName;
        this.password = password;
        this.secret = secret;
        this.roomNumber = roomNumber;
    }
}
