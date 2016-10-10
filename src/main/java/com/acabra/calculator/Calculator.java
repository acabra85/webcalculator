package com.acabra.calculator;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.integral.IntegrableFunction;
import com.acabra.calculator.integral.IntegralSolver;
import com.acabra.calculator.util.ShuntingYard;
import org.apache.log4j.Logger;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Agustin on 9/27/2016.
 */
public class Calculator {

    private static final Logger logger = Logger.getLogger(Calculator.class);

    public Calculator() {
    }

    /**
     * Method to parse and resolve an arithmetic expression
     * @param expression the expression in infix notation.
     * @return a double after parsing and evaluating the expression.
     */
    Double solveArithmeticExpression(String expression) {
        try {
            logger.debug("parsing expression " + expression );
            return solvePostFixExpression(ShuntingYard.postfix(expression));
        } catch (Exception e) {
            logger.error(e);
            throw new InputMismatchException("invalid expression");
        }
    }

    /**
     * Based on an integral request instantiates an Integral solver to use Riemann Areas aproximation methods
     * to calculate the area under the graph.
     * @param integralRequest a request of integral function
     * @return a future representing the integrableFunction object solved.
     */
    CompletableFuture<IntegrableFunction> resolveIntegralApproximateRiemannSequenceRequest(IntegralRequest integralRequest) {
        IntegralSolver integralSolver = new IntegralSolver(integralRequest);
        CompletableFuture<IntegrableFunction> fut1 = integralSolver.approximateAreaUnderCurve();
        return fut1;
    }

    private static double solvePostFixExpression(List<String> postFix) {
        Stack<Double> stack = new Stack<>();
        for (String item : postFix) {
            if (!Operator.OPERATOR_MAP.containsKey(item)) {
                stack.push(Double.parseDouble(item));
            } else {
                operateAndUpdate(stack, Operator.OPERATOR_MAP.get(item));
            }
        }
        double result = stack.size() == 1 ? stack.pop() : operatePendingStack(stack, Operator.MULTIPLY);
        logger.debug("postfix solved -> " + result);
        return result;
    }

    private static double operatePendingStack(Stack<Double> stack, Operator operator) {
        double identity = operator == Operator.ADD || operator == Operator.SUBTRACT ? 0 : 1;
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
            case SQRT:
                stack.push(Math.sqrt(stack.pop()));
                break;
            default:
                throw new UnsupportedOperationException("Invalid operator");
        }
    }
}
