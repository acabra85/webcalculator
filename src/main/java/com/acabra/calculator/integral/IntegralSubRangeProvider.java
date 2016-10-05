package com.acabra.calculator.integral;

import com.acabra.calculator.util.WebCalculatorConstants;
import com.google.common.util.concurrent.AtomicDouble;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Agustin on 9/29/2016.
 */
public class IntegralSubRangeProvider {

    private static final Logger logger = Logger.getLogger(IntegralSubRangeProvider.class);
    private AtomicDouble current;
    private final double rangeSize;
    private final int repeatedCalculations;
    private final double upperBound;
    private final double lowerBound;
    private final IntegralFunctionType functionType;
    private final boolean validConstruction;
    private final AtomicInteger provided = new AtomicInteger();

    /**
     *
     * @param lowerBound lower integral bound must be equal or lower than upperBound
     * @param upperBound upper integral bound must be greater or equal than lowerBound
     * @param repeatedCalculations amount of repeatedCalculations to obtain the integral must be > 0
     * @param functionType the type of the integral function to create
     */
    public IntegralSubRangeProvider(double lowerBound, double upperBound, int repeatedCalculations, IntegralFunctionType functionType) {
        this.functionType = functionType;
        this.repeatedCalculations = repeatedCalculations > 0 ? repeatedCalculations : 1;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.rangeSize = (this.upperBound - this.lowerBound) / (1.0 * this.repeatedCalculations);
        this.current = new AtomicDouble(lowerBound);
        this.validConstruction = this.lowerBound <= this.upperBound && this.repeatedCalculations > 0;
        logger.info("rangeSize -> " + rangeSize);
    }

    /**
     *
     * @return Validates with accuracy of 10^-6 if upperBound is exceeded by current value
     */
    boolean hasMoreSubRanges() {
        return Math.abs(upperBound - current.get()) > WebCalculatorConstants.ACCURACY_EPSILON;
    }

    public IntegrableFunction provideNextIntegral() {
        if (this.validConstruction && hasMoreSubRanges() && provided.get() < repeatedCalculations) {
            provided.incrementAndGet();
            if (provided.get() == repeatedCalculations) {
                logger.info(String.format("Ending upper bound -> %.3f total delivered: %d", current.get(), provided.get()));
            }
            return IntegralFunctionFactory.createIntegralFunction(functionType, current.get(), current.addAndGet(rangeSize), Optional.empty());
        }
        throw new NullPointerException("Unable to provide more subRanges");
    }
}
