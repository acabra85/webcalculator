package com.acabra.roulette.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RouletteStatsDTO {
    private final String even;
    private final String odd;
    private final String red;
    private final String black;
    private final String zero;
    private final String low;
    private final String high;
    private final String first;
    private final String second;
    private final String third;

    @JsonCreator
    public RouletteStatsDTO(long even, long odd, long red, long black, long zero, long low, long high,
                            long first, long second, long third, long counter) {
        this.even = format(100.0 * even / counter);
        this.odd = format(100.0 * odd / counter);
        this.red = format(100.0 * red / counter);
        this.black = format(100.0 * black / counter);
        this.zero = format(100.0 * zero / counter);
        this.low = format(100.0 * low / counter);
        this.high = format(100.0 * high / counter);
        this.first = format(100.0 * first / counter);
        this.second = format(100.0 * second / counter);
        this.third = format(100.0 * third / counter);
    }

    private static String format(double result) {
        return String.format("%.1f", result) + "%";
    }

    @JsonProperty("even")
    public String getEven() {
        return even;
    }

    @JsonProperty("odd")
    public String getOdd() {
        return odd;
    }

    @JsonProperty("red")
    public String getRed() {
        return red;
    }

    @JsonProperty("black")
    public String getBlack() {
        return black;
    }

    @JsonProperty("zero")
    public String getZero() {
        return zero;
    }

    @JsonProperty("low")
    public String getLow() {
        return low;
    }

    @JsonProperty("high")
    public String getHigh() {
        return high;
    }

    @JsonProperty("first")
    public String getFirst() {
        return first;
    }

    @JsonProperty("second")
    public String getSecond() {
        return second;
    }

    @JsonProperty("third")
    public String getThird() {
        return third;
    }

}
