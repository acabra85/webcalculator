package com.acabra.calculator.integral.definiteintegral;

import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;

/**
 * Describe your class
 */
public class DefiniteIntegralCosineTest {

    @Test
    public void should_return_zero() {
        DefiniteIntegralCosine definiteIntegralCosine = new DefiniteIntegralCosine(0,Math.PI, Optional.empty(), Optional.empty());

        Assert.assertEquals("failed returning zero for area under cos(x) between 0,180", 0.0d, definiteIntegralCosine.executeDefiniteIntegration(), 0.0001);
    }

    @Test
    public void should_return_positive_value() {
        DefiniteIntegralCosine definiteIntegralCosine_1 = new DefiniteIntegralCosine(0,Math.PI/2, Optional.empty(), Optional.empty());
        DefiniteIntegralCosine definiteIntegralCosine_2 = new DefiniteIntegralCosine(Math.PI/2, Math.PI, Optional.empty(), Optional.empty());

        Assert.assertEquals("failed returning zero for area under cos(x) between 0,180", 1.0d, definiteIntegralCosine_1.executeDefiniteIntegration(), 0.0001);
        Assert.assertEquals("failed returning zero for area under cos(x) between 0,180", 1.0d, definiteIntegralCosine_1.executeDefiniteIntegration(), 0.0001);
        Assert.assertEquals("failed returning zero for area under cos(x) between 0,180",
                2*definiteIntegralCosine_1.executeDefiniteIntegration(),
                definiteIntegralCosine_1.executeDefiniteIntegration() + Math.abs(definiteIntegralCosine_2.executeDefiniteIntegration()), 0.0001);
    }
}
