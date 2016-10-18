package com.acabra.calculator.integral.function;

import java.util.Optional;

/**
 * Created by Agustin on 10/11/2016.
 */
public class FLogarithmic extends IntegrableFunction {

    private static final FunctionDomain DOMAIN = new FunctionDomainFactory.DomainBuilder()
            .withPositiveRealNumbers()
            .exceptValue(0.0)
            .build();

    public FLogarithmic(double lowerLimit, double upperLimit, Optional<Double> result, Optional<Double> approx) {
        super(lowerLimit, upperLimit, result.orElse(null), IntegrableFunctionType.LOGARITHMIC.getLabel(), DOMAIN);
        this.approximation = approx.orElse(null);
        this.evaluatedLower = runEvaluation(this.lowerLimit);
        this.evaluatedUpper = runEvaluation(this.upperLimit);
    }

    @Override
    protected Double executeIntegration() {
        return evaluateIntegral(upperLimit) - evaluateIntegral(lowerLimit);
    }

    private Double evaluateIntegral(double point) {
        return point * (evaluate(point) - 1);
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
        return Math.log(domainPoint);
    }

    public static boolean inFunctionsDomain(double val) {
        return DOMAIN.belongsDomain(val);
    }

}
