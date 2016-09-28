package com.acabra.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author acabra
 * @created 2016-09-27
 */
public class SimpleResponse implements Serializable {

    protected final long id;

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    protected SimpleResponse(long id) {
        this.id = id;
    }
}
