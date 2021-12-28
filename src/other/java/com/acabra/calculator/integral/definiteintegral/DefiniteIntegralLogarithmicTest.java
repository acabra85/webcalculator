package com.acabra.calculator.integral.definiteintegral;

import com.acabra.calculator.util.WebCalculatorConstants;
import org.assertj.core.api.Assertions;


import java.util.InputMismatchException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 10/11/2016.
 */
public class DefiniteIntegralLogarithmicTest {

    @Test(expected = UnsupportedOperationException.class)
    public void instanceCreationFailTest() {
        double lowerLimit = -0.5;
        double upperLimit = 0.5;
        new DefiniteIntegralLogarithmic(lowerLimit, upperLimit, Optional.empty(), Optional.empty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void instanceCreationFail1Test() {
        double lowerLimit = 0.0;
        double upperLimit = 0.5;
        new DefiniteIntegralLogarithmic(lowerLimit, upperLimit, Optional.empty(), Optional.empty());
    }

    @Test(expected = InputMismatchException.class)
    public void instanceCreationFail2Test() {
        double lowerLimit = 0.2;
        double upperLimit = Double.POSITIVE_INFINITY;
        new DefiniteIntegralLogarithmic(lowerLimit, upperLimit, Optional.empty(), Optional.empty());
    }

    @Test
    public void solveTest() {
        double lowerLimit = 0.2;
        double upperLimit = 1.2;
        double expectedIntegral = -0.4593265;

        DefiniteIntegralLogarithmic definiteIntegralLogarithmic = new DefiniteIntegralLogarithmic(lowerLimit, upperLimit, Optional.empty(), Optional.empty());

        Assertions.assertThat(definiteIntegralLogarithmic.getApproximation()).isNull();
        assertEquals(expectedIntegral, definiteIntegralLogarithmic.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(lowerLimit, definiteIntegralLogarithmic.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperLimit, definiteIntegralLogarithmic.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{ln(x)}[0.2, 1.2]", definiteIntegralLogarithmic.toString());
    }
}
