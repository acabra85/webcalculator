package com.acabra.calculator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Agustin on 9/30/2016.
 */

public enum Operator {
    ADD(1), SUBTRACT(2), MULTIPLY(3), DIVIDE(4), SQRT(5);
    private final int precedence;

    Operator(int p) {
        precedence = p;
    }

    private static Map<String, Operator> operatorMap = new HashMap<String, Operator>() {{
        put("+", Operator.ADD);
        put("-", Operator.SUBTRACT);
        put("*", Operator.MULTIPLY);
        put("/", Operator.DIVIDE);
        put("@", Operator.SQRT);
    }};

    public int getPrecedence() {
        return precedence;
    }

    public static Map<String, Operator> getOperatorMap() {
        return operatorMap;
    }
}