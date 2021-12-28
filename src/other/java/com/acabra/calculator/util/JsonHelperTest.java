package com.acabra.calculator.util;

import com.acabra.calculator.response.CalculationResponse;


api.mockito.PowerMockito;
core.classloader.annotations.PowerMockIgnore;
core.classloader.annotations.PrepareForTest;
modules.junit4.PowerMockRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Agustin on 10/18/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({JsonHelper.class, CalculationResponse.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class JsonHelperTest {

    @Test
    public void serializeAndDeserialize() {
        long id = 1L;
        boolean failure = false;
        String expr = "expr";
        String aprox = "aprox";
        long responseTime = 2L;
        String desc = "desc";
        CalculationResponse calculationResponse = new CalculationResponse(id, failure, expr, aprox, responseTime, desc);

        Optional<CalculationResponse> deserialized = JsonHelper.fromJsonString(JsonHelper.toJsonString(calculationResponse), CalculationResponse.class);
        assertTrue(deserialized.isPresent());

        CalculationResponse actual = deserialized.get();

        assertEquals(aprox, actual.getResult());
        assertEquals(desc, actual.getDescription());
        assertEquals(id, actual.getId());
        assertEquals(responseTime, actual.getResponseTime());
        assertEquals(expr, actual.getExpression());
        assertEquals(failure, actual.isFailure());
    }

    @Test()
    public void failedDeSerializeTest() {
        Optional<CalculationResponse> calculationResponse = JsonHelper.fromJsonString("", CalculationResponse.class);
        assertFalse(calculationResponse.isPresent());
    }

    @Test()
    public void failedSerializeTest() {
        CalculationResponse mockObject = PowerMockito.mock(CalculationResponse.class);
        String calculationResponse = JsonHelper.toJsonString(mockObject);
        assertTrue(calculationResponse.equals("{}"));
    }
}
