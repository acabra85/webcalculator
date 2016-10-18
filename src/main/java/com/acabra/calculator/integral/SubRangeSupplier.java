package com.acabra.calculator.integral;

import com.acabra.calculator.util.WebCalculatorConstants;
import com.google.common.util.concurrent.AtomicDouble;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Created by Agustin on 10/17/2016.
 */
public class SubRangeSupplier implements Supplier {

    private final double lowerBound;
    private final double upperBound;
    private final int n;
    private final double rangeSize;
    private final AtomicInteger supplied = new AtomicInteger(0);
    private final AtomicDouble current;
    private final boolean validConstruction;

    /**
     *
     * @param lowerBound lower integral bound must be equal or lower than upperLimit
     * @param upperBound upper integral bound must be greater or equal than lowerLimit
     * @param desiredSubRanges amount of desired subranges to obtain should be greater than zero so
     *                         the constructions is valid
     */
    public SubRangeSupplier(double lowerBound, double upperBound, int desiredSubRanges) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.validConstruction = lowerBound <= upperBound && desiredSubRanges > 0;
        this.n = Math.abs(desiredSubRanges);
        this.current = new AtomicDouble(this.lowerBound);
        this.rangeSize = (this.upperBound - this.lowerBound) / (1.0 * (this.n > 0 ? this.n : 1));
    }

    /**
     *
     * @return Validates with accuracy of 10^-6 if upperLimit is exceeded by current value
     */
    boolean hasMoreSubRanges() {
        return Math.abs(upperBound - current.get()) > WebCalculatorConstants.ACCURACY_EPSILON;
    }

    @Override
    public Interval get() {
        if (this.validConstruction && supplied.get() < n && hasMoreSubRanges()) {
            supplied.incrementAndGet();
            return new Interval(current.get(), current.addAndGet(rangeSize));
        }
        throw new NoSuchElementException("No more ranges to supply");
    }
}
