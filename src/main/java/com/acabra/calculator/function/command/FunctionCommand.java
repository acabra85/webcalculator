package com.acabra.calculator.function.command;

import java.util.List;

/**
 * Created by Agustin on 11/2/2016.
 */
public interface FunctionCommand {

    double evaluate(double value);

    double calculateDerivative(double value);
}
