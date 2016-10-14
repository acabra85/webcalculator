package com.acabra.calculator.integral;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by Agustin on 10/11/2016.
 */
public class FunctionDomain {

    private final Set<Interval> intervalSet;
    private final List<Double> nonDomainPoints;

    public FunctionDomain(Set<Interval> intervalSet, List<Double> nonDomainPoints) {
        this.intervalSet = Collections.unmodifiableSet(intervalSet);
        this.nonDomainPoints = Collections.unmodifiableList(nonDomainPoints);
    }

    public boolean belongsDomain(final double val) {
        return !nonDomainPoints.contains(val) && intervalSet.stream().anyMatch(dr -> dr.belongs(val));
    }

    public boolean doesRangeContainsNonDomainPoints(final Interval range) {
        return !nonDomainPoints.isEmpty()
                && nonDomainPoints.stream().filter(range::belongs).count() > 0
                || !isRangeContainedInDomain(range);
    }

    private boolean isRangeContainedInDomain(final Interval range) {
        return intervalSet.stream().anyMatch(dr -> dr.containsInterval(range));
    }
}
