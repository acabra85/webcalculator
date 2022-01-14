package com.acabra.fsands.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder(setterPrefix = "with")
@Getter
@AllArgsConstructor
public class FixSpikeMoveResult {
    public static final char[] EMPTY_GUESS = new char[0];
    @NonNull
    Integer fixes;
    @NonNull
    Integer spikes;

    char[] guess;
    int index;
    String playerName;
    private long id;
}
