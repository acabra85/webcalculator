package com.acabra.calculator.integral;

import com.acabra.calculator.domain.IntegralRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Agustin on 9/30/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConcurrentIntegralSolver.class, IntegralSubRangeProvider.class, IntegrableFunction.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class ConcurrentIntegralSolverTest {

    @Test
    public void resolveIntegral1Test() throws ExecutionException, InterruptedException {
        double lowerBound1 = 0;
        double upperBound1 = 10;
        double[] expectedLowerRanges = {lowerBound1, 2.5, 5.0, 7.5};
        double[] expectedUpperRanges = {2.5, 5.0, 7.5, upperBound1};
        int repeatedCalculations = 4;
        int numThreads = 1;
        int functionId = 0;

        double expectedResult = 22025.465794;
        List<IntegrableFunction> integralSubRangesList = buildIntegralSubRanges(expectedLowerRanges, expectedUpperRanges);
        PowerMockito.mockStatic(IntegralSubRangeProvider.class);
        when(IntegralSubRangeProvider.provideIntegralsOnSubRanges(eq(lowerBound1), eq(upperBound1), eq(repeatedCalculations), eq(IntegralFunctionType.EXPONENTIAL)))
                .thenReturn(integralSubRangesList);

        IntegralRequest integralRequest = new IntegralRequest(lowerBound1, upperBound1, repeatedCalculations, numThreads, functionId);
        ConcurrentIntegralSolver concurrentIntegralSolver = new ConcurrentIntegralSolver(integralRequest);
        IntegrableFunction integrableFunction = concurrentIntegralSolver.resolveIntegral().get();

        verifyStatic(times(1));
        assertEquals(lowerBound1, integrableFunction.getLowerBound(), IntegralSubRangeProvider.accuracyEpsilon);
        assertEquals(upperBound1, integrableFunction.getUpperBound(), IntegralSubRangeProvider.accuracyEpsilon);
        assertEquals(expectedResult, integrableFunction.getResult(), IntegralSubRangeProvider.accuracyEpsilon);

    }

    @Test
    public void resolveIntegral2Test() throws ExecutionException, InterruptedException {
        double lowerBound1 = 0;
        double upperBound1 = 10;
        int repeatedCalculations = 4;
        int numThreads = 1;
        int functionId = 0;

        double desiredReturn = 1.0;
        double expectedResult = 4.0;
        ExponentialIntegral exponentialIntegralMock = PowerMockito.mock(ExponentialIntegral.class);
        when(exponentialIntegralMock.solve()).thenReturn(desiredReturn);
        List<IntegrableFunction> integralSubRangesList = buildIntegralSubRangesMocks(exponentialIntegralMock);

        PowerMockito.mockStatic(IntegralSubRangeProvider.class);
        when(IntegralSubRangeProvider.provideIntegralsOnSubRanges(eq(lowerBound1), eq(upperBound1), eq(repeatedCalculations), eq(IntegralFunctionType.EXPONENTIAL)))
                .thenReturn(integralSubRangesList);

        IntegralRequest integralRequest = new IntegralRequest(lowerBound1, upperBound1, repeatedCalculations, numThreads, functionId);
        ConcurrentIntegralSolver concurrentIntegralSolver = new ConcurrentIntegralSolver(integralRequest);
        IntegrableFunction integrableFunction = concurrentIntegralSolver.resolveIntegral().get();
        verifyStatic(times(1));

        assertEquals(lowerBound1, integrableFunction.getLowerBound(), IntegralSubRangeProvider.accuracyEpsilon);
        assertEquals(upperBound1, integrableFunction.getUpperBound(), IntegralSubRangeProvider.accuracyEpsilon);
        assertEquals(expectedResult, integrableFunction.getResult(), IntegralSubRangeProvider.accuracyEpsilon);
        assertEquals("integral{e^x}[0.00, 10.00]", integrableFunction.toString());

    }

    private List<IntegrableFunction> buildIntegralSubRangesMocks(ExponentialIntegral exponentialIntegralMock) {
        return Arrays.asList(exponentialIntegralMock, exponentialIntegralMock, exponentialIntegralMock, exponentialIntegralMock);
    }

    private List<IntegrableFunction> buildIntegralSubRanges(double[] expectedLowerRanges, double[] expectedUpperRanges) {
        List<IntegrableFunction> integralSubRanges = new ArrayList<>();

        for(int i =0; i<expectedLowerRanges.length; i++) {
            integralSubRanges.add(new ExponentialIntegral(expectedLowerRanges[i], expectedUpperRanges[i]));
        }
        return integralSubRanges;
    }
}
