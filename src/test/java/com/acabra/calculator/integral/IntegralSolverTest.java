package com.acabra.calculator.integral;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.integral.approx.RiemannSolver;
import com.acabra.calculator.integral.approx.SimpsonSolver;
import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralInverse;
import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralFunction;
import com.acabra.calculator.integral.definiteintegral.IntegrableFunctionType;
import com.acabra.calculator.util.WebCalculatorConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by Agustin on 9/30/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SimpsonSolver.class, RiemannSolver.class, IntegralSolver.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class IntegralSolverTest {

    private static final List<Double> EMPTY = Collections.emptyList();

    @Test
    public void approximateAreaUnderCurveSimpsonTest() throws Exception {
        double lowerLimit = 1.0;
        double upperLimit = 2.0;
        int repeatedCalculations = 10;
        int numThreads = 1;
        int functionId = 3;
        int approximationMethodId = 1;
        boolean areaInscribed = false;
        double approximation = 0.69351;
        IntegralRequest requestStub = new IntegralRequest(lowerLimit, upperLimit, repeatedCalculations, numThreads, functionId, approximationMethodId, areaInscribed, EMPTY);
        CompletableFuture<DefiniteIntegralFunction> functionStub = CompletableFuture.completedFuture(new DefiniteIntegralInverse(lowerLimit, upperLimit, Optional.empty(), Optional.of(approximation)));

        SimpsonSolver simpsonMock = Mockito.mock(SimpsonSolver.class);
        PowerMockito.when(simpsonMock.approximate(eq(repeatedCalculations), any(ExecutorService.class))).thenReturn(functionStub);

        PowerMockito.whenNew(SimpsonSolver.class).withAnyArguments().thenReturn(simpsonMock);

        IntegralSolver is = new IntegralSolver(requestStub);
        DefiniteIntegralFunction definiteIntegralFunction = is.approximateAreaUnderCurve().get();

        PowerMockito.verifyNew(SimpsonSolver.class).withArguments(any(), any(), any(), any(IntegrableFunctionType.class));
        Mockito.verify(simpsonMock, times(1)).approximate(eq(repeatedCalculations), any(ExecutorService.class));

        assertEquals(approximation, definiteIntegralFunction.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test
    public void approximateAreaUnderCurveRiemannTest() throws Exception {
        double lowerLimit = 1.0;
        double upperLimit = 2.0;
        int repeatedCalculations = 10;
        int numThreads = 1;
        int functionId = 3;
        int approximationMethodId = 0;
        boolean areaInscribed = false;
        double approximation = 0.69351;
        IntegralRequest requestStub = new IntegralRequest(lowerLimit, upperLimit, repeatedCalculations, numThreads, functionId, approximationMethodId, areaInscribed, EMPTY);
        CompletableFuture<DefiniteIntegralFunction> functionStub = CompletableFuture.completedFuture(new DefiniteIntegralInverse(lowerLimit, upperLimit, Optional.empty(), Optional.of(approximation)));

        RiemannSolver riemannMock = Mockito.mock(RiemannSolver.class);
        PowerMockito.when(riemannMock.approximate(eq(repeatedCalculations), any(ExecutorService.class))).thenReturn(functionStub);

        PowerMockito.whenNew(RiemannSolver.class).withAnyArguments().thenReturn(riemannMock);

        IntegralSolver is = new IntegralSolver(requestStub);
        DefiniteIntegralFunction definiteIntegralFunction = is.approximateAreaUnderCurve().get();

        PowerMockito.verifyNew(RiemannSolver.class).withArguments(any(), any(), any(), any(IntegrableFunctionType.class), anyBoolean());
        Mockito.verify(riemannMock, times(1)).approximate(eq(repeatedCalculations), any(ExecutorService.class));

        assertEquals(approximation, definiteIntegralFunction.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test(expected = NoSuchElementException.class)
    public void approximateAreaUnderUnknownMethodTest() throws Exception {
        double lowerLimit = 1.0;
        double upperLimit = 2.0;
        int repeatedCalculations = 10;
        int numThreads = 1;
        int functionId = 3;
        int approximationMethodId = 2;
        boolean areaInscribed = false;
        double approximation = 0.69351;
        IntegralRequest requestStub = new IntegralRequest(lowerLimit, upperLimit, repeatedCalculations, numThreads, functionId, approximationMethodId, areaInscribed, EMPTY);
        CompletableFuture<DefiniteIntegralFunction> functionStub = CompletableFuture.completedFuture(new DefiniteIntegralInverse(lowerLimit, upperLimit, Optional.empty(), Optional.of(approximation)));

        RiemannSolver riemannMock = Mockito.mock(RiemannSolver.class);
        PowerMockito.when(riemannMock.approximate(eq(repeatedCalculations), any(ExecutorService.class))).thenReturn(functionStub);

        PowerMockito.whenNew(RiemannSolver.class).withAnyArguments().thenReturn(riemannMock);

        IntegralSolver integralSolver = new IntegralSolver(requestStub);
        integralSolver.approximateAreaUnderCurve().get();
    }


    @Test
    public void approximateAreaUnderCurveEqualLimitsTest() throws Exception {
        double lowerLimit = 1.0;
        double upperLimit = 1.0;
        int repeatedCalculations = 10;
        int numThreads = 1;
        int functionId = 3;
        int approximationMethodId = 0;
        boolean areaInscribed = false;
        IntegralRequest requestStub = new IntegralRequest(lowerLimit, upperLimit, repeatedCalculations, numThreads, functionId, approximationMethodId, areaInscribed, EMPTY);

        IntegralSolver is = new IntegralSolver(requestStub);
        DefiniteIntegralFunction definiteIntegralFunction = is.approximateAreaUnderCurve().get();

        assertEquals(0.0, definiteIntegralFunction.getApproximation(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(0.0, definiteIntegralFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(lowerLimit, definiteIntegralFunction.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperLimit, definiteIntegralFunction.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
    }

}
