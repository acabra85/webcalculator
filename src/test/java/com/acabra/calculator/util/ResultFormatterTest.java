package com.acabra.calculator.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 10/3/2016.
 */
public class ResultFormatterTest {

    @Test
    public void formatTest() {
        assertEquals("0.456", ResultFormatter.formatResult(0.456));
    }

    @Test
    public void format1Test() {
        assertEquals("0.45600E-6", ResultFormatter.formatResult(0.000000456));
    }

    @Test
    public void format2Test() {
        assertEquals("-0.456", ResultFormatter.formatResult(-0.456));
    }

    @Test
    public void format3Test() {
        assertEquals("0.111112", ResultFormatter.formatResult(0.111111898));
    }

    @Test
    public void format4Test() {
        assertEquals("0", ResultFormatter.formatResult(0.0));
    }

    @Test
    public void trimIntegerResult1Test() {
        assertEquals("10", ResultFormatter.trimIntegerResults("10.0"));
    }

    @Test
    public void formatIntegralRequest1Test() {
        assertEquals("Integ{e^x}[1, 2] #Rep=3 #Th=4", ResultFormatter.formatIntegralRequest("e^x", "1", "2", 3, 4));
    }

    @Test
    public void formatPercentage1Test() {
        assertEquals("99.9447%", ResultFormatter.formatPercentage(99.9447));
    }

    @Test
    public void formatPercentage2Test() {
        assertEquals("99.997%", ResultFormatter.formatPercentage(99.997));
    }

    @Test
    public void formatPercentage3Test() {
        assertEquals("0.00%", ResultFormatter.formatPercentage(0.000000055));
    }

    @Test
    public void formatNanoSeconds1Test() {
        assertEquals("9.05s", ResultFormatter.formatNanoSeconds(9050000000L));
    }

    @Test
    public void formatNanoSeconds2Test() {
        assertEquals("0.004s", ResultFormatter.formatNanoSeconds(4000000L));
    }
}
