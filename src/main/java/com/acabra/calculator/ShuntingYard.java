package com.acabra.calculator;

import java.util.*;

/**
 * Created by Agustin on 9/28/2016.
 * @see <a href="http://eddmann.com/posts/shunting-yard-implementation-in-java/">Shunting Yard, Edd Mann</a>
 */
public class ShuntingYard {

    private static List<String> GROUPING_LEFT = Arrays.asList("[", "(", "{");
    private static List<String> GROUPING_RIGHT = Arrays.asList("]", ")", "}");

    private enum Operator {
        ADD(1), SUBTRACT(2), MULTIPLY(3), DIVIDE(4);
        final int precedence;

        Operator(int p) {
            precedence = p;
        }
    }

    private static Map<String, Operator> ops = new HashMap<String, Operator>() {{
        put("+", Operator.ADD);
        put("-", Operator.SUBTRACT);
        put("*", Operator.MULTIPLY);
        put("/", Operator.DIVIDE);
    }};

    private static boolean hasHigherPrecedence(String op, String sub) {
        return (ops.containsKey(sub) && ops.get(sub).precedence >= ops.get(op).precedence);
    }

    public static String postfix(String infix) {
        StringBuilder output = new StringBuilder();
        Deque<String> stack = new LinkedList<>();

        for (String token : infix.split("\\s")) {
            if (ops.containsKey(token)) {
                while (!stack.isEmpty() && hasHigherPrecedence(token, stack.peek()))
                    output.append(stack.pop()).append(' ');
                stack.push(token);
            } else if (GROUPING_LEFT.contains(token)) {
                stack.push(token);
            } else if (GROUPING_RIGHT.contains(token)) {
                while (!GROUPING_LEFT.contains(stack.peek()))
                    output.append(stack.pop()).append(' ');
                stack.pop();
            } else {
                output.append(token).append(' ');
            }
        }
        while (!stack.isEmpty())
            output.append(stack.pop()).append(' ');

        return output.toString();
    }

}
