package com.acabra.calculator.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author acabra
 * @created 2016-09-27
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleResponse implements Serializable {

    protected long id;

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    protected SimpleResponse(){}
}
