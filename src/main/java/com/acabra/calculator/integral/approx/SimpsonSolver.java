package com.acabra.calculator.integral.approx;

import com.acabra.calculator.integral.IntegralSubRangeSupplier;
import com.acabra.calculator.integral.WebCalculatorCompletableFutureUtils;
import com.acabra.calculator.integral.function.IntegrableFunction;
import com.acabra.calculator.integral.function.IntegrableFunctionType;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Agustin on 10/17/2016.
 */
public class SimpsonSolver extends AreaApproximatorNumericalMethod {

    public SimpsonSolver(double lowerLimit, double upperLimit, List<Double> coefficients, IntegrableFunctionType functionType) {
        super(lowerLimit, upperLimit, coefficients, functionType);
    }

    /**
     * This method evaluates the integral using the Simpson's Rule(Cavalieri y Gregory).
     *
     * @param function the function to calculate the Riemann Rectangle
     * @see <a href="https://en.wikipedia.org/wiki/Simpson%27s_rule">Simpson's Rule<a/>
     * @return the area of the calculated rectangle
     */
    @Override
    protected Double applyRule(IntegrableFunction function) {
        double lowerLimit = function.getLowerLimit();
        double upperLimit = function.getUpperLimit();
        double midPoint = lowerLimit + (upperLimit - lowerLimit) / 2.0;
        return function.evaluate(lowerLimit) + ( 4 * function.evaluate(midPoint) ) + function.evaluate(upperLimit);
    }

    @Override
    public CompletableFuture<IntegrableFunction> approximate(final int repeatedCalculations, final ExecutorService executor) {
        if (repeatedCalculations%2 == 0) {
            final double simpsonDelta = (upperLimit - lowerLimit) /  (3.0 * repeatedCalculations); // h/3 where h = (b-a)/n
            int subIntervals = repeatedCalculations / 2;
            final IntegralSubRangeSupplier functionSupplier = createRangeSupplier(subIntervals);
            List<CompletableFuture<Double>> simpsonResolvedValues = IntStream.range(0, subIntervals)
                    .mapToObj(i -> CompletableFuture.supplyAsync(() -> applyRule(functionSupplier.get()), executor))
                    .collect(Collectors.toList());
            CompletableFuture<List<Double>> allDone = WebCalculatorCompletableFutureUtils.sequence(simpsonResolvedValues);
            return allDone.thenApply(list -> {
                Double simpsonApproximation = (list.stream().reduce(0.0, Double::sum)) * simpsonDelta;
                return provideIntegralFunctionWithApproximation(simpsonApproximation);
            });
        }
        throw new UnsupportedOperationException("unable to apply Simpson's rule to odd number of sub-areas");
    }
}
