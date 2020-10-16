package com.acabra.roulette.response;

import com.acabra.calculator.response.SimpleResponse;
import com.acabra.roulette.stats.RouletteStats;
import com.acabra.roulette.stats.RouletteStatsFull;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude
public class RouletteResponse extends SimpleResponse {

    private static final long serialVersionUID = -6689095279599722534L;
    private final List<Integer> hotNumbers;
    private final List<Integer> coldNumbers;
    private final int number;
    private final RouletteStatsDTO stats;
    private final RouletteStatsDTO statsWindow;

    @JsonCreator
    public RouletteResponse(@JsonProperty("id") long id,
                            @JsonProperty("failure") boolean failure,
                            @JsonProperty("hotNumbers") List<Integer> hotNumbers,
                            @JsonProperty("coldNumbers") List<Integer> coldNumbers,
                            @JsonProperty("number") int number,
                            @JsonProperty("stats") RouletteStatsDTO stats,
                            @JsonProperty("statsWindow") RouletteStatsDTO statsWindow
                            ) {
        super(id, failure);
        this.hotNumbers = hotNumbers;
        this.coldNumbers = coldNumbers;
        this.number = number;
        this.stats = stats;
        this.statsWindow = statsWindow;
    }

    public List<Integer> getHotNumbers() {
        return hotNumbers;
    }

    public List<Integer> getColdNumbers() {
        return coldNumbers;
    }

    public int getNumber() {
        return number;
    }

    public RouletteStatsDTO getStats() {
        return stats;
    }

    public RouletteStatsDTO getStatsWindow() {
        return statsWindow;
    }
}
