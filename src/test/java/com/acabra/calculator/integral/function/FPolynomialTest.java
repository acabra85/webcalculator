package com.acabra.calculator.integral.function;

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
public class FPolynomialTest {
    private static final List<Double> EMPTY_LIST = Collections.emptyList();

    @Test
    public void solveArea1Test() {
        int lowerbound = 1;
        int upperbound = 2;
        int order = 3;
        double integralResult = 10.0;
        List<Double> coefficients = Arrays.asList(0.0, 2.0, 3.0);

        FPolynomial fPolynomial = new FPolynomial(lowerbound, upperbound, coefficients, Optional.empty(), Optional.empty());

        assertEquals(lowerbound, fPolynomial.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, fPolynomial.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, fPolynomial.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertNull(fPolynomial.getApproximation());
        assertEquals(order, fPolynomial.getOrder());
        assertEquals(fPolynomial.getOrder(), fPolynomial.getCoefficients().size());
        assertEquals("Integ{2x+3x^2}[1, 2]", fPolynomial.toString());
    }

    @Test
    public void solveArea2Test() {
        int lowerbound = 1;
        int upperbound = 2;
        int order = 2;
        List<Double> coefficients = Arrays.asList(0.0, 2.0);
        double integralResult = 3.0;

        FPolynomial fPolynomial = new FPolynomial(lowerbound, upperbound, coefficients, Optional.empty(), Optional.empty());

        assertEquals(lowerbound, fPolynomial.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, fPolynomial.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, fPolynomial.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertNull(fPolynomial.getApproximation());
        assertEquals(order, fPolynomial.getOrder());
        assertEquals(fPolynomial.getOrder(), fPolynomial.getCoefficients().size());
        assertEquals("Integ{2x}[1, 2]", fPolynomial.toString());
    }
    @Test
    public void solveIntegral1Test() {
        int lowerbound = 1;
        int upperbound = 2;
        int order = 2;
        double integralResult = 3.0;
        List<Double> coefficients = Arrays.asList(0.0, 2.0);

        FPolynomial fPolynomial = new FPolynomial(lowerbound, upperbound, coefficients, Optional.empty(), Optional.empty());

        assertEquals(lowerbound, fPolynomial.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, fPolynomial.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, fPolynomial.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertNull(fPolynomial.getApproximation());
        assertEquals(order, fPolynomial.getOrder());
        assertEquals(fPolynomial.getOrder(), fPolynomial.getCoefficients().size());
        assertEquals("Integ{2x}[1, 2]", fPolynomial.toString());
    }

    @Test
    public void solveIntegral2Test() {
        int lowerbound = -1;
        int upperbound = 0;
        int order = 2;
        double integralResult = -1.0;
        List<Double> coefficients = Arrays.asList(0.0, 2.0);

        FPolynomial fPolynomial = new FPolynomial(lowerbound, upperbound, coefficients, Optional.empty(), Optional.empty());

        assertEquals(lowerbound, fPolynomial.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, fPolynomial.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, fPolynomial.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertNull(fPolynomial.getApproximation());
        assertEquals(order, fPolynomial.getOrder());
        assertEquals(fPolynomial.getOrder(), fPolynomial.getCoefficients().size());
        assertEquals("Integ{2x}[-1, 0]", fPolynomial.toString());
    }

    @Test @Ignore
    public void solveIntegral3Test() { //TODO test ignored since area implied covers one root for the function
        int lowerbound = -2;
        int upperbound = 1;
        int order = 2;
        double integralResult = -3.0;
        List<Double> coefficients = Arrays.asList(0.0, 2.0);

        FPolynomial fPolynomialFullRange = new FPolynomial(lowerbound, upperbound, coefficients, Optional.empty(), Optional.empty());

        double expectedAreaCircumscribedRange2 = 2.0;

        double circumscribedAreaExpected = expectedAreaCircumscribedRange2 + expectedAreaCircumscribedRange2;

        assertEquals(lowerbound, fPolynomialFullRange.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, fPolynomialFullRange.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, fPolynomialFullRange.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, fPolynomialFullRange.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(order, fPolynomialFullRange.getOrder());
        assertEquals(fPolynomialFullRange.getOrder(), fPolynomialFullRange.getCoefficients().size());
        assertEquals("Integ{2x}[-2, 1]", fPolynomialFullRange.toString());
    }

    @Test(expected = InputMismatchException.class)
    public void instanceCreationFailTest() {
        double lowerBound = 0;
        double upperBound = Double.POSITIVE_INFINITY;
        new FPolynomial(lowerBound, upperBound, EMPTY_LIST, Optional.empty(), Optional.empty());
    }

    @Test(expected = InputMismatchException.class)
    public void instanceCreationFail2Test() {
        double lowerBound = Double.POSITIVE_INFINITY;
        double upperBound = 0;
        new FPolynomial(lowerBound, upperBound, EMPTY_LIST, Optional.empty(), Optional.empty());
    }

    @Test
    public void solveZeroOrderTest() {
        double lowerLimit = 0.0;
        double upperLimit = 1.0;
        double expectedIntegral = 0.0;

        FPolynomial fPolynomial = new FPolynomial(lowerLimit, upperLimit, EMPTY_LIST, Optional.empty(), Optional.empty());

        Assertions.assertThat(fPolynomial.getApproximation()).isNull();
        assertEquals(lowerLimit, fPolynomial.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperLimit, fPolynomial.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedIntegral, fPolynomial.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(0.0, fPolynomial.evaluate(0.0), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{0}[0, 1]", fPolynomial.toString());
    }

    @Test
    public void evaluateTest() {
        double lowerLimit = 0.0;
        double upperLimit = 2.0;
        double expectedIntegral = 7.3333333;
        double evaluated = 3.0;

        List<Double> coeff = Arrays.asList(2.0, -1.0, 2.0);

        FPolynomial fPolynomial = new FPolynomial(lowerLimit, upperLimit, coeff, Optional.empty(), Optional.empty());

        Assertions.assertThat(fPolynomial.getApproximation()).isNull();
        assertEquals(lowerLimit, fPolynomial.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperLimit, fPolynomial.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedIntegral, fPolynomial.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(evaluated, fPolynomial.evaluate(1.0), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(coeff.get(0), fPolynomial.evaluate(lowerLimit), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(8.0, fPolynomial.evaluate(upperLimit), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{2-x+2x^2}[0, 2]", fPolynomial.toString());
    }
}
