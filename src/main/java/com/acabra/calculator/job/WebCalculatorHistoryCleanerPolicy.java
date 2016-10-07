package com.acabra.calculator.job;

import java.time.temporal.ChronoUnit;

/**
 * Created by Agustin on 10/7/2016.
 */
public class WebCalculatorHistoryCleanerPolicy {

    private final ChronoUnit unit;
    private final int expirationInterval;

    public WebCalculatorHistoryCleanerPolicy(ChronoUnit unit, int expirationInterval) {
        this.unit = unit;
        this.expirationInterval = expirationInterval;
    }

    public ChronoUnit getUnit() {
        return unit;
    }

    public int getExpirationInterval() {
        return expirationInterval;
    }
}
