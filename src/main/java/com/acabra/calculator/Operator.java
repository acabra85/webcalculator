package com.acabra.calculator;

import java.util.*;

/**
 * Created by Agustin on 9/30/2016.
 * A class containing the operators supported for the system.
 */
public enum Operator {
    ADD(1, "+"), SUBTRACT(2, "-"), MULTIPLY(3, "*"), DIVIDE(4, "/"), SQRT(5, "@");

    private final int precedence;
    private final String label;

    public static final Map<String, Operator> OPERATOR_MAP  = Collections.unmodifiableMap(new HashMap<String, Operator>() {{
        put(Operator.ADD.getLabel(), Operator.ADD);
        put(Operator.SUBTRACT.getLabel(), Operator.SUBTRACT);
        put(Operator.MULTIPLY.getLabel(), Operator.MULTIPLY);
        put(Operator.DIVIDE.getLabel(), Operator.DIVIDE);
        put(Operator.SQRT.getLabel(), Operator.SQRT);
    }});

    public static List<String> OPEN_GROUP_SYMBOLS = Collections.unmodifiableList(Arrays.asList("(", "[", "{"));
    public static List<String> CLOSE_GROUP_SYMBOLS = Collections.unmodifiableList(Arrays.asList(")", "]", "}"));


    public static Set<String> OPEN_GROUP_SYMBOLS_SET = Collections.unmodifiableSet(new HashSet<String>() {{
        add(Operator.OPEN_GROUP_SYMBOLS.get(0));
        add(Operator.OPEN_GROUP_SYMBOLS.get(1));
        add(Operator.OPEN_GROUP_SYMBOLS.get(2));
    }});

    public static Set<String> CLOSE_GROUP_SYMBOLS_SET = Collections.unmodifiableSet(new HashSet<String>() {{
        add(Operator.CLOSE_GROUP_SYMBOLS.get(0));
        add(Operator.CLOSE_GROUP_SYMBOLS.get(1));
        add(Operator.CLOSE_GROUP_SYMBOLS.get(2));
    }});

    Operator(int p, String label) {
        precedence = p;
        this.label = label;
    }


    public String getLabel() {
        return label;
    }

    public int getPrecedence() {
        return precedence;
    }
}