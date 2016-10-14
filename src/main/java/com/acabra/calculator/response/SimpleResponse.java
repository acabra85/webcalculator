package com.acabra.calculator.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author acabra
 * @created 2016-09-27
 */
public abstract class SimpleResponse implements Serializable {

    protected final long id;
    protected final boolean failure;

    @JsonCreator
    protected SimpleResponse(@JsonProperty("id") long id, @JsonProperty("failure") boolean failure){
        this.id = id;
        this.failure = failure;
    }

    public long getId() {
        return id;
    }

    public boolean isFailure() {
        return failure;
    }
}
