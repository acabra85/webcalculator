package com.acabra.calculator.function;

import com.acabra.calculator.integral.definiteintegral.FunctionDomain;
import com.acabra.calculator.integral.definiteintegral.FunctionDomainFactory;
import java.util.function.Function;

/**
 * Describe your class
 */
public class InverseFunction extends RealFunction {


    public static final FunctionDomain DOMAIN = new FunctionDomainFactory.DomainBuilder()
            .withRealNumbers()
            .exceptValue(0.0d)
            .build();

    private final Function<Double, Double> derivativeFunction = evalPoint ->  {
        FunctionDomain domain = FunctionDomainFactory.builder()
                .withRealNumbers()
                .exceptValue(0.0d)
                .build();
        if (domain.belongsDomain(evalPoint))
            return -1.0 / Math.pow(evalPoint, 2.0);
        throw new UnsupportedOperationException(String.format("%.4f does not belong to functions DOMAIN", evalPoint));
    };

    private final RealFunction integrationFunction;

    public InverseFunction(RealFunction integration) {
        super(InverseFunction.DOMAIN);
        this.integrationFunction = integration == null ? new LogarithmicFunction(this) : integration;
    }

    @Override
    public Double apply(Double evalPoint) {
        if (domain.belongsDomain(evalPoint)) {
            return 1.0 / evalPoint;
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
