package com.acabra.calculator.integral;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.util.WebCalculatorConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Agustin on 9/30/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({IntegralSolver.class, IntegralSubRangeProvider.class, IntegrableFunction.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class IntegralSolverTest {

    @Test
    public void resolveIntegral1Test() throws ExecutionException, InterruptedException {
        double lowerBound1 = 0;
        double upperBound1 = 10;
        double[] expectedLowerRanges = {lowerBound1, 2.5, 5.0, 7.5};
        double[] expectedUpperRanges = {2.5, 5.0, 7.5, upperBound1};
        int repeatedCalculations = 4;
        int numThreads = 1;
        int functionId = 0;
        double expectedResult = 22025.465794806718;
        List<IntegrableFunction> integralSubRangesList = buildIntegralSubRanges(expectedLowerRanges, expectedUpperRanges);

        PowerMockito.mockStatic(IntegralSubRangeProvider.class);
        when(IntegralSubRangeProvider.provideIntegralsOnSubRanges(eq(lowerBound1), eq(upperBound1), eq(repeatedCalculations), eq(IntegralFunctionType.EXPONENTIAL)))
                .thenReturn(integralSubRangesList);

        IntegralSolver integralSolver = new IntegralSolver(new IntegralRequest(lowerBound1, upperBound1, repeatedCalculations, numThreads, functionId));
        IntegrableFunction integrableFunction = integralSolver.approximateSequenceRiemannArea(true).get();

        PowerMockito.verifyStatic(Mockito.times(1));

        assertEquals(lowerBound1, integrableFunction.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperBound1, integrableFunction.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedResult, integrableFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);

    }

    @Test
    public void resolveIntegral2Test() throws ExecutionException, InterruptedException {
        double lowerBound1 = 0;
        double upperBound1 = 10;
        int repeatedCalculations = 5;
        int numThreads = 5;
        int functionId = 0;

        double subAreaApproximated = 1.0;
        double expectedResult = 22025.46579480671;
        double expectedApproximateArea = 5.0;
        ExponentialIntegral exponentialIntegralMock = PowerMockito.mock(ExponentialIntegral.class);
        List<IntegrableFunction> integralSubRangesList = buildIntegralSubRangesMocks(exponentialIntegralMock, 5);

        when(exponentialIntegralMock.solveIntegralWithRiemannSequences(eq(true))).thenReturn(subAreaApproximated);

        PowerMockito.mockStatic(IntegralSubRangeProvider.class);
        when(IntegralSubRangeProvider.provideIntegralsOnSubRanges(eq(lowerBound1), eq(upperBound1), eq(repeatedCalculations), eq(IntegralFunctionType.EXPONENTIAL)))
                .thenReturn(integralSubRangesList);

        IntegralSolver integralSolver = new IntegralSolver(new IntegralRequest(lowerBound1, upperBound1, repeatedCalculations, numThreads, functionId));
        IntegrableFunction integrableFunction = integralSolver.approximateSequenceRiemannArea(true).get();

        verify(exponentialIntegralMock, times(5)).solveIntegralWithRiemannSequences(eq(true));
        PowerMockito.verifyStatic(Mockito.times(1));

        assertEquals(lowerBound1, integrableFunction.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperBound1, integrableFunction.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedResult, integrableFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedApproximateArea, integrableFunction.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[0, 10]", integrableFunction.toString());

    }

    private List<IntegrableFunction> buildIntegralSubRangesMocks(ExponentialIntegral exponentialIntegralMock, int size) {
        List<IntegrableFunction> subRangesStub = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            subRangesStub.add(exponentialIntegralMock);
        }
        return subRangesStub;
    }

    private List<IntegrableFunction> buildIntegralSubRanges(double[] expectedLowerRanges, double[] expectedUpperRanges) {
        List<IntegrableFunction> integralSubRanges = new ArrayList<>();
        for(int i =0; i<expectedLowerRanges.length; i++) {
            integralSubRanges.add(new ExponentialIntegral(expectedLowerRanges[i], expectedUpperRanges[i]));
        }
        return integralSubRanges;
    }
}
