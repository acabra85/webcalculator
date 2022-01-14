package com.acabra.fsands.response;

import com.acabra.calculator.response.SimpleResponse;
import com.acabra.fsands.core.FixSpikeGameStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FixSpikeStatusResponseDTO extends SimpleResponse {
    private static final long serialVersionUID = -5809423247343074385L;

    private final FixSpikeGameStatus gameStatus;
    private final Boolean isOwnMove;
    private final FixSpikeMoveResultDTO lastMove;
    private final String opponentName;
    private final long sequenceId;

    @Builder(setterPrefix = "with")
    @JsonCreator
    protected FixSpikeStatusResponseDTO(@JsonProperty("id") long id, @JsonProperty("failure") boolean failure, FixSpikeGameStatus gameStatus, Boolean isOwnMove, FixSpikeMoveResultDTO lastMove, String opponentName, long sequenceId) {
        super(id, failure);
        this.gameStatus = gameStatus;
        this.isOwnMove = isOwnMove;
        this.lastMove = lastMove;
        this.opponentName = opponentName;
        this.sequenceId = sequenceId;
    }
}
