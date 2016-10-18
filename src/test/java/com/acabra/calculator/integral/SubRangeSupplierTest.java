package com.acabra.calculator.integral;

import com.acabra.calculator.util.WebCalculatorConstants;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 10/17/2016.
 */
public class SubRangeSupplierTest {

    @Test
    public void getTest() {
        int desiredSubRanges = 2;
        SubRangeSupplier subRangeSupplier = new SubRangeSupplier(1, 2, desiredSubRanges);

        Interval interval = subRangeSupplier.get();
        assertEquals(1, interval.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(1.5, interval.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);

        interval = subRangeSupplier.get();
        assertEquals(1.5, interval.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(2, interval.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test
    public void get2Test() {
        int desiredSubRanges = 4;
        SubRangeSupplier subRangeSupplier = new SubRangeSupplier(1, 2, desiredSubRanges);

        Interval interval = subRangeSupplier.get();
        assertEquals(1, interval.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(1.25, interval.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);

        interval = subRangeSupplier.get();
        assertEquals(1.25, interval.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(1.5, interval.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);

        interval = subRangeSupplier.get();
        assertEquals(1.5, interval.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(1.75, interval.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);

        interval = subRangeSupplier.get();
        assertEquals(1.75, interval.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(2, interval.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
    }

    @Test(expected = NoSuchElementException.class)
    public void get3Test() {
        int desiredSubRanges = 0;
        SubRangeSupplier subRangeSupplier = new SubRangeSupplier(1, 2, desiredSubRanges);
        subRangeSupplier.get();
    }

    @Test(expected = NoSuchElementException.class)
    public void getErrorTest() {
        int desiredSubRanges = 1;
        SubRangeSupplier subRangeSupplier = new SubRangeSupplier(1, 2, desiredSubRanges);

        subRangeSupplier.get();
        subRangeSupplier.get();
    }
}
