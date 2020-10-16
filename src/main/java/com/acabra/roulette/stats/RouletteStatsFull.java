package com.acabra.roulette.stats;

import com.acabra.roulette.RouletteColor;

public class RouletteStatsFull extends RouletteStats {

    public RouletteStatsFull() { }

    @Override
    public void accept(int integer, int color) {
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
}
