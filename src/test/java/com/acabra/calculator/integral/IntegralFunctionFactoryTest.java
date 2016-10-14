package com.acabra.calculator.integral;

import com.acabra.calculator.integral.input.IntegrableFunctionInputParametersBuilder;
import com.acabra.calculator.integral.input.IntegrableFunctionInputParameters;
import com.acabra.calculator.util.WebCalculatorConstants;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Agustin on 9/30/2016.
 */
public class IntegralFunctionFactoryTest {

    @Test
    public void createIntegralFunctionResultAndApproximationTest() {
        int lowerbound = 0;
        int upperbound = 1;
        double contentResult = 9.5;
        double approximation = 1.0;
        IntegralFunctionType exponential = IntegralFunctionFactory.evaluateFunctionType(0);
        assertNotNull(exponential);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerBound(lowerbound)
                .withUpperBound(upperbound)
                .withIntegrationResult(contentResult)
                .withApproximation(approximation)
                .build();
        IntegrableFunction solvedIntegralFunction = IntegralFunctionFactory.createIntegralFunction(exponential, parameters);
        assertNotNull(solvedIntegralFunction);
        assertEquals(lowerbound, solvedIntegralFunction.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, solvedIntegralFunction.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(contentResult, solvedIntegralFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(approximation, solvedIntegralFunction.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[0, 1]", solvedIntegralFunction.toString());

        IntegrableFunction unsolvedFunction = IntegralFunctionFactory.createIntegralFunction(exponential,
                new IntegrableFunctionInputParametersBuilder()
                        .withLowerBound(lowerbound)
                        .withUpperBound(upperbound)
                        .build());
        assertNotNull(unsolvedFunction);
        assertNotEquals(solvedIntegralFunction.getResult(), unsolvedFunction.getResult());

    }

    @Test
    public void createIntegralFunctionResultTest() {
        int lowerbound = 0;
        int upperbound = 1;
        double contentResult = 9.5;
        IntegralFunctionType exponential = IntegralFunctionFactory.evaluateFunctionType(0);
        assertNotNull(exponential);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerBound(lowerbound)
                .withUpperBound(upperbound)
                .withIntegrationResult(contentResult)
                .build();
        IntegrableFunction solvedIntegralFunction = IntegralFunctionFactory.createIntegralFunction(exponential, parameters);
        assertNotNull(solvedIntegralFunction);
        assertEquals(lowerbound, solvedIntegralFunction.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, solvedIntegralFunction.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(contentResult, solvedIntegralFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[0, 1]", solvedIntegralFunction.toString());
    }

    @Test
    public void createIntegralFunctionApproximationTest() {
        int lowerbound = 1;
        int upperbound = 2;
        double approximation = 5.5;
        IntegralFunctionType exponential = IntegralFunctionFactory.evaluateFunctionType(0);
        assertNotNull(exponential);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerBound(lowerbound)
                .withUpperBound(upperbound)
                .withApproximation(approximation)
                .build();
        IntegrableFunction solvedIntegralFunction = IntegralFunctionFactory.createIntegralFunction(exponential, parameters);
        assertNotNull(solvedIntegralFunction);
        assertEquals(lowerbound, solvedIntegralFunction.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, solvedIntegralFunction.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(approximation, solvedIntegralFunction.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[1, 2]", solvedIntegralFunction.toString());
    }

    @Test
    public void createIntegralFunctionNoResultNoApproximationTest() {
        double lowerbound = 0;
        double upperbound = 1;
        IntegralFunctionType exponential = IntegralFunctionFactory.evaluateFunctionType(0);
        assertNotNull(exponential);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerBound(lowerbound)
                .withUpperBound(upperbound)
                .build();
        IntegrableFunction integralFunction = IntegralFunctionFactory.createIntegralFunction(exponential, parameters);
        assertNotNull(integralFunction);
        assertEquals(lowerbound, integralFunction.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, integralFunction.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[0, 1]", integralFunction.toString());
    }

    @Test
    public void createIntegralFunctionInvalidTypeTest() {
        int expectedExceptions = 4;
        int foundExceptions = 0;
        int inverseFunctionId = 3;
        IntegralFunctionType inverse = IntegralFunctionFactory.evaluateFunctionType(inverseFunctionId);
        assertNotNull(inverse);

        try {
            IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                    .withLowerBound(0)
                    .withUpperBound(1)
                    .build();
            IntegralFunctionFactory.createIntegralFunction(inverse, parameters);
        } catch (NoSuchElementException nse) {
            foundExceptions++;
        }

        try {
            IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                    .withLowerBound(0)
                    .withUpperBound(1)
                    .withIntegrationResult(1.0)
                    .build();
            IntegralFunctionFactory.createIntegralFunction(inverse, parameters);
        } catch (NoSuchElementException nse) {
            foundExceptions++;
        }

        try {
            IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                    .withLowerBound(0)
                    .withUpperBound(1)
                    .withApproximation(1.0)
                    .build();
            IntegralFunctionFactory.createIntegralFunction(inverse, parameters);
        } catch (NoSuchElementException nse) {
            foundExceptions++;
        }

        try {
            IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                    .withLowerBound(0)
                    .withUpperBound(1)
                    .withIntegrationResult(1.0)
                    .withApproximation(1.0)
                    .build();
            IntegralFunctionFactory.createIntegralFunction(inverse, parameters);
        } catch (NoSuchElementException nse) {
            foundExceptions++;
        }

        assertEquals(expectedExceptions, foundExceptions);

    }

    @Test
    public void createPolynomialFunctionNonResultTest() {
        int lowerbound = -3;
        int upperbound = -1;
        List<Double> coefficients = Arrays.asList(0.0, 0.0, 1.0);
        Double integralResult = 8.666666666;
        double inscribedAreaExpected = 2.0;
        double circumscribedAreaExpected = 18.0;
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerBound(lowerbound)
                .withUpperBound(upperbound)
                .withCoefficients(coefficients)
                .build();

        FPolynomial unsolvedIntegralFunction = (FPolynomial)IntegralFunctionFactory.createIntegralFunction(IntegralFunctionType.POLYNOMIAL, parameters);

        assertNotNull(unsolvedIntegralFunction);
        assertEquals(lowerbound, unsolvedIntegralFunction.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, unsolvedIntegralFunction.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, unsolvedIntegralFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(inscribedAreaExpected, unsolvedIntegralFunction.calculateRiemannSequenceRectangleArea(true), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, unsolvedIntegralFunction.solveIntegralWithRiemannSequences(false), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, unsolvedIntegralFunction.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(unsolvedIntegralFunction.getOrder(), unsolvedIntegralFunction.getCoefficients().size());
        assertEquals("Integ{x^2}[-3, -1]", unsolvedIntegralFunction.toString());
    }

    @Test
    public void createPolynomialFunctionResultTest() {
        int lowerbound = 1;
        int upperbound = 3;
        Double integralResult = 26.0;
        double inscribedAreaExpected = 6.0;
        double circumscribedAreaExpected = 54.0;
        List<Double> coefficients = Arrays.asList(0.0, 0.0, 3.0);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerBound(lowerbound)
                .withUpperBound(upperbound)
                .withCoefficients(coefficients)
                .build();

        FPolynomial unsolvedIntegralFunction = (FPolynomial)IntegralFunctionFactory.createIntegralFunction(IntegralFunctionType.POLYNOMIAL, parameters);

        assertNotNull(unsolvedIntegralFunction);
        assertEquals(lowerbound, unsolvedIntegralFunction.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, unsolvedIntegralFunction.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, unsolvedIntegralFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(inscribedAreaExpected, unsolvedIntegralFunction.calculateRiemannSequenceRectangleArea(true), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, unsolvedIntegralFunction.solveIntegralWithRiemannSequences(false), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, unsolvedIntegralFunction.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(unsolvedIntegralFunction.getOrder(), unsolvedIntegralFunction.getCoefficients().size());
        assertEquals("Integ{3x^2}[1, 3]", unsolvedIntegralFunction.toString());

    }

    @Test
    public void createPolynomialFunctionResult2Test() {
        int lowerbound = 2;
        int upperbound = 5;
        Double integralResult = 109.5;
        double inscribedAreaExpected = 33.0;
        double circumscribedAreaExpected = 213.0;
        List<Double> coefficients = Arrays.asList(1.0, -1.0, 3.0);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerBound(lowerbound)
                .withUpperBound(upperbound)
                .withCoefficients(coefficients)
                .build();

        FPolynomial unsolvedIntegralFunction = (FPolynomial)IntegralFunctionFactory.createIntegralFunction(IntegralFunctionType.POLYNOMIAL, parameters);

        assertNotNull(unsolvedIntegralFunction);
        assertEquals(lowerbound, unsolvedIntegralFunction.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, unsolvedIntegralFunction.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, unsolvedIntegralFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(inscribedAreaExpected, unsolvedIntegralFunction.calculateRiemannSequenceRectangleArea(true), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, unsolvedIntegralFunction.solveIntegralWithRiemannSequences(false), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, unsolvedIntegralFunction.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(unsolvedIntegralFunction.getOrder(), unsolvedIntegralFunction.getCoefficients().size());
        assertEquals("Integ{1-x+3x^2}[2, 5]", unsolvedIntegralFunction.toString());

    }

    @Test
    public void createPolynomialFunctionResult3Test() {
        int lowerbound = 3;
        int upperbound = 7;
        Double integralResult = 4.0;
        double inscribedAreaExpected = 100.0;
        double circumscribedAreaExpected = 564.0;
        List<Double> coefficients = Arrays.asList(1.0, -1.0, 3.0);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerBound(lowerbound)
                .withUpperBound(upperbound)
                .withApproximation(inscribedAreaExpected)
                .withIntegrationResult(integralResult)
                .withCoefficients(coefficients)
                .build();

        FPolynomial unsolvedIntegralFunction = (FPolynomial)IntegralFunctionFactory.createIntegralFunction(IntegralFunctionType.POLYNOMIAL, parameters);

        assertNotNull(unsolvedIntegralFunction);
        assertEquals(lowerbound, unsolvedIntegralFunction.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, unsolvedIntegralFunction.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, unsolvedIntegralFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, unsolvedIntegralFunction.calculateRiemannSequenceRectangleArea(false), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(inscribedAreaExpected, unsolvedIntegralFunction.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(unsolvedIntegralFunction.getOrder(), unsolvedIntegralFunction.getCoefficients().size());
        assertEquals("Integ{1-x+3x^2}[3, 7]", unsolvedIntegralFunction.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createPolynomialFunctionFailResultTest() {
        int lowerbound = 2;
        Double upperbound = Double.POSITIVE_INFINITY;
        IntegralFunctionType polynomial = IntegralFunctionFactory.evaluateFunctionType(1);
        assertNotNull(polynomial);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerBound(lowerbound)
                .withUpperBound(upperbound)
                .build();

        IntegralFunctionFactory.createIntegralFunction(polynomial, parameters);

    }

    @Test(expected = UnsupportedOperationException.class)
    public void createExponentialFunctionFailResultTest() {
        int lowerbound = 1;
        Double upperbound = Double.POSITIVE_INFINITY;
        IntegralFunctionType exponential = IntegralFunctionFactory.evaluateFunctionType(0);
        assertNotNull(exponential);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerBound(lowerbound)
                .withUpperBound(upperbound).build();
        IntegralFunctionFactory.createIntegralFunction(exponential, parameters);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createLogarithmicFunctionFailResultTest() {
        int lowerbound = 1;
        Double upperbound = Double.POSITIVE_INFINITY;
        IntegralFunctionType logarithmic = IntegralFunctionFactory.evaluateFunctionType(2);
        assertNotNull(logarithmic);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerBound(lowerbound)
                .withUpperBound(upperbound).build();
        IntegralFunctionFactory.createIntegralFunction(logarithmic, parameters);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createLogarithmicFunctionFailResult2Test() {
        int lowerbound = -1;
        int upperbound = 1;
        double contentResult = 9.5;
        int functionId = 2;
        IntegralFunctionType logarithmic = IntegralFunctionFactory.evaluateFunctionType(functionId);
        assertNotNull(logarithmic);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerBound(lowerbound)
                .withUpperBound(upperbound)
                .withIntegrationResult(contentResult).build();
        IntegralFunctionFactory.createIntegralFunction(logarithmic, parameters);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createPolynomialFunctionFailResult2Test() {
        double lowerbound = Double.NEGATIVE_INFINITY;
        Double upperbound = 1.0;
        IntegralFunctionType polynomial = IntegralFunctionFactory.evaluateFunctionType(1);
        assertNotNull(polynomial);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerBound(lowerbound)
                .withUpperBound(upperbound)
                .build();

        IntegralFunctionFactory.createIntegralFunction(polynomial, parameters);

    }

    @Test(expected = UnsupportedOperationException.class)
    public void createExponentialFunctionFailResult2Test() {
        double lowerbound = Double.NEGATIVE_INFINITY;
        Double upperbound = 1.0;
        IntegralFunctionType exponential = IntegralFunctionFactory.evaluateFunctionType(0);
        assertNotNull(exponential);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerBound(lowerbound)
                .withUpperBound(upperbound).build();
        IntegralFunctionFactory.createIntegralFunction(exponential, parameters);
    }

    @Test(expected = NoSuchElementException.class)
    public void retrieveApproximationMethod() {
        assertNotNull(IntegralFunctionFactory.evaluateApproximationMethodType(0));
        assertNotNull(IntegralFunctionFactory.evaluateApproximationMethodType(1));
        assertNotNull(IntegralFunctionFactory.evaluateApproximationMethodType(2));
        assertNotNull(IntegralFunctionFactory.evaluateApproximationMethodType(3));
        assertNotNull(IntegralFunctionFactory.evaluateApproximationMethodType(4));
    }
}
