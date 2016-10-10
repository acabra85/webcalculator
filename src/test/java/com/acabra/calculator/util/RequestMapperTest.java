package com.acabra.calculator.util;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.request.IntegralRequestDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Agustin on 9/30/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({IntegralRequestDTO.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class RequestMapperTest {

    @Test
    public void mapTest() {
        String lowerBoundStr = "0";
        String upperBoundStr = "1";
        String numThreadsStr = "2";
        String repeatedCalculationsStr = "3";
        double lowerBound = 0;
        double upperBound = 1;
        double numThreads = 2;
        double repeatedCalculations = 3;
        int functionId = 0;
        int approximationMethodId = 0;

        IntegralRequestDTO dtoRequestMock = PowerMockito.mock(IntegralRequestDTO.class);

        when(dtoRequestMock.getLowerBound()).thenReturn(lowerBoundStr);
        when(dtoRequestMock.getUpperBound()).thenReturn(upperBoundStr);
        when(dtoRequestMock.getNumberThreads()).thenReturn(numThreadsStr);
        when(dtoRequestMock.getRepeatedCalculations()).thenReturn(repeatedCalculationsStr);
        when(dtoRequestMock.getFunctionId()).thenReturn(functionId);
        when(dtoRequestMock.getApproximationMethodId()).thenReturn(approximationMethodId);

        IntegralRequest mappedRequest = RequestMapper.fromInternalRequest(dtoRequestMock);

        verify(dtoRequestMock, times(1)).getLowerBound();
        verify(dtoRequestMock, times(1)).getUpperBound();
        verify(dtoRequestMock, times(1)).getNumberThreads();
        verify(dtoRequestMock, times(1)).getRepeatedCalculations();
        verify(dtoRequestMock, times(1)).getFunctionId();
        verify(dtoRequestMock, times(1)).getApproximationMethodId();

        assertEquals(lowerBound, mappedRequest.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperBound, mappedRequest.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(numThreads, mappedRequest.getNumThreads(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(repeatedCalculations, mappedRequest.getRepeatedCalculations(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(functionId, mappedRequest.getFunctionId());
        assertEquals(approximationMethodId, mappedRequest.getApproximationMethodId());
    }


    @Test
    public void mapLimitsTest() {
        String lowerBoundStr = "0";
        String upperBoundStr = "1";
        String numThreadsStr = "-2147483700";
        String repeatedCalculationsStr = "2147483800";
        double lowerBound = 0;
        double upperBound = 1;
        int numThreads = -2147483648;
        double repeatedCalculations = 2147483647;
        int functionId = 0;
        int approximationMethodId = 0;

        IntegralRequestDTO dtoRequestMock = PowerMockito.mock(IntegralRequestDTO.class);

        when(dtoRequestMock.getLowerBound()).thenReturn(lowerBoundStr);
        when(dtoRequestMock.getUpperBound()).thenReturn(upperBoundStr);
        when(dtoRequestMock.getNumberThreads()).thenReturn(numThreadsStr);
        when(dtoRequestMock.getRepeatedCalculations()).thenReturn(repeatedCalculationsStr);
        when(dtoRequestMock.getFunctionId()).thenReturn(functionId);
        when(dtoRequestMock.getApproximationMethodId()).thenReturn(approximationMethodId);

        IntegralRequest mappedRequest = RequestMapper.fromInternalRequest(dtoRequestMock);

        verify(dtoRequestMock, times(1)).getLowerBound();
        verify(dtoRequestMock, times(1)).getUpperBound();
        verify(dtoRequestMock, times(1)).getNumberThreads();
        verify(dtoRequestMock, times(1)).getRepeatedCalculations();
        verify(dtoRequestMock, times(1)).getFunctionId();
        verify(dtoRequestMock, times(1)).getApproximationMethodId();

        assertEquals(lowerBound, mappedRequest.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperBound, mappedRequest.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(numThreads, mappedRequest.getNumThreads(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(repeatedCalculations, mappedRequest.getRepeatedCalculations(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(functionId, mappedRequest.getFunctionId());
        assertEquals(approximationMethodId, mappedRequest.getApproximationMethodId());
    }
}
