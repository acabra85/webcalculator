package com.acabra.roulette;

import com.acabra.roulette.response.RouletteStatsDTO;

public class RouletteStats {
    private long even = 0;
    private long odd = 0;
    private long red = 0;
    private long black = 0;
    private long zero = 0;
    private long low = 0;
    private long high = 0;
    private long counter = 0;
    private long first = 0;
    private long second = 0;
    private long third = 0;

    RouletteStats() { }

    public void accept(Integer integer, Integer color) {
        ++counter;
        if(integer == 0) ++zero;
        else {
            if(integer % 2 == 0) ++even;
            else ++odd;
            if(integer > 18) ++high;
            else ++low;
            if (integer > 24) ++third;
            else if(integer > 12) ++second;
            else ++first;
            if (color == RouletteColor.RED.getId()) ++red;
            else ++black;
        }
    }

    public RouletteStatsDTO toDto() {
        return new RouletteStatsDTO(even, odd, red, black, zero, low, high, first, second, third, counter);
    }
}
