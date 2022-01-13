package com.acabra.fsands.response;

import com.acabra.calculator.response.SimpleResponse;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FixSpikeMoveResponse extends SimpleResponse {
    private static final long serialVersionUID = 5993028867477999789L;
    private final boolean gameOver;
    FixSpikeMoveResultDTO moveResult;

    @JsonCreator
    public FixSpikeMoveResponse(@JsonProperty("id") long id, @JsonProperty("failure") boolean failure,
                             @JsonProperty("gameOver") boolean gameOver,
                             @JsonProperty("moveResult") FixSpikeMoveResultDTO resultDTO) {
        super(id, failure);
        this.moveResult = resultDTO;
        this.gameOver = gameOver;
    }

    public static FixSpikeMoveResponse ok(long id, boolean isGameOver, FixSpikeMoveResultDTO dto) {
        return new FixSpikeMoveResponse(id, false, isGameOver, dto);
    }

    public static FixSpikeMoveResponse fail(long id) {
        return new FixSpikeMoveResponse(id, true, true, null);
    }
}
