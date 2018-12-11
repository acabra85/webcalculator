package com.acabra.calculator;

import com.google.common.base.Stopwatch;
import com.acabra.calculator.domain.CalculationHistoryRecord;
import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.integral.WebCalculatorCompletableFutureUtils;
import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralFunction;
import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralFunctionFactory;
import com.acabra.calculator.response.CalculationResponse;
import com.acabra.calculator.response.HistoryResponse;
import com.acabra.calculator.response.SimpleResponse;
import com.acabra.calculator.response.WebCalculatorFactoryResponse;
import com.acabra.calculator.response.WebCalculatorFactorySimpleResponse;
import com.acabra.calculator.util.ResultFormatter;
import com.acabra.calculator.util.WebCalculatorValidation;
import com.acabra.calculator.view.WebCalculatorRenderer;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;

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

    public WebCalculatorManager(WebCalculatorRenderer renderer) {
        this.history = new ConcurrentHashMap<>(16, 0.9f, 1);
        this.calculator = new Calculator();
        this.renderer = renderer;
        this.counter = new AtomicLong();
    }

    private void appendCalculationHistory(CalculationResponse calculationResponse, String token) {
        CalculationHistoryRecord calculationHistoryRecord = history.get(token);
        if (null != calculationHistoryRecord) {
            calculationHistoryRecord.append(calculationResponse);
        } else {
            history.putIfAbsent(token, new CalculationHistoryRecord(calculationResponse));
        }
    }

    private Function<DefiniteIntegralFunction, CalculationResponse> retrieveIntegralCalculationResponse(final IntegralRequest integralRequest, final String token, final long responseTime) {
        return  solvedIntegral -> {
            CalculationResponse calculationResponse;
            String expression = ResultFormatter.formatIntegralRequest(solvedIntegral.toString(),
                    integralRequest.getRepeatedCalculations(), integralRequest.getNumThreads());
            calculationResponse = WebCalculatorFactoryResponse
                    .createCalculationResponse(counter.getAndIncrement(), expression, responseTime, solvedIntegral,
                            DefiniteIntegralFunctionFactory.evaluateApproximationMethodType(integralRequest.getApproximationMethodId()).getLabel());
            appendCalculationHistory(calculationResponse, token);
            return calculationResponse;
        };
    }


    private BiFunction<
                CompletableFuture<DefiniteIntegralFunction>,
                Throwable,
                CompletableFuture<CalculationResponse>> provideBiFunctionHandler(final IntegralRequest integralRequest, String token, Stopwatch stopwatch) {
        return (solvedIntegral, exception) -> {
            if (exception != null) {
                logger.error(exception);
                String expression = ResultFormatter.formatIntegralRequest(solvedIntegral.toString(),
                        integralRequest.getRepeatedCalculations(), integralRequest.getNumThreads());
                CalculationResponse failed = WebCalculatorFactoryResponse.createFailedCalculationResponse(counter.getAndIncrement(),
                        expression, stopwatch.elapsed(TimeUnit.NANOSECONDS), exception.getCause().getMessage());
                return CompletableFuture.completedFuture(failed);
            }
            return solvedIntegral.thenApply(retrieveIntegralCalculationResponse(integralRequest, token, stopwatch.elapsed(TimeUnit.NANOSECONDS)));
        };
    }

    /**
     * Validates the integral request and request the calculator object to provide the result
     * @param integralRequest a integrable function request
     * @param token a session token to group history results
     * @return a future representing the integrable function solved.
     */
    public CompletableFuture<CalculationResponse> processIntegralCalculation(final IntegralRequest integralRequest, final String token) {
        final Stopwatch stopwatch = Stopwatch.createStarted();
        WebCalculatorValidation.validateIntegralRequest(integralRequest);
        CompletableFuture<DefiniteIntegralFunction> future = calculator.approximateAreaUnderCurve(integralRequest);
        return WebCalculatorCompletableFutureUtils.withFallbackDifferentResponse(future,
                provideBiFunctionHandler(integralRequest, token, stopwatch),
                x -> x.thenApply(retrieveIntegralCalculationResponse(integralRequest, token, stopwatch.elapsed(TimeUnit.NANOSECONDS))));
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
        CalculationResponse calculationResponse = WebCalculatorFactoryResponse.createCalculationResponse(counter.getAndIncrement(), expression, calculator.solveArithmeticExpression(parsedExpression).toString(), stopwatch.elapsed(TimeUnit.NANOSECONDS), "Arithmetic");
        appendCalculationHistory(calculationResponse, token);
        return calculationResponse;
    }

    /**
     * Retrieves a history rendered response of requests made with the specified token
     * @param token the token used for the requests
     * @return  a RenderedHistoryResponse object containing the rendered result.
     */
    public SimpleResponse provideRenderedHistoryResponse(String token) {
        List<CalculationResponse> calculationResponseList = provideRawHistory(token);
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
     * @param token token identifying the requests made
     * @return
     */
    protected synchronized List<CalculationResponse> provideRawHistory(String token) {
        if (history.containsKey(token)) {
            return history.get(token).getCalculationHistory();
        }
        throw new NoSuchElementException("No request history found with token: " + token);
    }

    /**
     * Retrieves a list of requests made with a specified token
     * @param token token identifying the requests made
     * @return
     */
    public SimpleResponse provideHistoryResponse(String token) {
        if (history.containsKey(token)) {
            return new HistoryResponse(counter.getAndIncrement(), history.get(token).getCalculationHistory());
        }
        throw new NoSuchElementException("No request history found with token: " + token);
    }

    public CompletableFuture<Integer> cleanExpiredEntries(LocalDateTime lastRun, int expirationPolicy, ChronoUnit unit) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Running cleaning ...");
            List<String> expiredKeys = history.entrySet().stream()
                    .filter(entry -> {
                        long difference = Math.abs(lastRun.until(entry.getValue().getLastUsed(), unit));
                        return difference > expirationPolicy;
                    })
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            expiredKeys.forEach(expiredKey -> {
                history.remove(expiredKey);
            });
            logger.info("Cleaned entries: " + expiredKeys.size());
            return expiredKeys.size();
        });
    }

    /**
     * Retrieves the size of the history table
     * @return
     */
    public int countHistorySize() {
        return history.size();
    }
}
