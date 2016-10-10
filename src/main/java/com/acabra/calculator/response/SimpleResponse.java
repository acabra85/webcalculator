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

    public long getId() {
        return id;
    }

    @JsonCreator
    protected SimpleResponse(@JsonProperty("id") long id){
        this.id = id;
    }
}
