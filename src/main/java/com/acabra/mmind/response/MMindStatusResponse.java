package com.acabra.mmind.response;

import com.acabra.calculator.response.SimpleResponse;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MMindStatusResponse extends SimpleResponse {
    private final boolean makeMove;
    private final boolean gameOver;
    private final MMindMoveResultDTO lastMove;
    private final String result;

    @Builder(setterPrefix = "with")
    @JsonCreator
    protected MMindStatusResponse(@JsonProperty("id") long id, @JsonProperty("failure") boolean failure,
                                  @JsonProperty("makeMove") boolean makeMove,
                                  @JsonProperty("gameOver") boolean gameOver,
                                  @JsonProperty("lastMove") MMindMoveResultDTO lastMove,
                                  @JsonProperty("result") String result) {
        super(id, failure);
        this.makeMove = makeMove;
        this.lastMove = lastMove;
        this.gameOver = gameOver;
        this.result = result;
    }
}
