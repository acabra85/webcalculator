package com.acabra.calculator.integral.definiteintegral;

import com.acabra.calculator.integral.Interval;

import java.util.*;

/**
 * Created by Agustin on 10/11/2016.
 */
public class FunctionDomainFactory {

    private static final Set<Interval> REAL_NUMBERS_RANGE = new HashSet<Interval>(){{
        add(new Interval(false, Double.NEGATIVE_INFINITY, false, Double.POSITIVE_INFINITY));
    }};

    private static final Set<Interval> POSITIVE_REAL_NUMBERS_RANGE = new HashSet<Interval>() {{
        add(new Interval(true, 0.0, false, Double.POSITIVE_INFINITY));
    }};

    private static final Set<Interval> NEGATIVE_REAL_NUMBERS_RANGE = new HashSet<Interval>(){{
        add(new Interval(false, Double.NEGATIVE_INFINITY, false, 0.0));
    }};

    public static final FunctionDomain REAL_NUMBERS = new FunctionDomain(REAL_NUMBERS_RANGE, Collections.emptyList());
    public static final FunctionDomain POSITIVE_REAL_NUMBERS = new FunctionDomain(POSITIVE_REAL_NUMBERS_RANGE, Collections.emptyList());
    public static final FunctionDomain NEGATIVE_REAL_NUMBERS = new FunctionDomain(NEGATIVE_REAL_NUMBERS_RANGE, Collections.emptyList());


    public static class DomainBuilder {

        private Set<Interval> intervalSet = new HashSet<>();
        private List<Double> except = new ArrayList<>();

        public DomainBuilder() {
        }

        private Interval createSinglePoint(double val) {
            if(Double.isNaN(val) || Double.isInfinite(val)) {
                throw new InputMismatchException("invalid value to include");
            }
            return new Interval(true, val, true, val);
        }

        public DomainBuilder exceptValue(double val) {
            if(Double.isNaN(val) || Double.isInfinite(val)) {
                throw new InputMismatchException("invalid value to exclude");
            }
            except.add(val);
            return DomainBuilder.this;
        }

        public DomainBuilder withValue(double val) {
            intervalSet.add(createSinglePoint(val));
            return DomainBuilder.this;
        }

        public DomainBuilder withRealNumbers() {
            intervalSet.addAll(FunctionDomainFactory.REAL_NUMBERS_RANGE);
            return DomainBuilder.this;
        }

        public DomainBuilder withPositiveRealNumbers() {
            intervalSet.addAll(FunctionDomainFactory.POSITIVE_REAL_NUMBERS_RANGE);
            return DomainBuilder.this;
        }

        public DomainBuilder withNegativeRealNumbers() {
            intervalSet.addAll(FunctionDomainFactory.NEGATIVE_REAL_NUMBERS_RANGE);
            return DomainBuilder.this;
        }

        public DomainBuilder withInclusiveFromInclusiveTo(double from, double to) {
            if (from == to) return withValue(from);
            intervalSet.add(new Interval(true, Math.min(from, to), true, Math.max(from, to)));
            return this;
        }

        public DomainBuilder withInclusiveFromExclusiveTo(double from, double to) {
            if (from == to) return withValue(from);
            intervalSet.add(new Interval(true, Math.min(from, to), false, Math.max(from, to)));
            return this;
        }

        public DomainBuilder withExclusiveFromInclusiveTo(double from, double to) {
            if (from == to) return withValue(from);
            intervalSet.add(new Interval(false, Math.min(from, to), true, Math.max(from, to)));
            return this;
        }

        public DomainBuilder withExclusiveFromExclusiveTo(double from, double to) {
            if (from == to) return withValue(from);
            intervalSet.add(new Interval(false, Math.min(from, to), false, Math.max(from, to)));
            return this;
        }

        public FunctionDomain build() {
            return new FunctionDomain(intervalSet, except);
        }
    }
}
