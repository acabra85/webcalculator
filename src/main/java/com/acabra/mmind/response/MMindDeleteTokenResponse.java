package com.acabra.mmind.response;

import com.acabra.calculator.response.SimpleResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MMindDeleteTokenResponse extends SimpleResponse {

    @Builder(setterPrefix = "with")
    protected MMindDeleteTokenResponse(long id, boolean failure) {
        super(id, failure);
    }
}
