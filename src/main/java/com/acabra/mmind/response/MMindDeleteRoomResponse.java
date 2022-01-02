package com.acabra.mmind.response;

import com.acabra.calculator.response.SimpleResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MMindDeleteRoomResponse extends SimpleResponse {
    private final String message;
    @Builder(setterPrefix = "with")
    protected MMindDeleteRoomResponse(@JsonProperty("id") long id, @JsonProperty("failure") boolean failure,
                                      @JsonProperty("message") String message) {
        super(id, failure);
        this.message = message;
    }
}
