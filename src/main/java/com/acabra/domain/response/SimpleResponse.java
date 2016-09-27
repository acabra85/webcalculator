package com.acabra.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author acabra
 * @created 2016-09-27
 */
public abstract class SimpleResponse implements Serializable {

    protected final String message;

    public SimpleResponse(String message) {
        this.message = message;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
}
