package com.acabra.calculator.integral;

import com.acabra.calculator.util.WebCalculatorConstants;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Agustin on 9/30/2016.
 */
public class PolynomialIntegralTest {

    @Test
    public void solveArea1Test() {
        int lowerbound = 1;
        int upperbound = 2;
        int order = 3;
        double integralResult = 10.0;
        double inscribedAreaExpected = 5.0;
        double circumscribedAreaExpected = 16.0;
        List<Double> coefficients = Arrays.asList(0.0, 2.0, 3.0);

        PolynomialIntegral polynomialIntegral = new PolynomialIntegral(lowerbound, upperbound, coefficients, Optional.empty(), Optional.empty());

        assertEquals(lowerbound, polynomialIntegral.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, polynomialIntegral.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, polynomialIntegral.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(inscribedAreaExpected, polynomialIntegral.calculateRiemannSequenceRectangleArea(true), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, polynomialIntegral.solveIntegralWithRiemannSequences(false), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, polynomialIntegral.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(order, polynomialIntegral.getOrder());
        assertEquals(polynomialIntegral.getOrder(), polynomialIntegral.getCoefficients().size());
        assertEquals("Integ{2x+3x^2}[1, 2]", polynomialIntegral.toString());
    }

    @Test
    public void solveArea2Test() {
        int lowerbound = 1;
        int upperbound = 2;
        int order = 2;
        List<Double> coefficients = Arrays.asList(0.0, 2.0);
        double integralResult = 3.0;
        double inscribedAreaExpected = 2.0;
        double circumscribedAreaExpected = 4.0;

        PolynomialIntegral polynomialIntegral = new PolynomialIntegral(lowerbound, upperbound, coefficients, Optional.empty(), Optional.empty());

        assertEquals(lowerbound, polynomialIntegral.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, polynomialIntegral.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, polynomialIntegral.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(inscribedAreaExpected, polynomialIntegral.calculateRiemannSequenceRectangleArea(true), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, polynomialIntegral.solveIntegralWithRiemannSequences(false), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, polynomialIntegral.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(order, polynomialIntegral.getOrder());
        assertEquals(polynomialIntegral.getOrder(), polynomialIntegral.getCoefficients().size());
        assertEquals("Integ{2x}[1, 2]", polynomialIntegral.toString());
    }
    @Test
    public void solveIntegral1Test() {
        int lowerbound = 1;
        int upperbound = 2;
        int order = 2;
        double integralResult = 3.0;
        double inscribedAreaExpected = 2.0;
        double circumscribedAreaExpected = 4.0;
        List<Double> coefficients = Arrays.asList(0.0, 2.0);

        PolynomialIntegral polynomialIntegral = new PolynomialIntegral(lowerbound, upperbound, coefficients, Optional.empty(), Optional.empty());

        assertEquals(lowerbound, polynomialIntegral.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, polynomialIntegral.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, polynomialIntegral.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(inscribedAreaExpected, polynomialIntegral.calculateRiemannSequenceRectangleArea(true), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, polynomialIntegral.solveIntegralWithRiemannSequences(false), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, polynomialIntegral.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(order, polynomialIntegral.getOrder());
        assertEquals(polynomialIntegral.getOrder(), polynomialIntegral.getCoefficients().size());
        assertEquals("Integ{2x}[1, 2]", polynomialIntegral.toString());
    }

    @Test
    public void solveIntegral2Test() {
        int lowerbound = -1;
        int upperbound = 0;
        int order = 2;
        double integralResult = -1.0;
        double inscribedAreaExpected = 0.0;
        double circumscribedAreaExpected = 2.0;
        List<Double> coefficients = Arrays.asList(0.0, 2.0);

        PolynomialIntegral polynomialIntegral = new PolynomialIntegral(lowerbound, upperbound, coefficients, Optional.empty(), Optional.empty());

        assertEquals(lowerbound, polynomialIntegral.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, polynomialIntegral.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, polynomialIntegral.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(inscribedAreaExpected, polynomialIntegral.calculateRiemannSequenceRectangleArea(true), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, polynomialIntegral.solveIntegralWithRiemannSequences(false), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, polynomialIntegral.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(order, polynomialIntegral.getOrder());
        assertEquals(polynomialIntegral.getOrder(), polynomialIntegral.getCoefficients().size());
        assertEquals("Integ{2x}[-1, 0]", polynomialIntegral.toString());
    }
}
