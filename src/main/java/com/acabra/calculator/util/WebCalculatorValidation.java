package com.acabra.calculator.util;

import com.acabra.calculator.Operator;
import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralFunctionFactory;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;

/**
 * Created by Agustin on 9/29/2016.
 */
public class WebCalculatorValidation {


    private static final Map<String, String> CLOSE_PARENTHESIS_CONTROL = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put(Operator.CLOSE_GROUP_SYMBOLS.get(0), Operator.OPEN_GROUP_SYMBOLS.get(0));
        put(Operator.CLOSE_GROUP_SYMBOLS.get(1), Operator.OPEN_GROUP_SYMBOLS.get(1));
        put(Operator.CLOSE_GROUP_SYMBOLS.get(2), Operator.OPEN_GROUP_SYMBOLS.get(2));
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
        StringTokenizer stringTokenizer = new StringTokenizer(expression, " ");
        Stack<String> stackControl = new Stack<>();
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            if (Operator.OPEN_GROUP_SYMBOLS_SET.contains(token)) {
                stackControl.push(token);
            } else if (Operator.CLOSE_GROUP_SYMBOLS_SET.contains(token)) {
                if (stackControl.isEmpty() || !stackControl.pop().equals(CLOSE_PARENTHESIS_CONTROL.get(token))) {
                    return false;
                }
            } else if (!Operator.OPERATOR_MAP.containsKey(token) && !NumberUtils.isNumber(token)) {
                throw new InputMismatchException(String.format("invalid expression: unrecognized value[%s]", token));
            }
        }
        return stackControl.isEmpty();
    }

}
