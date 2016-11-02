package com.acabra.calculator.function.command;

/**
 * Created by Agustin on 11/2/2016.
 */
public class SinFunctionCommand implements FunctionCommand {

    @Override
    public double evaluate(double value) {
        return Math.sin(value);
    }

    @Override
    public double calculateDerivative(double value) {
        return Math.cos(value);
    }
}