package com.acabra.calculator.integral.definiteintegral;

import com.acabra.calculator.function.LogarithmicFunction;
import java.util.Optional;

/**
 * Created by Agustin on 10/11/2016.
 */
public final class DefiniteIntegralLogarithmic extends DefiniteIntegralFunction {

    DefiniteIntegralLogarithmic(double lowerLimit, double upperLimit, Optional<Double> result, Optional<Double> approx) {
        super(lowerLimit, upperLimit, result.orElse(null), IntegrableFunctionType.LOGARITHMIC.getLabel(), new LogarithmicFunction(null));
        this.approximation = approx.orElse(null);
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
