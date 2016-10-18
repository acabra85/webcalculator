package com.acabra.calculator.integral.function;

import com.acabra.calculator.util.WebCalculatorConstants;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.InputMismatchException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 10/18/2016.
 */
public class FInverseTest {

    @Test(expected = UnsupportedOperationException.class)
    public void solveDoesNotConvergeTest() {
        FInverse fInverse = new FInverse(-1, 1, null, null);
        fInverse.solve();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void solveWrongInputTest() {
        FInverse fInverse = new FInverse(0, 1, null, null);
        fInverse.solve();
    }

    @Test
    public void solveTest() {
        double lowerLimit = 0.1;
        double upperLimit = 1.0;
        double expectedIntegral = 2.30258509;

        FInverse fInverse = new FInverse(lowerLimit, upperLimit, null, null);

        assertEquals(expectedIntegral, fInverse.solve(), WebCalculatorConstants.ACCURACY_EPSILON);
        Assertions.assertThat(fInverse.getUpperLimit()).isEqualTo(upperLimit);
        Assertions.assertThat(fInverse.getLowerLimit()).isEqualTo(lowerLimit);
        Assertions.assertThat(fInverse.getApproximation()).isNull();
    }

    @Test
    public void solve1Test() {
        double lowerLimit = 0.2;
        double upperLimit = 1.2;
        double expectedIntegral = 1.7917594;

        FInverse fInverse = new FInverse(lowerLimit, upperLimit, null, null);

        Assertions.assertThat(fInverse.getApproximation()).isNull();
        assertEquals(expectedIntegral, fInverse.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(lowerLimit, fInverse.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperLimit, fInverse.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(1.0, fInverse.evaluate(1.0), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(5.0, fInverse.evaluate(0.2), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{1/x}[0.2, 1.2]", fInverse.toString());
    }
}
