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
public class SecantMethodTest {

    @Test
    public void shouldCalculate_root_within_given_iterationsTest() {
        List<Double> coefficients = Arrays.asList(-2.0, -2.0, 3.0);
        RealFunction realFunction = new PolynomialFunction(coefficients);
        double[] doubleArray = {0,3};
        SecantMethod secantMethod = new SecantMethod(realFunction, doubleArray);
        IterativeRootFindingResult iterativeRootFindingResult = secantMethod.iterateMethod(200);
        assertEquals(0.0, realFunction.apply(iterativeRootFindingResult.getApproximatedRoot()), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(11, iterativeRootFindingResult.getIterations());
    }

    @Test
    public void shouldCalculate_root_within_given_iterations2Test() {
        List<Double> coefficients = Arrays.asList(-1.0, -2.0);
        RealFunction realFunction = new PolynomialFunction(coefficients);
        double[] doubleArray = {-4,5};
        SecantMethod secantMethod = new SecantMethod(realFunction, doubleArray);
        IterativeRootFindingResult iterativeRootFindingResult = secantMethod.iterateMethod(200);
        assertEquals(0.0, realFunction.apply(iterativeRootFindingResult.getApproximatedRoot()), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(2, iterativeRootFindingResult.getIterations());
    }

    @Test
    public void shouldCalculate_root_within_given_iterations_cosine_test() {
        RealFunction realFunction = new CosineFunction(null);
        double[] doubleArray = {0.5, Math.PI/4.0};
        SecantMethod secantMethod = new SecantMethod(realFunction, doubleArray, Math.pow(10.0, -4.0));
        IterativeRootFindingResult iterativeRootFindingResult = secantMethod.iterateMethod(200);
        assertEquals(0.0, realFunction.apply(iterativeRootFindingResult.getApproximatedRoot()), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(5, iterativeRootFindingResult.getIterations());
    }
}
