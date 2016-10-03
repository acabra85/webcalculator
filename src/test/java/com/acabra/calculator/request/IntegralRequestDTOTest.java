package com.acabra.calculator.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Agustin on 10/3/2016.
 */
public class IntegralRequestDTOTest {

    @Test
    public void creationEmptyTest() {
        IntegralRequestDTO integralRequestDTO = new IntegralRequestDTO();
        int defaultValue = 0;
        assertNull(integralRequestDTO.getLowerBound());
        assertNull(integralRequestDTO.getUpperBound());
        assertNull(integralRequestDTO.getNumberThreads());
        assertNull(integralRequestDTO.getRepeatedCalculations());
        assertEquals(defaultValue, integralRequestDTO.getFunctionId());
    }

    @Test
    public void creationVariablesTest() {
        String lowerBound = "0";
        String upperBound = "1";
        String numberThreads = "2";
        String repeatedCalculations = "3";
        int functionId = 0;
        IntegralRequestDTO integralRequestDTO = new IntegralRequestDTO(lowerBound, upperBound, numberThreads, repeatedCalculations, functionId);
        assertEquals(lowerBound, integralRequestDTO.getLowerBound());
        assertEquals(upperBound, integralRequestDTO.getUpperBound());
        assertEquals(numberThreads, integralRequestDTO.getNumberThreads());
        assertEquals(repeatedCalculations, integralRequestDTO.getRepeatedCalculations());
        assertEquals(functionId, integralRequestDTO.getFunctionId());
    }
}
