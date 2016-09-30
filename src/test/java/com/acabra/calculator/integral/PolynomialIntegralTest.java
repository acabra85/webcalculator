package com.acabra.calculator.integral;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 9/30/2016.
 */
public class PolynomialIntegralTest {

    @Test
    public void solve1Test() {
        int lowerbound = -2;
        int upperbound = 2;
        int order = 2;
        boolean[] exponents = {false, true};
        double[] coefficients = {0, 2};
        PolynomialIntegral polynomialIntegral = new PolynomialIntegral(lowerbound, upperbound, order, exponents, coefficients);
        assertEquals(0.0, polynomialIntegral.getResult(), IntegralSubRangeProvider.accuracyEpsilon);
    }

    @Test
    public void solveArea1Test() {
        int lowerbound = -2;
        int upperbound = 2;
        int order = 2;
        boolean[] exponents = {false, true};
        double[] coefficients = {0, 2};
        double expectedResult = 8.0;
        PolynomialIntegral polynomialIntegral = new PolynomialIntegral(lowerbound, upperbound, order, exponents, coefficients);
        assertEquals(expectedResult, polynomialIntegral.getAreaUnderTheGraph(), IntegralSubRangeProvider.accuracyEpsilon);
    }
}
