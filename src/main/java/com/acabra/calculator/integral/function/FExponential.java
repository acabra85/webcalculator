package com.acabra.calculator.integral.function;

/**
 * Created by Agustin on 9/29/2016.
 */
public class FExponential extends IntegrableFunction {
    private static final FunctionDomain DOMAIN = FunctionDomainFactory.REAL_NUMBERS;

    public FExponential(double lowerBound, double upperBound, Double result, Double approximation) {
        super(lowerBound, upperBound, result, IntegrableFunctionType.EXPONENTIAL.getLabel(), DOMAIN);
        this.approximation = approximation;
        this.evaluatedLower = runEvaluation(this.lowerLimit);
        this.evaluatedUpper = runEvaluation(this.upperLimit);
    }

    @Override
    public double evaluate(double domainPoint) {
        return inFunctionsDomain(domainPoint) ?
                domainPoint == upperLimit ? evaluatedUpper :
                        domainPoint == lowerLimit ? evaluatedLower :
                                runEvaluation(domainPoint) :
                Double.NaN;
    }

    private double runEvaluation(double domainPoint) {
        return Math.exp(domainPoint);
    }

    @Override
    protected Double executeIntegration() {
        return evaluateIntegral(upperLimit) - evaluateIntegral(lowerLimit);
    }

    private Double evaluateIntegral(double point) {
        return point == lowerLimit ? evaluatedLower : (point == upperLimit ? evaluatedUpper : evaluate(point));
    }

    public static boolean inFunctionsDomain(double val) {
        return DOMAIN.belongsDomain(val);
    }
}