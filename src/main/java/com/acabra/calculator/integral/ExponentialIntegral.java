package com.acabra.calculator.integral;

/**
 * Created by Agustin on 9/29/2016.
 */
public class ExponentialIntegral extends IntegrableFunction {

    public ExponentialIntegral(double lowerBound, double upperBound) {
        super(lowerBound, upperBound, IntegralFunctionType.EXPONENTIAL.getLabel());
    }

    public ExponentialIntegral(double lowerBound, double upperBound, double result) {
        super(lowerBound, upperBound, result, IntegralFunctionType.EXPONENTIAL.getLabel());
    }

    public ExponentialIntegral(double lowerBound, double upperBound, double result, double sequenceRiemannRectangleAreaSum) {
        super(lowerBound, upperBound, result, IntegralFunctionType.EXPONENTIAL.getLabel());
        this.sequenceRiemannRectangle = sequenceRiemannRectangleAreaSum;
    }

    @Override
    protected double evaluate(double domainPoint) {
        return Math.exp(domainPoint);
    }

    @Override
    public double solve() {
        if (null == result) {
            result = executeIntegration();
        }
        return result;
    }

    @Override
    protected Double calculateRiemannSequenceRectangleArea(boolean inscribed) {
        /*TODO Areas under the x-axis should be taken in count
               this runs on the assumption that the evaluations on all points on this
               function are positive values on y-axis, to solve discrepancy a split
               of ranges is required
          */
        double width = (upperBound - lowerBound);
        double height = inscribed ? evaluate(lowerBound) : evaluate(upperBound);
        return width * height;
    }

    @Override
    protected double solveIntegralWithRiemannSequences(boolean inscribedRectangle) {
        if (sequenceRiemannRectangle == null) {
            sequenceRiemannRectangle = calculateRiemannSequenceRectangleArea(inscribedRectangle);
        }
        return sequenceRiemannRectangle;
    }

    private Double executeIntegration() {
        return Math.exp(upperBound) - Math.exp(lowerBound);
    }
}
