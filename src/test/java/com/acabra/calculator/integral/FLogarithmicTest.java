package com.acabra.calculator.integral;

import com.acabra.calculator.util.WebCalculatorConstants;
import org.junit.Test;

import java.util.InputMismatchException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 10/11/2016.
 */
public class FLogarithmicTest {

    @Test(expected = UnsupportedOperationException.class)
    public void instanceCreationFailTest() {
        double lowerBound = -0.5;
        double upperBound = 0.5;
        new FLogarithmic(lowerBound, upperBound, Optional.empty(), Optional.empty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void instanceCreationFail1Test() {
        double lowerBound = 0.0;
        double upperBound = 0.5;
        new FLogarithmic(lowerBound, upperBound, Optional.empty(), Optional.empty());
    }

    @Test(expected = InputMismatchException.class)
    public void instanceCreationFail2Test() {
        double lowerBound = 0.2;
        double upperBound = Double.POSITIVE_INFINITY;
        new FLogarithmic(lowerBound, upperBound, Optional.empty(), Optional.empty());
    }

    @Test
    public void solvingPolynomialIntegralTest() {
        double lowerBound = 1;
        double upperBound = Math.E;
        double expectedApproximationInscribed = 0.0;
        double expectedApproximationCircumscribed = 1.71828182;
        double integralResult = 1.0;

        FLogarithmic fLogarithmic = new FLogarithmic(lowerBound, upperBound, Optional.empty(), Optional.empty());

        assertEquals(expectedApproximationCircumscribed, fLogarithmic.calculateRiemannSequenceRectangleArea(false), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedApproximationInscribed, fLogarithmic.solveIntegralWithRiemannSequences(true), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedApproximationInscribed, fLogarithmic.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, fLogarithmic.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{ln(x)}[1, 2.718281828459045]", fLogarithmic.toString());
    }

    @Test
    public void solvingPolynomialSolvedTest() {
        double lowerBound = 1.0;
        double upperBound = Math.E;
        double expectedApproximationInscribed = 0.0;
        double expectedApproximationCircumscribed = 1.71828182;
        double integralResult = 1.0;

        FLogarithmic fLogarithmic = new FLogarithmic(lowerBound, upperBound, Optional.of(integralResult), Optional.of(expectedApproximationInscribed));

        assertEquals(integralResult, fLogarithmic.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedApproximationInscribed, fLogarithmic.solveIntegralWithRiemannSequences(true), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedApproximationCircumscribed, fLogarithmic.calculateRiemannSequenceRectangleArea(false), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedApproximationInscribed, fLogarithmic.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{ln(x)}[1, 2.718281828459045]", fLogarithmic.toString());
    }
}
