package com.acabra.fsands.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicLong;

@Getter
public class FixSpikeGameState {
    private static final AtomicLong GEN_ID = new AtomicLong(1);

    private final FixSpikeGameStatus gameStatus;
    private final String opponentName;
    private final Boolean isOwnMove;
    private final FixSpikeMoveResult lastMove;
    private final long sequenceId;


    @JsonCreator
    @Builder(setterPrefix = "with")
    public FixSpikeGameState(@JsonProperty("gameStatus") FixSpikeGameStatus gameStatus,
                          @JsonProperty("opponentName") String opponentName,
                          @JsonProperty("isOwnMove") Boolean isOwnMove,
                          @JsonProperty("lastMove") FixSpikeMoveResult lastMove) {
        this.gameStatus = gameStatus;
        this.opponentName = opponentName;
        this.isOwnMove = isOwnMove;
        this.lastMove = lastMove;
        this.sequenceId = GEN_ID.getAndIncrement();
    }
}
