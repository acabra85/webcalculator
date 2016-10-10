package com.acabra.calculator.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Agustin on 9/27/2016.
 */
public class HistoryResponse extends SimpleResponse {

    private List<CalculationResponse> resultList;

    public HistoryResponse() {}

    public HistoryResponse(long id, List<CalculationResponse> resultsList) {
        this.id = id;
        this.resultList = resultsList;
    }

    @JsonProperty("resultList")
    public List<CalculationResponse> getResultList() {
        return resultList;
    }
}
