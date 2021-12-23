package com.acabra.roulette.stats;

import com.acabra.roulette.RouletteColor;
import com.acabra.roulette.response.RouletteStatsDTO;

public class RouletteStats {
    private final Integer size;

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

    public RouletteStats() {
        this.size = null;
    }

    public RouletteStats(int size) {
        this.size = size;
    }

    public void accept(int integer, int color) {
        if (null==size || counter < size) ++counter;
        if(integer == 0) {
            ++zero;
        } else {
            if(integer % 2 == 0) {
                ++even;
            } else {
                ++odd;
            }
            if(integer > 18) {
                ++high;
            } else {
                ++low;
            }
            if (integer > 24) {
                ++third;
            } else if(integer > 12) {
                ++second;
            } else {
                ++first;
            }
            if (color == RouletteColor.RED.getId()) {
                ++red;
            } else {
                ++black;
            }
        }
    }


    public void removeLastNumber(int integer, int color) {
        if(integer == 0) {
            if(zero > 0) {
                --zero;
            }
        } else {
            if(integer % 2 == 0){
                if(even > 0) {
                    --even;
                }
            } else{
                if(odd > 0) {
                    --odd;
                }
            }
            if(integer > 18) {
                if(high > 0) {
                    --high;
                }
            } else {
                if(low > 0) {
                    --low;
                }
            }
            if (integer > 24) {
                if(third > 0) {
                    --third;
                }
            } else if(integer > 12) {
                if(second > 0) {
                    --second;
                }
            } else {
                if(first > 0) {
                    --first;
                }
            }
            if (color == RouletteColor.RED.getId()) {
                if(red > 0 ) {
                    --red;
                }
            } else {
                if(black > 0) {
                    --black;
                }
            }
        }
    }

    public RouletteStatsDTO toDto() {
        return new RouletteStatsDTO(even, odd, red, black, zero, low, high, first, second, third, counter);
    }

}
