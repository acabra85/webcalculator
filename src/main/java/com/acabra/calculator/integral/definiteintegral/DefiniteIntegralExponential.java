package com.acabra.calculator.integral.definiteintegral;

import java.util.Optional;

/**
 * Created by Agustin on 9/29/2016.
 */
public class DefiniteIntegralExponential extends DefiniteIntegralFunction {

    private static final FunctionDomain DOMAIN = FunctionDomainFactory.REAL_NUMBERS;

    public DefiniteIntegralExponential(double lowerBound, double upperBound, Optional<Double> result, Optional<Double> approximation) {
        super(lowerBound, upperBound, result.orElse(null), IntegrableFunctionType.EXPONENTIAL.getLabel(), DOMAIN);
        this.approximation = approximation.orElse(null);
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

    @Override
    public double calculateDerivative(double domainPoint) {
        return runEvaluation(domainPoint);
    }

    /**
     * Evaluates whether the value belongs to the domain
     * @param val value to evaluate
     * @return true if belongs false otherwise
     */
    public static boolean inFunctionsDomain(double val) {
        return DOMAIN.belongsDomain(val);
    }
}