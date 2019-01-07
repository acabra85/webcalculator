package com.acabra.calculator.integral.definiteintegral;

import com.acabra.calculator.util.WebCalculatorConstants;
import java.util.InputMismatchException;
import java.util.Optional;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Agustin on 9/30/2016.
 */
public class DefiniteIntegralExponentialTest {

    @Test
    public void solveIntegral1Test() {
        int lowerLimit = 0;
        double upperLimit = 1;
        double expected = 1.718281828459045;
        DefiniteIntegralExponential unsolvedIntegral = new DefiniteIntegralExponential(lowerLimit, upperLimit, Optional.empty(), Optional.empty());
        DefiniteIntegralExponential solvedIntegral = new DefiniteIntegralExponential(lowerLimit, upperLimit, Optional.of(unsolvedIntegral.solve()), Optional.empty());
        assertEquals(expected, solvedIntegral.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[0, 1]", solvedIntegral.toString());
    }

    @Test
    public void solveIntegral2Test() {
        int lowerLimit = -10;
        double upperLimit = -9.99;
        double expected = 4.56277E-7;
        DefiniteIntegralExponential unsolvedIntegral = new DefiniteIntegralExponential(lowerLimit, upperLimit, Optional.empty(), Optional.empty());
        DefiniteIntegralExponential solvedIntegral = new DefiniteIntegralExponential(unsolvedIntegral.getLowerLimit(), unsolvedIntegral.getUpperLimit(), Optional.of(unsolvedIntegral.solve()), Optional.empty());

        assertEquals(expected, solvedIntegral.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[-10, -9.99]", solvedIntegral.toString());
    }

    @Test
    public void solveIntegral3Test() {
        int lowerLimit = -10;
        double upperLimit = 10;
        double expected = 22026.465749406787;
        DefiniteIntegralExponential unsolvedIntegral = new DefiniteIntegralExponential(lowerLimit, upperLimit, Optional.empty(), Optional.empty());
        DefiniteIntegralExponential solvedIntegral = new DefiniteIntegralExponential(unsolvedIntegral.getLowerLimit(), unsolvedIntegral.getUpperLimit(), Optional.of(unsolvedIntegral.solve()), Optional.empty());

        assertEquals(expected, solvedIntegral.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[-10, 10]", solvedIntegral.toString());
    }

    @Test
    public void solveAreaInscribed1Test() {
        double lowerLimit = 0;
        double upperLimit = 5.0;
        double expected = 147.4131591025766;
        DefiniteIntegralExponential unsolvedIntegral = new DefiniteIntegralExponential(lowerLimit, upperLimit, Optional.empty(), Optional.empty());

        assertNull(unsolvedIntegral.getApproximation());
        assertEquals(expected, unsolvedIntegral.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{e^x}[0, 5]", unsolvedIntegral.toString());
    }

    @Test(expected = InputMismatchException.class)
    public void instanceCreationFailTest() {
        int lowerLimit = 0;
        double upperLimit = Double.POSITIVE_INFINITY;
        new DefiniteIntegralExponential(lowerLimit, upperLimit, Optional.empty(), Optional.empty());
    }

    @Test(expected = InputMismatchException.class)
    public void instanceCreationFail2Test() {
        int upperLimit = 0;
        double lowerLimit = Double.POSITIVE_INFINITY;
        new DefiniteIntegralExponential(lowerLimit, upperLimit, Optional.empty(), Optional.empty());
    }
}
