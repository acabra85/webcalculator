package com.acabra.calculator.util;

import com.acabra.calculator.Operator;
import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralFunctionFactory;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Agustin on 9/29/2016.
 */
public class WebCalculatorValidation {


    private static final Map<String, String> CLOSE_PARENTHESIS_CONTROL  = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put(Operator.GROUPING_RIGHT_SYMBOLS.get(0), Operator.GROUPING_LEFT_SYMBOLS.get(0));
        put(Operator.GROUPING_RIGHT_SYMBOLS.get(1), Operator.GROUPING_LEFT_SYMBOLS.get(1));
        put(Operator.GROUPING_RIGHT_SYMBOLS.get(2), Operator.GROUPING_LEFT_SYMBOLS.get(2));
    }});

    /**
     * Validates that a request complies with the restrictions of the system, throws runtime
     * exception in case criteria is not met.
     * @param integralRequest an integral request
     */
    public static void validateIntegralRequest(IntegralRequest integralRequest) {
        DefiniteIntegralFunctionFactory.evaluateFunctionType(integralRequest.getFunctionId());
        DefiniteIntegralFunctionFactory.evaluateApproximationMethodType(integralRequest.getApproximationMethodId());
        int numThreads = integralRequest.getNumThreads();
        if (numThreads < 1 || numThreads > 15)
            throw new InputMismatchException("invalid input num threads must be between [1,15]");
        int repeatedCalculations = integralRequest.getRepeatedCalculations();
        if (repeatedCalculations < 1 || repeatedCalculations >= Integer.MAX_VALUE)
            throw new InputMismatchException("invalid input repeated calculations must be > 0");
    }

    /**
     * Validates that an arithmetic expression contains proper grouping and valid operations and symbols.
     * @param arithmeticExpression the expression to be resolved
     */
    public static void validateArithmeticExpression(String arithmeticExpression) {
        if (!validateParenthesisGroupingAndContents(arithmeticExpression)) {
            throw new InputMismatchException("invalid expression: wrong grouping");
        }
    }

    /**
     * Uses a map to inspect the amount of parenthesis that have been closed and open.
     * @param expression the expression to be evaluated
     * @return true if the expression has proper grouping and valid symbols
     */
    private static boolean validateParenthesisGroupingAndContents(String expression) {
        boolean validClosing = true;
        Map<String, AtomicInteger> openParenthesisControl = provideOpenParenthesisControl();
        StringTokenizer stringTokenizer = new StringTokenizer(expression, " ");
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            if (openParenthesisControl.containsKey(token)) {
                openParenthesisControl.get(token).incrementAndGet();
            } else if (CLOSE_PARENTHESIS_CONTROL.containsKey(token)) {
                String openKey = CLOSE_PARENTHESIS_CONTROL.get(token);
                if (openParenthesisControl.get(openKey).decrementAndGet() < 0) {
                    validClosing = false;
                }
            } else if (!Operator.OPERATOR_MAP.containsKey(token) && !NumberUtils.isNumber(token)) {
                throw new InputMismatchException(String.format("invalid expression: unrecognized value[%s]", token));
            }
        }
        return validClosing && validOpening(openParenthesisControl);
    }

    private static boolean validOpening(Map<String, AtomicInteger> openParenthesisControl) {
        for (AtomicInteger openCounter: openParenthesisControl.values()) {
            if (openCounter.get() != 0) {
                return false;
            }
        }
        return true;
    }

    private static Map<String, AtomicInteger> provideOpenParenthesisControl() {
        Map<String, AtomicInteger> openParenthesisControl = new HashMap<>();
        openParenthesisControl.put(Operator.GROUPING_LEFT_SYMBOLS.get(0), new AtomicInteger());
        openParenthesisControl.put(Operator.GROUPING_LEFT_SYMBOLS.get(1), new AtomicInteger());
        openParenthesisControl.put(Operator.GROUPING_LEFT_SYMBOLS.get(2), new AtomicInteger());
        return openParenthesisControl;
    }
}
