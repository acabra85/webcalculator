package com.acabra.calculator;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.integral.ConcurrentIntegralSolver;
import com.acabra.calculator.integral.IntegrableFunction;
import org.apache.log4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Agustin on 9/27/2016.
 */
public class Calculator {

    private final AtomicLong counter;
    private static final Logger logger = Logger.getLogger(Calculator.class);

    public Calculator() {
        counter = new AtomicLong();
    }

    public String makeCalculation(String expression) {
        try {
            logger.info("parsing expression " + expression );
            double result = ShuntingYard.solveInFixExpression(expression);
            counter.incrementAndGet();
            return trimTrailingZeros(result + "");
        } catch (Exception e) {
            logger.error(e);
            return "NaN";
        }
    }

    private String trimTrailingZeros(String result) {
        return result.endsWith(".0") ? result.substring(0, result.length() - 2) : result;
    }

    public long calculationsPerformed() {
        return counter.get();
    }

    public CompletableFuture<IntegrableFunction> resolveIntegralRequest(IntegralRequest integralRequest) {
        ConcurrentIntegralSolver concurrentIntegralSolver = new ConcurrentIntegralSolver(integralRequest);
        return concurrentIntegralSolver.resolveIntegral();
    }
}
