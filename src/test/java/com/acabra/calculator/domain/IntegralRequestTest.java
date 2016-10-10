package com.acabra.calculator.domain;

import com.acabra.calculator.util.WebCalculatorConstants;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 10/8/2016.
 */
public class IntegralRequestTest {

    @Test
    public void creationAndGettersTest() {
        double lowerBound = 0.0;
        double upperBound = 1.0;
        int repeatedCalculations = 2;
        int numThreads = 1;
        int functionId = 0;
        int approximationMethodId = 0;
        boolean areaInscribed = true;
        IntegralRequest integralRequest = new IntegralRequest(lowerBound, upperBound, repeatedCalculations, numThreads, functionId, approximationMethodId, areaInscribed);

        assertEquals(approximationMethodId, integralRequest.getApproximationMethodId());
        assertEquals(lowerBound, integralRequest.getLowerBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperBound, integralRequest.getUpperBound(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(repeatedCalculations, integralRequest.getRepeatedCalculations());
        assertEquals(numThreads, integralRequest.getNumThreads());
        assertEquals(functionId, integralRequest.getFunctionId());
        assertEquals(areaInscribed, integralRequest.isAreaInscribed());
    }
}
