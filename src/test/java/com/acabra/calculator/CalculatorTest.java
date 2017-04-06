package com.acabra.calculator;

import com.acabra.calculator.integral.IntegralSolver;
import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralExponential;
import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralFunction;
import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralPolynomial;
import com.acabra.calculator.util.ShuntingYard;
import com.acabra.calculator.util.WebCalculatorConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by Agustin on 9/30/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Calculator.class, IntegralSolver.class, ShuntingYard.class, Operator.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class CalculatorTest {

    Calculator calculator = new Calculator();

    @Test
    public void solveExponentialIntegralExpression() throws Exception {
        double expected = 1.71828;
        CompletableFuture<DefiniteIntegralFunction> concurrentIntegralResult = CompletableFuture.completedFuture(new DefiniteIntegralExponential(0, 1, Optional.of(expected), Optional.empty()));

        IntegralSolver integralSolverMock = PowerMockito.mock(IntegralSolver.class);

        whenNew(IntegralSolver.class).withAnyArguments().thenReturn(integralSolverMock);
        when(integralSolverMock.approximateAreaUnderCurve()).thenReturn(concurrentIntegralResult);
        resolveAndVerifyIntegral(calculator.approximateAreaUnderCurve(null), expected, integralSolverMock);
    }

    @Test
    public void solvePolynomialIntegralExpression() throws Exception {
        double expected = 1.71828;
        List<Double> coefficients = Arrays.asList(0.0, 2.0);
        CompletableFuture<DefiniteIntegralFunction> concurrentIntegralResult =
                CompletableFuture.completedFuture(new DefiniteIntegralPolynomial(0, 1, coefficients, Optional.of(expected), Optional.empty()));

        IntegralSolver integralSolverMock = PowerMockito.mock(IntegralSolver.class);

        whenNew(IntegralSolver.class).withAnyArguments().thenReturn(integralSolverMock);
        when(integralSolverMock.approximateAreaUnderCurve()).thenReturn(concurrentIntegralResult);
        resolveAndVerifyIntegral(calculator.approximateAreaUnderCurve(null), expected, integralSolverMock);

    }

    private void resolveAndVerifyIntegral(CompletableFuture<DefiniteIntegralFunction> future, double expected, IntegralSolver integralSolverMock) throws ExecutionException, InterruptedException {
        DefiniteIntegralFunction definiteIntegralFunction = future.get();
        assertEquals(expected, definiteIntegralFunction.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        try {
            verifyNew(IntegralSolver.class).withArguments(eq(null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        verify(integralSolverMock, times(1));

    }

    @Test
    public void solveArithmeticExpression() throws Exception {
        String expr = "1 + 2 + 3";
        List<String> postFixExpressionList = Arrays.asList("1 2 3 + +".split("\\s+"));

        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expr)).thenReturn(postFixExpressionList);

        BigDecimal calculatorResult = calculator.solveArithmeticExpression(expr);

        PowerMockito.verifyStatic(times(1));
        ShuntingYard.postfix(expr);

        assertEquals("6", calculatorResult.toString());

    }

    @Test
    public void solveInFixExpression1Test() {
        String expression = "1 + 2 + 3 - 4 + 8";
        List<String> postFixExpressionList = Arrays.asList("1 2 + 3 4 - + 8 +".split("\\s+"));

        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expression)).thenReturn(postFixExpressionList);

        BigDecimal actual = calculator.solveArithmeticExpression(expression);

        PowerMockito.verifyStatic(times(1));
        ShuntingYard.postfix(expression);

        assertEquals("10", actual.toString());

    }

    @Test
    public void solveInFixExpression2Test() {
        String expression = "2 @ ( 9 ) * 5";
        List<String> postFixExpressionList = ShuntingYard.postfix(expression);

        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expression)).thenReturn(postFixExpressionList);

        BigDecimal actual = calculator.solveArithmeticExpression(expression);

        PowerMockito.verifyStatic(times(1));
        ShuntingYard.postfix(expression);

        assertEquals("30", actual.toString());
    }

    @Test
    public void solveInFixExpression3Test() {
        String expression = "2  + @ ( 9 )";
        List<String> postFixExpressionList = ShuntingYard.postfix(expression);

        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expression)).thenReturn(postFixExpressionList);

        BigDecimal actual = calculator.solveArithmeticExpression(expression);

        PowerMockito.verifyStatic(times(1));
        ShuntingYard.postfix(expression);

        assertEquals("5", actual.toString());
    }

    @Test
    public void solveInFixExpression4Test() {
        String expression = "1 + 2 * ( 3 - 4 ) + 8 / ( 1 + 1 )";
        List<String> postFixExpressionList = Arrays.asList("1 2 3 4 - * + 8 1 1 + / +".split("\\s+"));

        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expression)).thenReturn(postFixExpressionList);

        BigDecimal actual = calculator.solveArithmeticExpression(expression);

        PowerMockito.verifyStatic(times(1));
        ShuntingYard.postfix(expression);

        assertEquals("3", actual.toString());
    }

    @Test(expected = InputMismatchException.class)
    public void solveInFixExpressionInvalid1Test() {
        String expression = "1 + 2 *  ) ( 3 - 4 ) + 8 / ( 1 + 1 )";

        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expression)).thenThrow(NoSuchElementException.class);

        calculator.solveArithmeticExpression(expression);
    }

    @Test(expected = InputMismatchException.class)
    public void solveInFixExpressionPosInfiniteTest() {
        String expression = "6 / 0";
        double expected = Double.POSITIVE_INFINITY;
        List<String> postFixExpressionList = Arrays.asList("6 0 /".split("\\s+"));

        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expression)).thenReturn(postFixExpressionList);

        BigDecimal actual = calculator.solveArithmeticExpression(expression);
    }

    @Test(expected = InputMismatchException.class)
    public void solveInFixExpressionNegInfiniteTest() {
        String expression = "-6 / 0";
        double expected = Double.NEGATIVE_INFINITY;
        List<String> postFixExpressionList = Arrays.asList("-6 0 /".split("\\s+"));

        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expression)).thenReturn(postFixExpressionList);

        BigDecimal actual = calculator.solveArithmeticExpression(expression);
    }

    @Test(expected = InputMismatchException.class)
    public void solveInFixExpressionNaNTest() {
        String expression = "0 / 0";
        double expected = Double.NaN;
        List<String> postFixExpressionList = Arrays.asList("0 0 /".split("\\s+"));

        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expression)).thenReturn(postFixExpressionList);

        BigDecimal actual = calculator.solveArithmeticExpression(expression);
    }

    @Test
    public void solveArithmeticExpressionTest() {
        String expression = "6 + ( [ { 6 } ] )";
        int expected = 12;
        List<String> postFixExpressionList = Arrays.asList("6 6 +".split("\\s+"));

        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expression)).thenReturn(postFixExpressionList);

        BigDecimal actual = calculator.solveArithmeticExpression(expression);

        PowerMockito.verifyStatic(times(1));
        ShuntingYard.postfix(expression);

        assertEquals(expected+"", actual.toString());
    }

    @Test
    public void solveArithmeticExpression2Test() {
        String expression = "6 @ ( [ { 16 } ] )";
        int expected = 24;
        List<String> postFixExpressionList = ShuntingYard.postfix(expression);

        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expression)).thenReturn(postFixExpressionList);

        BigDecimal actual = calculator.solveArithmeticExpression(expression);

        PowerMockito.verifyStatic(times(1));
        ShuntingYard.postfix(expression);

        assertEquals(expected+"", actual.toString());
    }

    @Test
    public void solveArithmeticExpression3Test() {
        String expression = "8 @ ( @ ( 8 + 8 ) * 6 + 1 ) ";
        int expected = 40;
        List<String> postFixExpressionList = ShuntingYard.postfix(expression);

        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expression)).thenReturn(postFixExpressionList);

        BigDecimal actual = calculator.solveArithmeticExpression(expression);

        PowerMockito.verifyStatic(times(1));
        ShuntingYard.postfix(expression);

        assertEquals(expected+"", actual.toString());
    }

    @Test
    public void solveArithmeticExpression4Test() {
        String expression = "8 8";
        int expected = 64;

        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expression)).thenReturn(Arrays.asList("8", "8"));

        BigDecimal actual = calculator.solveArithmeticExpression(expression);

        PowerMockito.verifyStatic(times(1));
        ShuntingYard.postfix(expression);

        assertEquals(expected+"", actual.toString());
    }

    @Test
    public void solveArithmeticExpression5Test() {
        String expression = "";
        int expected = 80;

        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expression)).thenReturn(Arrays.asList("8", "8", "2", "+"));

        BigDecimal actual = calculator.solveArithmeticExpression(expression);

        PowerMockito.verifyStatic(times(1));
        ShuntingYard.postfix(expression);

        assertEquals(expected+"", actual.toString());
    }

    @Test
    public void solveArithmeticExpression6Test() {
        String expression = "100 / 10";
        int expected = 10;

        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expression)).thenReturn(Arrays.asList("100", "10", "/"));

        BigDecimal actual = calculator.solveArithmeticExpression(expression);

        PowerMockito.verifyStatic(times(1));
        ShuntingYard.postfix(expression);

        assertEquals(expected+"", actual.toString());
    }

    @Test
    public void solveArithmeticExpression7Test() {
        String expression = "10 / 100";
        double expected = 0.1;

        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expression)).thenReturn(Arrays.asList("10", "100", "/"));

        BigDecimal actual = calculator.solveArithmeticExpression(expression);

        PowerMockito.verifyStatic(times(1));
        ShuntingYard.postfix(expression);

        assertEquals(expected+"", actual.toString());
    }

    @Test
    public void should_return_minus_twelve_1() {
        String expression = "6 - ( 3 * { 6 } )";
        BigDecimal expected = new BigDecimal(-12.0);

        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expression)).thenReturn(Arrays.asList("6 3 6 * -".split("\\s+")));

        BigDecimal actual = calculator.solveArithmeticExpression(expression);

        PowerMockito.verifyStatic(times(1));
        ShuntingYard.postfix(expression);

        assertEquals(0, actual.compareTo(expected));
    }

    @Test
    public void should_return_minus_twelve_2() {
        String expression = "6 - ( 3 { 6 } )";
        BigDecimal expected = new BigDecimal(-12.0);

        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expression)).thenReturn(Arrays.asList("6 3 6 * -".split("\\s+")));

        BigDecimal actual = calculator.solveArithmeticExpression(expression);

        PowerMockito.verifyStatic(times(1));
        ShuntingYard.postfix(expression);

        assertEquals(0, actual.compareTo(expected));
    }

    @Test
    public void should_return_minus_five_hundred_seventy_1() {
        String expression = "6 - ( 3 { 4 [ 6 ( { 8 } ) ] } )";
        BigDecimal expected = new BigDecimal(-570.0);

        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expression)).thenReturn(Arrays.asList("6 3 4 6 8 * * * -".split("\\s+")));

        BigDecimal actual = calculator.solveArithmeticExpression(expression);

        PowerMockito.verifyStatic(times(1));
        ShuntingYard.postfix(expression);

        assertEquals(0, actual.compareTo(expected));
    }

    @Test
    public void should_return_minus_five_hundred_seventy_2() {
        String expression = "6 - ( { [ ( { 8 } ) 6 ] 4 } 3 )";
        BigDecimal expected = new BigDecimal(-570.0);

        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expression)).thenReturn(Arrays.asList("6 8 6 * 4 * 3 * -".split("\\s+")));

        BigDecimal actual = calculator.solveArithmeticExpression(expression);

        PowerMockito.verifyStatic(times(1));
        ShuntingYard.postfix(expression);

        assertEquals(0, actual.compareTo(expected));
    }

    @Test(expected = InputMismatchException.class)
    public void should_return_minus_five_hundred_seventy_3() {
        String expression = "6 - ( 3 { * 6 } )";
        BigDecimal expected = new BigDecimal(-570.0);

        List<String> arrayStub = Arrays.asList("6 3 6 * * -".split("\\s+"));
        PowerMockito.mockStatic(ShuntingYard.class);
        when(ShuntingYard.postfix(expression)).thenReturn(arrayStub);

        BigDecimal actual = calculator.solveArithmeticExpression(expression);

        PowerMockito.verifyStatic(times(1));
        ShuntingYard.postfix(expression);

        assertEquals(0, actual.compareTo(expected));
    }
}
