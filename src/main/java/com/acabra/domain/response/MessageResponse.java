package com.acabra.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author acabra
 * @created 2016-09-27
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class MessageResponse extends SimpleResponse {

    private final long id;
    private final SimpleResponse body;

    public MessageResponse(long id, final String message, SimpleResponse body) {
        super(message);
        this.id = id;
        this.body = body;
    }

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    @JsonProperty("body")
    public SimpleResponse getBody() {
        return body;
    }
}
