package com.acabra.fsands.request;

import com.acabra.fsands.auth.FixSpikeTokenInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class FixSpikeJoinRoomRequestDTO {
    @NonNull final String playerName;
    @NonNull final String password;
    @NonNull final String secret;
    @NonNull final Long roomNumber;
    final String token;

    @JsonCreator
    public FixSpikeJoinRoomRequestDTO(@JsonProperty("playerName") @NonNull String playerName,
                                   @JsonProperty("password") @NonNull String password,
                                   @JsonProperty("secret") @NonNull String secret,
                                   @JsonProperty("roomNumber") @NonNull Long roomNumber,
                                   @JsonProperty("token") String token) {
        this.playerName = playerName.length() < 10 ? playerName : playerName.substring(0, 10).trim();
        this.password = password;
        this.secret = secret;
        this.roomNumber = roomNumber;
        this.token = token != null && token.trim().length() == FixSpikeTokenInfo.TOKEN_LEN ? token : null;
    }
}
