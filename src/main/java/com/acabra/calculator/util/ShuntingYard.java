package com.acabra.calculator.util;

import com.acabra.calculator.Calculator;
import com.acabra.calculator.Operator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @see <a href="http://eddmann.com/posts/shunting-yard-implementation-in-java/">Shunting Yard, Edd Mann</a>
 */
public class ShuntingYard {

    private static final Logger logger = Logger.getLogger(Calculator.class);

    private static boolean hasHigherPrecedence(String op, String sub) {
        return Operator.OPERATOR_MAP.containsKey(sub) &&  Operator.OPERATOR_MAP.containsKey(op)
                && Operator.OPERATOR_MAP.get(sub).getPrecedence() >= Operator.OPERATOR_MAP.get(op).getPrecedence();
    }

    public static List<String> postfix(String infix) {
        List<String> output = new ArrayList<>();
        Deque<String> stack = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        for (String token : StringUtils.split(infix ,' ')) {
            if (Operator.OPERATOR_MAP.containsKey(token)) {
                while (!stack.isEmpty() && hasHigherPrecedence(token, stack.peek())) {
                    output.add(stack.pop());
                    sb.append(output.get(output.size() - 1)).append(' ');
                }
                stack.push(token);
            } else if (Operator.OPEN_GROUP_SYMBOLS_SET.contains(token)) {
                stack.push(token);
            } else if (Operator.CLOSE_GROUP_SYMBOLS_SET.contains(token)) {
                while (!Operator.OPEN_GROUP_SYMBOLS_SET.contains(stack.peek())) {
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
        logger.debug("postfix expression -> " + sb.toString());
        return output;
    }
}
