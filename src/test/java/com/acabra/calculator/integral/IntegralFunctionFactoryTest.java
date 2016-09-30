package com.acabra.calculator.integral;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 9/30/2016.
 */
public class IntegralFunctionFactoryTest {

    IntegralFunctionType exponential = IntegralFunctionType.EXPONENTIAL;

    @Test
    public void createIntegralFunctionNoResultTest() {
        double lowerbound = 0;
        double upperbound = 1;
        Optional<Double> result = Optional.empty();
        IntegrableFunction integralFunction = IntegralFunctionFactory.createIntegralFunction(exponential, lowerbound, upperbound, result);
        assertEquals(lowerbound, integralFunction.getLowerBound(), IntegralSubRangeProvider.accuracyEpsilon);
        assertEquals(upperbound, integralFunction.getUpperBound(), IntegralSubRangeProvider.accuracyEpsilon);
    }

    @Test
    public void createIntegralFunctionResultTest() {
        int lowerbound = 0;
        int upperbound = 1;
        double contentResult = 9.5;
        Optional<Double> result = Optional.of(contentResult);
        IntegrableFunction solvedIntegralFunction = IntegralFunctionFactory.createIntegralFunction(exponential, lowerbound, upperbound, result);
        assertEquals(lowerbound, solvedIntegralFunction.getLowerBound(), IntegralSubRangeProvider.accuracyEpsilon);
        assertEquals(upperbound, solvedIntegralFunction.getUpperBound(), IntegralSubRangeProvider.accuracyEpsilon);
        assertEquals(contentResult, solvedIntegralFunction.getResult(), IntegralSubRangeProvider.accuracyEpsilon);

    }

    @Test
    public void createPolynomialFunctionNonResultTest() {
        int lowerbound = -2;
        int upperbound = 2;
        int order = 3;
        boolean[] exponents = {false, false, true};
        double[] coefficients = {0, 0, 1};
        Optional<Double> result = Optional.empty();
        IntegrableFunction unsolvedIntegralFunction = IntegralFunctionFactory.createPolynomialIntegralFunction(lowerbound, upperbound, order, exponents, coefficients, result);
        assertEquals(lowerbound, unsolvedIntegralFunction.getLowerBound(), IntegralSubRangeProvider.accuracyEpsilon);
        assertEquals(upperbound, unsolvedIntegralFunction.getUpperBound(), IntegralSubRangeProvider.accuracyEpsilon);

    }

    @Test
    public void createPolynomialFunctionResultTest() {
        int lowerbound = -2;
        int upperbound = 2;
        int order = 3;
        boolean[] exponents = {false, false, true};
        double[] coefficients = {0, 0, 3};
        Optional<Double> result = Optional.of(16.0);
        IntegrableFunction unsolvedIntegralFunction = IntegralFunctionFactory.createPolynomialIntegralFunction(lowerbound, upperbound, order, exponents, coefficients, result);
        assertEquals(lowerbound, unsolvedIntegralFunction.getLowerBound(), IntegralSubRangeProvider.accuracyEpsilon);
        assertEquals(upperbound, unsolvedIntegralFunction.getUpperBound(), IntegralSubRangeProvider.accuracyEpsilon);
        assertEquals(result.get(), unsolvedIntegralFunction.getResult(), IntegralSubRangeProvider.accuracyEpsilon);

    }

    @Test(expected = NoSuchElementException.class)
    public void createFunctionFailureTest() {
        int lowerbound = 0;
        int upperbound = 1;
        double contentResult = 9.5;
        Optional<Double> result = Optional.of(contentResult);
        IntegralFunctionFactory.createIntegralFunction(IntegralFunctionType.LOGARITHMIC, lowerbound, upperbound, result);
    }

    @Test(expected = NoSuchElementException.class)
    public void evaluateFunctionTypeTest() {
        IntegralFunctionFactory.evaluateFunctionType(2);
    }
}
