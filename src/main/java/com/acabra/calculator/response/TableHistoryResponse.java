package com.acabra.calculator.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Agustin on 9/28/2016.
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class TableHistoryResponse extends SimpleResponse {

    protected String tableHTML;

    @JsonCreator
    public TableHistoryResponse(@JsonProperty("id") long id,
                                @JsonProperty("tableHTML") String tableHTML) {
        super(id);
        this.tableHTML = tableHTML;
    }


    public String getTableHTML() {
        return tableHTML;
    }
}
