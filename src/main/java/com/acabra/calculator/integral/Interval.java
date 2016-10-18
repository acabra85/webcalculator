package com.acabra.calculator.integral;

import java.util.InputMismatchException;

/**
 * Created by Agustin on 10/11/2016.
 */
public class Interval {

    private final boolean lowerLimitClosed;
    private final boolean upperLimitClosed;
    private final double lowerLimit;
    private final double upperLimit;

    public Interval(boolean lowerLimitClosed, double lowerLimit, boolean upperLimitClosed, double upperLimit) {
        this.lowerLimitClosed = lowerLimitClosed;
        this.lowerLimit = lowerLimit;
        this.upperLimitClosed = upperLimitClosed;
        this.upperLimit = upperLimit;
        validateInterval();
    }

    public Interval(double lowerLimit, double upperLimit) {
        lowerLimitClosed = true;
        upperLimitClosed = true;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        validateInterval();
    }

    private void validateInterval() {
        if (lowerLimit == Double.NEGATIVE_INFINITY && lowerLimitClosed
                || upperLimit == Double.POSITIVE_INFINITY && upperLimitClosed
                || upperLimit == Double.NEGATIVE_INFINITY
                || lowerLimit == Double.POSITIVE_INFINITY) {
            throw new InputMismatchException("wrong use of infinity values");
        }
        if(Double.isNaN(lowerLimit) || Double.isNaN(upperLimit)) {
            throw new InputMismatchException("range with NaN value");
        }
    }

    public double getLowerLimit() {
        return lowerLimit;
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    private boolean isLowerBoundContained(Interval range) {
        if (range.lowerLimit != lowerLimit) {
            return range.lowerLimit > lowerLimit;
        }
        return lowerLimitClosed || !range.lowerLimitClosed;
    }

    private boolean isUpperBoundContained(Interval range) {
        if (range.upperLimit != upperLimit) {
            return range.upperLimit < upperLimit;
        }
        return upperLimitClosed || !range.upperLimitClosed;
    }

    public boolean belongs(double val) {
        boolean lower = false;
        if (lowerLimitClosed && lowerLimit <= val || !lowerLimitClosed && lowerLimit < val) {
            lower = true;
        }
        return lower && (upperLimitClosed && upperLimit >= val || !upperLimitClosed && upperLimit > val);
    }

    public boolean containsInterval(Interval range) {
        return isLowerBoundContained(range) && isUpperBoundContained(range);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Interval)) return false;

        Interval that = (Interval) o;

        if (lowerLimitClosed != that.lowerLimitClosed) return false;
        if (upperLimitClosed != that.upperLimitClosed) return false;
        if (Double.compare(that.lowerLimit, lowerLimit) != 0) return false;
        return Double.compare(that.upperLimit, upperLimit) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (lowerLimitClosed ? 1 : 0);
        result = 31 * result + (upperLimitClosed ? 1 : 0);
        temp = Double.doubleToLongBits(lowerLimit);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(upperLimit);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
