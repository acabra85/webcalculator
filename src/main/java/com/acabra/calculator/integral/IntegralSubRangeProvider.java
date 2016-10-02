package com.acabra.calculator.integral;

import com.acabra.calculator.util.WebCalculatorConstants;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Agustin on 9/29/2016.
 */
public class IntegralSubRangeProvider {

    private static final Logger logger = Logger.getLogger(IntegralSubRangeProvider.class);
    /**
     *
     * @param lowerBound lower integral bound
     * @param upperBound upper integral bound
     * @param repeatedCalculations amount of repeatedCalculations to obtain the integral
     * @param functionType the type of the integral function to create
     * @return List of IntegrableFunctions containing sub ranges according to the repeatedCalculations parameters
     */
    public static List<IntegrableFunction> provideIntegralsOnSubRanges(double lowerBound, double upperBound, int repeatedCalculations, IntegralFunctionType functionType) {
        double rangeSize = (upperBound - lowerBound) / (1.0 * repeatedCalculations);
        logger.info("rangeSize -> " + rangeSize);
        double current = lowerBound;
        List<IntegrableFunction> ranges = new ArrayList<>();
        while (isGoodEnough(upperBound, current)) {
            ranges.add(IntegralFunctionFactory.createIntegralFunction(functionType, current, current + rangeSize, Optional.empty()));
            current = ranges.get(ranges.size() - 1).getUpperBound();
        }
        logger.info(String.format("ending upper bound -> %.3f size: %d", current, ranges.size()));
        return ranges;
    }

    /**
     *
     * @param upperBound the upper limit
     * @param current the current value
     * @return Validates with accuracy of 10^-4 if upperBound is exceeded by current value
     */
    private static boolean isGoodEnough(double upperBound, double current) {
        return Math.abs(upperBound - current) > WebCalculatorConstants.ACCURACY_EPSILON;
    }
}
