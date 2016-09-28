package com.acabra.calculator;

import com.acabra.calculator.response.CalculationResponse;
import com.acabra.calculator.response.SimpleResponse;
import com.acabra.calculator.view.WebCalculatorRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Agustin on 9/27/2016.
 */
public class CalculatorManager {

    private final Calculator calculator;
    private final HashMap<String, List<CalculationResponse>> history;
    private final AtomicLong counter;
    private final WebCalculatorRenderer renderer;

    public CalculatorManager(WebCalculatorRenderer renderer) {
        this.history = new HashMap<>();
        this.calculator = new Calculator();
        counter = new AtomicLong();
        this.renderer = renderer;
    }

    public SimpleResponse processCalculation(String expression, String token) {
        String result = calculator.makeCalculation(expression);
        return appendCalculationHistory(token, expression, result);
    }

    private CalculationResponse appendCalculationHistory(String token, String expression, String result) {
        if (!history.containsKey(token)) {
            history.put(token, new ArrayList<>());
        }
        List<CalculationResponse> calculationResponses = history.get(token);
        CalculationResponse calculationResponse = new CalculationResponse(calculationResponses.size() + 1, expression, result);
        calculationResponses.add(calculationResponse);
        return calculationResponse;
    }

    public List<CalculationResponse> provideCalculationHistory(String token) {
        return history.containsKey(token) && history.get(token).size() > 0 ? history.get(token) : Collections.emptyList();
    }

    public SimpleResponse provideRenderedHistoryResult(String token) {
        return renderer.renderCalculationHistory(provideCalculationHistory(token), true);
    }

}
