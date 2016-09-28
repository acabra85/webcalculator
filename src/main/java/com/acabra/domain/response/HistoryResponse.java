package com.acabra.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Agustin on 9/27/2016.
 */
public class HistoryResponse extends SimpleResponse {

    private final List<CalculationResponse> resultList;

    public HistoryResponse(long id, List<CalculationResponse> resultsList) {
        super(id);
        this.resultList = resultsList;
    }

    @JsonProperty("resultList")
    public List<CalculationResponse> getResultList() {
        return resultList;
    }
}
