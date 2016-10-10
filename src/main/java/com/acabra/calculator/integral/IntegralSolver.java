package com.acabra.calculator.integral;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.integral.approx.NumericalMethodApproximationType;
import com.acabra.calculator.integral.input.IntegrableFunctionInputParametersBuilder;
import com.acabra.calculator.integral.input.IntegrableFunctionInputParameters;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Agustin on 9/29/2016.
 */
public class IntegralSolver {

    private final double lowerBound;
    private final double upperBound;
    private final int numThreads;
    private final int repeatedCalculations;
    private final boolean areaInscribed;

    private final IntegralFunctionType functionType;
    private final NumericalMethodApproximationType approximationMethodType;

    private final Function<List<Double>, IntegrableFunction> createIntegralIntegralGivenApproximationList = subSolutions -> {
        double approximationSum = subSolutions.stream().mapToDouble(x -> x).sum();
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withApproximation(approximationSum)
                .withLowerBound(IntegralSolver.this.lowerBound)
                .withUpperBound(IntegralSolver.this.upperBound)
                .build();
        return IntegralFunctionFactory.createIntegralFunction(IntegralSolver.this.functionType, parameters);
    };

    public IntegralSolver(IntegralRequest integralRequest) {
        this.lowerBound = integralRequest.getLowerBound();
        this.upperBound = integralRequest.getUpperBound();
        this.numThreads = integralRequest.getNumThreads();
        this.repeatedCalculations = Integer.parseInt(integralRequest.getRepeatedCalculations() + "");
        this.functionType = IntegralFunctionFactory.evaluateFunctionType(integralRequest.getFunctionId());
        this.approximationMethodType = IntegralFunctionFactory.evaluateApproximationMethodType(integralRequest.getApproximationMethodId());
        this.areaInscribed = integralRequest.isAreaInscribed();
    }

    /**
     * This method creates sub-integral functions and aggregates the result of the Riemann areas for each integral.
     * @return A future containing the integral with the total area aggregated from the sub intervals.
     */
    public CompletableFuture<IntegrableFunction> approximateAreaUnderCurve() {
        if (lowerBound == upperBound) {
            IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                    .withLowerBound(lowerBound)
                    .withUpperBound(upperBound)
                    .withApproximation(0.0)
                    .withIntegrationResult(0.0)
                    .build();
            return CompletableFuture.completedFuture(IntegralFunctionFactory.createIntegralFunction(functionType, parameters));
        }
        switch (approximationMethodType) {
            case RIEMANN:
                return approximateUsingRiemannRectangles();
            default:
                throw new NoSuchElementException("unable to find the given approximation method");
        }

    }

    private CompletableFuture<IntegrableFunction> approximateUsingRiemannRectangles() {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        final IntegralSubRangeSupplier rangeProvider = new IntegralSubRangeSupplier(lowerBound, upperBound, repeatedCalculations, functionType);
        List<CompletableFuture<Double>> integralSolutionFutures = IntStream.range(0, repeatedCalculations)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> rangeProvider.get().solveIntegralWithRiemannSequences(areaInscribed), executor))
                .collect(Collectors.toList());
        CompletableFuture<List<Double>> allDone = ListFuturesAggregator.sequence(integralSolutionFutures);
        return allDone.thenApply(createIntegralIntegralGivenApproximationList);
    }

}
