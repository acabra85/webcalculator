package com.acabra.calculator;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.integral.ConcurrentIntegralSolver;
import com.acabra.calculator.integral.IntegrableFunction;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Agustin on 9/27/2016.
 */
public class Calculator {

    private static final Logger logger = Logger.getLogger(Calculator.class);
    private static final Map<String, Operator> ops = Operator.getOperatorMap();

    public Calculator() {
    }

    public Double solveArithmeticExpression(String expression) {
        try {
            logger.info("parsing expression " + expression );
            return solvePostFixExpression(ShuntingYard.postfix(expression));
        } catch (Exception e) {
            logger.error(e);
            return Double.NaN;
        }
    }

    private static double solvePostFixExpression(List<String> postFix) {
        Stack<Double> stack = new Stack<>();
        for (String item : postFix) {
            if (!ops.containsKey(item)) {
                stack.push(Double.parseDouble(item));
            } else {
                operateAndUpdate(stack, ops.get(item));
            }
        }
        double result = stack.size() == 1 ? stack.pop() : operatePendingStack(stack, Operator.MULTIPLY);
        logger.info("postfix solved -> " + result);
        return result;
    }

    private static double operatePendingStack(Stack<Double> stack, Operator operator) {
        double identity = operator == Operator.MULTIPLY || operator == Operator.DIVIDE ? 1 : 0;
        while (!stack.isEmpty()) {
            identity = identity * stack.pop();
        }
        return identity;
    }

    private static void operateAndUpdate(Stack<Double> stack, Operator operator) {
        switch (operator) {
            case ADD:
                stack.push(stack.pop() + stack.pop());
                break;
            case SUBTRACT:
                double firstTerm = stack.pop();
                stack.push(stack.pop() - firstTerm);
                break;
            case MULTIPLY:
                stack.push(stack.pop() * stack.pop());
                break;
            case DIVIDE:
                double divisor = stack.pop();
                stack.push(stack.pop() / divisor);
                break;
            default:
                stack.push(Math.sqrt(stack.pop()));
                break;
        }
    }

    public CompletableFuture<IntegrableFunction> resolveIntegralRequest(IntegralRequest integralRequest) {
        ConcurrentIntegralSolver concurrentIntegralSolver = new ConcurrentIntegralSolver(integralRequest);
        return concurrentIntegralSolver.resolveIntegral();
    }
}
