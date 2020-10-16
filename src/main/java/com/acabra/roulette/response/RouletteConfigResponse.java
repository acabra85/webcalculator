package com.acabra.roulette.response;

import com.acabra.calculator.response.SimpleResponse;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RouletteConfigResponse extends SimpleResponse {

    private final int[] colorsMap;
    private final String token;

    @JsonCreator
    public RouletteConfigResponse(long id, boolean failed,
                                  @JsonProperty("token") String token,
                                  @JsonProperty("colorsMap") int[] colorsMap) {
        super(id, failed);
        this.token = token;
        this.colorsMap = colorsMap.clone();
    }

    public int[] getColorsMap() {
        return colorsMap.clone();
    }

    public String getToken() {
        return token;
    }
}
