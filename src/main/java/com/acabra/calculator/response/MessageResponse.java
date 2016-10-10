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
@JsonInclude(JsonInclude.Include.ALWAYS)
public class MessageResponse extends SimpleResponse {
    private String body;
    private String message;

    @JsonCreator
    public MessageResponse(@JsonProperty("id") long id,
                           @JsonProperty("message") final String message,
                           @JsonProperty("body")String body) {
        super(id);
        this.message = message;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String getMessage() {
        return message;
    }
}
