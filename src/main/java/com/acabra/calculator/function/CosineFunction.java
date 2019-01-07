package com.acabra.calculator.function;

import com.acabra.calculator.integral.definiteintegral.FunctionDomain;
import com.acabra.calculator.integral.definiteintegral.FunctionDomainFactory;
import java.util.function.Function;

/**
 * Created by Agustin on 11/2/2016.
 */
public final class CosineFunction extends RealFunction {

    public static final FunctionDomain DOMAIN = FunctionDomainFactory.REAL_NUMBERS;

    private final Function<Double, Double> derivativeFunction = evalPoint -> {
        if (FunctionDomainFactory.REAL_NUMBERS.belongsDomain(evalPoint))
            return -Math.sin(evalPoint);
        throw new UnsupportedOperationException(String.format("%.4f does not belong to functions DOMAIN", evalPoint));
    };

    private final RealFunction integrationFunction;


    public CosineFunction(RealFunction integrationFunction) {
        super(CosineFunction.DOMAIN);
        this.integrationFunction = integrationFunction == null ? new SineFunction(this) : integrationFunction;
    }

    @Override
    public Double calculateDerivative(Double evalPoint) {
        return derivativeFunction.apply(evalPoint);
    }

    @Override
    public Double calculateIntegral(Double evalPoint) {
        return integrationFunction.apply(evalPoint);
    }

    @Override
    public Double apply(Double evalPoint) {
        if (domain.belongsDomain(evalPoint))
            return Math.cos(evalPoint);
        throw new UnsupportedOperationException(String.format("%.4f does not belong to functions DOMAIN", evalPoint));
    }
}
