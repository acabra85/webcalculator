package com.acabra.calculator.function;

import com.acabra.calculator.function.command.FunctionCommand;
import com.acabra.calculator.function.command.PolynomialFunctionCommand;
import com.acabra.calculator.integral.definiteintegral.FunctionDomain;
import com.acabra.calculator.integral.definiteintegral.FunctionDomainFactory;

import java.util.Collections;
import java.util.List;

/**
 * Created by Agustin on 11/2/2016.
 */
public class PolynomialFunction extends RealFunction {

    private final List<Double> coefficients;
    private final int order;
    private final FunctionCommand command;
    private static final FunctionDomain DOMAIN = FunctionDomainFactory.REAL_NUMBERS;

    public PolynomialFunction(List<Double> coefficients) {
        this.coefficients = coefficients == null ? Collections.emptyList() : Collections.unmodifiableList(coefficients);
        this.order = this.coefficients.size();
        this.command = new PolynomialFunctionCommand(this.coefficients);
    }

    @Override
    public double evaluate(double evalPoint) {
        if (0 == order) {
            return 0.0;
        }
        if (DOMAIN.belongsDomain(evalPoint)) {
            return command.evaluate(evalPoint);
        }
        throw new UnsupportedOperationException(String.format("%.4f does not belong to functions domain", evalPoint));
    }

    @Override
    public double calculateDerivative(double evalPoint) {
        if (DOMAIN.belongsDomain(evalPoint)) {
            return command.calculateDerivative(evalPoint);
        }
        throw new UnsupportedOperationException(String.format("%.4f does not belong to functions domain", evalPoint));
    }
}
