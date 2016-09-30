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
    private static final Map<String, Operator> ops = Operator.getOperatorMap();

    private static boolean hasHigherPrecedence(String op, String sub) {
        return (ops.containsKey(sub) && ops.get(sub).getPrecedence() >= ops.get(op).getPrecedence());
    }

    static List<String> postfix(String infix) {
        List<String> output = new ArrayList<>();
        Deque<String> stack = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        for (String token : infix.replace("sqrt", "@").split("\\s+")) {
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
