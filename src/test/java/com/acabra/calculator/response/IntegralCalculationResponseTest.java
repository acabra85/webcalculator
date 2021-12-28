package com.acabra.calculator.response;

import com.acabra.calculator.util.WebCalculatorConstants;


import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 10/18/2016.
 */
public class IntegralCalculationResponseTest {

    @Test
    public void createIntegralCalculationResponse() {
        String description = "arithmetic";
        long responseTime = 1L;
        double integralResult = 5.0;
        double result = 7.0;
        String expression = "4 + 2";
        boolean failure = false;
        long id = 1L;

        IntegralCalculationResponse response = new IntegralCalculationResponse(id, failure, expression, result, integralResult, responseTime, description);

        assertEquals(60.0, response.getAccuracy(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(id, response.getId());
        assertEquals(responseTime, response.getResponseTime());
        assertEquals(expression, response.getExpression());
        assertEquals(description, response.getDescription());
        assertEquals(result, Double.valueOf(response.getResult()), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, response.getIntegralResult(), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test
    public void createIntegralCalculation2Response() {
        String description = "arithmetic";
        long responseTime = 1L;
        double integralResult = 5.0;
        double result = 11.0;
        String expression = "4 + 2";
        boolean failure = false;
        long id = 1L;

        IntegralCalculationResponse response = new IntegralCalculationResponse(id, failure, expression, result, integralResult, responseTime, description);

        assertEquals(0.0, response.getAccuracy(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(id, response.getId());
        assertEquals(responseTime, response.getResponseTime());
        assertEquals(expression, response.getExpression());
        assertEquals(description, response.getDescription());
        assertEquals(result, Double.valueOf(response.getResult()), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(integralResult, response.getIntegralResult(), WebCalculatorConstants.ACCURACY_EPSILON);
    }
}
