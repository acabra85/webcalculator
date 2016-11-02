package com.acabra.calculator.function;

/**
 * Created by Agustin on 11/2/2016.
 */
public abstract class RealFunction {

    /**
     * Evaluates the function on a single domainPoint, it is recommended to
     * watch for domain constraints of the function.
     * @param evalPoint a domainPoint belonging to the domain of the function.
     * @return the evaluated function on the requested domainPoint
     * @throws UnsupportedOperationException when the evalPoint does not belong to functions domain
     */
    public abstract double evaluate(double evalPoint);

    /**
     * Calculates the value of the derivative in the requested domain point
     * @param evalPoint the domain point to calculate the derivative value
     * @return the evaluated derivative function on the requested domainPoint
     * @throws UnsupportedOperationException when the evalPoint does not belong to functions domain
     */
    public abstract double calculateDerivative(double evalPoint);

}
