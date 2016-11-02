package com.acabra.calculator.function;

import com.acabra.calculator.function.command.CosFunctionCommand;
import com.acabra.calculator.function.command.FunctionCommand;

/**
 * Created by Agustin on 11/2/2016.
 */
public class CosFunction extends RealFunction {

    private final FunctionCommand command;

    public CosFunction() {
        this.command = new CosFunctionCommand();
    }

    @Override
    public double evaluate(double evalPoint) {
        return command.evaluate(evalPoint);
    }

    @Override
    public double calculateDerivative(double evalPoint) {
        return command.calculateDerivative(evalPoint);
    }
}
