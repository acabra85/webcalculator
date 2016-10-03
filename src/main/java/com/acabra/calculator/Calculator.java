package com.acabra.calculator;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.integral.IntegrableFunction;
import com.acabra.calculator.integral.IntegralSolver;
import com.acabra.calculator.util.ShuntingYard;
import com.acabra.calculator.util.WebCalculatorValidation;
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

    public Double solveArithmeticExpression(String expression) {
        try {
            logger.info("parsing expression " + expression );
            String parsedExpression = expression.replace("sqrt", Operator.SQRT.getLabel());
            WebCalculatorValidation.validateArithmeticExpression(parsedExpression);
            return solvePostFixExpression(ShuntingYard.postfix(parsedExpression));
        } catch (Exception e) {
            logger.error(e);
            throw new InputMismatchException("invalid expression");
        }
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

    public CompletableFuture<IntegrableFunction> resolveIntegralApproximateRiemannSequenceRequest(IntegralRequest integralRequest) {
        IntegralSolver integralSolver = new IntegralSolver(integralRequest);
        return integralSolver.approximateSequenceRiemannArea(true);
    }
}
