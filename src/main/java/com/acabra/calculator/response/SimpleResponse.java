package com.acabra.calculator.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author acabra
 * @created 2016-09-27
 */
@Getter
public abstract class SimpleResponse implements Serializable {

    protected final long id;
    protected final boolean failure;

    @JsonCreator
    protected SimpleResponse(@JsonProperty("id") long id, @JsonProperty("failure") boolean failure){
        this.id = id;
        this.failure = failure;
    }

}
