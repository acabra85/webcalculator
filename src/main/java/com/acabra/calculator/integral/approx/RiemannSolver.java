package com.acabra.calculator.integral.approx;

import com.acabra.calculator.integral.IntegralSubRangeSupplier;
import com.acabra.calculator.integral.WebCalculatorCompletableFutureUtils;
import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralFunction;
import com.acabra.calculator.integral.definiteintegral.IntegrableFunctionType;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Agustin on 10/17/2016.
 */
public class RiemannSolver extends AreaApproximatorNumericalMethod {

    private final boolean inscribed;

    public RiemannSolver(double lowerLimit, double upperLimit, List<Double> coefficients, IntegrableFunctionType functionType, boolean inscribed) {
        super(lowerLimit, upperLimit, coefficients, functionType);
        this.inscribed = inscribed;
    }

    /**
     * This method evaluates the integral using
     * the Riemann Sequences technique by approximating either inscribed or
     * circumscribed rectangles from the graph.
     *
     * @param function the function to calculate the Riemann Rectangle
     * @see <a href="https://en.wikipedia.org/wiki/Riemann_integral">Riemann Integral<a/>
     * @return the area of the calculated rectangle
     */
    @Override
    public Double applyRule(DefiniteIntegralFunction function) {
        /*TODO Areas under the x-axis should be taken in count
               this runs on the assumption that the area evaluated is fully under the x-axis or either
               above the x-axis, to solve discrepancy split intervals and make proper area subtraction.
          */
        double upperLimit = function.getUpperLimit();
        double lowerLimit = function.getLowerLimit();
        double width = Math.abs(upperLimit - lowerLimit);
        double evaluatedLower = function.evaluate(lowerLimit);
        double evaluatedUpper = function.evaluate(upperLimit);
        double evAbsLower = Math.abs(evaluatedLower);
        double evAbsUpper = Math.abs(evaluatedUpper);
        double min = Math.min(evAbsLower, evAbsUpper);
        double max = Math.max(evAbsLower, evAbsUpper);
        double height = inscribed ? min : max;
        double signLower = evaluatedLower >= 0 ? 1.0 : -1.0;
        double signUpper = evaluatedUpper >= 0 ? 1.0 : -1.0;
        double sign = inscribed ? (min == evAbsLower ? signLower : signUpper) : (max == evAbsLower ? signLower : signUpper);
        return width * height * (sign);
    }

    @Override
    public CompletableFuture<DefiniteIntegralFunction> approximate(int repeatedCalculations, ExecutorService executor) {
        final IntegralSubRangeSupplier rangeProvider = createRangeSupplier(repeatedCalculations);
        List<CompletableFuture<Double>> integralSolutionFutures = IntStream.range(0, repeatedCalculations)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> applyRule(rangeProvider.get()), executor))
                .collect(Collectors.toList());
        CompletableFuture<List<Double>> allDone = WebCalculatorCompletableFutureUtils.sequence(integralSolutionFutures);
        return allDone.thenApply(
                list -> provideIntegralFunctionWithApproximation(list.stream().reduce(0.0, Double::sum))
        );
    }
}
