package com.acabra.calculator.integral;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 9/30/2016.
 */
public class ExponentialIntegralTest {

    @Test
    public void solve1Test() {
        int lowerBound = 0;
        double upperBound = 1;
        double expected = 1.71828;
        ExponentialIntegral unsolvedIntegral = new ExponentialIntegral(lowerBound, upperBound);
        ExponentialIntegral solvedIntegral = new ExponentialIntegral(lowerBound, upperBound, unsolvedIntegral.solve());
        assertEquals(expected, solvedIntegral.getResult(), IntegralSubRangeProvider.accuracyEpsilon);
        assertEquals("integral{e^x}[0.00, 1.00]", solvedIntegral.toString());
    }

    @Test
    public void solve2Test() {
        int lowerBound = -10;
        double upperBound = -9.99;
        double expected = 4.56277E-7;
        ExponentialIntegral unsolvedIntegral = new ExponentialIntegral(lowerBound, upperBound);
        ExponentialIntegral solvedIntegral = new ExponentialIntegral(unsolvedIntegral.getLowerBound(), unsolvedIntegral.getUpperBound(), unsolvedIntegral.solve());

        assertEquals(expected, solvedIntegral.getResult(), IntegralSubRangeProvider.accuracyEpsilon);
        assertEquals("integral{e^x}[-10.00, -9.99]", solvedIntegral.toString());
    }

    @Test
    public void solve3Test() {
        int lowerBound = -10;
        double upperBound = 10;
        double expected = 22026.465749406787;
        ExponentialIntegral unsolvedIntegral = new ExponentialIntegral(lowerBound, upperBound);
        ExponentialIntegral solvedIntegral = new ExponentialIntegral(unsolvedIntegral.getLowerBound(), unsolvedIntegral.getUpperBound(), unsolvedIntegral.solve());

        assertEquals(expected, solvedIntegral.getResult(), IntegralSubRangeProvider.accuracyEpsilon);
        assertEquals("integral{e^x}[-10.00, 10.00]", solvedIntegral.toString());
    }

    @Test
    public void solveArea1Test() {
        int lowerBound = -10;
        double upperBound = 10;
        double expected = 22026.465749406787;
        ExponentialIntegral unsolvedIntegral = new ExponentialIntegral(lowerBound, upperBound);

        assertEquals(expected, unsolvedIntegral.getAreaUnderTheGraph(), IntegralSubRangeProvider.accuracyEpsilon);
        assertEquals("integral{e^x}[-10.00, 10.00]", unsolvedIntegral.toString());
    }
}
