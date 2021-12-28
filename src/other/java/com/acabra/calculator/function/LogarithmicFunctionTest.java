package com.acabra.calculator.function;

import com.acabra.calculator.util.WebCalculatorConstants;

import static org.junit.Assert.assertEquals;

/**
 * Describe your class
 */
public class LogarithmicFunctionTest {
//TODO implement tests


    @Test
    public void solveTest() {
        LogarithmicFunction logarithmicFunction = new LogarithmicFunction(null);

        assertEquals(0.0, logarithmicFunction.apply(1.0), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(-1.6094379, logarithmicFunction.apply(0.2), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(1.0, logarithmicFunction.apply(Math.E), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test
    public void should_calculate_derivativeTest() {

        LogarithmicFunction logarithmicFunction = new LogarithmicFunction(null);
        assertEquals(1.0, logarithmicFunction.calculateDerivative(1.0d), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(0.5, logarithmicFunction.calculateDerivative(2.0d), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(0.1, logarithmicFunction.calculateDerivative(10.0d), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void should_fail_calculating_derivative_1_Test() {

        LogarithmicFunction logarithmicFunction = new LogarithmicFunction(null);
        logarithmicFunction.calculateDerivative(0.0d);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void should_fail_calculating_value_on_negative_values() {
        LogarithmicFunction logarithmicFunction = new LogarithmicFunction(null);
        logarithmicFunction.apply(-5.0d);
    }
}
