package com.acabra.calculator.util;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.integral.IntegralFunctionFactory;

import java.util.InputMismatchException;

/**
 * Created by Agustin on 9/29/2016.
 */
public class WebCalculatorValidation {
    public static void validateIntegralRequest(IntegralRequest integralRequest) {
        IntegralFunctionFactory.evaluateFunctionType(integralRequest.getFunctionId());
        if (integralRequest.getLowerBound() > integralRequest.getUpperBound())
            throw new InputMismatchException("invalid input lowerboud higher than upper bound");
        if (integralRequest.getNumThreads() > 15 || integralRequest.getNumThreads() < 1)
            throw new InputMismatchException("invalid input num threads must be between [1,15]");
        if (integralRequest.getRepeatedCalculations() < 1)
            throw new InputMismatchException("invalid input repeated calculations must be> 0");

    }
}
