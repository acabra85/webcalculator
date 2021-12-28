package com.acabra.calculator.util;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.domain.IntegralRequestBuilder;
import com.acabra.calculator.request.IntegralRequestDTO;
import lombok.extern.slf4j.Slf4j;


import java.util.InputMismatchException;

/**
 * Created by Agustin on 9/29/2016.
 */
@Slf4j
public class RequestMapper {

    public static IntegralRequest fromInternalRequest(IntegralRequestDTO integralRequestDTO) {
        try {
            IntegralRequestBuilder builder = new IntegralRequestBuilder();
            int repeatedCalculations = retrieveSafeMaxInteger(Long.parseLong(integralRequestDTO.getRepeatedCalculations()));
            int numThreads = retrieveSafeMaxInteger(Long.parseLong(integralRequestDTO.getNumberThreads()));
            return builder.withLowerBound(Double.parseDouble(integralRequestDTO.getLowerLimit()))
                    .withUpperBound(Double.parseDouble(integralRequestDTO.getUpperLimit()))
                    .withRepeatedCalculations(repeatedCalculations)
                    .withNumThreads(numThreads)
                    .withFunctionId(integralRequestDTO.getFunctionId())
                    .withApproximationMethodId(integralRequestDTO.getApproximationMethodId())
                    .withAreaInscribed(integralRequestDTO.isAreaInscribed())
                    .withCoefficients(integralRequestDTO.getCoefficients())
                    .build();
        } catch (NumberFormatException nfe) {
            logger.error("nfe ", nfe);
            throw new InputMismatchException("unable to retrieve input");
        }
    }

    private static int retrieveSafeMaxInteger(Long longValue) {
        if (longValue >= Integer.MAX_VALUE) {
            logger.info("Input parameters out of range please use valid request");
            return Integer.MAX_VALUE;
        } else if (longValue <= Integer.MIN_VALUE) {
            logger.info("Input parameters out of range please use valid request");
            return Integer.MIN_VALUE;
        }
        return Integer.parseInt(longValue.toString());
    }
}
