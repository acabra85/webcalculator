package com.acabra.calculator.response;

import com.acabra.calculator.integral.ExponentialIntegral;
import com.acabra.calculator.integral.IntegrableFunction;
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
@PrepareForTest({IntegrableFunction.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class WebCalculatorFactoryResponseTest {

    @Test
    public void createCalculationResponse1Test() {
        String description = "Integral";
        String expression = "expression";
        int id = 0;
        long responseTime = 1;
        double approxArea = 1.0;
        double exactIntegral = 1.0;

        IntegrableFunction solvedIntegralMock = PowerMockito.mock(IntegrableFunction.class);
        when(solvedIntegralMock.getSequenceRiemannRectangle()).thenReturn(approxArea);
        when(solvedIntegralMock.getResult()).thenReturn(exactIntegral);

        CalculationResponse calculationResponse = WebCalculatorFactoryResponse.createCalculationResponse(id, expression, responseTime, solvedIntegralMock, description);

        verify(solvedIntegralMock, times(1)).getResult();
        verify(solvedIntegralMock, times(1)).getSequenceRiemannRectangle();

        assertEquals(description, calculationResponse.getDescription());
        assertEquals(expression, calculationResponse.getExpression());
        assertEquals(id, calculationResponse.getId());
        assertEquals(approxArea, calculationResponse.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(responseTime, calculationResponse.getResponseTime());
        assertEquals(exactIntegral, ((IntegralCalculationResponse) calculationResponse).getIntegralResult(), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test
    public void createCalculationResponse2Test() {
        String description = "Arithmetic";
        String expression = "5 + 5";
        int id = 0;
        long responseTime = 1;
        int result = 10;

        CalculationResponse calculationResponse = WebCalculatorFactoryResponse.createCalculationResponse(id, expression, result, responseTime, description);

        assertEquals(id, calculationResponse.getId());
        assertEquals(responseTime, calculationResponse.getResponseTime());
        assertEquals(description, calculationResponse.getDescription());
        assertEquals(expression, calculationResponse.getExpression());
        assertEquals(result, calculationResponse.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
    }
}
