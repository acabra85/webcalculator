package com.acabra.calculator.integral;

import com.acabra.calculator.util.WebCalculatorConstants;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 9/30/2016.
 */
public class FPolynomialTest {

    @Test
    public void solveArea1Test() {
        int lowerbound = 1;
        int upperbound = 2;
        int order = 3;
        double integralResult = 10.0;
        double inscribedAreaExpected = 5.0;
        double circumscribedAreaExpected = 16.0;
        List<Double> coefficients = Arrays.asList(0.0, 2.0, 3.0);

        FPolynomial fPolynomial = new FPolynomial(lowerbound, upperbound, coefficients, Optional.empty(), Optional.empty());

        assertEquals(lowerbound, fPolynomial.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, fPolynomial.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, fPolynomial.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(inscribedAreaExpected, fPolynomial.calculateRiemannSequenceRectangleArea(true), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, fPolynomial.solveIntegralWithRiemannSequences(false), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, fPolynomial.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
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
        double inscribedAreaExpected = 2.0;
        double circumscribedAreaExpected = 4.0;

        FPolynomial fPolynomial = new FPolynomial(lowerbound, upperbound, coefficients, Optional.empty(), Optional.empty());

        assertEquals(lowerbound, fPolynomial.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, fPolynomial.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, fPolynomial.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(inscribedAreaExpected, fPolynomial.calculateRiemannSequenceRectangleArea(true), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, fPolynomial.solveIntegralWithRiemannSequences(false), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, fPolynomial.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
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
        double inscribedAreaExpected = 2.0;
        double circumscribedAreaExpected = 4.0;
        List<Double> coefficients = Arrays.asList(0.0, 2.0);

        FPolynomial fPolynomial = new FPolynomial(lowerbound, upperbound, coefficients, Optional.empty(), Optional.empty());

        assertEquals(lowerbound, fPolynomial.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, fPolynomial.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, fPolynomial.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(inscribedAreaExpected, fPolynomial.calculateRiemannSequenceRectangleArea(true), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, fPolynomial.solveIntegralWithRiemannSequences(false), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, fPolynomial.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
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
        double inscribedAreaExpected = 0.0;
        double circumscribedAreaExpected = -2.0;
        List<Double> coefficients = Arrays.asList(0.0, 2.0);

        FPolynomial fPolynomial = new FPolynomial(lowerbound, upperbound, coefficients, Optional.empty(), Optional.empty());

        assertEquals(lowerbound, fPolynomial.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, fPolynomial.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, fPolynomial.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(inscribedAreaExpected, fPolynomial.calculateRiemannSequenceRectangleArea(true), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, fPolynomial.solveIntegralWithRiemannSequences(false), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, fPolynomial.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
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

        FPolynomial fPolynomialPartRange1 = new FPolynomial(lowerbound, 0, coefficients, Optional.empty(), Optional.empty());
        FPolynomial fPolynomialPartRange2 = new FPolynomial(0, upperbound, coefficients, Optional.empty(), Optional.empty());
        double expectedAreaInscribedRange1 = 0.0;
        double expectedAreaInscribedRange2 = 0.0;
        double expectedAreaCircumscribedRange1 = -8.0;
        double expectedAreaCircumscribedRange2 = 2.0;

        assertEquals(expectedAreaInscribedRange1, fPolynomialPartRange1.calculateRiemannSequenceRectangleArea(true), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedAreaInscribedRange2, fPolynomialPartRange2.calculateRiemannSequenceRectangleArea(true), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedAreaCircumscribedRange1, fPolynomialPartRange1.calculateRiemannSequenceRectangleArea(false), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedAreaCircumscribedRange2, fPolynomialPartRange2.calculateRiemannSequenceRectangleArea(false), WebCalculatorConstants.ACCURACY_EPSILON);

        double inscribedAreaExpected = expectedAreaInscribedRange2 + expectedAreaInscribedRange1;
        double circumscribedAreaExpected = expectedAreaCircumscribedRange2 + expectedAreaCircumscribedRange2;

        assertEquals(lowerbound, fPolynomialFullRange.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, fPolynomialFullRange.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, fPolynomialFullRange.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(inscribedAreaExpected, fPolynomialFullRange.calculateRiemannSequenceRectangleArea(true), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, fPolynomialFullRange.solveIntegralWithRiemannSequences(false), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, fPolynomialFullRange.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(order, fPolynomialFullRange.getOrder());
        assertEquals(fPolynomialFullRange.getOrder(), fPolynomialFullRange.getCoefficients().size());
        assertEquals("Integ{2x}[-2, 1]", fPolynomialFullRange.toString());
    }

    @Test(expected = InputMismatchException.class)
    public void instanceCreationFailTest() {
        double lowerBound = 0;
        double upperBound = Double.POSITIVE_INFINITY;
        new FPolynomial(lowerBound, upperBound, Collections.emptyList(), Optional.empty(), Optional.empty());
    }

    @Test(expected = InputMismatchException.class)
    public void instanceCreationFail2Test() {
        double lowerBound = Double.POSITIVE_INFINITY;
        double upperBound = 0;
        new FPolynomial(lowerBound, upperBound, Collections.emptyList(), Optional.empty(), Optional.empty());
    }
}
