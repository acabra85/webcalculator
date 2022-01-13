package com.acabra.fsands.response;

import com.acabra.calculator.response.SimpleResponse;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class FixSpikeSystemStatusResponse extends SimpleResponse {

    private final List<FixSpikeSystemStatusRoomDTO> rooms;
    private final List<FixSpikeTokenInfoDTO> tokens;

    @Builder(setterPrefix = "with")
    @JsonCreator
    protected FixSpikeSystemStatusResponse(@JsonProperty("id")  long id, @JsonProperty("failure") boolean failure,
                                        @JsonProperty("rooms")  List<FixSpikeSystemStatusRoomDTO> rooms,
                                        @JsonProperty("rooms")  List<FixSpikeTokenInfoDTO> tokens) {
        super(id, failure);
        this.rooms = rooms;
        this.tokens = tokens;
    }
}
