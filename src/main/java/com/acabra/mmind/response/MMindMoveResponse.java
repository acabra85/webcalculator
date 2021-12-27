package com.acabra.mmind.response;

import com.acabra.calculator.response.SimpleResponse;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MMindMoveResponse extends SimpleResponse {
    private static final long serialVersionUID = 5993028867477999789L;
    private final boolean gameOver;
    MMindMoveResultDTO moveResult;

    @JsonCreator
    public MMindMoveResponse(@JsonProperty("id") long id, @JsonProperty("failure") boolean failure,
                             @JsonProperty("gameOver") boolean gameOver,
                             @JsonProperty("moveResult") MMindMoveResultDTO resultDTO) {
        super(id, failure);
        this.moveResult = resultDTO;
        this.gameOver = gameOver;
    }

    public static MMindMoveResponse ok(long id, boolean isGameOver, MMindMoveResultDTO dto) {
        return new MMindMoveResponse(id, false, isGameOver, dto);
    }

    public static MMindMoveResponse fail(long id) {
        return new MMindMoveResponse(id, true, true, null);
    }
}
