package com.acabra.calculator.util;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.request.IntegralRequestDTO;
import org.apache.log4j.Logger;

import java.util.InputMismatchException;

/**
 * Created by Agustin on 9/29/2016.
 */
public class RequestMapper {

    private static final Logger logger = Logger.getLogger(RequestMapper.class);

    public static IntegralRequest fromInternalRequest(IntegralRequestDTO integralRequest) {
        try {
            IntegralRequest.IntegralRequestBuilder builder = new IntegralRequest.IntegralRequestBuilder();
            return builder.withLowerBound(Double.parseDouble(integralRequest.getLowerBound()))
                    .withUpperBound(Double.parseDouble(integralRequest.getUpperBound()))
                    .withRepeatedCalculations(Integer.parseInt(integralRequest.getRepeatedCalculations()))
                    .withNumThreads(Integer.parseInt(integralRequest.getNumberThreads()))
                    .withFunctionId(integralRequest.getFunctionId())
                    .build();
        } catch (NumberFormatException nfe) {
            logger.error(nfe);
            throw new InputMismatchException("unable to retrieve input");
        }
    }
}
