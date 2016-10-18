package com.acabra.calculator.response;

import com.acabra.calculator.integral.function.IntegrableFunction;

/**
 * Created by Agustin on 10/5/2016.
 */
public class WebCalculatorFactoryResponse {

    public static CalculationResponse createCalculationResponse(long id, String expression, long responseTime, IntegrableFunction solvedIntegral, String description) {
        return new IntegralCalculationResponse(id, false, expression, solvedIntegral.getApproximation(), solvedIntegral.getResult(), responseTime, description);
    }

    public static CalculationResponse createCalculationResponse(long id, String expression, String result, long responseTime, String arithmetic) {
        return new CalculationResponse(id, true, expression, result, responseTime, arithmetic);
    }

    public static CalculationResponse createFailedCalculationResponse(long id, String expression, long responseTime, String message) {
        return new CalculationResponse(id, true, expression, "NaN", responseTime, message);
    }
}
