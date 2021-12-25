package com.acabra.mmind.core;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

@Builder(setterPrefix = "with")
@Getter
public class MMmindMoveResult {
    @NonNull
    Integer fixes;
    @NonNull
    Integer spikes;
    @NonNull
    char[] guess;

    int index;
    String playerName;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}
