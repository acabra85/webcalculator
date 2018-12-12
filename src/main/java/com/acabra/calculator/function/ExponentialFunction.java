package com.acabra.calculator.function;

import com.acabra.calculator.integral.definiteintegral.FunctionDomain;
import com.acabra.calculator.integral.definiteintegral.FunctionDomainFactory;

/**
 * Describe your class
 */
public class ExponentialFunction extends RealFunction {

    public static final FunctionDomain DOMAIN = FunctionDomainFactory.builder()
            .withRealNumbers()
            .build();

    public ExponentialFunction() {
        super(ExponentialFunction.DOMAIN);
    }

    @Override
    public Double apply(Double evalPoint) {
        if(domain.belongsDomain(evalPoint)) {
            return Math.exp(evalPoint);
        }
        throw new UnsupportedOperationException(String.format("%.4f does not belong to functions DOMAIN", evalPoint));
    }

    @Override
    public Double calculateDerivative(Double evalPoint) {
        return apply(evalPoint);
    }

    @Override
    public Double calculateIntegral(Double evalPoint) {
        return apply(evalPoint);
    }
}
