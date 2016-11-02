package com.acabra.calculator.integral.definiteintegral;

import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralPolynomial;
import com.acabra.calculator.util.WebCalculatorConstants;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Agustin on 9/30/2016.
 */
public class DefiniteIntegralPolynomialTest {
    private static final List<Double> EMPTY_LIST = Collections.emptyList();

    @Test
    public void solveArea1Test() {
        int lowerbound = 1;
        int upperbound = 2;
        int order = 3;
        double integralResult = 10.0;
        List<Double> coefficients = Arrays.asList(0.0, 2.0, 3.0);

        DefiniteIntegralPolynomial definiteIntegralPolynomial = new DefiniteIntegralPolynomial(lowerbound, upperbound, coefficients, Optional.empty(), Optional.empty());

        assertEquals(lowerbound, definiteIntegralPolynomial.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, definiteIntegralPolynomial.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, definiteIntegralPolynomial.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertNull(definiteIntegralPolynomial.getApproximation());
        assertEquals(order, definiteIntegralPolynomial.getOrder());
        assertEquals(definiteIntegralPolynomial.getOrder(), definiteIntegralPolynomial.getCoefficients().size());
        assertEquals("Integ{2x+3x^2}[1, 2]", definiteIntegralPolynomial.toString());
    }

    @Test
    public void solveArea2Test() {
        int lowerbound = 1;
        int upperbound = 2;
        int order = 2;
        List<Double> coefficients = Arrays.asList(0.0, 2.0);
        double integralResult = 3.0;

        DefiniteIntegralPolynomial definiteIntegralPolynomial = new DefiniteIntegralPolynomial(lowerbound, upperbound, coefficients, Optional.empty(), Optional.empty());

        assertEquals(lowerbound, definiteIntegralPolynomial.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, definiteIntegralPolynomial.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, definiteIntegralPolynomial.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertNull(definiteIntegralPolynomial.getApproximation());
        assertEquals(order, definiteIntegralPolynomial.getOrder());
        assertEquals(definiteIntegralPolynomial.getOrder(), definiteIntegralPolynomial.getCoefficients().size());
        assertEquals("Integ{2x}[1, 2]", definiteIntegralPolynomial.toString());
    }
    @Test
    public void solveIntegral1Test() {
        int lowerbound = 1;
        int upperbound = 2;
        int order = 2;
        double integralResult = 3.0;
        List<Double> coefficients = Arrays.asList(0.0, 2.0);

        DefiniteIntegralPolynomial definiteIntegralPolynomial = new DefiniteIntegralPolynomial(lowerbound, upperbound, coefficients, Optional.empty(), Optional.empty());

        assertEquals(lowerbound, definiteIntegralPolynomial.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, definiteIntegralPolynomial.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, definiteIntegralPolynomial.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertNull(definiteIntegralPolynomial.getApproximation());
        assertEquals(order, definiteIntegralPolynomial.getOrder());
        assertEquals(definiteIntegralPolynomial.getOrder(), definiteIntegralPolynomial.getCoefficients().size());
        assertEquals("Integ{2x}[1, 2]", definiteIntegralPolynomial.toString());
    }

    @Test
    public void solveIntegral2Test() {
        int lowerbound = -1;
        int upperbound = 0;
        int order = 2;
        double integralResult = -1.0;
        List<Double> coefficients = Arrays.asList(0.0, 2.0);

        DefiniteIntegralPolynomial definiteIntegralPolynomial = new DefiniteIntegralPolynomial(lowerbound, upperbound, coefficients, Optional.empty(), Optional.empty());

        assertEquals(lowerbound, definiteIntegralPolynomial.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, definiteIntegralPolynomial.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, definiteIntegralPolynomial.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertNull(definiteIntegralPolynomial.getApproximation());
        assertEquals(order, definiteIntegralPolynomial.getOrder());
        assertEquals(definiteIntegralPolynomial.getOrder(), definiteIntegralPolynomial.getCoefficients().size());
        assertEquals("Integ{2x}[-1, 0]", definiteIntegralPolynomial.toString());
    }

    @Test @Ignore
    public void solveIntegral3Test() { //TODO test ignored since area implied covers one root for the function
        int lowerbound = -2;
        int upperbound = 1;
        int order = 2;
        double integralResult = -3.0;
        List<Double> coefficients = Arrays.asList(0.0, 2.0);

        DefiniteIntegralPolynomial definiteIntegralPolynomialFullRange = new DefiniteIntegralPolynomial(lowerbound, upperbound, coefficients, Optional.empty(), Optional.empty());

        double expectedAreaCircumscribedRange2 = 2.0;

        double circumscribedAreaExpected = expectedAreaCircumscribedRange2 + expectedAreaCircumscribedRange2;

        assertEquals(lowerbound, definiteIntegralPolynomialFullRange.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, definiteIntegralPolynomialFullRange.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, definiteIntegralPolynomialFullRange.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, definiteIntegralPolynomialFullRange.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(order, definiteIntegralPolynomialFullRange.getOrder());
        assertEquals(definiteIntegralPolynomialFullRange.getOrder(), definiteIntegralPolynomialFullRange.getCoefficients().size());
        assertEquals("Integ{2x}[-2, 1]", definiteIntegralPolynomialFullRange.toString());
    }

    @Test(expected = InputMismatchException.class)
    public void instanceCreationFailTest() {
        double lowerBound = 0;
        double upperBound = Double.POSITIVE_INFINITY;
        new DefiniteIntegralPolynomial(lowerBound, upperBound, EMPTY_LIST, Optional.empty(), Optional.empty());
    }

    @Test(expected = InputMismatchException.class)
    public void instanceCreationFail2Test() {
        double lowerBound = Double.POSITIVE_INFINITY;
        double upperBound = 0;
        new DefiniteIntegralPolynomial(lowerBound, upperBound, EMPTY_LIST, Optional.empty(), Optional.empty());
    }

    @Test
    public void solveZeroOrderTest() {
        double lowerLimit = 0.0;
        double upperLimit = 1.0;
        double expectedIntegral = 0.0;

        DefiniteIntegralPolynomial definiteIntegralPolynomial = new DefiniteIntegralPolynomial(lowerLimit, upperLimit, EMPTY_LIST, Optional.empty(), Optional.empty());

        Assertions.assertThat(definiteIntegralPolynomial.getApproximation()).isNull();
        assertEquals(lowerLimit, definiteIntegralPolynomial.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperLimit, definiteIntegralPolynomial.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedIntegral, definiteIntegralPolynomial.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(0.0, definiteIntegralPolynomial.evaluate(0.0), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{0}[0, 1]", definiteIntegralPolynomial.toString());
    }

    @Test
    public void evaluateTest() {
        double lowerLimit = 0.0;
        double upperLimit = 2.0;
        double expectedIntegral = 7.3333333;
        double evaluated = 3.0;

        List<Double> coeff = Arrays.asList(2.0, -1.0, 2.0);

        DefiniteIntegralPolynomial definiteIntegralPolynomial = new DefiniteIntegralPolynomial(lowerLimit, upperLimit, coeff, Optional.empty(), Optional.empty());

        Assertions.assertThat(definiteIntegralPolynomial.getApproximation()).isNull();
        assertEquals(lowerLimit, definiteIntegralPolynomial.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperLimit, definiteIntegralPolynomial.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedIntegral, definiteIntegralPolynomial.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(evaluated, definiteIntegralPolynomial.evaluate(1.0), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(coeff.get(0), definiteIntegralPolynomial.evaluate(lowerLimit), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(8.0, definiteIntegralPolynomial.evaluate(upperLimit), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{2-x+2x^2}[0, 2]", definiteIntegralPolynomial.toString());
    }

    @Test
    public void should_calculate_derivative1Test() {
        List<Double> coefficients = Arrays.asList(1.0, 2.1);
        DefiniteIntegralPolynomial definiteIntegralPolynomial = new DefiniteIntegralPolynomial(0.00001, Double.MAX_VALUE, coefficients, Optional.empty(), Optional.empty());
        assertEquals(coefficients.get(1), definiteIntegralPolynomial.calculateDerivative(1), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(coefficients.get(1), definiteIntegralPolynomial.calculateDerivative(2), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(coefficients.get(1), definiteIntegralPolynomial.calculateDerivative(100), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test
    public void should_calculate_derivative2Test() {
        List<Double> coefficients = Arrays.asList(5.0, 0.0, -3.0, 0.0, -6.2);
        DefiniteIntegralPolynomial definiteIntegralPolynomial = new DefiniteIntegralPolynomial(0.00001, Double.MAX_VALUE, coefficients, Optional.empty(), Optional.empty());
        assertEquals(-30.8, definiteIntegralPolynomial.calculateDerivative(1), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(-210.4, definiteIntegralPolynomial.calculateDerivative(2), WebCalculatorConstants.ACCURACY_EPSILON);
    }
}
