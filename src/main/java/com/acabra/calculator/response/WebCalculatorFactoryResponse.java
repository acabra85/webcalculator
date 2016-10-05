package com.acabra.calculator.response;

import com.acabra.calculator.integral.IntegrableFunction;

/**
 * Created by Agustin on 10/5/2016.
 */
public class WebCalculatorFactoryResponse {

    public static CalculationResponse createCalculationResponse(long id, String expression, long responseTime, IntegrableFunction solvedIntegral, String description) {
        return new IntegralCalculationResponse(id, expression, solvedIntegral.getSequenceRiemannRectangle(), solvedIntegral.getResult(), responseTime, description);
    }

    public static CalculationResponse createCalculationResponse(long id, String expression, double result, long responseTime, String arithmetic) {
        return new CalculationResponse(id, expression, result, responseTime, arithmetic);
    }
}
