package com.acabra.calculator.integral;

import com.acabra.calculator.util.WebCalculatorConstants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 9/30/2016.
 */
public class PolynomialIntegralTest {

    @Test
    public void solveIntegral1Test() {
        int lowerbound = 1;
        int upperbound = 2;
        int order = 2;
        double[] coefficients = {0, 2};
        PolynomialIntegral polynomialIntegral = new PolynomialIntegral(lowerbound, upperbound, order, coefficients);
        double expected = 3.0;
        assertEquals(expected, polynomialIntegral.solve(), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test
    public void solveIntegral2Test() {
        int lowerbound = -1;
        int upperbound = 1;
        int order = 2;
        double[] coefficients = {0, 2};
        PolynomialIntegral polynomialIntegral = new PolynomialIntegral(lowerbound, upperbound, order, coefficients);
        double expected = 0.0;
        assertEquals(expected, polynomialIntegral.solve(), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test
    public void solveAreaInscribed1Test() {
        int lowerbound = 1;
        int upperbound = 2;
        int order = 2;
        double[] coefficients = {0, 2};
        double expectedResult = 2.0;
        PolynomialIntegral polynomialIntegral = new PolynomialIntegral(lowerbound, upperbound, order, coefficients);
        assertEquals(expectedResult, polynomialIntegral.calculateRiemannSequenceRectangleArea(true), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test
    public void solveAreaCircumscribed1Test() {
        int lowerbound = 1;
        int upperbound = 2;
        int order = 2;
        double[] coefficients = {0, 2};
        double expectedResult = 4.0;
        PolynomialIntegral polynomialIntegral = new PolynomialIntegral(lowerbound, upperbound, order, coefficients);
        assertEquals(expectedResult, polynomialIntegral.calculateRiemannSequenceRectangleArea(false), WebCalculatorConstants.ACCURACY_EPSILON);
    }
}
