package com.acabra.calculator.integral;

import com.acabra.calculator.domain.IntegralRequest;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Created by Agustin on 9/29/2016.
 */
public class IntegralSolver {

    private final double lowerBound;
    private final double upperBound;
    private final int numThreads;
    private final int repeatedCalculations;
    private final IntegralFunctionType functionType;

    public IntegralSolver(IntegralRequest integralRequest) {
        this.lowerBound = integralRequest.getLowerBound();
        this.upperBound = integralRequest.getUpperBound();
        this.numThreads = integralRequest.getNumThreads();
        this.repeatedCalculations = integralRequest.getRepeatedCalculations();
        this.functionType = IntegralFunctionFactory.evaluateFunctionType(integralRequest.getFunctionId());
    }

    /**
     * Transforms a list of futures to a future containing a list of doubles
     * @param futures the list of futures to aggregate
     * @return a future representing the list of doubles to aggregate.
     */
    private static CompletableFuture<List<Double>> sequence(List<CompletableFuture<Double>> futures) {
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v -> futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList())
        );
    }

    /**
     * This method creates sub-integral functions and aggregates the result of the Riemann areas for each integral.
     * @param inscribedArea indicates if the rectangle should be inscribed otherwise will be circumscribed
     * @return A future containing the integral with the total area aggregated from the sub intervals.
     */
    public CompletableFuture<IntegrableFunction> approximateSequenceRiemannArea(final boolean inscribedArea) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<IntegrableFunction> subRanges = IntegralSubRangeProvider.provideIntegralsOnSubRanges(lowerBound, upperBound, repeatedCalculations, functionType);
        List<CompletableFuture<Double>> integralSolutionFutures = subRanges.stream()
                .map(integral -> CompletableFuture.supplyAsync(() -> integral.solveIntegralWithRiemannSequences(inscribedArea), executor))
                .collect(Collectors.toList());
        CompletableFuture<List<Double>> allDone = sequence(integralSolutionFutures);
        return allDone.thenApply(subSolutions -> {
            double sequenceRiemannRectangleAreaSum = subSolutions.stream()
                    .mapToDouble(x -> x)
                    .sum();
            IntegrableFunction integralFunction = IntegralFunctionFactory.createIntegralFunction(functionType, lowerBound, upperBound, Optional.empty());
            return IntegralFunctionFactory.createFullySolvedIntegralFunction(functionType, lowerBound, upperBound, integralFunction.solve(), sequenceRiemannRectangleAreaSum);
        } );
    }
}
