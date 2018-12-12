package com.acabra.calculator.function;

import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralFunction;
import com.acabra.calculator.integral.definiteintegral.IntegrableFunctionType;
import java.util.Optional;

/**
 * Class representing a solver for definite integrals of Sine function
 */
public class DefiniteIntegralSine extends DefiniteIntegralFunction {

    public DefiniteIntegralSine(double lowerLimit, double upperLimit, Optional<Double> result, Optional<Double> approx) {
        super(lowerLimit, upperLimit, result.orElse(null), IntegrableFunctionType.SINE.getLabel(), new SineFunction(null));
        this.approximation = approx.orElse(null);
    }

    @Override
    public Double executeDefiniteIntegration() {
        return function.calculateIntegral(this.upperLimit) - function.calculateIntegral(this.lowerLimit);
    }

    @Override
    public Double evaluateOnBaseFunction(Double evalPoint) {
        return function.apply(evalPoint);
    }
}
