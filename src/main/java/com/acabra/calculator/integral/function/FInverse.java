package com.acabra.calculator.integral.function;

/**
 * Created by Agustin on 10/17/2016.
 */
public class FInverse extends IntegrableFunction {

    private static final FunctionDomain DOMAIN = new FunctionDomainFactory.DomainBuilder().withRealNumbers().exceptValue(0.0).build();

    public FInverse(double lowerLimit, double upperLimit, Double result, Double approximation) {
        super(lowerLimit, upperLimit, result, IntegrableFunctionType.INVERSE.getLabel(), DOMAIN);
        this.approximation = approximation;
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

    public static boolean inFunctionsDomain(double val) {
        return DOMAIN.belongsDomain(val);
    }
}
