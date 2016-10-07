package com.acabra.calculator.integral;

/**
 * Created by Agustin on 9/29/2016.
 */
public class ExponentialIntegral extends IntegrableFunction {

    public ExponentialIntegral(double lowerBound, double upperBound) {
        super(lowerBound, upperBound, IntegralFunctionType.EXPONENTIAL.getLabel());
    }

    public ExponentialIntegral(double lowerBound, double upperBound, Double result) {
        super(lowerBound, upperBound, result, IntegralFunctionType.EXPONENTIAL.getLabel());
    }

    public ExponentialIntegral(double lowerBound, double upperBound, Double result, Double sequenceRiemannRectangleAreaSum) {
        super(lowerBound, upperBound, result, IntegralFunctionType.EXPONENTIAL.getLabel());
        this.sequenceRiemannRectangle = sequenceRiemannRectangleAreaSum;
    }

    @Override
    protected double evaluate(double domainPoint) {
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
        return Math.exp(upperBound) - Math.exp(lowerBound);
    }
}
