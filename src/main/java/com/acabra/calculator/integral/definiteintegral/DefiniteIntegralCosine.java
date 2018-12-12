package com.acabra.calculator.integral.definiteintegral;

import com.acabra.calculator.function.CosineFunction;
import java.util.Optional;

/**
 * Represents a Definite Integral for the Cosine function cos(x)
 */
public final class DefiniteIntegralCosine extends DefiniteIntegralFunction {

    public DefiniteIntegralCosine(double lowerLimit, double upperLimit, Optional<Double> result, Optional<Double> approx) {
        super(lowerLimit, upperLimit, result.orElse(null), IntegrableFunctionType.COS.getLabel(), new CosineFunction(null));
        this.approximation = approx.orElse(null);
    }

    @Override
    protected Double executeDefiniteIntegration() {
        return function.calculateIntegral(this.upperLimit) - function.calculateIntegral(this.lowerLimit);
    }

    @Override
    public Double evaluateOnBaseFunction(Double evalPoint) {
        return function.apply(evalPoint);
    }
}
