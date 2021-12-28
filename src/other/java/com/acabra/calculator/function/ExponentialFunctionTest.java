package com.acabra.calculator.function;

import com.acabra.calculator.util.WebCalculatorConstants;

import static org.junit.Assert.assertEquals;

/**
 * Describe your class
 */
public class ExponentialFunctionTest {
//TODO implement tests

    @Test
    public void solveAreaInscribed1Test() {
        ExponentialFunction exponentialFunction = new ExponentialFunction();
        assertEquals(1.0, exponentialFunction.apply(0.0), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(54.59815003, exponentialFunction.apply(4.0), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(148.4131591, exponentialFunction.apply(5.0), WebCalculatorConstants.ACCURACY_EPSILON);
    }



    @Test
    public void should_get_derivative_and_calculateResultTest() {
        ExponentialFunction exponentialFunction = new ExponentialFunction();
        double domainPoint = 2.0;
        double derivativeValue = exponentialFunction.calculateDerivative(domainPoint);
        assertEquals(derivativeValue, exponentialFunction.apply(domainPoint), WebCalculatorConstants.ACCURACY_EPSILON);
    }
}
