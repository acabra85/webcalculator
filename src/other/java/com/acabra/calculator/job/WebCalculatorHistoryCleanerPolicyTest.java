package com.acabra.calculator.job;



core.classloader.annotations.PowerMockIgnore;
core.classloader.annotations.PrepareForTest;
modules.junit4.PowerMockRunner;

import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 10/7/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({WebCalculatorHistoryCleanerPolicy.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class WebCalculatorHistoryCleanerPolicyTest {

    @Test
    public void testPolicyCleaner() {
        ChronoUnit unit = ChronoUnit.MINUTES;
        int expirationInterval = 10;
        WebCalculatorHistoryCleanerPolicy calculatorHistoryCleanerPolicy = new WebCalculatorHistoryCleanerPolicy(unit, expirationInterval);
        assertEquals(expirationInterval, calculatorHistoryCleanerPolicy.getExpirationInterval());
        assertEquals(unit, calculatorHistoryCleanerPolicy.getUnit());
    }
}
