package com.acabra.calculator.integral.definiteintegral;

import com.acabra.calculator.integral.input.IntegrableFunctionInputParameters;
import com.acabra.calculator.integral.input.IntegrableFunctionInputParametersBuilder;
import com.acabra.calculator.util.WebCalculatorConstants;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Agustin on 9/30/2016.
 */
public class DefiniteIntegralFunctionFactoryTest {
    //TODO modify tests to only test DefiniteIntegralFunctionFactory

    @Test
    public void createIntegralFunctionResultAndApproximationTest() {
        int lowerbound = 0;
        int upperbound = 1;
        double contentResult = 9.5;
        double approximation = 1.0;
        IntegrableFunctionType exponential = DefiniteIntegralFunctionFactory.evaluateFunctionType(0);
        assertNotNull(exponential);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound)
                .withIntegrationResult(contentResult)
                .withApproximation(approximation)
                .build();
        DefiniteIntegralFunction solvedIntegralFunction = DefiniteIntegralFunctionFactory.createIntegralFunction(exponential, parameters);
        assertNotNull(solvedIntegralFunction);
        assertEquals(lowerbound, solvedIntegralFunction.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, solvedIntegralFunction.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(contentResult, solvedIntegralFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(approximation, solvedIntegralFunction.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[0, 1]", solvedIntegralFunction.toString());

        DefiniteIntegralFunction unsolvedFunction = DefiniteIntegralFunctionFactory.createIntegralFunction(exponential,
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
        IntegrableFunctionType exponential = DefiniteIntegralFunctionFactory.evaluateFunctionType(0);
        assertNotNull(exponential);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound)
                .withIntegrationResult(contentResult)
                .build();
        DefiniteIntegralFunction solvedIntegralFunction = DefiniteIntegralFunctionFactory.createIntegralFunction(exponential, parameters);
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
        IntegrableFunctionType exponential = DefiniteIntegralFunctionFactory.evaluateFunctionType(0);
        assertNotNull(exponential);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound)
                .withApproximation(approximation)
                .build();
        DefiniteIntegralFunction solvedIntegralFunction = DefiniteIntegralFunctionFactory.createIntegralFunction(exponential, parameters);
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
        IntegrableFunctionType exponential = DefiniteIntegralFunctionFactory.evaluateFunctionType(0);
        assertNotNull(exponential);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound)
                .build();
        DefiniteIntegralFunction integralFunction = DefiniteIntegralFunctionFactory.createIntegralFunction(exponential, parameters);
        assertNotNull(integralFunction);
        assertEquals(lowerbound, integralFunction.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, integralFunction.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[0, 1]", integralFunction.toString());
    }

    @Test(expected = NoSuchElementException.class)
    public void should_fail_invalid_function_id_type() {
        int inverseFunctionId = 6;
        DefiniteIntegralFunctionFactory.evaluateFunctionType(inverseFunctionId);
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

        DefiniteIntegralPolynomial unsolvedIntegralFunction = (DefiniteIntegralPolynomial) DefiniteIntegralFunctionFactory.createIntegralFunction(IntegrableFunctionType.POLYNOMIAL, parameters);

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

        DefiniteIntegralPolynomial unsolvedIntegralFunction = (DefiniteIntegralPolynomial) DefiniteIntegralFunctionFactory.createIntegralFunction(IntegrableFunctionType.POLYNOMIAL, parameters);

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

        DefiniteIntegralPolynomial unsolvedIntegralFunction = (DefiniteIntegralPolynomial) DefiniteIntegralFunctionFactory.createIntegralFunction(IntegrableFunctionType.POLYNOMIAL, parameters);

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

        DefiniteIntegralPolynomial unsolvedIntegralFunction = (DefiniteIntegralPolynomial) DefiniteIntegralFunctionFactory.createIntegralFunction(IntegrableFunctionType.POLYNOMIAL, parameters);

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
        IntegrableFunctionType polynomial = DefiniteIntegralFunctionFactory.evaluateFunctionType(1);
        assertNotNull(polynomial);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound)
                .build();

        DefiniteIntegralFunctionFactory.createIntegralFunction(polynomial, parameters);

    }

