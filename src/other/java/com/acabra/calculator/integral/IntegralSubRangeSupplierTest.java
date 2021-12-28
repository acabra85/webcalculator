package com.acabra.calculator.integral;

import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralFunction;
import com.acabra.calculator.integral.definiteintegral.IntegrableFunctionType;
import com.acabra.calculator.util.WebCalculatorConstants;



core.classloader.annotations.PowerMockIgnore;
core.classloader.annotations.PrepareForTest;
modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 9/29/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({IntegralSubRangeSupplier.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class IntegralSubRangeSupplierTest {

    @BeforeEach
    public void setup() {

    }

    @Test
    public void provideIntegralsOnSubRanges1Test() {
        double lowerBound1 = -10;
        double upperBound1 = -0.0;
        double[] expectedLowerRanges = {lowerBound1, -8.0, -6.0, -4.0, -2.0};
        double[] expectedUpperRanges = {-8.0, -6.0, -4.0, -2.0, upperBound1};
        int repeatedCalculations1 = 5;
        IntegralSubRangeSupplier integralSubRangeSupplier = new IntegralSubRangeSupplier(IntegrableFunctionType.EXPONENTIAL, lowerBound1, upperBound1, Collections.emptyList(), repeatedCalculations1);
        int i = 0;
        for (i = 0; integralSubRangeSupplier.hasMoreSubRanges(); i++) {
            DefiniteIntegralFunction definiteIntegralFunction = integralSubRangeSupplier.get();
            assertEquals(expectedLowerRanges[i], definiteIntegralFunction.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
            assertEquals(expectedUpperRanges[i], definiteIntegralFunction.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        }
        assertEquals(i, repeatedCalculations1);
    }

    @Test
    public void provideIntegralsOnSubRanges2Test() {
        double lowerBound1 = -10;
        double upperBound1 = -0.0;
        int repeatedCalculations1 = 1;
        IntegralSubRangeSupplier integralSubRangeSupplier = new IntegralSubRangeSupplier(IntegrableFunctionType.EXPONENTIAL, lowerBound1, upperBound1, Collections.emptyList(), repeatedCalculations1);
        int i = 0;
        for (i = 0; integralSubRangeSupplier.hasMoreSubRanges(); i++) {
            DefiniteIntegralFunction currentIntegral = integralSubRangeSupplier.get();
            assertEquals(lowerBound1, currentIntegral.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
            assertEquals(upperBound1, currentIntegral.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        }
        assertEquals(i, repeatedCalculations1);
    }

    @Test
    public void provideIntegralsOnSubRanges3Test() {
        double lowerBound1 = -0.2;
        double upperBound1 = 0.2;
        double[] expectedLowerRanges = {lowerBound1, 0.0};
        double[] expectedUpperRanges = {0.0, upperBound1};
        int repeatedCalculations1 = 2;
        IntegralSubRangeSupplier integralSubRangeSupplier = new IntegralSubRangeSupplier(IntegrableFunctionType.EXPONENTIAL, lowerBound1, upperBound1, Collections.emptyList(), repeatedCalculations1);
        int i = 0;
        for (i = 0; integralSubRangeSupplier.hasMoreSubRanges(); i++) {
            DefiniteIntegralFunction currentIntegral = integralSubRangeSupplier.get();
            assertEquals(expectedLowerRanges[i], currentIntegral.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
            assertEquals(expectedUpperRanges[i], currentIntegral.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        }
        assertEquals(i, repeatedCalculations1);
    }

    @Test(expected = NoSuchElementException.class)
    public void getErrorTest() {
        IntegralSubRangeSupplier supp = new IntegralSubRangeSupplier(IntegrableFunctionType.EXPONENTIAL, 1, 2, Collections.emptyList(), 0);
        supp.get();
    }

}
