package com.acabra.calculator;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.integral.IntegrableFunction;
import com.acabra.calculator.response.CalculationResponse;
import com.acabra.calculator.response.SimpleResponse;
import com.acabra.calculator.response.WebCalculatorFactoryResponse;
import com.acabra.calculator.response.WebCalculatorFactorySimpleResponse;
import com.acabra.calculator.util.ResultFormatter;
import com.acabra.calculator.util.WebCalculatorValidation;
import com.acabra.calculator.view.WebCalculatorRenderer;
import com.google.common.base.Stopwatch;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Agustin on 9/27/2016.
 * Controller of the system receives requests from the Endpoint Resource class and delegates execution and then
 * renders the results.
 */
public class WebCalculatorManager {

    private static final Logger logger = Logger.getLogger(WebCalculatorManager.class);
    private final Calculator calculator;
    private final ConcurrentHashMap<String, CalculationHistoryRecord> history;
    private final WebCalculatorRenderer renderer;
    private final AtomicLong counter;
    private final AtomicInteger historySize;

    public WebCalculatorManager(WebCalculatorRenderer renderer) {
        this.history = new ConcurrentHashMap<>(16, 0.9f, 1);
        this.calculator = new Calculator();
        this.renderer = renderer;
        this.counter = new AtomicLong();
        this.historySize = new AtomicInteger();
    }

    private synchronized void appendCalculationHistory(CalculationResponse calculationResponse, String token) {
        if (!history.containsKey(token)) {
            history.put(token, new CalculationHistoryRecord());
        }
        history.get(token).append(calculationResponse);
        historySize.incrementAndGet();
    }

    private Function<IntegrableFunction, CalculationResponse> retrieveIntegralCalculationResponse(final IntegralRequest integralRequest, final String token, final long responseTime) {
        return  solvedIntegral -> {
            String expression = ResultFormatter.formatIntegralRequest(
                    solvedIntegral.getLabel(),
                    integralRequest.getLowerBound() + "", integralRequest.getUpperBound() + "",
                    integralRequest.getRepeatedCalculations(), integralRequest.getNumThreads());
            CalculationResponse calculationResponse = WebCalculatorFactoryResponse.createCalculationResponse(counter.getAndIncrement(), expression, responseTime, solvedIntegral, "Integral");
            appendCalculationHistory(calculationResponse, token);
            return calculationResponse;
        };
    }

    /**
     * Validates the integral request and request the calculator object to provide the result
     * @param integralRequest a integrable function request
     * @param token a session token to group history results
     * @return a future representing the integrable function solved.
     */
    public CompletableFuture<CalculationResponse> processExponentialIntegralCalculation(final IntegralRequest integralRequest, final String token) {
        final Stopwatch stopwatch = Stopwatch.createStarted();
        WebCalculatorValidation.validateIntegralRequest(integralRequest);
        return calculator.resolveIntegralApproximateRiemannSequenceRequest(integralRequest)
                .thenApply(retrieveIntegralCalculationResponse(integralRequest, token, stopwatch.elapsed(TimeUnit.NANOSECONDS)));
    }

    /**
     * Validates the arithmetic expression to be calculated and creates
     * @param expression the arithmetic expression
     * @param token the session token to track history of requests
     * @return a response object containing the results and the requested expression
     */
    public SimpleResponse processArithmeticCalculation(String expression, String token) {
        final Stopwatch stopwatch = Stopwatch.createStarted();
        String parsedExpression = expression.replace("sqrt", Operator.SQRT.getLabel());
        WebCalculatorValidation.validateArithmeticExpression(parsedExpression);
        CalculationResponse calculationResponse = WebCalculatorFactoryResponse.createCalculationResponse(counter.getAndIncrement(), expression, calculator.solveArithmeticExpression(parsedExpression), stopwatch.elapsed(TimeUnit.NANOSECONDS), "Arithmetic");
        appendCalculationHistory(calculationResponse, token);
        return calculationResponse;
    }

    /**
     * Retrieves a history rendered response of requests made with the specified token
     * @param token the token used for the requests
     * @return  a TableHistoryResponse object containing the rendered result.
     */
    public SimpleResponse provideRenderedHistoryResult(String token) {
        List<CalculationResponse> calculationResponseList = provideCalculationHistory(token);
        String table = renderer.renderCalculationHistory(calculationResponseList, true);
        return WebCalculatorFactorySimpleResponse.createTableResponse(counter.getAndIncrement(), table);
    }

    /**
     * Creates and returns a token, to use for making requests to the server
     * @return a session token response
     */
    public SimpleResponse provideSessionToken() {
        return WebCalculatorFactorySimpleResponse.createTokenResponse(counter.getAndIncrement());
    }

    /**
     * Retrieves a list of requests made with a specified token
     * @param token
     * @return
     */
    synchronized List<CalculationResponse> provideCalculationHistory(String token) {
        return history.containsKey(token) ? history.get(token).getCalculationHistory() : Collections.emptyList();
    }

    public CompletableFuture<Integer> cleanExpiredEntries(LocalDateTime lastRun, int expirationPolicy, ChronoUnit unit) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Running cleaning ...");
            List<String> expiredKeys = history.entrySet().stream()
                    .filter(entry -> {
                        long difference = Math.abs(lastRun.until(entry.getValue().getLastUsed(), unit));
                        logger.info(entry.getKey() + "-> difference :" + difference);
                        return difference > expirationPolicy;
                    })
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            expiredKeys.forEach(expiredKey -> {
                history.remove(expiredKey);
                historySize.decrementAndGet();});
            return expiredKeys.size();
        });
    }

    /**
     * Retrieves the size of the history table
     * @return
     */
    public int countHistorySize() {
        return historySize.get();
    }
}
