package com.acabra.roulette.request;

import com.acabra.calculator.request.SimpleRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class RouletteRequestDTO implements SimpleRequest {

    private String token;

    public RouletteRequestDTO() {
    }

    public RouletteRequestDTO(String token) {
        this.token = token;

    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }
}
