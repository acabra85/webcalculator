package com.acabra.calculator.function;

import com.acabra.calculator.util.WebCalculatorConstants;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Describe your class
 */
public class InverseFunctionTest {
//TODO implement tests


    @Test
    public void should_calculate_derivativeTest() {
        InverseFunction inverseFunction = new InverseFunction(null);
        assertEquals(-1.0, inverseFunction.calculateDerivative(1.0d), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(-0.25, inverseFunction.calculateDerivative(2.0d), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void should_fail_calculating_derivative_1_Test() {
        InverseFunction inverseFunction = new InverseFunction(null);
        inverseFunction.calculateDerivative(0.0d);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void should_fail_calculating_derivative_negative_infinity() {
        InverseFunction inverseFunction = new InverseFunction(null);
        inverseFunction.calculateDerivative(Double.NEGATIVE_INFINITY);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void should_fail_calculating_derivative_positive_infinity() {
        InverseFunction inverseFunction = new InverseFunction(null);
        inverseFunction.calculateDerivative(Double.POSITIVE_INFINITY);
    }


    @Test(expected = UnsupportedOperationException.class)
    public void should_fail_calculating_derivative_NaN() {
        InverseFunction inverseFunction = new InverseFunction(null);
        inverseFunction.calculateDerivative(Double.NaN);
    }

    @Test
    public void solve1Test() {
        InverseFunction definiteIntegralInverse = new InverseFunction(null);

        assertEquals(1.0, definiteIntegralInverse.apply(1.0), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(5.0, definiteIntegralInverse.apply(0.2), WebCalculatorConstants.ACCURACY_EPSILON);
    }
}
