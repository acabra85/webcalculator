package com.acabra.calculator.integral;

import com.acabra.calculator.domain.IntegralRequest;

import java.util.List;
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
    private final Function<List<Double>, IntegrableFunction> listIntegrableFunctionFunction = subSolutions -> {
        double sequenceRiemannRectangleAreaSum = subSolutions.stream()
                .mapToDouble(x -> x)
                .sum();
        IntegrableFunction integralFunction = IntegralFunctionFactory.createIntegralFunction(IntegralSolver.this.functionType,
                IntegralSolver.this.lowerBound, IntegralSolver.this.upperBound, Optional.empty(), Optional.empty());
        return IntegralFunctionFactory.createIntegralFunction(IntegralSolver.this.functionType, IntegralSolver.this.lowerBound,
                IntegralSolver.this.upperBound,
                Optional.of(integralFunction.solve()),
                Optional.of(sequenceRiemannRectangleAreaSum));
    };

    public IntegralSolver(IntegralRequest integralRequest) {
        this.lowerBound = integralRequest.getLowerBound();
        this.upperBound = integralRequest.getUpperBound();
        this.numThreads = integralRequest.getNumThreads();
        this.repeatedCalculations = Integer.parseInt(integralRequest.getRepeatedCalculations() + "");
        this.functionType = IntegralFunctionFactory.evaluateFunctionType(integralRequest.getFunctionId());
        this.areaInscribed = integralRequest.isAreaInscribed();
    }

    /**
     * This method creates sub-integral functions and aggregates the result of the Riemann areas for each integral.
     * @return A future containing the integral with the total area aggregated from the sub intervals.
     */
    public CompletableFuture<IntegrableFunction> approximateSequenceRiemannArea() {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        final IntegralSubRangeSupplier rangeProvider = new IntegralSubRangeSupplier(lowerBound, upperBound, repeatedCalculations, functionType);
        List<CompletableFuture<Double>> integralSolutionFutures = IntStream.range(0, repeatedCalculations)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> rangeProvider.get().solveIntegralWithRiemannSequences(areaInscribed), executor))
                .collect(Collectors.toList());
        CompletableFuture<List<Double>> allDone = ListFuturesAggregator.sequence(integralSolutionFutures);
        return allDone.thenApply(listIntegrableFunctionFunction);
    }

}
