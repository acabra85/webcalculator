package com.acabra.calculator.util;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.domain.IntegralRequestBuilder;
import com.acabra.calculator.request.IntegralRequestDTO;
import org.apache.log4j.Logger;

import java.util.InputMismatchException;

/**
 * Created by Agustin on 9/29/2016.
 */
public class RequestMapper {

    private static final Logger logger = Logger.getLogger(RequestMapper.class);

    public static IntegralRequest fromInternalRequest(IntegralRequestDTO integralRequestDTO) {
        try {
            IntegralRequestBuilder builder = new IntegralRequestBuilder();
            int repeatedCalculations = retrieveSafeMaxInteger(Long.parseLong(integralRequestDTO.getRepeatedCalculations()));
            int numThreads = retrieveSafeMaxInteger(Long.parseLong(integralRequestDTO.getNumberThreads()));
            return builder.withLowerBound(Double.parseDouble(integralRequestDTO.getLowerBound()))
                    .withUpperBound(Double.parseDouble(integralRequestDTO.getUpperBound()))
                    .withRepeatedCalculations(repeatedCalculations)
                    .withNumThreads(numThreads)
                    .withFunctionId(integralRequestDTO.getFunctionId())
                    .withApproximationMethodId(integralRequestDTO.getApproximationMethodId())
                    .withAreaInscribed(integralRequestDTO.isAreaInscribed())
                    .withCoefficients(integralRequestDTO.getCoefficients())
                    .build();
        } catch (NumberFormatException nfe) {
            logger.error(nfe);
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
