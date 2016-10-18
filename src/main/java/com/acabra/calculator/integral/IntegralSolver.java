package com.acabra.calculator.integral;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.integral.approx.NumericalMethodApproximationType;
import com.acabra.calculator.integral.approx.RiemannSolver;
import com.acabra.calculator.integral.approx.SimpsonSolver;
import com.acabra.calculator.integral.function.IntegrableFunction;
import com.acabra.calculator.integral.function.FunctionFactory;
import com.acabra.calculator.integral.function.IntegrableFunctionType;
import com.acabra.calculator.integral.input.IntegrableFunctionInputParameters;
import com.acabra.calculator.integral.input.IntegrableFunctionInputParametersBuilder;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Agustin on 9/29/2016.
 */
public class IntegralSolver {


    private static final Logger logger = Logger.getLogger(IntegralSolver.class);
    private final double lowerLimit;
    private final double upperLimit;
    private final int numThreads;
    private final int repeatedCalculations;
    private final boolean areaInscribed;
    private final IntegrableFunctionType functionType;
    private final NumericalMethodApproximationType approximationMethodType;
    private final List<Double> coefficients;

    public IntegralSolver(IntegralRequest integralRequest) {
        this.lowerLimit = integralRequest.getLowerLimit();
        this.upperLimit = integralRequest.getUpperLimit();
        this.numThreads = integralRequest.getNumThreads();
        this.repeatedCalculations = Integer.parseInt(integralRequest.getRepeatedCalculations() + "");
        this.functionType = FunctionFactory.evaluateFunctionType(integralRequest.getFunctionId());
        this.approximationMethodType = FunctionFactory.evaluateApproximationMethodType(integralRequest.getApproximationMethodId());
        this.areaInscribed = integralRequest.isAreaInscribed();
        this.coefficients = Collections.unmodifiableList(integralRequest.getCoefficients());
    }

    /**
     * This method creates sub-integral functions and aggregates the result of the Riemann areas for each integral.
     * @return A future containing the integral with the total area aggregated from the sub intervals.
     */
    public CompletableFuture<IntegrableFunction> approximateAreaUnderCurve() {
        if (lowerLimit == upperLimit) {
            IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                    .withLowerLimit(lowerLimit)
                    .withUpperLimit(upperLimit)
                    .withApproximation(0.0)
                    .withCoefficients(coefficients)
                    .withIntegrationResult(0.0)
                    .build();
            return CompletableFuture.completedFuture(FunctionFactory.createIntegralFunction(functionType, parameters));
        }
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        switch (approximationMethodType) {
            case RIEMANN:
                return new RiemannSolver(lowerLimit, upperLimit, coefficients, functionType, areaInscribed).approximate(repeatedCalculations, executor);
            case SIMPSON:
                return new SimpsonSolver(lowerLimit, upperLimit, coefficients, functionType).approximate(repeatedCalculations, executor);
            default:
                throw new NoSuchElementException("unable to solve using given result method");
        }

    }

}
