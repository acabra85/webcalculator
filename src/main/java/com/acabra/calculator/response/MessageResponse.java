package com.acabra.calculator.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author acabra
 * @created 2016-09-27
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class MessageResponse extends SimpleResponse {
    private String body;
    private String message;

    public MessageResponse(){}

    public MessageResponse(long id, final String message, String body) {
        this.id = id;
        this.message = message;
        this.body = body;
    }

    @JsonProperty("body")
    public String getBody() {
        return body;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
}
