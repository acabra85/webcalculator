package com.acabra.calculator.integral.function;

import com.acabra.calculator.integral.input.IntegrableFunctionInputParametersBuilder;
import com.acabra.calculator.integral.input.IntegrableFunctionInputParameters;
import com.acabra.calculator.util.WebCalculatorConstants;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by Agustin on 9/30/2016.
 */
public class FunctionFactoryTest {

    @Test
    public void createIntegralFunctionResultAndApproximationTest() {
        int lowerbound = 0;
        int upperbound = 1;
        double contentResult = 9.5;
        double approximation = 1.0;
        IntegrableFunctionType exponential = FunctionFactory.evaluateFunctionType(0);
        assertNotNull(exponential);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound)
                .withIntegrationResult(contentResult)
                .withApproximation(approximation)
                .build();
        IntegrableFunction solvedIntegralFunction = FunctionFactory.createIntegralFunction(exponential, parameters);
        assertNotNull(solvedIntegralFunction);
        assertEquals(lowerbound, solvedIntegralFunction.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, solvedIntegralFunction.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(contentResult, solvedIntegralFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(approximation, solvedIntegralFunction.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[0, 1]", solvedIntegralFunction.toString());

        IntegrableFunction unsolvedFunction = FunctionFactory.createIntegralFunction(exponential,
                new IntegrableFunctionInputParametersBuilder()
                        .withLowerLimit(lowerbound)
                        .withUpperLimit(upperbound)
                        .build());
        assertNotNull(unsolvedFunction);
        assertNotEquals(solvedIntegralFunction.getResult(), unsolvedFunction.getResult());

    }

    @Test
    public void createIntegralFunctionResultTest() {
        int lowerbound = 0;
        int upperbound = 1;
        double contentResult = 9.5;
        IntegrableFunctionType exponential = FunctionFactory.evaluateFunctionType(0);
        assertNotNull(exponential);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound)
                .withIntegrationResult(contentResult)
                .build();
        IntegrableFunction solvedIntegralFunction = FunctionFactory.createIntegralFunction(exponential, parameters);
        assertNotNull(solvedIntegralFunction);
        assertEquals(lowerbound, solvedIntegralFunction.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, solvedIntegralFunction.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(contentResult, solvedIntegralFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[0, 1]", solvedIntegralFunction.toString());
    }

    @Test
    public void createIntegralFunctionApproximationTest() {
        int lowerbound = 1;
        int upperbound = 2;
        double approximation = 5.5;
        IntegrableFunctionType exponential = FunctionFactory.evaluateFunctionType(0);
        assertNotNull(exponential);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound)
                .withApproximation(approximation)
                .build();
        IntegrableFunction solvedIntegralFunction = FunctionFactory.createIntegralFunction(exponential, parameters);
        assertNotNull(solvedIntegralFunction);
        assertEquals(lowerbound, solvedIntegralFunction.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, solvedIntegralFunction.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(approximation, solvedIntegralFunction.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[1, 2]", solvedIntegralFunction.toString());
    }

    @Test
    public void createIntegralFunctionNoResultNoApproximationTest() {
        double lowerbound = 0;
        double upperbound = 1;
        IntegrableFunctionType exponential = FunctionFactory.evaluateFunctionType(0);
        assertNotNull(exponential);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound)
                .build();
        IntegrableFunction integralFunction = FunctionFactory.createIntegralFunction(exponential, parameters);
        assertNotNull(integralFunction);
        assertEquals(lowerbound, integralFunction.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, integralFunction.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[0, 1]", integralFunction.toString());
    }

    @Test
    public void createIntegralFunctionInvalidTypeTest() {
        int expectedExceptions = 4;
        int foundExceptions = 0;
        int inverseFunctionId = 4;
        IntegrableFunctionType inverse = FunctionFactory.evaluateFunctionType(inverseFunctionId);
        assertNotNull(inverse);

        try {
            IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                    .withLowerLimit(0)
                    .withUpperLimit(1)
                    .build();
            FunctionFactory.createIntegralFunction(inverse, parameters);
        } catch (NoSuchElementException nse) {
            foundExceptions++;
        }

        try {
            IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                    .withLowerLimit(0)
                    .withUpperLimit(1)
                    .withIntegrationResult(1.0)
                    .build();
            FunctionFactory.createIntegralFunction(inverse, parameters);
        } catch (NoSuchElementException nse) {
            foundExceptions++;
        }

        try {
            IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                    .withLowerLimit(0)
                    .withUpperLimit(1)
                    .withApproximation(1.0)
                    .build();
            FunctionFactory.createIntegralFunction(inverse, parameters);
        } catch (NoSuchElementException nse) {
            foundExceptions++;
        }

        try {
            IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                    .withLowerLimit(0)
                    .withUpperLimit(1)
                    .withIntegrationResult(1.0)
                    .withApproximation(1.0)
                    .build();
            FunctionFactory.createIntegralFunction(inverse, parameters);
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
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound)
                .withCoefficients(coefficients)
                .build();

        FPolynomial unsolvedIntegralFunction = (FPolynomial) FunctionFactory.createIntegralFunction(IntegrableFunctionType.POLYNOMIAL, parameters);

        assertNotNull(unsolvedIntegralFunction);
        assertEquals(lowerbound, unsolvedIntegralFunction.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, unsolvedIntegralFunction.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, unsolvedIntegralFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertNull(unsolvedIntegralFunction.getApproximation());
        assertEquals(unsolvedIntegralFunction.getOrder(), unsolvedIntegralFunction.getCoefficients().size());
        assertEquals("Integ{x^2}[-3, -1]", unsolvedIntegralFunction.toString());
    }

    @Test
    public void createPolynomialFunctionResultTest() {
        int lowerbound = 1;
        int upperbound = 3;
        Double integralResult = 26.0;
        List<Double> coefficients = Arrays.asList(0.0, 0.0, 3.0);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound)
                .withCoefficients(coefficients)
                .build();

        FPolynomial unsolvedIntegralFunction = (FPolynomial) FunctionFactory.createIntegralFunction(IntegrableFunctionType.POLYNOMIAL, parameters);

        assertNotNull(unsolvedIntegralFunction);
        assertEquals(lowerbound, unsolvedIntegralFunction.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, unsolvedIntegralFunction.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, unsolvedIntegralFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertNull(unsolvedIntegralFunction.getApproximation());
        assertEquals(unsolvedIntegralFunction.getOrder(), unsolvedIntegralFunction.getCoefficients().size());
        assertEquals("Integ{3x^2}[1, 3]", unsolvedIntegralFunction.toString());

    }

    @Test
    public void createPolynomialFunctionResult2Test() {
        int lowerbound = 2;
        int upperbound = 5;
        Double integralResult = 109.5;
        List<Double> coefficients = Arrays.asList(1.0, -1.0, 3.0);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound)
                .withCoefficients(coefficients)
                .build();

        FPolynomial unsolvedIntegralFunction = (FPolynomial) FunctionFactory.createIntegralFunction(IntegrableFunctionType.POLYNOMIAL, parameters);

        assertNotNull(unsolvedIntegralFunction);
        assertEquals(lowerbound, unsolvedIntegralFunction.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, unsolvedIntegralFunction.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, unsolvedIntegralFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertNull(unsolvedIntegralFunction.getApproximation());
        assertEquals(unsolvedIntegralFunction.getOrder(), unsolvedIntegralFunction.getCoefficients().size());
        assertEquals("Integ{1-x+3x^2}[2, 5]", unsolvedIntegralFunction.toString());

    }

    @Test
    public void createPolynomialFunctionResult3Test() {
        int lowerbound = 3;
        int upperbound = 7;
        Double integralResult = 4.0;
        double inscribedAreaExpected = 100.0;
        List<Double> coefficients = Arrays.asList(1.0, -1.0, 3.0);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound)
                .withApproximation(inscribedAreaExpected)
                .withIntegrationResult(integralResult)
                .withCoefficients(coefficients)
                .build();

        FPolynomial unsolvedIntegralFunction = (FPolynomial) FunctionFactory.createIntegralFunction(IntegrableFunctionType.POLYNOMIAL, parameters);

        assertNotNull(unsolvedIntegralFunction);
        assertEquals(lowerbound, unsolvedIntegralFunction.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, unsolvedIntegralFunction.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, unsolvedIntegralFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(inscribedAreaExpected, unsolvedIntegralFunction.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(unsolvedIntegralFunction.getOrder(), unsolvedIntegralFunction.getCoefficients().size());
        assertEquals("Integ{1-x+3x^2}[3, 7]", unsolvedIntegralFunction.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createPolynomialFunctionFailResultTest() {
        int lowerbound = 2;
        Double upperbound = Double.POSITIVE_INFINITY;
        IntegrableFunctionType polynomial = FunctionFactory.evaluateFunctionType(1);
        assertNotNull(polynomial);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound)
                .build();

        FunctionFactory.createIntegralFunction(polynomial, parameters);

    }

    @Test(expected = UnsupportedOperationException.class)
    public void createExponentialFunctionFailResultTest() {
        int lowerbound = 1;
        Double upperbound = Double.POSITIVE_INFINITY;
        IntegrableFunctionType exponential = FunctionFactory.evaluateFunctionType(0);
        assertNotNull(exponential);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound).build();
        FunctionFactory.createIntegralFunction(exponential, parameters);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createLogarithmicFunctionFailResultTest() {
        int lowerbound = 1;
        Double upperbound = Double.POSITIVE_INFINITY;
        IntegrableFunctionType logarithmic = FunctionFactory.evaluateFunctionType(2);
        assertNotNull(logarithmic);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound).build();
        FunctionFactory.createIntegralFunction(logarithmic, parameters);
    }

    @Test
    public void createLogarithmicFunctionResultTest() {
        double lowerbound = 1.0;
        double upperbound = 2.0;
        IntegrableFunctionType logarithmic = FunctionFactory.evaluateFunctionType(2);
        assertNotNull(logarithmic);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound).build();
        assertNotNull(FunctionFactory.createIntegralFunction(logarithmic, parameters));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createLogarithmicFunctionFailResult2Test() {
        int lowerbound = -1;
        int upperbound = 1;
        double contentResult = 9.5;
        int functionId = 2;
        IntegrableFunctionType logarithmic = FunctionFactory.evaluateFunctionType(functionId);
        assertNotNull(logarithmic);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound)
                .withIntegrationResult(contentResult).build();
        FunctionFactory.createIntegralFunction(logarithmic, parameters);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createPolynomialFunctionFailResult2Test() {
        double lowerbound = Double.NEGATIVE_INFINITY;
        Double upperbound = 1.0;
        IntegrableFunctionType polynomial = FunctionFactory.evaluateFunctionType(1);
        assertNotNull(polynomial);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound)
                .build();

        FunctionFactory.createIntegralFunction(polynomial, parameters);

    }

    @Test(expected = UnsupportedOperationException.class)
    public void createExponentialFunctionFailResult2Test() {
        double lowerbound = Double.NEGATIVE_INFINITY;
        Double upperbound = 1.0;
        IntegrableFunctionType exponential = FunctionFactory.evaluateFunctionType(0);
        assertNotNull(exponential);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound).build();
        FunctionFactory.createIntegralFunction(exponential, parameters);
    }

    @Test(expected = NoSuchElementException.class)
    public void retrieveApproximationMethod() {
        assertNotNull(FunctionFactory.evaluateApproximationMethodType(0));
        assertNotNull(FunctionFactory.evaluateApproximationMethodType(1));
        assertNotNull(FunctionFactory.evaluateApproximationMethodType(2));
        assertNotNull(FunctionFactory.evaluateApproximationMethodType(3));
    }

    @Test(expected = NoSuchElementException.class)
    public void retrieveFunctionTypeTest() {
        assertNotNull(FunctionFactory.evaluateFunctionType(0));
        assertNotNull(FunctionFactory.evaluateFunctionType(1));
        assertNotNull(FunctionFactory.evaluateFunctionType(2));
        assertNotNull(FunctionFactory.evaluateFunctionType(3));
        assertNotNull(FunctionFactory.evaluateFunctionType(4));
        assertNotNull(FunctionFactory.evaluateFunctionType(5));
    }

    @Test
    public void createInverseFunctionTest() {
        IntegrableFunctionType integrableFunctionType = FunctionFactory.evaluateFunctionType(3);
        double lowerLimit = 0.1;
        double upperLimit = 1;
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerLimit)
                .withUpperLimit(upperLimit)
                .build();
        IntegrableFunction inverseFunction = FunctionFactory.createIntegralFunction(integrableFunctionType, parameters);
        assertNotNull(inverseFunction);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createInverseFunctionFailTest() {
        IntegrableFunctionType integrableFunctionType = FunctionFactory.evaluateFunctionType(3);
        double lowerLimit = 0.0;
        double upperLimit = 1;
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerLimit)
                .withUpperLimit(upperLimit)
                .build();
        FunctionFactory.createIntegralFunction(integrableFunctionType, parameters);
    }
}
