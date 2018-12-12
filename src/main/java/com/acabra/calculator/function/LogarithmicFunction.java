package com.acabra.calculator.function;

import com.acabra.calculator.integral.definiteintegral.FunctionDomain;
import com.acabra.calculator.integral.definiteintegral.FunctionDomainFactory;
import java.util.function.Function;

/**
 * Describe your class
 */
public class LogarithmicFunction extends RealFunction {

    public static final FunctionDomain DOMAIN = FunctionDomainFactory
            .builder()
            .withPositiveRealNumbers()
                .exceptValue(0.0d)
                .build();

    private final RealFunction derivativeFunction;
    private final Function<Double, Double> integrationFunction = evalPoint -> {
        FunctionDomain domain = FunctionDomainFactory.builder()
                .withPositiveRealNumbers()
                .exceptValue(0.0d)
                .build();
        if (domain.belongsDomain(evalPoint))
            return evalPoint * (Math.log(evalPoint) - 1);
        throw new UnsupportedOperationException(String.format("%.4f does not belong to functions DOMAIN", evalPoint));
    };

    public LogarithmicFunction(RealFunction derivative) {
        super(DOMAIN);
        this.derivativeFunction = derivative == null ? new InverseFunction(this) : derivative;
    }

    @Override
    public Double apply(Double evalPoint) {
        if (domain.belongsDomain(evalPoint)) {
            return Math.log(evalPoint);
        }
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
