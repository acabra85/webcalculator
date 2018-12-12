package com.acabra.calculator.function;

import com.acabra.calculator.util.WebCalculatorConstants;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for class PolynomialFunction
 */
public class PolynomialFunctionTest {
//TODO implement tests

    @Test
    public void should_return_six() {
        double expected = 6.0d;
        RealFunction function = new PolynomialFunction(Arrays.asList(0.0d,1.0d,1.0d));
        assertEquals("expected 6.0 as result", expected, function.apply(2.0), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test
    public void should_calculate_derivative1Test() {
        List<Double> coefficients = Arrays.asList(1.0, 2.1);
        PolynomialFunction definiteIntegralPolynomial = new PolynomialFunction(coefficients);
        assertEquals(coefficients.get(1), definiteIntegralPolynomial.calculateDerivative(1.0d), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(coefficients.get(1), definiteIntegralPolynomial.calculateDerivative(2.0d), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(coefficients.get(1), definiteIntegralPolynomial.calculateDerivative(100.0d), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test
    public void should_calculate_derivative2Test() {
        List<Double> coefficients = Arrays.asList(5.0, 0.0, -3.0, 0.0, -6.2);
        PolynomialFunction definiteIntegralPolynomial = new PolynomialFunction(coefficients);
        assertEquals(-30.8, definiteIntegralPolynomial.calculateDerivative(1.0d), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(-210.4, definiteIntegralPolynomial.calculateDerivative(2.0d), WebCalculatorConstants.ACCURACY_EPSILON);
    }


    @Test
    public void solveZeroOrderTest() {
        PolynomialFunction definiteIntegralPolynomial = new PolynomialFunction(Collections.emptyList());
        assertEquals(0.0, definiteIntegralPolynomial.apply(0.0), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test
    public void evaluateTest() {
        double lowerLimit = 0.0;
        double upperLimit = 2.0;
        double evaluated = 3.0;

        List<Double> coeff = Arrays.asList(2.0, -1.0, 2.0);

        PolynomialFunction definiteIntegralPolynomial = new PolynomialFunction(coeff);

        assertEquals(evaluated, definiteIntegralPolynomial.apply(1.0), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(coeff.get(0), definiteIntegralPolynomial.apply(lowerLimit), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(8.0, definiteIntegralPolynomial.apply(upperLimit), WebCalculatorConstants.ACCURACY_EPSILON);
    }
}
