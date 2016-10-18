package com.acabra.calculator.integral.function;

import com.acabra.calculator.util.WebCalculatorConstants;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.InputMismatchException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 10/11/2016.
 */
public class FLogarithmicTest {

    @Test(expected = UnsupportedOperationException.class)
    public void instanceCreationFailTest() {
        double lowerLimit = -0.5;
        double upperLimit = 0.5;
        new FLogarithmic(lowerLimit, upperLimit, Optional.empty(), Optional.empty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void instanceCreationFail1Test() {
        double lowerLimit = 0.0;
        double upperLimit = 0.5;
        new FLogarithmic(lowerLimit, upperLimit, Optional.empty(), Optional.empty());
    }

    @Test(expected = InputMismatchException.class)
    public void instanceCreationFail2Test() {
        double lowerLimit = 0.2;
        double upperLimit = Double.POSITIVE_INFINITY;
        new FLogarithmic(lowerLimit, upperLimit, Optional.empty(), Optional.empty());
    }

    @Test
    public void solveTest() {
        double lowerLimit = 0.2;
        double upperLimit = 1.2;
        double expectedIntegral = -0.4593265;

        FLogarithmic fLogarithmic = new FLogarithmic(lowerLimit, upperLimit, Optional.empty(), Optional.empty());

        Assertions.assertThat(fLogarithmic.getApproximation()).isNull();
        assertEquals(expectedIntegral, fLogarithmic.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(lowerLimit, fLogarithmic.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperLimit, fLogarithmic.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(0.0, fLogarithmic.evaluate(1.0), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(-1.6094379, fLogarithmic.evaluate(0.2), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(1.0, fLogarithmic.evaluate(Math.E), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{ln(x)}[0.2, 1.2]", fLogarithmic.toString());
    }
}
