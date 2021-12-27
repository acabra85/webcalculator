package com.acabra.mmind.request;

import com.acabra.mmind.auth.MMindTokenInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class MMindJoinRoomRequestDTO {
    @NonNull final String playerName;
    @NonNull final String password;
    @NonNull final String secret;
    @NonNull final Long roomNumber;
    final String token;

    @JsonCreator
    public MMindJoinRoomRequestDTO(@JsonProperty("playerName") @NonNull String playerName,
                                   @JsonProperty("password") @NonNull String password,
                                   @JsonProperty("secret") @NonNull String secret,
                                   @JsonProperty("roomNumber") @NonNull Long roomNumber,
                                   @JsonProperty("token") String token) {
        this.playerName = playerName;
        this.password = password;
        this.secret = secret;
        this.roomNumber = roomNumber;
        this.token = token != null && token.trim().length() == MMindTokenInfo.TOKEN_LEN ? token : null;
    }
}
