package com.acabra.calculator;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by Agustin on 9/28/2016.
 *
 * @see <a href="http://eddmann.com/posts/shunting-yard-implementation-in-java/">Shunting Yard, Edd Mann</a>
 */
public class ShuntingYard {

    private static List<String> GROUPING_LEFT = Arrays.asList("(", "[", "{");
    private static List<String> GROUPING_RIGHT = Arrays.asList(")", "]", "}");
    private static final Logger logger = Logger.getLogger(Calculator.class);

    private enum Operator {
        ADD(1), SUBTRACT(2), MULTIPLY(3), DIVIDE(4), SQRT(5);
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
        put("@", Operator.SQRT);
    }};

    public static double solveInFixExpression(String inFixExpression) {
        List<String> postFix = postfix(inFixExpression.replace("sqrt", "@"));
        Stack<Double> stack = new Stack<>();
        for (String item : postFix) {
            if (!ops.containsKey(item)) {
                stack.push(Double.parseDouble(item));
            } else {
                operateAndUpdate(stack, ops.get(item));
            }
        }
        return stack.isEmpty()? 0.0d : stack.pop();
    }

    private static void operateAndUpdate(Stack<Double> stack, Operator operator) {
        switch (operator) {
            case ADD:
                stack.push(stack.pop() + stack.pop());
                break;
            case SUBTRACT:
                stack.push(stack.pop() - stack.pop());
                break;
            case MULTIPLY:
                stack.push(stack.pop() * stack.pop());
                break;
            case DIVIDE:
                stack.push(stack.pop() / stack.pop());
                break;
            case SQRT:
                stack.push(Math.sqrt(stack.pop()));
                break;
            default:
                throw new RuntimeException("Invalid operator found");
        }
    }

    private static boolean hasHigherPrecedence(String op, String sub) {
        return (ops.containsKey(sub) && ops.get(sub).precedence >= ops.get(op).precedence);
    }

    private static List<String> postfix(String infix) {
        List<String> output = new ArrayList<>();
        Deque<String> stack = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        for (String token : infix.split("\\s+")) {
            if (ops.containsKey(token)) {
                while (!stack.isEmpty() && hasHigherPrecedence(token, stack.peek())) {
                    output.add(stack.pop());
                    sb.append(output.get(output.size() - 1)).append(' ');
                }
                stack.push(token);
            } else if (GROUPING_LEFT.contains(token)) {
                stack.push(GROUPING_LEFT.get(0));
            } else if (GROUPING_RIGHT.contains(token)) {
                while (!GROUPING_LEFT.contains(stack.peek())) {
                    output.add(stack.pop());
                    sb.append(output.get(output.size() - 1)).append(' ');
                }
                stack.pop();
            } else {
                output.add(token);
                sb.append(token).append(' ');
            }
        }
        while (!stack.isEmpty()) {
            output.add(stack.pop());
            sb.append(output.get(output.size() - 1)).append(' ');
        }
        logger.info("postfix expression -> " + sb.toString());
        return output;
    }

}
