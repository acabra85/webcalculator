package com.acabra.calculator.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Agustin on 9/28/2016.
 */
@JsonInclude
public class RenderedHistoryResponse extends SimpleResponse {

    private static final long serialVersionUID = -8032854785162616793L;
    private final String renderedTable;

    @JsonCreator
    public RenderedHistoryResponse(@JsonProperty("id") long id,
                                   @JsonProperty("renderedTable") String renderedTable) {
        super(id, false);
        this.renderedTable = renderedTable;
    }


    public String getRenderedTable() {
        return renderedTable;
    }
}
