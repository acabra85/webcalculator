package com.acabra.calculator.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Agustin on 9/27/2016.
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class HistoryResponse extends SimpleResponse {

    private final List<CalculationResponse> resultList;

    @JsonCreator
    public HistoryResponse(@JsonProperty("id") long id,
                           @JsonProperty("resultList") List<CalculationResponse> resultsList) {
        super(id, false);
        this.resultList = resultsList;
    }

    public List<CalculationResponse> getResultList() {
        return resultList;
    }
}
