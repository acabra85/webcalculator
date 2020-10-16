package com.acabra.roulette.stats;

import com.acabra.roulette.RouletteColor;

public class RouletteStatsWindow extends RouletteStats {

    private final int windowSize;

    public RouletteStatsWindow(int windowSize) {
        this.windowSize = windowSize;
    }

    @Override
    public void accept(int integer, int color) {
        if(counter < windowSize) {
            ++counter;
        }
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

    public void decreaseCountersByOne(int integer, int color) {
        if(integer == 0) {
            if(zero > 0) --zero;
        } else {
            if(integer % 2 == 0){
                if(even > 0 ) --even;
            } else{
                if(odd > 0 ) --odd;
            }
            if(integer > 18) {
                if(high > 0 ) --high;
            } else {
                if(low > 0 ) --low;
            }
            if (integer > 24) {
                if(third > 0 ) --third;
            } else if(integer > 12) {
                if(second > 0 ) --second;
            } else {
                if(first > 0 ) --first;
            }
            if (color == RouletteColor.RED.getId()) {
                if(red > 0 ) --red;
            }
            else {
                if(black > 0 ) --black;
            }
        }
    }

    public boolean isFull() {
        return counter >= this.windowSize;
    }
}
