package com.acabra.calculator.integral;

import com.acabra.calculator.util.WebCalculatorConstants;
import org.junit.Test;

import java.util.InputMismatchException;
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
        assertEquals(lowerbound, integralFunction.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, integralFunction.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test
    public void createIntegralFunctionResultTest() {
        int lowerbound = 0;
        int upperbound = 1;
        double contentResult = 9.5;
        Optional<Double> result = Optional.of(contentResult);
        IntegrableFunction solvedIntegralFunction = IntegralFunctionFactory.createIntegralFunction(exponential, lowerbound, upperbound, result);
        assertEquals(lowerbound, solvedIntegralFunction.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, solvedIntegralFunction.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(contentResult, solvedIntegralFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);

    }

    @Test
    public void createPolynomialFunctionNonResultTest() {
        int lowerbound = -2;
        int upperbound = 2;
        int order = 3;
        double[] coefficients = {0, 0, 1};
        Optional<Double> result = Optional.empty();
        IntegrableFunction unsolvedIntegralFunction = IntegralFunctionFactory.createPolynomialIntegralFunction(lowerbound, upperbound, order, coefficients, result);
        assertEquals(lowerbound, unsolvedIntegralFunction.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, unsolvedIntegralFunction.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);

    }

    @Test
    public void createPolynomialFunctionResultTest() {
        int lowerbound = -2;
        int upperbound = 2;
        int order = 3;
        double[] coefficients = {0, 0, 3};
        Optional<Double> result = Optional.of(16.0);
        IntegrableFunction unsolvedIntegralFunction = IntegralFunctionFactory.createPolynomialIntegralFunction(lowerbound, upperbound, order, coefficients, result);
        assertEquals(lowerbound, unsolvedIntegralFunction.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, unsolvedIntegralFunction.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(result.get(), unsolvedIntegralFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);

    }

    @Test(expected = NoSuchElementException.class)
    public void createFunctionFailure1Test() {
        int lowerbound = 0;
        int upperbound = 1;
        double contentResult = 9.5;
        Optional<Double> result = Optional.of(contentResult);
        IntegralFunctionFactory.createIntegralFunction(IntegralFunctionType.LOGARITHMIC, lowerbound, upperbound, result);
    }

    @Test(expected = InputMismatchException.class)
    public void createFunctionFailure2Test() {
        int lowerbound = 1;
        int upperbound = 0;
        double contentResult = 9.5;
        Optional<Double> result = Optional.of(contentResult);
        IntegralFunctionFactory.createIntegralFunction(IntegralFunctionType.LOGARITHMIC, lowerbound, upperbound, result);
    }

    @Test(expected = NoSuchElementException.class)
    public void evaluateFunctionTypeTest() {
        IntegralFunctionFactory.evaluateFunctionType(2);
    }
}