    @Test(expected = UnsupportedOperationException.class)
    public void createExponentialFunctionFailResultTest() {
        int lowerbound = 1;
        Double upperbound = Double.POSITIVE_INFINITY;
        IntegrableFunctionType exponential = DefiniteIntegralFunctionFactory.evaluateFunctionType(0);
        assertNotNull(exponential);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound).build();
        DefiniteIntegralFunctionFactory.createIntegralFunction(exponential, parameters);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createLogarithmicFunctionFailResultTest() {
        int lowerbound = 1;
        Double upperbound = Double.POSITIVE_INFINITY;
        IntegrableFunctionType logarithmic = DefiniteIntegralFunctionFactory.evaluateFunctionType(2);
        assertNotNull(logarithmic);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound).build();
        DefiniteIntegralFunctionFactory.createIntegralFunction(logarithmic, parameters);
    }

    @Test
    public void createLogarithmicFunctionResultTest() {
        double lowerbound = 1.0;
        double upperbound = 2.0;
        IntegrableFunctionType logarithmic = DefiniteIntegralFunctionFactory.evaluateFunctionType(2);
        assertNotNull(logarithmic);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound).build();
        assertNotNull(DefiniteIntegralFunctionFactory.createIntegralFunction(logarithmic, parameters));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createLogarithmicFunctionFailResult2Test() {
        int lowerbound = -1;
        int upperbound = 1;
        double contentResult = 9.5;
        int functionId = 2;
        IntegrableFunctionType logarithmic = DefiniteIntegralFunctionFactory.evaluateFunctionType(functionId);
        assertNotNull(logarithmic);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound)
                .withIntegrationResult(contentResult).build();
        DefiniteIntegralFunctionFactory.createIntegralFunction(logarithmic, parameters);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createPolynomialFunctionFailResult2Test() {
        double lowerbound = Double.NEGATIVE_INFINITY;
        Double upperbound = 1.0;
        IntegrableFunctionType polynomial = DefiniteIntegralFunctionFactory.evaluateFunctionType(1);
        assertNotNull(polynomial);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound)
                .build();

        DefiniteIntegralFunctionFactory.createIntegralFunction(polynomial, parameters);

    }

    @Test(expected = UnsupportedOperationException.class)
    public void createExponentialFunctionFailResult2Test() {
        double lowerbound = Double.NEGATIVE_INFINITY;
        Double upperbound = 1.0;
        IntegrableFunctionType exponential = DefiniteIntegralFunctionFactory.evaluateFunctionType(0);
        assertNotNull(exponential);
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerbound)
                .withUpperLimit(upperbound).build();
        DefiniteIntegralFunctionFactory.createIntegralFunction(exponential, parameters);
    }

    @Test(expected = NoSuchElementException.class)
    public void retrieveApproximationMethod() {
        assertNotNull(DefiniteIntegralFunctionFactory.evaluateApproximationMethodType(0));
        assertNotNull(DefiniteIntegralFunctionFactory.evaluateApproximationMethodType(1));
        assertNotNull(DefiniteIntegralFunctionFactory.evaluateApproximationMethodType(2));
        assertNotNull(DefiniteIntegralFunctionFactory.evaluateApproximationMethodType(3));
    }

    @Test(expected = NoSuchElementException.class)
    public void retrieveFunctionTypeTest() {
        assertNotNull(DefiniteIntegralFunctionFactory.evaluateFunctionType(0));
        assertNotNull(DefiniteIntegralFunctionFactory.evaluateFunctionType(1));
        assertNotNull(DefiniteIntegralFunctionFactory.evaluateFunctionType(2));
        assertNotNull(DefiniteIntegralFunctionFactory.evaluateFunctionType(3));
        assertNotNull(DefiniteIntegralFunctionFactory.evaluateFunctionType(4));
        assertNotNull(DefiniteIntegralFunctionFactory.evaluateFunctionType(5));
        assertNotNull(DefiniteIntegralFunctionFactory.evaluateFunctionType(6));
    }

    @Test
    public void createInverseFunctionTest() {
        IntegrableFunctionType integrableFunctionType = DefiniteIntegralFunctionFactory.evaluateFunctionType(3);
        double lowerLimit = 0.1;
        double upperLimit = 1;
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerLimit)
                .withUpperLimit(upperLimit)
                .build();
        DefiniteIntegralFunction inverseFunction = DefiniteIntegralFunctionFactory.createIntegralFunction(integrableFunctionType, parameters);
        assertNotNull(inverseFunction);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createInverseFunctionFailTest() {
        IntegrableFunctionType integrableFunctionType = DefiniteIntegralFunctionFactory.evaluateFunctionType(3);
        double lowerLimit = 0.0;
        double upperLimit = 1;
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withLowerLimit(lowerLimit)
                .withUpperLimit(upperLimit)
                .build();
        DefiniteIntegralFunctionFactory.createIntegralFunction(integrableFunctionType, parameters);
    }
}
