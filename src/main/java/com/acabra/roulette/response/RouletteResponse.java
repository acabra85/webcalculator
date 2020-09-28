package com.acabra.roulette.response;

import com.acabra.calculator.response.SimpleResponse;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude
public class RouletteResponse extends SimpleResponse {

    private static final long serialVersionUID = -6689095279599722534L;
    private final List<Integer> hotNumbers;
    private final List<Integer> coldNumbers;
    private final List<Integer> history;

    @JsonCreator
    public RouletteResponse(@JsonProperty("id") long id,
                            @JsonProperty("failure") boolean failure,
                            @JsonProperty("hotNumbers") List<Integer> hotNumbers,
                            @JsonProperty("coldNumbers") List<Integer> coldNumbers,
                            @JsonProperty("history") List<Integer> history) {
        super(id, failure);
        this.hotNumbers = hotNumbers;
        this.coldNumbers = coldNumbers;
        this.history = history;
    }

    public List<Integer> getHotNumbers() {
        return hotNumbers;
    }

    public List<Integer> getColdNumbers() {
        return coldNumbers;
    }

    public List<Integer> getHistory() {
        return history;
    }
}
