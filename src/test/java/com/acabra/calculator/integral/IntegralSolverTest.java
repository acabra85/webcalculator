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
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Agustin on 9/30/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({IntegralSolver.class, IntegralSubRangeProvider.class,
        IntegrableFunction.class, IntegralRequest.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class IntegralSolverTest {

    @Test
    public void resolveIntegral1Test() throws Exception {
        double lowerBound1 = 0;
        double upperBound1 = 10;
        int repeatedCalculations = 4;
        int numThreads = 1;
        int functionId = 0;
        double area = 1.0;
        double expectedArea = 4.0;
        double expectedIntegral = 22025.465794806718;
        boolean inscribed = true;

        IntegralRequest integralRequestMock = PowerMockito.mock(IntegralRequest.class);
        IntegrableFunction integralMock = PowerMockito.mock(IntegrableFunction.class);
        IntegralSubRangeProvider integralSubRangeProviderMock = PowerMockito.mock(IntegralSubRangeProvider.class);

        when(integralRequestMock.getLowerBound()).thenReturn(lowerBound1);
        when(integralRequestMock.getUpperBound()).thenReturn(upperBound1);
        when(integralRequestMock.getNumThreads()).thenReturn(numThreads);
        when(integralRequestMock.getRepeatedCalculations()).thenReturn(repeatedCalculations);
        when(integralRequestMock.getFunctionId()).thenReturn(functionId);

        when(integralMock.solveIntegralWithRiemannSequences(eq(inscribed))).thenReturn(area);

        whenNew(IntegralSubRangeProvider.class).withAnyArguments().thenReturn(integralSubRangeProviderMock);
        when(integralSubRangeProviderMock.provideNextIntegral()).thenReturn(integralMock);

        IntegralSolver integralSolver = new IntegralSolver(integralRequestMock);
        IntegrableFunction integrableFunction = integralSolver.approximateSequenceRiemannArea(inscribed).get();

        verify(integralRequestMock, times(1)).getLowerBound();
        verify(integralRequestMock, times(1)).getUpperBound();
        verify(integralRequestMock, times(1)).getNumThreads();
        verify(integralRequestMock, times(1)).getRepeatedCalculations();
        verify(integralRequestMock, times(1)).getFunctionId();

        verify(integralMock, times(repeatedCalculations)).solveIntegralWithRiemannSequences(eq(inscribed));

        verify(integralSubRangeProviderMock, times(repeatedCalculations)).provideNextIntegral();

        assertEquals(lowerBound1, integrableFunction.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperBound1, integrableFunction.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedIntegral, integrableFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedArea, integrableFunction.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[0, 10]", integrableFunction.toString());

    }

    @Test
    public void resolveIntegral2Test() throws Exception {
        double lowerBound1 = -8;
        double upperBound1 = 5;
        int repeatedCalculations = 5;
        int numThreads = 5;
        int functionId = 0;
        double subAreaApproximated = 1.0;
        double expectedResult = 148.4128236399487;
        double expectedApproximateArea = 5.0;
        boolean inscribed = true;

        IntegralRequest integralRequestMock = PowerMockito.mock(IntegralRequest.class);
        IntegrableFunction integralMock = PowerMockito.mock(IntegrableFunction.class);
        IntegralSubRangeProvider integralSubRangeProviderMock = PowerMockito.mock(IntegralSubRangeProvider.class);

        when(integralRequestMock.getLowerBound()).thenReturn(lowerBound1);
        when(integralRequestMock.getUpperBound()).thenReturn(upperBound1);
        when(integralRequestMock.getNumThreads()).thenReturn(numThreads);
        when(integralRequestMock.getRepeatedCalculations()).thenReturn(repeatedCalculations);
        when(integralRequestMock.getFunctionId()).thenReturn(functionId);

        when(integralMock.solveIntegralWithRiemannSequences(eq(inscribed))).thenReturn(subAreaApproximated);

        whenNew(IntegralSubRangeProvider.class).withAnyArguments().thenReturn(integralSubRangeProviderMock);
        when(integralSubRangeProviderMock.provideNextIntegral()).thenReturn(integralMock);

        IntegralSolver integralSolver = new IntegralSolver(integralRequestMock);
        IntegrableFunction integrableFunction = integralSolver.approximateSequenceRiemannArea(inscribed).get();

        verify(integralRequestMock, times(1)).getLowerBound();
        verify(integralRequestMock, times(1)).getUpperBound();
        verify(integralRequestMock, times(1)).getNumThreads();
        verify(integralRequestMock, times(1)).getRepeatedCalculations();
        verify(integralRequestMock, times(1)).getFunctionId();

        verify(integralMock, times(repeatedCalculations)).solveIntegralWithRiemannSequences(inscribed);

        verify(integralSubRangeProviderMock, times(repeatedCalculations)).provideNextIntegral();

        assertEquals(lowerBound1, integrableFunction.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperBound1, integrableFunction.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedResult, integrableFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedApproximateArea, integrableFunction.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[-8, 5]", integrableFunction.toString());

    }

}
