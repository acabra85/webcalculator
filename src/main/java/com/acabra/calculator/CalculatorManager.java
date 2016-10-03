package com.acabra.calculator;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.response.CalculationResponse;
import com.acabra.calculator.response.SimpleResponse;
import com.acabra.calculator.response.WebCalculatorFactoryResponse;
import com.acabra.calculator.util.ResultFormatter;
import com.acabra.calculator.util.WebCalculatorValidation;
import com.acabra.calculator.view.WebCalculatorRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Agustin on 9/27/2016.
 * Controller of the system receives requests from the Endpoint Resource class and delegates execution and then
 * renders the results.
 */
public class CalculatorManager {

    private final Calculator calculator;
    private final HashMap<String, List<CalculationResponse>> history;
    private final WebCalculatorRenderer renderer;
    private AtomicLong counter;

    public CalculatorManager(WebCalculatorRenderer renderer) {
        this.history = new HashMap<>();
        this.calculator = new Calculator();
        this.renderer = renderer;
        counter = new AtomicLong();
    }

    /**
     * Validates the integral request and request the calculator object to provide the result
     * @param integralRequest a integrable function request
     * @param token a session token to group history results
     * @return a future representing the integrable function solved.
     */
    public CompletableFuture<CalculationResponse> processExponentialIntegralCalculation(IntegralRequest integralRequest, String token) {
        WebCalculatorValidation.validateIntegralRequest(integralRequest);
        return calculator.resolveIntegralApproximateRiemannSequenceRequest(integralRequest)
                .thenApply(solvedIntegral ->
                        appendCalculationHistory(token,
                                ResultFormatter.formatIntegralRequest(solvedIntegral.getLabel(),
                                        integralRequest.getLowerBound()+"", integralRequest.getUpperBound()+"",
                                        integralRequest.getRepeatedCalculations(), integralRequest.getNumThreads()),
                                solvedIntegral.getSequenceRiemannRectangle(),
                                Double.toString(solvedIntegral.getResult())));
    }

    /**
     * Validates the arithmetic expression to be calculated and creates
     * @param expression the arithmetic expression
     * @param token the session token to track history of requests
     * @return a response object containing the results and the requested expression
     */
    public SimpleResponse processArithmeticCalculation(String expression, String token) {
        String parsedExpression = expression.replace("sqrt", Operator.SQRT.getLabel());
        WebCalculatorValidation.validateArithmeticExpression(parsedExpression);
        double result = calculator.solveArithmeticExpression(parsedExpression);
        return appendCalculationHistory(token, expression, result, "");
    }

    /**
     * Retrieves a history rendered response of requests made with the specified token
     * @param token the token used for the requests
     * @return  a TableHistoryResponse object containing the rendered result.
     */
    public SimpleResponse provideRenderedHistoryResult(String token) {
        List<CalculationResponse> calculationResponseList = provideCalculationHistory(token);
        String table = renderer.renderCalculationHistory(calculationResponseList, true);
        return WebCalculatorFactoryResponse.createTableResponse(counter.getAndIncrement(), table);
    }

    /**
     * Creates and returns a token, to use for making requests to the server
     * @return a session token response
     */
    public SimpleResponse provideSessionToken() {
        return WebCalculatorFactoryResponse.createTokenResponse(counter.getAndIncrement());
    }

    /**
     * Retrieves a list of requests made with a specified token
     * @param token
     * @return
     */
    List<CalculationResponse> provideCalculationHistory(String token) {
        return history.containsKey(token) && history.get(token).size() > 0 ? history.get(token) : Collections.emptyList();
    }

    private CalculationResponse appendCalculationHistory(String token, String expression, double result, String description) {
        if (!history.containsKey(token)) {
            history.put(token, new ArrayList<>());
        }
        List<CalculationResponse> calculationResponses = history.get(token);
        calculationResponses.add(new CalculationResponse(calculationResponses.size() + 1, expression, result, description));
        return calculationResponses.get(calculationResponses.size() - 1);
    }
}
