package com.acabra.calculator.integral.definiteintegral;

import java.util.Optional;

/**
 * Created by Agustin on 10/17/2016.
 */
public class DefiniteIntegralInverse extends DefiniteIntegralFunction {

    public static final FunctionDomain DOMAIN = new FunctionDomainFactory.DomainBuilder().withPositiveRealNumbers().exceptValue(0.0).build();

    public DefiniteIntegralInverse(double lowerLimit, double upperLimit, Optional<Double> result, Optional<Double> approximation) {
        super(lowerLimit, upperLimit, result.orElse(null), IntegrableFunctionType.INVERSE.getLabel(), DOMAIN);
        this.approximation = approximation.orElse(null);
        this.evaluatedLower = runEvaluation(lowerLimit);
        this.evaluatedUpper = runEvaluation(upperLimit);
    }

    private Double runEvaluation(double domainPoint) {
        return 1.0 / domainPoint;
    }

    @Override
    protected Double executeIntegration() {
        return Math.log(upperLimit) - Math.log(lowerLimit);
    }

    @Override
    public double evaluate(double domainPoint) {
        return inFunctionsDomain(domainPoint) ?
                (domainPoint == lowerLimit ? evaluatedLower :
                        (domainPoint == upperLimit) ? evaluatedUpper :
                                runEvaluation(domainPoint)) :
                0.0;
    }

    @Override
    public double calculateDerivative(double evalPoint) {
        if (inFunctionsDomain(evalPoint)) {
            return -1.0 / (evalPoint * evalPoint);
        }
        throw new UnsupportedOperationException(String.format("Entered value %.4f does not belong to functions domain", evalPoint));
    }

    public static boolean inFunctionsDomain(double val) {
        return DOMAIN.belongsDomain(val);
    }
}
