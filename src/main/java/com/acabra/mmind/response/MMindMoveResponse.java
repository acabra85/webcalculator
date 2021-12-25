package com.acabra.mmind.response;

import com.acabra.calculator.response.SimpleResponse;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MMindMoveResponse extends SimpleResponse {
    private static final long serialVersionUID = 5993028867477999789L;
    MMindMoveResultDTO moveResult;

    @JsonCreator
    public MMindMoveResponse(@JsonProperty("id")long id, @JsonProperty("failure")boolean failure,
                             MMindMoveResultDTO resultDTO) {
        super(id, failure);
        this.moveResult = resultDTO;
    }
}
