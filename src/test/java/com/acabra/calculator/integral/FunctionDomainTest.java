package com.acabra.calculator.integral;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Agustin on 10/12/2016.
 */
public class FunctionDomainTest {

    @Test
    public void doesRangeContainsNonDomainPointsTest() {
        FunctionDomain domain = new FunctionDomainFactory.DomainBuilder()
                .withNegativeRealNumbers()
                .withPositiveRealNumbers()
                .exceptValue(0.0)
                .build();

        Interval test = new Interval(true, -5, true, 5);
        assertEquals(true, domain.doesRangeContainsNonDomainPoints(test));
    }

    @Test
    public void doesRangeContainsNonDomainPointsEmptyTest() {
        FunctionDomain domain = new FunctionDomainFactory.DomainBuilder()
                .withRealNumbers()
                .build();

        Interval test = new Interval(true, -5, true, 5);
        assertEquals(false, domain.doesRangeContainsNonDomainPoints(test));
    }

    @Test
    public void doesRangeContainsNonDomainPointsOnPartedDomainTest() {
        FunctionDomain domain = new FunctionDomainFactory.DomainBuilder()
                .withInclusiveFromInclusiveTo(3.0, 8.0)
                .build();

        Interval test = new Interval(true, -5.0, true, 5.0);
        assertEquals(true, domain.doesRangeContainsNonDomainPoints(test));
    }

    @Test
    public void doesRangeContainsNonDomainPointsOnPartedDomain1Test() {
        FunctionDomain domain = new FunctionDomainFactory.DomainBuilder()
                .withExclusiveFromInclusiveTo(3.0, 8.0)
                .build();

        assertEquals(true, domain.doesRangeContainsNonDomainPoints(new Interval(true, -5.0, true, 5.0)));
        assertEquals(true, domain.doesRangeContainsNonDomainPoints(new Interval(true, 3.0, true, 8.0)));
        assertEquals(true, domain.doesRangeContainsNonDomainPoints(new Interval(true, 1.0, false, 8.0)));
    }

    @Test
    public void doesRangeContainsNonDomainPointsOnPartedDomain2Test() {
        FunctionDomain domain = new FunctionDomainFactory.DomainBuilder()
                .withExclusiveFromExclusiveTo(3.0, 8.0)
                .build();

        assertEquals(false, domain.doesRangeContainsNonDomainPoints(new Interval(false, 3.0, false, 8.0)));
        assertEquals(true, domain.doesRangeContainsNonDomainPoints(new Interval(true, 3.0, false, 8.0)));
        assertEquals(true, domain.doesRangeContainsNonDomainPoints(new Interval(false, 3.0, true, 8.0)));
        assertEquals(true, domain.doesRangeContainsNonDomainPoints(new Interval(true, 3.0, true, 8.0)));
    }

    @Test
    public void failureBuildingIntervals1Test() {
        List<Double> list = Arrays.asList(Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        int exceptions = 0;
        for (int i = 0; i < list.size(); i++) {
            try {
                new FunctionDomainFactory.DomainBuilder()
                        .withExclusiveFromExclusiveTo(list.get(i), 8.0)
                        .build();
            } catch (InputMismatchException ime) {
                exceptions++;
            }
        }
        assertEquals(1, exceptions);
    }

    @Test
    public void failureBuildingIntervals2Test() {
        List<Double> list = Arrays.asList(Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        int exceptions = 0;
        for (int i = 0; i < list.size(); i++) {
            try {
                new FunctionDomainFactory.DomainBuilder()
                        .withExclusiveFromExclusiveTo(3.0, list.get(i))
                        .build();
            } catch (InputMismatchException ime) {
                exceptions++;
            }
        }
        assertEquals(1, exceptions);
    }

    @Test
    public void failureBuildingIntervals3Test() {
        List<Double> list = Arrays.asList(Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        int exceptions = 0;
        for (int i = 0; i < list.size(); i++) {
            try {
                new FunctionDomainFactory.DomainBuilder()
                        .withExclusiveFromInclusiveTo(list.get(i), 8.0)
                        .build();
            } catch (InputMismatchException ime) {
                exceptions++;
            }
        }
        assertEquals(2, exceptions);
    }

    @Test
    public void failureBuildingIntervals4Test() {
        List<Double> list = Arrays.asList(Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        int exceptions = 0;
        for (int i = 0; i < list.size(); i++) {
            try {
                new FunctionDomainFactory.DomainBuilder()
                        .withExclusiveFromInclusiveTo(3.0, list.get(i))
                        .build();
            } catch (InputMismatchException ime) {
                exceptions++;
            }
        }
        assertEquals(2, exceptions);
    }

    @Test
    public void failureBuildingIntervals5Test() {
        List<Double> list = Arrays.asList(Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        int exceptions = 0;
        for (int i = 0; i < list.size(); i++) {
            try {
                new FunctionDomainFactory.DomainBuilder()
                        .withInclusiveFromInclusiveTo(list.get(i), 8.0)
                        .build();
            } catch (InputMismatchException ime) {
                exceptions++;
            }
        }
        assertEquals(3, exceptions);
    }

    @Test
    public void failureBuildingIntervals6Test() {
        List<Double> list = Arrays.asList(Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        int exceptions = 0;
        for (int i = 0; i < list.size(); i++) {
            try {
                new FunctionDomainFactory.DomainBuilder()
                        .withInclusiveFromInclusiveTo(3.0, list.get(i))
                        .build();
            } catch (InputMismatchException ime) {
                exceptions++;
            }
        }
        assertEquals(3, exceptions);
    }

    @Test
    public void failureBuildingIntervals7Test() {
        List<Double> list = Arrays.asList(Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        int exceptions = 0;
        for (int i = 0; i < list.size(); i++) {
            try {
                new FunctionDomainFactory.DomainBuilder()
                        .withInclusiveFromExclusiveTo(list.get(i), 8.0)
                        .build();
            } catch (InputMismatchException ime) {
                exceptions++;
            }
        }
        assertEquals(2, exceptions);
    }

    @Test
    public void failureBuildingIntervals8Test() {
        List<Double> list = Arrays.asList(Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        int exceptions = 0;
        for (int i = 0; i < list.size(); i++) {
            try {
                new FunctionDomainFactory.DomainBuilder()
                        .withInclusiveFromExclusiveTo(3.0, list.get(i))
                        .build();
            } catch (InputMismatchException ime) {
                exceptions++;
            }
        }
        assertEquals(2, exceptions);
    }

}
