package com.acabra.calculator.integral;

/**
 * Created by Agustin on 9/29/2016.
 */
public class FExponential extends IntegrableFunction {

    private static final FunctionDomain DOMAIN = FunctionDomainFactory.REAL_NUMBERS;

    public FExponential(double lowerBound, double upperBound, Double result, Double sequenceRiemannRectangleAreaSum) {
        super(lowerBound, upperBound, result, IntegralFunctionType.EXPONENTIAL.getLabel());
        this.sequenceRiemannRectangle = sequenceRiemannRectangleAreaSum;
        this.evaluatedLower = runEvaluation(this.lowerBound);
        this.evaluatedUpper = runEvaluation(this.upperBound);
    }

    @Override
    protected void validateBounds() {
        if (!DOMAIN.belongsDomain(this.lowerBound) || !DOMAIN.belongsDomain(this.upperBound)) {
            throw new UnsupportedOperationException("unable to create function, bounds not in function's domain");
        }
    }

    @Override
    protected double evaluate(double domainPoint) {
        return inFunctionsDomain(domainPoint) ?
                domainPoint == upperBound ? evaluatedUpper :
                        domainPoint == lowerBound ? evaluatedLower :
                                runEvaluation(domainPoint) :
                Double.NaN;
    }

    private double runEvaluation(double domainPoint) {
        return Math.exp(domainPoint);
    }

    @Override
    protected double solveIntegralWithRiemannSequences(boolean inscribedRectangle) {
        if (sequenceRiemannRectangle == null) {
            sequenceRiemannRectangle = calculateRiemannSequenceRectangleArea(inscribedRectangle);
        }
        return sequenceRiemannRectangle;
    }

    @Override
    protected Double executeIntegration() {
        return evaluateIntegral(upperBound) - evaluateIntegral(lowerBound);
    }

    private Double evaluateIntegral(double point) {
        return point == lowerBound ? evaluatedLower : (point == upperBound ? evaluatedUpper : evaluate(point));
    }

    public static boolean inFunctionsDomain(double val) {
        return DOMAIN.belongsDomain(val);
    }
}