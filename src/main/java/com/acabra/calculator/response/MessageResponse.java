package com.acabra.calculator.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author acabra
 * @created 2016-09-27
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude
public class MessageResponse<T> extends SimpleResponse {

    private static final long serialVersionUID = -6406713160086373312L;
    private T body;
    private String message;

    @JsonCreator
    public MessageResponse(@JsonProperty("id") long id,
                           @JsonProperty("failure") final boolean failure,
                           @JsonProperty("message") final String message,
                           @JsonProperty("body") T body) {
        super(id, failure);
        this.message = message;
        this.body = body;
    }

    public T getBody() {
        return body;
    }

    public String getMessage() {
        return message;
    }
}
