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
public class ConcurrentIntegralSolver {

    private final double lowerBound;
    private final double upperBound;
    private final int numThreads;
    private final int repeatedCalculations;
    private final IntegralFunctionType functionType;

    public ConcurrentIntegralSolver(IntegralRequest integralRequest) {
        this.lowerBound = integralRequest.getLowerBound();
        this.upperBound = integralRequest.getUpperBound();
        this.numThreads = integralRequest.getNumThreads();
        this.repeatedCalculations = integralRequest.getRepeatedCalculations();
        this.functionType = IntegralFunctionFactory.evaluateFunctionType(integralRequest.getFunctionId());
    }

    private static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v -> futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList())
        );
    }

    public CompletableFuture<IntegrableFunction> resolveIntegral() {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<IntegrableFunction> subRanges = IntegralSubRangeProvider.provideIntegralsOnSubRanges(lowerBound, upperBound, repeatedCalculations, functionType);
        List<CompletableFuture<Double>> integralSolutionFutures = subRanges.stream()
                .map(integral -> CompletableFuture.supplyAsync(() -> integral.solve(), executor))
                .collect(Collectors.toList());
        CompletableFuture<List<Double>> allDone = sequence(integralSolutionFutures);
        return allDone.thenApply(subSolutions -> {
            double result = subSolutions.stream()
                    .mapToDouble(x -> x)
                    .sum();
            return IntegralFunctionFactory.createIntegralFunction(functionType, lowerBound, upperBound, Optional.of(result));
        } );
    }
}
