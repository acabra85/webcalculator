package com.acabra.calculator.function;

import com.acabra.calculator.integral.definiteintegral.FunctionDomain;
import com.acabra.calculator.integral.definiteintegral.FunctionDomainFactory;
import java.util.function.Function;

/**
 * Describe your class
 */
public class SineFunction extends RealFunction {

    public static final FunctionDomain DOMAIN = FunctionDomainFactory.REAL_NUMBERS;

    private Function<Double, Double> integrationFunction = evalPoint -> {
        if (FunctionDomainFactory.REAL_NUMBERS.belongsDomain(evalPoint))
            return -Math.cos(evalPoint);
        throw new UnsupportedOperationException(String.format("%.4f does not belong to functions DOMAIN", evalPoint));
    };

    private final RealFunction derivativeFunction;

    SineFunction(RealFunction derivativeFunction) {
        super(SineFunction.DOMAIN);
        this.derivativeFunction = derivativeFunction == null ? new CosineFunction(this) : derivativeFunction;
    }

    @Override
    public Double apply(Double evalPoint) {
        if (domain.belongsDomain(evalPoint))
            return Math.sin(evalPoint);
        throw new UnsupportedOperationException(String.format("%.4f does not belong to functions DOMAIN", evalPoint));
    }

    @Override
    public Double calculateDerivative(Double evalPoint) {
        return derivativeFunction.apply(evalPoint);
    }

    @Override
    public Double calculateIntegral(Double evalPoint) {
        return integrationFunction.apply(evalPoint);
    }
}
