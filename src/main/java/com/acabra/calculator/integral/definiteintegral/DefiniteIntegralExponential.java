package com.acabra.calculator.integral.definiteintegral;

import com.acabra.calculator.function.ExponentialFunction;
import java.util.Optional;

/**
 * Created by Agustin on 9/29/2016.
 */
public final class DefiniteIntegralExponential extends DefiniteIntegralFunction {

    public static final FunctionDomain DOMAIN = FunctionDomainFactory.REAL_NUMBERS;

    public DefiniteIntegralExponential(double lowerBound, double upperBound, Optional<Double> result, Optional<Double> approximation) {
        super(lowerBound, upperBound, result.orElse(null), IntegrableFunctionType.EXPONENTIAL.getLabel(), new ExponentialFunction());
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