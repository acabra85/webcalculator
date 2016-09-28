package com.acabra.view;

import com.acabra.domain.response.SimpleResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Agustin on 9/28/2016.
 */
public class TableHistoryResponse extends SimpleResponse {

    protected String tableHTML;

    public TableHistoryResponse(long id, String tableHTML) {
        super(id);
        this.tableHTML = tableHTML;
    }

    @JsonProperty("tableHTML")
    public String getTableHTML() {
        return tableHTML;
    }
}
