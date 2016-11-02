package com.acabra.calculator.function.command;

import com.google.common.util.concurrent.AtomicDouble;

import java.util.List;

/**
 * Created by Agustin on 11/2/2016.
 */
public class PolynomialFunctionCommand implements FunctionCommand {

    private final List<Double> coefficients;

    public PolynomialFunctionCommand(List<Double> coefficients) {
        this.coefficients = coefficients;
    }

    @Override
    public double evaluate(double evalPoint) {
        AtomicDouble total = new AtomicDouble();
        for(int exponent = 0; exponent < coefficients.size(); exponent++) {
            if (coefficients.get(exponent) != 0) {
                total.addAndGet(Math.pow(evalPoint, exponent) * coefficients.get(exponent));
            }
        }
        return total.get();
    }

    @Override
    public double calculateDerivative(double evalPoint) {
        AtomicDouble total = new AtomicDouble();
        for (int i = 1; i < coefficients.size(); i++) {
            if (coefficients.get(i) != 0.0) {
                total.addAndGet(i * coefficients.get(i) * Math.pow(evalPoint, i-1));
            }
        }
        return total.get();
    }
}
