package com.acabra.calculator;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.integral.function.IntegrableFunction;
import com.acabra.calculator.integral.IntegralSolver;
import com.acabra.calculator.util.ShuntingYard;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Agustin on 9/27/2016.
 */
public class Calculator {

    private static final Logger logger = Logger.getLogger(Calculator.class);
    private static final BigDecimal BIGD_ZERO = new BigDecimal("0");
    private static final BigDecimal BIGD_ONE = new BigDecimal("1");

    public Calculator() {
    }

    /**
     * Method to parse and resolve an arithmetic expression
     * @param expression the expression in infix notation.
     * @return a double after parsing and evaluating the expression.
     */
    BigDecimal solveArithmeticExpression(String expression) {
        try {
            logger.debug("parsing expression " + expression );
            return solvePostFixExpression(ShuntingYard.postfix(expression));
        } catch (Exception e) {
            logger.error(e);
            throw new InputMismatchException("invalid expression: " + e.getMessage());
        }
    }

    /**
     * Based on an integral request instantiates an Integral solver to use Riemann Areas aproximation methods
     * to calculate the area under the graph.
     * @param integralRequest a request of integral function
     * @return a future representing the integrableFunction object solved.
     */
    CompletableFuture<IntegrableFunction> approximateAreaUnderCurve(IntegralRequest integralRequest) {
        return new IntegralSolver(integralRequest).approximateAreaUnderCurve();
    }

    private static BigDecimal solvePostFixExpression(List<String> postFix) {
        Stack<BigDecimal> stack = new Stack<>();
        for (String item : postFix) {
            if (!Operator.OPERATOR_MAP.containsKey(item)) {
                stack.push(new BigDecimal(item));
            } else {
                operateAndUpdate(stack, Operator.OPERATOR_MAP.get(item));
            }
        }
        BigDecimal result = stack.size() == 1 ? stack.pop() : operatePendingStack(stack);
        logger.debug("postfix solved -> " + result);
        return result;
    }

    private static BigDecimal operatePendingStack(Stack<BigDecimal> stack) {
        BigDecimal identity = BIGD_ONE;
        while (!stack.isEmpty()) {
            identity = identity.multiply(stack.pop());
        }
        return identity;
    }

    private static void operateAndUpdate(Stack<BigDecimal> stack, Operator operator) {
        switch (operator) {
            case ADD:
                stack.push(stack.pop().add(stack.pop()));
                break;
            case SUBTRACT:
                BigDecimal firstTerm = stack.pop();
                stack.push(stack.pop().subtract(firstTerm));
                break;
            case MULTIPLY:
                stack.push(stack.pop().multiply(stack.pop()));
                break;
            case DIVIDE:
                BigDecimal divisor = stack.pop();
                stack.push(stack.pop().divide(divisor, MathContext.DECIMAL32));
                break;
            case SQRT:
                stack.push(sqrt(stack.pop()));
                break;
            default:
                throw new UnsupportedOperationException("Invalid operator");
        }
    }

    public static BigDecimal sqrt(BigDecimal value) {
        BigDecimal x = new BigDecimal(Math.sqrt(value.doubleValue()));
        return x.add(new BigDecimal(value.subtract(x.multiply(x)).doubleValue() / (x.doubleValue() * 2.0)));
    }
}
