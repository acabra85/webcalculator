package com.acabra.calculator.integral;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.util.WebCalculatorConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Agustin on 9/30/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({IntegralSolver.class, IntegralSubRangeSupplier.class,
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
        int approximationMethodId = 0;

        IntegralRequest integralRequestMock = PowerMockito.mock(IntegralRequest.class);
        IntegrableFunction integralMock = PowerMockito.mock(IntegrableFunction.class);
        IntegralSubRangeSupplier integralSubRangeSupplierMock = PowerMockito.mock(IntegralSubRangeSupplier.class);

        when(integralRequestMock.getLowerBound()).thenReturn(lowerBound1);
        when(integralRequestMock.getUpperBound()).thenReturn(upperBound1);
        when(integralRequestMock.getNumThreads()).thenReturn(numThreads);
        when(integralRequestMock.getRepeatedCalculations()).thenReturn(repeatedCalculations);
        when(integralRequestMock.getFunctionId()).thenReturn(functionId);
        when(integralRequestMock.getApproximationMethodId()).thenReturn(approximationMethodId);
        when(integralRequestMock.isAreaInscribed()).thenReturn(inscribed);

        when(integralMock.solveIntegralWithRiemannSequences(eq(inscribed))).thenReturn(area);

        whenNew(IntegralSubRangeSupplier.class).withAnyArguments().thenReturn(integralSubRangeSupplierMock);
        when(integralSubRangeSupplierMock.get()).thenReturn(integralMock);

        IntegralSolver integralSolver = new IntegralSolver(integralRequestMock);
        IntegrableFunction integrableFunction = integralSolver.approximateAreaUnderCurve().get();

        verify(integralRequestMock, times(1)).getLowerBound();
        verify(integralRequestMock, times(1)).getUpperBound();
        verify(integralRequestMock, times(1)).getNumThreads();
        verify(integralRequestMock, times(1)).getRepeatedCalculations();
        verify(integralRequestMock, times(1)).getFunctionId();
        verify(integralRequestMock, times(1)).getApproximationMethodId();
        verify(integralRequestMock, times(1)).isAreaInscribed();

        verify(integralMock, times(repeatedCalculations)).solveIntegralWithRiemannSequences(eq(inscribed));

        verify(integralSubRangeSupplierMock, times(repeatedCalculations)).get();

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
        int approximationMethodId = 0;
        boolean inscribed = true;

        IntegralRequest integralRequestMock = PowerMockito.mock(IntegralRequest.class);
        IntegrableFunction integralMock = PowerMockito.mock(IntegrableFunction.class);
        IntegralSubRangeSupplier integralSubRangeSupplierMock = PowerMockito.mock(IntegralSubRangeSupplier.class);

        when(integralRequestMock.getLowerBound()).thenReturn(lowerBound1);
        when(integralRequestMock.getUpperBound()).thenReturn(upperBound1);
        when(integralRequestMock.getNumThreads()).thenReturn(numThreads);
        when(integralRequestMock.getRepeatedCalculations()).thenReturn(repeatedCalculations);
        when(integralRequestMock.getFunctionId()).thenReturn(functionId);
        when(integralRequestMock.getApproximationMethodId()).thenReturn(approximationMethodId);
        when(integralRequestMock.isAreaInscribed()).thenReturn(inscribed);

        when(integralMock.solveIntegralWithRiemannSequences(eq(inscribed))).thenReturn(subAreaApproximated);

        whenNew(IntegralSubRangeSupplier.class).withAnyArguments().thenReturn(integralSubRangeSupplierMock);
        when(integralSubRangeSupplierMock.get()).thenReturn(integralMock);

        IntegralSolver integralSolver = new IntegralSolver(integralRequestMock);
        IntegrableFunction integrableFunction = integralSolver.approximateAreaUnderCurve().get();
        verify(integralRequestMock, times(1)).getLowerBound();
        verify(integralRequestMock, times(1)).getUpperBound();
        verify(integralRequestMock, times(1)).getNumThreads();
        verify(integralRequestMock, times(1)).getRepeatedCalculations();
        verify(integralRequestMock, times(1)).getFunctionId();
        verify(integralRequestMock, times(1)).getApproximationMethodId();
        verify(integralRequestMock, times(1)).isAreaInscribed();

        verify(integralMock, times(repeatedCalculations)).solveIntegralWithRiemannSequences(inscribed);

        verify(integralSubRangeSupplierMock, times(repeatedCalculations)).get();

        assertEquals(lowerBound1, integrableFunction.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperBound1, integrableFunction.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedResult, integrableFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedApproximateArea, integrableFunction.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[-8, 5]", integrableFunction.toString());

    }

    @Test
    public void resolveIntegral3Test() throws Exception {
        double lowerBound1 = 0;
        double upperBound1 = 6;
        int repeatedCalculations = 1;
        int numThreads = 5;
        int functionId = 0;
        double expectedResult = 402.4287934927351;
        double expectedApproximateArea = 6.0;
        int approximationId = 0;
        boolean inscribed = true;

        IntegralRequest integralRequestMock = PowerMockito.mock(IntegralRequest.class);
        IntegrableFunction expFunction = new FExponential(lowerBound1, upperBound1, null, null);
        IntegralSubRangeSupplier integralSubRangeSupplierMock = PowerMockito.mock(IntegralSubRangeSupplier.class);

        when(integralRequestMock.getLowerBound()).thenReturn(lowerBound1);
        when(integralRequestMock.getUpperBound()).thenReturn(upperBound1);
        when(integralRequestMock.getNumThreads()).thenReturn(numThreads);
        when(integralRequestMock.getRepeatedCalculations()).thenReturn(repeatedCalculations);
        when(integralRequestMock.getFunctionId()).thenReturn(functionId);
        when(integralRequestMock.getFunctionId()).thenReturn(approximationId);
        when(integralRequestMock.isAreaInscribed()).thenReturn(inscribed);


        whenNew(IntegralSubRangeSupplier.class).withAnyArguments().thenReturn(integralSubRangeSupplierMock);
        when(integralSubRangeSupplierMock.get()).thenReturn(expFunction);

        IntegralSolver integralSolver = new IntegralSolver(integralRequestMock);
        IntegrableFunction integrableFunction = integralSolver.approximateAreaUnderCurve().get();
        verify(integralRequestMock, times(1)).getLowerBound();
        verify(integralRequestMock, times(1)).getUpperBound();
        verify(integralRequestMock, times(1)).getNumThreads();
        verify(integralRequestMock, times(1)).getRepeatedCalculations();
        verify(integralRequestMock, times(1)).getFunctionId();
        verify(integralRequestMock, times(1)).getApproximationMethodId();
        verify(integralRequestMock, times(1)).isAreaInscribed();

        verify(integralSubRangeSupplierMock, times(repeatedCalculations)).get();

        assertEquals(lowerBound1, integrableFunction.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperBound1, integrableFunction.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedResult, integrableFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedApproximateArea, integrableFunction.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[0, 6]", integrableFunction.toString());

    }

    @Test
    public void resolveIntegral4Test() throws Exception {
        double lowerBound1 = 5;
        double upperBound1 = 5;
        int repeatedCalculations = 1;
        int numThreads = 5;
        int functionId = 0;
        double expectedResult = 0.0;
        double expectedApproximateArea = 0.0;
        boolean inscribed = false;
        int approximationMethodId = 0;

        IntegralRequest integralRequestMock = PowerMockito.mock(IntegralRequest.class);

        when(integralRequestMock.getLowerBound()).thenReturn(lowerBound1);
        when(integralRequestMock.getUpperBound()).thenReturn(upperBound1);
        when(integralRequestMock.getNumThreads()).thenReturn(numThreads);
        when(integralRequestMock.getRepeatedCalculations()).thenReturn(repeatedCalculations);
        when(integralRequestMock.getFunctionId()).thenReturn(functionId);
        when(integralRequestMock.getFunctionId()).thenReturn(approximationMethodId);
        when(integralRequestMock.isAreaInscribed()).thenReturn(inscribed);

        IntegralSolver integralSolver = new IntegralSolver(integralRequestMock);

        IntegrableFunction integrableFunction = integralSolver.approximateAreaUnderCurve().get();

        verify(integralRequestMock, times(1)).getLowerBound();
        verify(integralRequestMock, times(1)).getUpperBound();
        verify(integralRequestMock, times(1)).getNumThreads();
        verify(integralRequestMock, times(1)).getRepeatedCalculations();
        verify(integralRequestMock, times(1)).getFunctionId();
        verify(integralRequestMock, times(1)).getApproximationMethodId();
        verify(integralRequestMock, times(1)).isAreaInscribed();

        assertEquals(lowerBound1, integrableFunction.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperBound1, integrableFunction.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedResult, integrableFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedApproximateArea, integrableFunction.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[5, 5]", integrableFunction.toString());
    }

}
