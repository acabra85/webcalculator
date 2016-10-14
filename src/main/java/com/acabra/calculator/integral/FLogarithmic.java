package com.acabra.calculator.integral;

import java.util.Optional;

/**
 * Created by Agustin on 10/11/2016.
 */
public class FLogarithmic extends IntegrableFunction {

    private static final FunctionDomain DOMAIN = new FunctionDomainFactory.DomainBuilder()
            .withPositiveRealNumbers()
            .exceptValue(0.0)
            .build();

    public FLogarithmic(double lowerBound, double upperBound, Optional<Double> result, Optional<Double> approx) {
        super(lowerBound, upperBound, result.orElse(null), IntegralFunctionType.LOGARITHMIC.getLabel());
        this.sequenceRiemannRectangle = approx.orElse(null);
        this.evaluatedLower = runEvaluation(this.lowerBound);
        this.evaluatedUpper = runEvaluation(this.upperBound);
    }

    @Override
    protected void validateBounds() {
        if (!DOMAIN.belongsDomain(lowerBound) || !DOMAIN.belongsDomain(upperBound)
                || DOMAIN.doesRangeContainsNonDomainPoints(this.requestedIntegrationRange)) {
            throw new UnsupportedOperationException("unable to create function, bounds not in function's domain");
        }
    }

    @Override
    protected Double executeIntegration() {
        return evaluateIntegral(upperBound) - evaluateIntegral(lowerBound);
    }

    private Double evaluateIntegral(double point) {
        return point * (evaluate(point) - 1);
    }

    @Override
    protected double solveIntegralWithRiemannSequences(boolean inscribedRectangle) {
        if (sequenceRiemannRectangle == null) {
            sequenceRiemannRectangle = calculateRiemannSequenceRectangleArea(inscribedRectangle);
        }
        return sequenceRiemannRectangle;
    }

    @Override
    protected double evaluate(double domainPoint) {
        if (evaluatedUpper != null && evaluatedLower != null) {
            if (domainPoint == upperBound) return evaluatedUpper;
            if (domainPoint == lowerBound) return evaluatedLower;
        }
        return inFunctionsDomain(domainPoint) ?
                domainPoint == upperBound ? evaluatedUpper :
                        domainPoint == lowerBound ? evaluatedLower :
                                runEvaluation(domainPoint) :
                Double.NaN;
    }

    private double runEvaluation(double domainPoint) {
        return Math.log(domainPoint);
    }

    public static boolean inFunctionsDomain(double val) {
        return DOMAIN.belongsDomain(val);
    }

}
