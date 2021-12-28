package com.acabra.calculator.integral;

import com.acabra.calculator.util.WebCalculatorConstants;


import java.util.InputMismatchException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Agustin on 10/12/2016.
 */
public class IntervalTest {

    @Test
    public void creationTest() {
        double upperLimit = 10.0;
        double lowerLimit = 0.0;
        Interval interval = new Interval(true, lowerLimit, true, upperLimit);
        Interval intervalTwo = new Interval(0.0, 10.0);
        Interval intervalThree = new Interval(false, 1.0, false, 9.0);

        assertEquals(lowerLimit, interval.getLowerLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(upperLimit, interval.getUpperLimit(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertTrue(interval.equals(intervalTwo));
        assertTrue(interval.containsInterval(intervalTwo));
        assertTrue(interval.containsInterval(intervalThree));
        assertEquals(interval.hashCode(), intervalTwo.hashCode());
        assertTrue(interval.belongs(1.0));
        assertTrue(interval.belongs(0.0));
        assertTrue(interval.belongs(10.0));
        assertFalse(interval.belongs(-0.1));
    }

    @Test(expected = InputMismatchException.class)
    public void invalidCreation1Test() {
        new Interval(Double.POSITIVE_INFINITY, 0.0);
    }

    @Test(expected = InputMismatchException.class)
    public void invalidCreation2Test() {
        new Interval(Double.NaN, 0.0);
    }
}
