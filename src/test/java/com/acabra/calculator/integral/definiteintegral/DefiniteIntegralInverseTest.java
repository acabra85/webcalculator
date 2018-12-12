package com.acabra.calculator.integral.definiteintegral;

import com.acabra.calculator.util.WebCalculatorConstants;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 10/18/2016.
 */
public class DefiniteIntegralInverseTest {

    @Test(expected = UnsupportedOperationException.class)
    public void solveDoesNotConvergeTest() {
        DefiniteIntegralInverse definiteIntegralInverse = new DefiniteIntegralInverse(-1, 1, Optional.empty(), Optional.empty());
        definiteIntegralInverse.solve();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void solveWrongInputTest() {
        DefiniteIntegralInverse definiteIntegralInverse = new DefiniteIntegralInverse(0, 1, Optional.empty(), Optional.empty());
        definiteIntegralInverse.solve();
    }

    @Test
    public void solveTest() {
        double lowerLimit = 0.1;
        double upperLimit = 1.0;
        double expectedIntegral = 2.30258509;

        DefiniteIntegralInverse definiteIntegralInverse = new DefiniteIntegralInverse(lowerLimit, upperLimit, Optional.empty(), Optional.empty());

        assertEquals(expectedIntegral, definiteIntegralInverse.solve(), WebCalculatorConstants.ACCURACY_EPSILON);
        Assertions.assertThat(definiteIntegralInverse.getUpperLimit()).isEqualTo(upperLimit);
        Assertions.assertThat(definiteIntegralInverse.getLowerLimit()).isEqualTo(lowerLimit);
        Assertions.assertThat(definiteIntegralInverse.getApproximation()).isNull();
    }

    @Test
    public void solve1Test() {
        double lowerLimit = 0.2;
        double upperLimit = 1.2;
        double expectedIntegral = 1.7917594;

        DefiniteIntegralInverse definiteIntegralInverse = new DefiniteIntegralInverse(lowerLimit, upperLimit, Optional.empty(), Optional.empty());

        Assertions.assertThat(definiteIntegralInverse.getApproximation()).isNull();
        assertEquals(expectedIntegral, definiteIntegralInverse.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(lowerLimit, definiteIntegralInverse.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperLimit, definiteIntegralInverse.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integ{1/x}[0.2, 1.2]", definiteIntegralInverse.toString());
    }

    @Test(expected = NullPointerException.class)
    public void should_fail_creating_function_null_parameters() {
        new DefiniteIntegralInverse(0.1, Double.MAX_VALUE, null, null);
    }
}
