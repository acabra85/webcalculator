package com.acabra.calculator.integral;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.integral.approx.NumericalMethodAreaApproximationType;
import com.acabra.calculator.integral.approx.RiemannSolver;
import com.acabra.calculator.integral.approx.SimpsonSolver;
import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralFunction;
import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralFunctionFactory;
import com.acabra.calculator.integral.definiteintegral.IntegrableFunctionType;
import com.acabra.calculator.integral.input.IntegrableFunctionInputParameters;
import com.acabra.calculator.integral.input.IntegrableFunctionInputParametersBuilder;
import com.acabra.calculator.util.WebCalculatorConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Agustin on 9/29/2016.
 */
@Slf4j
public class IntegralSolver {


    private final double lowerLimit;
    private final double upperLimit;
    private final int numThreads;
    private final int repeatedCalculations;
    private final boolean areaInscribed;
    private final IntegrableFunctionType functionType;
    private final NumericalMethodAreaApproximationType approximationMethodType;
    private final List<Double> coefficients;

    public IntegralSolver(IntegralRequest integralRequest) {
        this.lowerLimit = integralRequest.getLowerLimit();
        this.upperLimit = integralRequest.getUpperLimit();
        this.numThreads = integralRequest.getNumThreads();
        this.repeatedCalculations = Integer.parseInt(integralRequest.getRepeatedCalculations() + "");
        this.functionType = DefiniteIntegralFunctionFactory.evaluateFunctionType(integralRequest.getFunctionId());
        this.approximationMethodType = DefiniteIntegralFunctionFactory.evaluateApproximationMethodType(integralRequest.getApproximationMethodId());
        this.areaInscribed = integralRequest.isAreaInscribed();
        this.coefficients = Collections.unmodifiableList(integralRequest.getCoefficients());
    }

    /**
     * This method creates sub-integral functions and aggregates the result of the Riemann areas for each integral.
     * @return A future containing the integral with the total area aggregated from the sub intervals.
     */
    public CompletableFuture<DefiniteIntegralFunction> approximateAreaUnderCurve() {
        if (Math.abs(lowerLimit - upperLimit) < WebCalculatorConstants.ACCURACY_EPSILON) {
            IntegrableFunctionInputParameters parameters = new IntegrableFunctionInputParametersBuilder()
                    .withLowerLimit(lowerLimit)
                    .withUpperLimit(upperLimit)
                    .withApproximation(0.0)
                    .withCoefficients(coefficients)
                    .withIntegrationResult(0.0)
                    .build();
            return CompletableFuture.completedFuture(DefiniteIntegralFunctionFactory.createIntegralFunction(functionType, parameters));
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
