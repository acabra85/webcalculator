package com.acabra.calculator.integral.definiteintegral;

import com.acabra.calculator.function.RealFunction;
import com.acabra.calculator.integral.Interval;
import com.acabra.calculator.util.ResultFormatter;

/**
 * Created by Agustin on 9/29/2016.
 */
public abstract class DefiniteIntegralFunction {

    protected static final String STRING_REPRESENTATION_FORMAT = "Integ{%s}[%s, %s]";

    protected final double lowerLimit;
    protected final double upperLimit;
    private final String label;
    protected final Interval requestedIntegrationRange; //Stores the integration requested in constructor by the boundaries.
    public final FunctionDomain domain;
    protected final RealFunction function;
    protected Double result;
    protected volatile Double approximation;
    protected volatile Double evaluatedLower;
    protected volatile Double evaluatedUpper;

    public DefiniteIntegralFunction(double lowerLimit, double upperLimit, Double result, String label, RealFunction baseFunction) {
        this.function = baseFunction;
        this.domain = baseFunction.domain;
        this.lowerLimit = Math.min(lowerLimit, upperLimit);
        this.upperLimit = Math.max(lowerLimit, upperLimit);
        this.result = result;
        this.label = label;
        this.approximation = null;
        this.requestedIntegrationRange = new Interval(true, Math.min(this.lowerLimit, this.upperLimit), true, Math.max(this.lowerLimit, this.upperLimit));
        evaluatedLower = null;
        evaluatedUpper = null;
        validateLimits();
    }

    /**
     * Implement this method that verifies that both limits are within
     * functions DOMAIN
     */
    protected void validateLimits() {
        if (!domain.belongsDomain(this.lowerLimit) || !domain.belongsDomain(this.upperLimit)) {
            throw new UnsupportedOperationException("unable to create function, bounds not in function's DOMAIN");
        }
    }

    /**
     * This method evaluates the integral using the
     * integration techniques for the specified function.
     *
     * Recommended to cache the result of this method to be stored as result.
     * Since the bounds of this object are final the result is not meant to change.
     *
     * @return the result of integration.
     */
    public synchronized double solve() {
        if (null == result) {
            if (domain.doesRangeContainsNonDomainPoints(requestedIntegrationRange)) {
                throw new UnsupportedOperationException("Unable to integrate over given limits, area does not converge");
            }
            result = executeDefiniteIntegration();
        }
        return result;
    }

    /**
     * This method is meant to provide the integration solution using the integration
     * techniques for this function
     * @return the result of integration between limits
     */
    protected abstract Double executeDefiniteIntegration();

    public double getLowerLimit() {
        return lowerLimit;
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    public double getResult() {
        return solve();
    }

    public synchronized Double getApproximation() {
        return approximation;
    }

    @Override
    public String toString() {
        return String.format(STRING_REPRESENTATION_FORMAT,
                label,
                ResultFormatter.trimIntegerResults(lowerLimit + ""),
                ResultFormatter.trimIntegerResults(upperLimit + ""));
    }

    public abstract Double evaluateOnBaseFunction(Double evalPoint);
}
