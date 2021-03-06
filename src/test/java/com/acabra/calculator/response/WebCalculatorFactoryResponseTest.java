package com.acabra.calculator.response;

import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralFunction;
import com.acabra.calculator.util.WebCalculatorConstants;
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
 * Created by Agustin on 10/5/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DefiniteIntegralFunction.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class WebCalculatorFactoryResponseTest {

    @Test
    public void createIntegralCalculationResponse1Test() {
        String description = "Integral";
        String expression = "expression";
        int id = 0;
        long responseTime = 1;
        double approxArea = 1.0;
        double exactIntegral = 1.0;
        double expectedAccuracy = 100.0;

        DefiniteIntegralFunction solvedIntegralMock = PowerMockito.mock(DefiniteIntegralFunction.class);
        when(solvedIntegralMock.getApproximation()).thenReturn(approxArea);
        when(solvedIntegralMock.getResult()).thenReturn(exactIntegral);

        IntegralCalculationResponse integralCalculationResponse = (IntegralCalculationResponse) WebCalculatorFactoryResponse.createCalculationResponse(id, expression, responseTime, solvedIntegralMock, description);

        verify(solvedIntegralMock, times(1)).getResult();
        verify(solvedIntegralMock, times(1)).getApproximation();

        assertEquals(description, integralCalculationResponse.getDescription());
        assertEquals(expression, integralCalculationResponse.getExpression());
        assertEquals(id, integralCalculationResponse.getId());
        assertEquals(approxArea+"", integralCalculationResponse.getResult());
        assertEquals(responseTime, integralCalculationResponse.getResponseTime());
        assertEquals(exactIntegral, integralCalculationResponse.getIntegralResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedAccuracy, integralCalculationResponse.getAccuracy(), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test
    public void createIntegralCalculationResponse2Test() {
        String description = "Integral";
        String expression = "expression";
        int id = 0;
        long responseTime = 1;
        double approxArea = 1.0;
        double exactIntegral = 2.0;
        double expectedAccuracy = 50.0;

        DefiniteIntegralFunction solvedIntegralMock = PowerMockito.mock(DefiniteIntegralFunction.class);
        when(solvedIntegralMock.getApproximation()).thenReturn(approxArea);
        when(solvedIntegralMock.getResult()).thenReturn(exactIntegral);

        IntegralCalculationResponse integralCalculationResponse = (IntegralCalculationResponse) WebCalculatorFactoryResponse.createCalculationResponse(id, expression, responseTime, solvedIntegralMock, description);

        verify(solvedIntegralMock, times(1)).getResult();
        verify(solvedIntegralMock, times(1)).getApproximation();

        assertEquals(description, integralCalculationResponse.getDescription());
        assertEquals(expression, integralCalculationResponse.getExpression());
        assertEquals(id, integralCalculationResponse.getId());
        assertEquals(approxArea+"", integralCalculationResponse.getResult());
        assertEquals(responseTime, integralCalculationResponse.getResponseTime());
        assertEquals(exactIntegral, integralCalculationResponse.getIntegralResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedAccuracy, integralCalculationResponse.getAccuracy(), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test
    public void createCalculationResponse2Test() {
        String description = "Arithmetic";
        String expression = "5 + 5";
        int id = 0;
        long responseTime = 1;
        int result = 10;

        CalculationResponse calculationResponse = WebCalculatorFactoryResponse.createCalculationResponse(id, expression, result+"", responseTime, description);

        assertEquals(id, calculationResponse.getId());
        assertEquals(responseTime, calculationResponse.getResponseTime());
        assertEquals(description, calculationResponse.getDescription());
        assertEquals(expression, calculationResponse.getExpression());
        assertEquals(result+"", calculationResponse.getResult());
    }

    @Test
    public void createFailedCalculationResponseTest() {
        String description = "failed";
        String expression = "5 +-/ 5";
        int id = 0;
        long responseTime = 1;
        String result = "NaN";

        CalculationResponse calculationResponse = WebCalculatorFactoryResponse.createFailedCalculationResponse(id, expression, responseTime, description);

        assertEquals(id, calculationResponse.getId());
        assertEquals(responseTime, calculationResponse.getResponseTime());
        assertEquals(description, calculationResponse.getDescription());
        assertEquals(expression, calculationResponse.getExpression());
        assertEquals(result, calculationResponse.getResult());
    }
}
