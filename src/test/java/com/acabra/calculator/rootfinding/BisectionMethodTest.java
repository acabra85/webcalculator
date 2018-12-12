package com.acabra.calculator.rootfinding;

import com.acabra.calculator.function.CosineFunction;
import com.acabra.calculator.function.PolynomialFunction;
import com.acabra.calculator.function.RealFunction;
import com.acabra.calculator.util.WebCalculatorConstants;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 11/2/2016.
 */
public class BisectionMethodTest {

    @Test
    public void shouldCalculate_root_within_given_iterationsTest() {
        List<Double> coefficients = Arrays.asList(-2.0, -2.0, 3.0);
        RealFunction realFunction = new PolynomialFunction(coefficients);
        double[] doubleArray = {0,3};
        BisectionMethod bisectionMethod = new BisectionMethod(realFunction, doubleArray);
        IterativeRootFindingResult iterativeRootFindingResult = bisectionMethod.iterateMethod(200);
        assertEquals(24, iterativeRootFindingResult.getIterations());
        assertEquals(0.0, realFunction.apply(iterativeRootFindingResult.getApproximatedRoot()), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test
    public void shouldCalculate_root_within_given_iterations2Test() {
        List<Double> coefficients = Arrays.asList(-1.0, -2.0);
        RealFunction realFunction = new PolynomialFunction(coefficients);
        double[] doubleArray = {-4,5};
        BisectionMethod bisectionMethod = new BisectionMethod(realFunction, doubleArray);
        IterativeRootFindingResult iterativeRootFindingResult = bisectionMethod.iterateMethod(200);
        assertEquals(27, iterativeRootFindingResult.getIterations());
        assertEquals(0.0, realFunction.apply(iterativeRootFindingResult.getApproximatedRoot()), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test
    public void shouldCalculate_root_within_given_iterations_cosine_test() {
        RealFunction realFunction = new CosineFunction(null);
        double[] doubleArray = {0.5, 2 * Math.PI};
        double tolerance = Math.pow(10.0, -4.0);
        BisectionMethod bisectionMethod = new BisectionMethod(realFunction, doubleArray, tolerance);
        IterativeRootFindingResult iterativeRootFindingResult = bisectionMethod.iterateMethod(200);
        assertEquals(15, iterativeRootFindingResult.getIterations());
        assertEquals(0.0, realFunction.apply(iterativeRootFindingResult.getApproximatedRoot()), tolerance);
    }

    @Test
    public void shouldCalculate_root_within_given_iterations_polynomial_test() {
        List<Double> coefficients = Arrays.asList(-10.0, 0.0, 4.0, 1.0);
        RealFunction realFunction = new PolynomialFunction(coefficients);
        double[] doubleArray = {1.0, 2.0};
        double tolerance = Math.pow(10.0, -4.0);
        BisectionMethod bisectionMethod = new BisectionMethod(realFunction, doubleArray, tolerance);
        IterativeRootFindingResult iterativeRootFindingResult = bisectionMethod.iterateMethod(200);
        assertEquals(9, iterativeRootFindingResult.getIterations());
        assertEquals(0.0, realFunction.apply(iterativeRootFindingResult.getApproximatedRoot()), tolerance);
    }
}
