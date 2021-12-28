package com.acabra.calculator.rootfinding;

import com.acabra.calculator.function.CosineFunction;
import com.acabra.calculator.function.PolynomialFunction;
import com.acabra.calculator.function.RealFunction;
import com.acabra.calculator.util.WebCalculatorConstants;


import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 11/2/2016.
 */
public class NewtonMethodTest {

    @Test
    public void shouldCalculate_root_within_given_iterationsTest() {
        List<Double> coefficients = Arrays.asList(-2.0, -2.0, 3.0);
        RealFunction realFunction = new PolynomialFunction(coefficients);
        double[] doubleArray = {0,3};
        NewtonMethod newtonMethod = new NewtonMethod(realFunction, doubleArray);
        IterativeRootFindingResult iterativeRootFindingResult = newtonMethod.iterateMethod(200);
        assertEquals(0.0, realFunction.apply(iterativeRootFindingResult.getApproximatedRoot()), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(5, iterativeRootFindingResult.getIterations());
    }

    @Test
    public void shouldCalculate_root_within_given_iterations2Test() {
        List<Double> coefficients = Arrays.asList(-1.0, -2.0);
        RealFunction realFunction = new PolynomialFunction(coefficients);
        double[] doubleArray = {-4,5};
        NewtonMethod newtonMethod = new NewtonMethod(realFunction, doubleArray);
        IterativeRootFindingResult iterativeRootFindingResult = newtonMethod.iterateMethod(200);
        assertEquals(0.0, realFunction.apply(iterativeRootFindingResult.getApproximatedRoot()), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(1, iterativeRootFindingResult.getIterations());
    }

    @Test
    public void shouldCalculate_root_within_given_iterations_and_tolerance_cosine_test() {
        RealFunction realFunction = new CosineFunction(null);
        double[] doubleArray = {Math.PI/4.0};
        double tolerance = Math.pow(10.0, -4.0);
        NewtonMethod newtonMethod = new NewtonMethod(realFunction, doubleArray, tolerance);
        IterativeRootFindingResult iterativeRootFindingResult = newtonMethod.iterateMethod(200);
        assertEquals(0.0, realFunction.apply(iterativeRootFindingResult.getApproximatedRoot()), tolerance);
        assertEquals(3, iterativeRootFindingResult.getIterations());
    }

    @Test
    public void shouldCalculate_root_within_given_iterations_cosine_test() {
        RealFunction realFunction = new CosineFunction(null);
        double[] doubleArray = {Math.PI/4.0};
        NewtonMethod newtonMethod = new NewtonMethod(realFunction, doubleArray);
        IterativeRootFindingResult iterativeRootFindingResult = newtonMethod.iterateMethod(200);
        assertEquals(0.0, realFunction.apply(iterativeRootFindingResult.getApproximatedRoot()), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(3, iterativeRootFindingResult.getIterations());
    }
}
