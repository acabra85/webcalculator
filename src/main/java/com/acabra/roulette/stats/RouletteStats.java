package com.acabra.roulette.stats;

import com.acabra.roulette.response.RouletteStatsDTO;

    public abstract class RouletteStats {
    protected long even = 0;
    protected long odd = 0;
    protected long red = 0;
    protected long black = 0;
    protected long zero = 0;
    protected long low = 0;
    protected long high = 0;
    protected long counter = 0;
    protected long first = 0;
    protected long second = 0;
    protected long third = 0;

    public abstract void accept(int number, int color);

    public RouletteStatsDTO toDto() {
        return new RouletteStatsDTO(even, odd, red, black, zero, low, high, first, second, third, counter);
    }
}
