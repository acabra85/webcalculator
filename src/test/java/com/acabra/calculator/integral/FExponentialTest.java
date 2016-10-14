package com.acabra.calculator.integral;

import com.acabra.calculator.util.WebCalculatorConstants;
import org.junit.Test;

import java.util.InputMismatchException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 9/30/2016.
 */
public class FExponentialTest {

    @Test
    public void solveIntegral1Test() {
        int lowerBound = 0;
        double upperBound = 1;
        double expected = 1.718281828459045;
        FExponential unsolvedIntegral = new FExponential(lowerBound, upperBound, null, null);
        FExponential solvedIntegral = new FExponential(lowerBound, upperBound, unsolvedIntegral.solve(), null);
        assertEquals(expected, solvedIntegral.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[0, 1]", solvedIntegral.toString());
    }

    @Test
    public void solveIntegral2Test() {
        int lowerBound = -10;
        double upperBound = -9.99;
        double expected = 4.56277E-7;
        FExponential unsolvedIntegral = new FExponential(lowerBound, upperBound, null, null);
        FExponential solvedIntegral = new FExponential(unsolvedIntegral.getLowerBound(), unsolvedIntegral.getUpperBound(), unsolvedIntegral.solve(), null);

        assertEquals(expected, solvedIntegral.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[-10, -9.99]", solvedIntegral.toString());
    }

    @Test
    public void solveIntegral3Test() {
        int lowerBound = -10;
        double upperBound = 10;
        double expected = 22026.465749406787;
        FExponential unsolvedIntegral = new FExponential(lowerBound, upperBound, null, null);
        FExponential solvedIntegral = new FExponential(unsolvedIntegral.getLowerBound(), unsolvedIntegral.getUpperBound(), unsolvedIntegral.solve(), null);

        assertEquals(expected, solvedIntegral.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[-10, 10]", solvedIntegral.toString());
    }

    @Test
    public void solveAreaInscribed1Test() {
        int lowerBound = 0;
        double upperBound = 5.0;
        double expected = 5.0;
        FExponential unsolvedIntegral = new FExponential(lowerBound, upperBound, null, null);

        assertEquals(expected, unsolvedIntegral.getSequenceRiemannRectangle(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[0, 5]", unsolvedIntegral.toString());
    }

    @Test
    public void solveAreaCircumscribed1Test() {
        int lowerBound = 0;
        double upperBound = 5.0;
        double expected = 742.0657955;
        FExponential unsolvedIntegral = new FExponential(lowerBound, upperBound, null, null);

        assertEquals(expected, unsolvedIntegral.calculateRiemannSequenceRectangleArea(false), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[0, 5]", unsolvedIntegral.toString());
    }

    @Test(expected = InputMismatchException.class)
    public void instanceCreationFailTest() {
        int lowerBound = 0;
        double upperBound = Double.POSITIVE_INFINITY;
        new FExponential(lowerBound, upperBound, null, null);
    }

    @Test(expected = InputMismatchException.class)
    public void instanceCreationFail2Test() {
        int upperBound = 0;
        double lowerBound = Double.POSITIVE_INFINITY;
        new FExponential(lowerBound, upperBound, null, null);
    }
}
