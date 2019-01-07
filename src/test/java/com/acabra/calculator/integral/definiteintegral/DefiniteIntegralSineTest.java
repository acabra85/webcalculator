package com.acabra.calculator.integral.definiteintegral;

import com.acabra.calculator.function.DefiniteIntegralSine;
import com.acabra.calculator.util.WebCalculatorConstants;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;

/**
 * Describe your class
 */
public class DefiniteIntegralSineTest {
    //TODO test methods
    @Test
    public void should_return_two() {
        DefiniteIntegralSine definiteIntegralSine = new DefiniteIntegralSine(0.0d, Math.PI, Optional.empty(), Optional.empty());
        Assert.assertEquals("error didn't return 2", 2.0d,
                definiteIntegralSine.executeDefiniteIntegration(),
                WebCalculatorConstants.ACCURACY_EPSILON);
    }
}
