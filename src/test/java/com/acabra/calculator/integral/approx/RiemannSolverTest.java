package com.acabra.calculator.integral.approx;

import com.acabra.calculator.integral.function.*;
import com.acabra.calculator.util.WebCalculatorConstants;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 10/17/2016.
 */
public class RiemannSolverTest {

    private static final List<Double> EMPTY_LIST = Collections.EMPTY_LIST;

    @Test
    public void solveAreaCircumscribed1Test() throws ExecutionException, InterruptedException {
        int lowerBound = 0;
        double upperBound = 5.0;
        double expected = 742.0657955;
        IntegrableFunction unsolvedIntegral = new RiemannSolver(lowerBound, upperBound, EMPTY_LIST, IntegrableFunctionType.EXPONENTIAL, false)
                .approximate(1, Executors.newFixedThreadPool(1))
                .get();

        assertEquals(expected, unsolvedIntegral.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[0, 5]", unsolvedIntegral.toString());
    }

    @Test
    public void solvingPolynomialIntegralTest() throws ExecutionException, InterruptedException {
        double lowerBound = 1;
        double upperBound = Math.E;
        double expectedApproximationInscribed = 0.0;
        double expectedApproximationCircumscribed = 1.71828182;
        double integralResult = 1.0;

        IntegrableFunction unsolvedIntegral = new RiemannSolver(lowerBound, upperBound, EMPTY_LIST, IntegrableFunctionType.LOGARITHMIC, false)
                .approximate(1, Executors.newFixedThreadPool(1))
                .get();

        assertEquals(expectedApproximationCircumscribed, unsolvedIntegral.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);

        unsolvedIntegral = new RiemannSolver(lowerBound, upperBound, EMPTY_LIST, IntegrableFunctionType.LOGARITHMIC, true)
                .approximate(1, Executors.newFixedThreadPool(1))
                .get();

        assertEquals(expectedApproximationInscribed, unsolvedIntegral.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, unsolvedIntegral.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{ln(x)}[1, 2.718281828459045]", unsolvedIntegral.toString());
    }

    @Test
    public void solvingPolynomialSolvedTest() throws ExecutionException, InterruptedException {
        double lowerBound = 1.0;
        double upperBound = Math.E;
        double expectedApproximationInscribed = 0.0;
        double expectedApproximationCircumscribed = 1.71828182;
        double integralResult = 1.0;

        IntegrableFunction unsolvedIntegral = new RiemannSolver(lowerBound, upperBound, EMPTY_LIST, IntegrableFunctionType.LOGARITHMIC, true)
                .approximate(1, Executors.newFixedThreadPool(1))
                .get();

        assertEquals(integralResult, unsolvedIntegral.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedApproximationInscribed, unsolvedIntegral.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);

        unsolvedIntegral = new RiemannSolver(lowerBound, upperBound, EMPTY_LIST, IntegrableFunctionType.LOGARITHMIC, false)
                .approximate(1, Executors.newFixedThreadPool(1))
                .get();

        assertEquals(expectedApproximationCircumscribed, unsolvedIntegral.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{ln(x)}[1, 2.718281828459045]", unsolvedIntegral.toString());
    }


    @Test
    public void solveArea1Test() throws ExecutionException, InterruptedException {
        int lowerBound = 1;
        int upperBound = 2;
        int order = 3;
        double integralResult = 10.0;
        double inscribedAreaExpected = 5.0;
        double circumscribedAreaExpected = 16.0;
        List<Double> coefficients = Arrays.asList(0.0, 2.0, 3.0);

        FPolynomial unsolvedIntegral = (FPolynomial) new RiemannSolver(lowerBound, upperBound, coefficients, IntegrableFunctionType.POLYNOMIAL, true)
                .approximate(1, Executors.newFixedThreadPool(1))
                .get();

        assertEquals(lowerBound, unsolvedIntegral.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperBound, unsolvedIntegral.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, unsolvedIntegral.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);

        assertEquals(inscribedAreaExpected, unsolvedIntegral.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);

        unsolvedIntegral = (FPolynomial) new RiemannSolver(lowerBound, upperBound, coefficients, IntegrableFunctionType.POLYNOMIAL, false)
                .approximate(1, Executors.newFixedThreadPool(1))
                .get();

        assertEquals(circumscribedAreaExpected, unsolvedIntegral.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(order, unsolvedIntegral.getOrder());
        assertEquals(unsolvedIntegral.getOrder(), unsolvedIntegral.getCoefficients().size());
        assertEquals("Integ{2x+3x^2}[1, 2]", unsolvedIntegral.toString());
    }

    @Test
    public void solveArea2Test() throws ExecutionException, InterruptedException {
        int lowerBound = 1;
        int upperBound = 2;
        int order = 2;
        List<Double> coefficients = Arrays.asList(0.0, 2.0);
        double integralResult = 3.0;
        double inscribedAreaExpected = 2.0;
        double circumscribedAreaExpected = 4.0;

        FPolynomial fPolynomial = (FPolynomial) new RiemannSolver(lowerBound, upperBound, coefficients, IntegrableFunctionType.POLYNOMIAL, true)
                .approximate(1, Executors.newFixedThreadPool(1))
                .get();

        assertEquals(lowerBound, fPolynomial.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperBound, fPolynomial.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, fPolynomial.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(inscribedAreaExpected, fPolynomial.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);

        fPolynomial = (FPolynomial) new RiemannSolver(lowerBound, upperBound, coefficients, IntegrableFunctionType.POLYNOMIAL, false)
                .approximate(1, Executors.newFixedThreadPool(1))
                .get();

        assertEquals(circumscribedAreaExpected, fPolynomial.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, fPolynomial.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(order, fPolynomial.getOrder());
        assertEquals(fPolynomial.getOrder(), fPolynomial.getCoefficients().size());
        assertEquals("Integ{2x}[1, 2]", fPolynomial.toString());
    }
    @Test
    public void solveIntegral1Test() throws ExecutionException, InterruptedException {
        int lowerbound = 1;
        int upperbound = 2;
        int order = 2;
        double integralResult = 3.0;
        double inscribedAreaExpected = 2.0;
        double circumscribedAreaExpected = 4.0;
        List<Double> coefficients = Arrays.asList(0.0, 2.0);

        FPolynomial fPolynomial = (FPolynomial) new RiemannSolver(lowerbound, upperbound, coefficients, IntegrableFunctionType.POLYNOMIAL, true)
                .approximate(1, Executors.newFixedThreadPool(1))
                .get();

        assertEquals(lowerbound, fPolynomial.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, fPolynomial.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, fPolynomial.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(inscribedAreaExpected, fPolynomial.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);

        fPolynomial = (FPolynomial) new RiemannSolver(lowerbound, upperbound, coefficients, IntegrableFunctionType.POLYNOMIAL, false)
                .approximate(1, Executors.newFixedThreadPool(1))
                .get();

        assertEquals(circumscribedAreaExpected, fPolynomial.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, fPolynomial.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(order, fPolynomial.getOrder());
        assertEquals(fPolynomial.getOrder(), fPolynomial.getCoefficients().size());
        assertEquals("Integ{2x}[1, 2]", fPolynomial.toString());
    }

    @Test
    public void solveIntegral2Test() throws ExecutionException, InterruptedException {
        int lowerbound = -1;
        int upperbound = 0;
        int order = 2;
        double integralResult = -1.0;
        double inscribedAreaExpected = 0.0;
        double circumscribedAreaExpected = -2.0;
        List<Double> coefficients = Arrays.asList(0.0, 2.0);

        FPolynomial fPolynomial = (FPolynomial) new RiemannSolver(lowerbound, upperbound, coefficients, IntegrableFunctionType.POLYNOMIAL, true)
                .approximate(1, Executors.newFixedThreadPool(1))
                .get();

        assertEquals(lowerbound, fPolynomial.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, fPolynomial.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, fPolynomial.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(inscribedAreaExpected, fPolynomial.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);

        fPolynomial = (FPolynomial) new RiemannSolver(lowerbound, upperbound, coefficients, IntegrableFunctionType.POLYNOMIAL, false)
                .approximate(1, Executors.newFixedThreadPool(1))
                .get();

        assertEquals(circumscribedAreaExpected, fPolynomial.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(circumscribedAreaExpected, fPolynomial.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(order, fPolynomial.getOrder());
        assertEquals(fPolynomial.getOrder(), fPolynomial.getCoefficients().size());
        assertEquals("Integ{2x}[-1, 0]", fPolynomial.toString());
    }

    @Test @Ignore
    public void solveIntegral3Test() throws ExecutionException, InterruptedException { //TODO test ignored since area implied covers one root for the function
        int lowerbound = -2;
        int upperbound = 1;
        int order = 2;
        double integralResult = -3.0;
        List<Double> coefficients = Arrays.asList(0.0, 2.0);

        double expectedAreaInscribedRange1 = 0.0;
        double expectedAreaInscribedRange2 = 0.0;
        double expectedAreaCircumscribedRange1 = -8.0;
        double expectedAreaCircumscribedRange2 = 2.0;

        FPolynomial fPolynomialPartRange1 = (FPolynomial) new RiemannSolver(-2, 0, coefficients, IntegrableFunctionType.POLYNOMIAL, true)
                .approximate(1, Executors.newFixedThreadPool(1))
                .get();
        FPolynomial fPolynomialPartRange2 = (FPolynomial) new RiemannSolver(0, 1, coefficients, IntegrableFunctionType.POLYNOMIAL, true)
                .approximate(1, Executors.newFixedThreadPool(1))
                .get();

        assertEquals(expectedAreaInscribedRange1, fPolynomialPartRange1.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedAreaInscribedRange2, fPolynomialPartRange2.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);

        fPolynomialPartRange1 = (FPolynomial) new RiemannSolver(-2, 0, coefficients, IntegrableFunctionType.POLYNOMIAL, false)
                .approximate(1, Executors.newFixedThreadPool(1))
                .get();
        fPolynomialPartRange2 = (FPolynomial) new RiemannSolver(0, 1, coefficients, IntegrableFunctionType.POLYNOMIAL, false)
                .approximate(1, Executors.newFixedThreadPool(1))
                .get();

        assertEquals(expectedAreaCircumscribedRange1, fPolynomialPartRange1.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedAreaCircumscribedRange2, fPolynomialPartRange2.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);

        double inscribedAreaExpected = expectedAreaInscribedRange2 + expectedAreaInscribedRange1;
        double circumscribedAreaExpected = expectedAreaCircumscribedRange2 + expectedAreaCircumscribedRange2;


        FPolynomial fPolynomialFullRange = (FPolynomial) new RiemannSolver(lowerbound, upperbound, coefficients, IntegrableFunctionType.POLYNOMIAL, true)
                .approximate(1000, Executors.newFixedThreadPool(1))
                .get();


        assertEquals(lowerbound, fPolynomialFullRange.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperbound, fPolynomialFullRange.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, fPolynomialFullRange.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);

        assertEquals(inscribedAreaExpected, fPolynomialFullRange.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);

        fPolynomialFullRange = (FPolynomial) new RiemannSolver(lowerbound, upperbound, coefficients, IntegrableFunctionType.POLYNOMIAL, false)
                .approximate(100, Executors.newFixedThreadPool(1))
                .get();

        assertEquals(circumscribedAreaExpected, fPolynomialFullRange.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(order, fPolynomialFullRange.getOrder());
        assertEquals(fPolynomialFullRange.getOrder(), fPolynomialFullRange.getCoefficients().size());
        assertEquals("Integ{2x}[-2, 1]", fPolynomialFullRange.toString());
    }
}
