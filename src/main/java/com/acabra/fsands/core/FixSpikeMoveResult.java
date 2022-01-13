package com.acabra.fsands.core;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder(setterPrefix = "with")
@Getter
public class FixSpikeMoveResult {
    @NonNull
    Integer fixes;
    @NonNull
    Integer spikes;

    char[] guess;
    int index;
    String playerName;
    private long id;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
