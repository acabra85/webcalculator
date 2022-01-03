package com.acabra.mmind.response;

import com.acabra.calculator.response.SimpleResponse;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MMindStatusResponse extends SimpleResponse {
    private static final long serialVersionUID = -5809423247343074385L;
    private final MMindMoveResultDTO lastMove;
    private final Long result;
    private final String opponentName;
    private final String opponentSecret;
    private final Boolean isOwnMove;
    private final String eventType;

    @Builder(setterPrefix = "with")
    @JsonCreator
    protected MMindStatusResponse(@JsonProperty("id") long id, @JsonProperty("failure") boolean failure,
                                  @JsonProperty("eventType") String eventType,
                                  @JsonProperty("lastMove") MMindMoveResultDTO lastMove,
                                  @JsonProperty("result") Long result,
                                  @JsonProperty("opponentName") String opponentName,
                                  @JsonProperty("opponentSecret") String opponentSecret,
                                  @JsonProperty("isOwnMove") Boolean isOwnMove) {
        super(id, failure);
        this.eventType = eventType;
        this.lastMove = lastMove;
        this.result = result;
        this.opponentName = opponentName;
        this.isOwnMove = isOwnMove;
        this.opponentSecret = opponentSecret;
    }
}
