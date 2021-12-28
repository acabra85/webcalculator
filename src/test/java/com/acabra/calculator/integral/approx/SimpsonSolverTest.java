package com.acabra.calculator.integral.approx;

import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralFunction;
import com.acabra.calculator.integral.definiteintegral.IntegrableFunctionType;
import com.acabra.calculator.util.WebCalculatorConstants;


import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 10/17/2016.
 */
public class SimpsonSolverTest {

    List<Double> EMPTY_LIST = Collections.emptyList();

    @Test
    public void approximateTest() throws ExecutionException, InterruptedException {
        int repeatedCalculations = 10;
        CompletableFuture<DefiniteIntegralFunction> approximate = new SimpsonSolver(1, 2, EMPTY_LIST, IntegrableFunctionType.INVERSE).approximate(repeatedCalculations, Executors.newFixedThreadPool(1));
        DefiniteIntegralFunction definiteIntegralFunction = approximate.get();
        assertEquals(0.6931502, definiteIntegralFunction.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void approximateSimpsonOddAmountTest() throws ExecutionException, InterruptedException {
        int repeatedCalculations = 11;
        new SimpsonSolver(1, 2, EMPTY_LIST, IntegrableFunctionType.INVERSE).approximate(repeatedCalculations, Executors.newFixedThreadPool(1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void approximateSimpsonNonDomainPointsTest() throws ExecutionException, InterruptedException {
        int repeatedCalculations = 11;
        new SimpsonSolver(-0.1, 2, EMPTY_LIST, IntegrableFunctionType.INVERSE).approximate(repeatedCalculations, Executors.newFixedThreadPool(1));
    }
}
