package com.acabra.roulette.response;

import com.acabra.calculator.response.SimpleResponse;
import com.acabra.calculator.util.RouletteConfigResponseMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class RouletteConfigResponse extends SimpleResponse {

    private final String configMap;
    private final String token;

    @JsonCreator
    public RouletteConfigResponse(long id, boolean failed, @JsonProperty("token") String token, Map<Integer, Integer> configMap) {
        super(id, failed);
        this.token = token;
        this.configMap = RouletteConfigResponseMapper.toJsonMap(configMap);
    }

    @JsonProperty("configMap")
    public String getConfigMap() {
        return configMap;
    }

    public String getToken() {
        return token;
    }
}
