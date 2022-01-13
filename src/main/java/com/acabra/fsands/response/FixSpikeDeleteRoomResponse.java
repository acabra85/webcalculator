package com.acabra.fsands.response;

import com.acabra.calculator.response.SimpleResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FixSpikeDeleteRoomResponse extends SimpleResponse {
    private final String message;
    @Builder(setterPrefix = "with")
    protected FixSpikeDeleteRoomResponse(@JsonProperty("id") long id, @JsonProperty("failure") boolean failure,
                                      @JsonProperty("message") String message) {
        super(id, failure);
        this.message = message;
    }
}
