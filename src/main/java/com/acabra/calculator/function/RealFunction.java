package com.acabra.calculator.function;

import com.acabra.calculator.integral.definiteintegral.FunctionDomain;
import java.util.function.Function;

/**
 * Created by Agustin on 11/2/2016.
 */
public abstract class RealFunction implements Function<Double, Double> {

    public final FunctionDomain domain;

    public RealFunction(FunctionDomain domain) {
        this.domain = domain;
    }

    /**
     * Evaluates the function on a single domainPoint, it is recommended to
     * watch for DOMAIN constraints of the function.
     * @param evalPoint a domainPoint belonging to the DOMAIN of the function.
     * @return the evaluated function on the requested domainPoint
     * @throws UnsupportedOperationException when the evalPoint does not belong to function's DOMAIN
     */
    public abstract Double apply(Double evalPoint);

    /**
     * Calculates the value of the derivative in the requested DOMAIN point
     * @param evalPoint the DOMAIN point to calculate the derivative value
     * @return the evaluated derivative function on the requested domainPoint
     * @throws UnsupportedOperationException when the evalPoint does not belong to function's DOMAIN
     */
    public abstract Double calculateDerivative(Double evalPoint);

    /**
     * returns the result of evaluating the integral function on the given point
     * @param evalPoint the point, must belong to the DOMAIN of the integrating function
     * @return the result of evaluating the integral on the given point
     */
    public abstract Double calculateIntegral(Double evalPoint);

}
