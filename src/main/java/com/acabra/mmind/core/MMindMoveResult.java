package com.acabra.mmind.core;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

@Builder(setterPrefix = "with")
@Getter
public class MMindMoveResult {
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
