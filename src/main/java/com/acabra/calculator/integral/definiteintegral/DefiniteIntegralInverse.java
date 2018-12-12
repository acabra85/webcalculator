package com.acabra.calculator.integral.definiteintegral;

import com.acabra.calculator.function.InverseFunction;
import java.util.Optional;

/**
 * Created by Agustin on 10/17/2016.
 */
public final class DefiniteIntegralInverse extends DefiniteIntegralFunction {

    public static final FunctionDomain DOMAIN = new FunctionDomainFactory.DomainBuilder().withPositiveRealNumbers().exceptValue(0.0).build();

    public DefiniteIntegralInverse(double lowerLimit, double upperLimit, Optional<Double> result, Optional<Double> approximation) {
        super(lowerLimit, upperLimit, result.orElse(null), IntegrableFunctionType.INVERSE.getLabel(), new InverseFunction(null));
        this.approximation = approximation.orElse(null);
    }

    @Override
    protected Double executeDefiniteIntegration() {
        return function.calculateIntegral(upperLimit) - function.calculateIntegral(lowerLimit);
    }

    @Override
    public Double evaluateOnBaseFunction(Double evalPoint) {
        return function.apply(evalPoint);
    }
}
