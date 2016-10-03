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

    public SimpleResponse processArithmeticCalculation(String expression, String token) {
        double result = calculator.solveArithmeticExpression(expression);
        return appendCalculationHistory(token, expression, result, "");
    }

    private CalculationResponse appendCalculationHistory(String token, String expression, double result, String description) {
        if (!history.containsKey(token)) {
            history.put(token, new ArrayList<>());
        }
        List<CalculationResponse> calculationResponses = history.get(token);
        CalculationResponse calculationResponse = new CalculationResponse(calculationResponses.size() + 1, expression, result, description);
        calculationResponses.add(calculationResponse);
        return calculationResponse;
    }

    List<CalculationResponse> provideCalculationHistory(String token) {
        return history.containsKey(token) && history.get(token).size() > 0 ? history.get(token) : Collections.emptyList();
    }

    public SimpleResponse provideRenderedHistoryResult(String token) {
        List<CalculationResponse> calculationResponseList = provideCalculationHistory(token);
        String table = renderer.renderCalculationHistory(calculationResponseList, true);
        return WebCalculatorFactoryResponse.createTableResponse(counter.getAndIncrement(), table);
    }

    public SimpleResponse provideSessionToken() {
        return WebCalculatorFactoryResponse.createTokenResponse(counter.getAndIncrement());
    }
}
