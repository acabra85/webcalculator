package com.acabra.calculator.integral;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.integral.approx.NumericalMethodApproximationType;
import com.acabra.calculator.integral.input.IntegrableFunctionInputParametersBuilder;
import com.acabra.calculator.integral.input.IntegrableFunctionInputParameters;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
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


    private static final Logger logger = Logger.getLogger(IntegralSolver.class);
    private final double lowerBound;
    private final double upperBound;
    private final int numThreads;
    private final int repeatedCalculations;
    private final boolean areaInscribed;
    private final IntegralFunctionType functionType;
    private final NumericalMethodApproximationType approximationMethodType;
    private final List<Double> coefficients;

    public IntegralSolver(IntegralRequest integralRequest) {
        this.lowerBound = integralRequest.getLowerBound();
        this.upperBound = integralRequest.getUpperBound();
        this.numThreads = integralRequest.getNumThreads();
        this.repeatedCalculations = Integer.parseInt(integralRequest.getRepeatedCalculations() + "");
        this.functionType = IntegralFunctionFactory.evaluateFunctionType(integralRequest.getFunctionId());
        this.approximationMethodType = IntegralFunctionFactory.evaluateApproximationMethodType(integralRequest.getApproximationMethodId());
        this.areaInscribed = integralRequest.isAreaInscribed();
        this.coefficients = Collections.unmodifiableList(integralRequest.getCoefficients());
    }

    private final Function<List<Double>, IntegrableFunction> createIntegralIntegralGivenApproximationList = subSolutions -> {
        double approximationSum = subSolutions.stream().mapToDouble(x -> x).sum();
        IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                .withApproximation(approximationSum)
                .withLowerBound(IntegralSolver.this.lowerBound)
                .withUpperBound(IntegralSolver.this.upperBound)
                .withCoefficients(IntegralSolver.this.coefficients)
                .build();
        return IntegralFunctionFactory.createIntegralFunction(IntegralSolver.this.functionType, parameters);
    };

    private CompletableFuture<IntegrableFunction> approximateUsingRiemannRectangles() {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        final IntegralSubRangeSupplier rangeProvider = new IntegralSubRangeSupplier(lowerBound, upperBound, repeatedCalculations, functionType, coefficients);
        List<CompletableFuture<Double>> integralSolutionFutures = IntStream.range(0, repeatedCalculations)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> rangeProvider.get().solveIntegralWithRiemannSequences(areaInscribed), executor))
                .collect(Collectors.toList());
        CompletableFuture<List<Double>> allDone = WebCalculatorCompletableFutureUtils.sequence(integralSolutionFutures);
        return allDone.thenApply(createIntegralIntegralGivenApproximationList);
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
                    .withCoefficients(coefficients)
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

}
