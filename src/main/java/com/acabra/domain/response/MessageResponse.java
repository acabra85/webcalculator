package com.acabra.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author acabra
 * @created 2016-09-27
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class MessageResponse extends SimpleResponse {
    private final SimpleResponse body;
    private final String message;


    public MessageResponse(long id, final String message, SimpleResponse body) {
        super(id);
        this.message = message;
        this.body = body;
    }

    @JsonProperty("body")
    public SimpleResponse getBody() {
        return body;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
}
