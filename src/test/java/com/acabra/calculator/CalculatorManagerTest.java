package com.acabra.calculator;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.integral.ExponentialIntegral;
import com.acabra.calculator.integral.IntegrableFunction;
import com.acabra.calculator.response.CalculationResponse;
import com.acabra.calculator.response.SimpleResponse;
import com.acabra.calculator.response.TableHistoryResponse;
import com.acabra.calculator.util.WebCalculatorValidation;
import com.acabra.calculator.view.WebCalculatorRenderer;
import com.acabra.calculator.view.WebCalculatorRendererHTML;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by Agustin on 9/28/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CalculatorManager.class, Calculator.class,
        WebCalculatorRendererHTML.class, WebCalculatorValidation.class,
        IntegralRequest.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class CalculatorManagerTest {

    private CalculatorManager calculatorManager;
    private final static String TOKEN = "default-token";
    private final static String TOKEN2 = "default-token2";
    private final static String expr1 = "3 + 3";
    private final static double res1 = 6;
    private final static String res1Str = "6";
    private final static String expr2 = "4 + 4";
    private final static double res2 = 8;
    private final static String res2Str = "8";
    private final static String expr3 = "sqrt ( 4 )";
    private final static double res3 = 8;
    private final static String res3Str = "8";
    private final static String tableHeader = "<caption>History</caption><thead><tr><th>Id.</th><th>Expression</th><th>Result</th></tr></thead>";
    private final static String renderedTableEmpty = "<table>" + tableHeader + "</table>";
    private final static String rowExpr1 = "<tr><td>1</td><td>3 + 3</td><td>6</td></tr>";
    private final static String rowExpr2 = "<tr><td>2</td><td>4 + 4</td><td>8</td></tr>";
    private final static String rowExpr3 = "<tr><td>1</td><td>sqrt ( 4 )</td><td>2</td></tr>";
    private final static String renderedTable1 = "<table>" + tableHeader + "<tbody>" + rowExpr1 + "</tbody></table>";
    private final static String renderedTable2 = "<table>" + tableHeader + "<tbody>" + rowExpr1 + rowExpr2 + "</tbody></table>";
    private final static String renderedTable3 = "<table>" + tableHeader + "<tbody>" + rowExpr3 + "</tbody></table>";
    private SimpleResponse defaultTableResponseEmpty = new TableHistoryResponse(0L, renderedTableEmpty);
    private SimpleResponse defaultTableResponse1 = new TableHistoryResponse(1L, renderedTable1);
    private SimpleResponse defaultTableResponse2 = new TableHistoryResponse(2L, renderedTable2);
    private SimpleResponse defaultTableResponse3 = new TableHistoryResponse(3L, renderedTable3);

    private Calculator calculatorMock;
    private WebCalculatorRenderer rendererMock;

    @Before
    public void prepare() throws Exception {
        this.calculatorMock = PowerMockito.mock(Calculator.class);
        this.rendererMock = PowerMockito.mock(WebCalculatorRendererHTML.class);

        whenNew(Calculator.class).withNoArguments().thenReturn(calculatorMock);

        calculatorManager = new CalculatorManager(rendererMock);

        verifyNew(Calculator.class).withNoArguments();
    }

    @Test
    public void processCalculationTest() {

        when(calculatorMock.solveArithmeticExpression(eq(expr1))).thenReturn(res1);
        when(calculatorMock.solveArithmeticExpression(eq(expr2))).thenReturn(res2);

        CalculationResponse calculationResponse = (CalculationResponse) calculatorManager.processArithmeticCalculation(expr1, TOKEN);

        assertEquals(calculationResponse.getId(), 1);
        assertEquals(calculationResponse.getExpression(), expr1);
        assertEquals(res1Str,  calculationResponse.getResult());

        CalculationResponse calculationResponse2 = (CalculationResponse) calculatorManager.processArithmeticCalculation(expr2, TOKEN);

        assertEquals(2, calculationResponse2.getId());
        assertEquals(expr2, calculationResponse2.getExpression());
        assertEquals(res2Str, calculationResponse2.getResult());

        verify(calculatorMock, times(1)).solveArithmeticExpression(eq(expr1));
        verify(calculatorMock, times(1)).solveArithmeticExpression(eq(expr2));
    }

    @Test
    public void provideCalculationHistoryTest() {

        when(calculatorMock.solveArithmeticExpression(eq(expr1))).thenReturn(res1);

        calculatorManager.processArithmeticCalculation(expr1, TOKEN);
        calculatorManager.processArithmeticCalculation(expr1, TOKEN);


        List<CalculationResponse> calculationHistory = calculatorManager.provideCalculationHistory(TOKEN2);

        assertEquals(calculationHistory.size(), 0);

        List<CalculationResponse> resultList = calculatorManager.provideCalculationHistory(TOKEN);

        assertEquals(resultList.size(), 2); //valid size according to number of operations on that Token
        assertEquals(resultList.get(0).getId(), 1);
        assertEquals(resultList.get(1).getId(), 2);

        verify(calculatorMock, times(2)).solveArithmeticExpression(eq(expr1));
    }

    @Test
    public void provideRenderedHistoryResultEmptyTest() {
        when(rendererMock.renderCalculationHistory(eq(Collections.emptyList()), eq(true))).thenReturn(defaultTableResponseEmpty);

        String emptyTable = ((TableHistoryResponse) calculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML();

        assertEquals(renderedTableEmpty, emptyTable);

        verify(rendererMock, times(1)).renderCalculationHistory(eq(Collections.emptyList()), eq(true));
    }

    @Test
    public void provideRenderedHistoryResult1Test() {
        when(rendererMock.renderCalculationHistory(eq(Collections.emptyList()), eq(true))).thenReturn(defaultTableResponse1);

        assertEquals(renderedTable1, ((TableHistoryResponse) calculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML());

        verify(rendererMock, times(1)).renderCalculationHistory(eq(Collections.emptyList()), eq(true));
    }

    @Test
    public void provideRenderedHistoryResult2Test() {
        when(calculatorMock.solveArithmeticExpression(eq(expr1))).thenReturn(res1);
        when(calculatorMock.solveArithmeticExpression(eq(expr2))).thenReturn(res2);

        calculatorManager.processArithmeticCalculation(expr1, TOKEN);
        calculatorManager.processArithmeticCalculation(expr2, TOKEN);

        List<CalculationResponse> historyList = calculatorManager.provideCalculationHistory(TOKEN);
        when(rendererMock.renderCalculationHistory(eq(historyList), eq(true))).thenReturn(defaultTableResponse2);

        assertEquals(renderedTable2, ((TableHistoryResponse) calculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML());
        verify(rendererMock, times(1)).renderCalculationHistory(eq(historyList), eq(true));
        verify(calculatorMock, times(1)).solveArithmeticExpression(eq(expr1));
        verify(calculatorMock, times(1)).solveArithmeticExpression(eq(expr2));
    }

    @Test
    public void provideRenderedHistoryResult3Test() {
        when(rendererMock.renderCalculationHistory(eq(Collections.emptyList()), eq(true))).thenReturn(defaultTableResponse3);

        assertEquals(renderedTable3, ((TableHistoryResponse) calculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML());

        verify(rendererMock, times(1)).renderCalculationHistory(eq(Collections.emptyList()), eq(true));
    }

    @Test
    public void provideRenderedHistoryResult4Test() {
        String rowExpr4 = "<tr><td>5</td><td>10 - 5</td><td>5</td></tr>";
        String renderedTable4 = "<table>" + tableHeader + "<tbody>" + rowExpr4 + "</tbody></table>";
        SimpleResponse defaultTableResponse4 = new TableHistoryResponse(4L, renderedTable4);

        when(rendererMock.renderCalculationHistory(eq(Collections.emptyList()), eq(true))).thenReturn(defaultTableResponse4);

        assertEquals(renderedTable4, ((TableHistoryResponse) calculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML());

        verify(rendererMock, times(1)).renderCalculationHistory(eq(Collections.emptyList()), eq(true));
    }

    @Test
    public void provideRenderedHistoryResult5Test() {
        double res = -4;
        String rowExpr = "<tr><td>5</td><td>6 - 10</td><td>" + res + "</td></tr>";
        String renderedTable = "<table>" + tableHeader + "<tbody>" + rowExpr + "</tbody></table>";
        SimpleResponse defaultTableResponse = new TableHistoryResponse(4L, renderedTable);

        when(rendererMock.renderCalculationHistory(eq(Collections.emptyList()), eq(true))).thenReturn(defaultTableResponse);

        assertEquals(renderedTable, ((TableHistoryResponse) calculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML());

        verify(rendererMock, times(1)).renderCalculationHistory(eq(Collections.emptyList()), eq(true));
    }

    @Test
    public void provideRenderedHistoryResult6Test() {
        String expr = "6 - 10 * (2 - 5)";
        double res = -4;
        String rowExpr = "<tr><td>5</td><td>" + expr + "</td><td>" + res + "</td></tr>";
        String renderedTable = "<table>" + tableHeader + "<tbody>" + rowExpr + "</tbody></table>";
        SimpleResponse defaultTableResponse = new TableHistoryResponse(4L, renderedTable);

        when(rendererMock.renderCalculationHistory(eq(Collections.emptyList()), eq(true))).thenReturn(defaultTableResponse);

        assertEquals(renderedTable, ((TableHistoryResponse) calculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML());

        verify(rendererMock, times(1)).renderCalculationHistory(eq(Collections.emptyList()), eq(true));
    }

    @Test
    public void provideRenderedHistoryResultIntegral1Test() throws ExecutionException, InterruptedException {
        String expr = "integral{e^x}[0.00, 1.00]";
        String res = "1.71456";
        String rowExpr = "<tr><td>1</td><td>" + expr + "</td><td>" + res + "</td></tr>";
        String renderedTable = "<table>" + tableHeader + "<tbody>" + rowExpr + "</tbody></table>";
        SimpleResponse defaultTableResponse = new TableHistoryResponse(4L, renderedTable);

        when(rendererMock.renderCalculationHistory(eq(Collections.emptyList()), eq(true))).thenReturn(defaultTableResponse);

        assertEquals(renderedTable, ((TableHistoryResponse) calculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML());

        verify(rendererMock, times(1)).renderCalculationHistory(eq(Collections.emptyList()), eq(true));

    }

    @Test
    public void provideRenderedHistoryResultIntegral2Test() throws ExecutionException, InterruptedException {
        String expr = "integral{e^x}[0.00, 0.00]";
        String res = "0";
        String rowExpr = "<tr><td>1</td><td>" + expr + "</td><td>" + res + "</td></tr>";
        String renderedTable = "<table>" + tableHeader + "<tbody>" + rowExpr + "</tbody></table>";
        SimpleResponse defaultTableResponse = new TableHistoryResponse(4L, renderedTable);

        when(rendererMock.renderCalculationHistory(eq(Collections.emptyList()), eq(true))).thenReturn(defaultTableResponse);

        assertEquals(renderedTable, ((TableHistoryResponse) calculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML());

        verify(rendererMock, times(1)).renderCalculationHistory(eq(Collections.emptyList()), eq(true));

    }

    @Test
    public void provideRenderedHistoryResultIntegral3Test() throws Exception {
        String expr = "integral{e^x}[-10.0, -9.99]";
        String res = "0.00456E-5";
        String rowExpr = "<tr><td>1</td><td>" + expr + "</td><td>" + res + "</td></tr>";
        String renderedTable = "<table>" + tableHeader + "<tbody>" + rowExpr + "</tbody></table>";
        SimpleResponse defaultTableResponse = new TableHistoryResponse(4L, renderedTable);

        when(rendererMock.renderCalculationHistory(eq(Collections.emptyList()), eq(true))).thenReturn(defaultTableResponse);

        String tableHTML = ((TableHistoryResponse) calculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML();
        assertEquals(renderedTable, tableHTML);

        verify(rendererMock, times(1)).renderCalculationHistory(eq(Collections.emptyList()), eq(true));


    }

    @Test
    public void resultIntegralCalculationResponse1Test() throws ExecutionException, InterruptedException {
        double lowerBound = 0;
        double upperBound = 1;
        double result = 1.71828;
        String expr = "integral{e^x}[0.00, 1.00]";
        String res = "1.71828";
        CompletableFuture<IntegrableFunction> integralResult = CompletableFuture.completedFuture(new ExponentialIntegral(lowerBound, upperBound, result));

        when(calculatorMock.resolveIntegralRequest(eq(null))).thenReturn(integralResult);

        PowerMockito.mockStatic(WebCalculatorValidation.class);
        IntegralRequest reqMock = PowerMockito.mock(IntegralRequest.class);
        try {
            doNothing().when(WebCalculatorValidation.class, "validateIntegralRequest", reqMock);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CalculationResponse calculationResponse = calculatorManager.processExponentialIntegralCalculation(null, TOKEN).get();

        verify(calculatorMock, Mockito.times(1)).resolveIntegralRequest(eq(null));
        PowerMockito.verifyStatic(Mockito.times(1));

        assertEquals(res, calculationResponse.getResult());
        assertEquals(expr, calculationResponse.getExpression());
    }

    @Test
    public void resultIntegralCalculationResponse2Test() throws ExecutionException, InterruptedException {
        int functionId = 0;
        double lowerBound = 0;
        double upperBound = 1;
        double result = 1.71828;
        int numThreads = 5;
        int repeatedCalculations = 6;
        String expr = "integral{e^x}[0.00, 1.00]";
        String res = "1.71828";
        evaluateResultFormatting(functionId, lowerBound, upperBound, result, numThreads, repeatedCalculations, expr, res);
    }

    @Test
    public void resultIntegralCalculationResponse3Test() throws ExecutionException, InterruptedException {
        int functionId = 0;
        double lowerBound = -10;
        double upperBound = -9.99;
        double result = 0.0000043345435;
        int numThreads = 5;
        int repeatedCalculations = 6;
        String expr = "integral{e^x}[" + lowerBound + "0, " + upperBound +"]";
        String res = "0.04335E-5";
        evaluateResultFormatting(functionId, lowerBound, upperBound, result, numThreads, repeatedCalculations, expr, res);
    }

    @Test
    public void resultIntegralCalculationResponse4Test() throws ExecutionException, InterruptedException {
        int functionId = 0;
        double lowerBound = 0;
        double upperBound = 0;
        double result = 0.0;
        int numThreads = 5;
        int repeatedCalculations = 6;
        String expr = "integral{e^x}[" + lowerBound + "0, " + upperBound +"0]";
        String res = "0.0";
        evaluateResultFormatting(functionId, lowerBound, upperBound, result, numThreads, repeatedCalculations, expr, res);
    }

    @Test
    public void resultIntegralCalculationResponse5Test() throws ExecutionException, InterruptedException {
        int functionId = 0;
        double lowerBound = 0;
        double upperBound = 0;
        double result = 0.0;
        int numThreads = 5;
        int repeatedCalculations = 6;
        String expr = "integral{e^x}[" + lowerBound + "0, " + upperBound +"0]";
        String res = "0.0";
        CompletableFuture<IntegrableFunction> integralResult = CompletableFuture.completedFuture(new ExponentialIntegral(lowerBound, upperBound, result));
        IntegralRequest integralRequest = new IntegralRequest.IntegralRequestBuilder()
                .withFunctionId(functionId)
                .withLowerBound(lowerBound)
                .withUpperBound(upperBound)
                .withNumThreads(numThreads)
                .withRepeatedCalculations(repeatedCalculations)
                .build();

        when(calculatorMock.resolveIntegralRequest(eq(integralRequest))).thenReturn(integralResult);

        CalculationResponse calculationResponse = calculatorManager.processExponentialIntegralCalculation(integralRequest, TOKEN).get();

        verify(calculatorMock, Mockito.times(1)).resolveIntegralRequest(eq(integralRequest));

        assertEquals(res, calculationResponse.getResult());
        assertEquals(expr, calculationResponse.getExpression());
    }

    private void evaluateResultFormatting(int functionId, double lowerBound, double upperBound, double result, int numThreads, int repeatedCalculations, String expr, String res) throws InterruptedException, ExecutionException {
        CompletableFuture<IntegrableFunction> integralResult = CompletableFuture.completedFuture(new ExponentialIntegral(lowerBound, upperBound, result));
        IntegralRequest integralReqMock = PowerMockito.mock(IntegralRequest.class);

        when(integralReqMock.getFunctionId()).thenReturn(functionId);
        when(integralReqMock.getLowerBound()).thenReturn(lowerBound);
        when(integralReqMock.getUpperBound()).thenReturn(upperBound);
        when(integralReqMock.getNumThreads()).thenReturn(numThreads);
        when(integralReqMock.getRepeatedCalculations()).thenReturn(repeatedCalculations);
        when(calculatorMock.resolveIntegralRequest(eq(integralReqMock))).thenReturn(integralResult);

        CalculationResponse calculationResponse = calculatorManager.processExponentialIntegralCalculation(integralReqMock, TOKEN).get();

        verify(integralReqMock, Mockito.times(1)).getRepeatedCalculations();
        verify(integralReqMock, Mockito.times(2)).getNumThreads();
        verify(integralReqMock, Mockito.times(1)).getFunctionId();
        verify(integralReqMock, Mockito.times(1)).getLowerBound();
        verify(integralReqMock, Mockito.times(1)).getUpperBound();
        verify(calculatorMock, Mockito.times(1)).resolveIntegralRequest(eq(integralReqMock));

        assertEquals(res, calculationResponse.getResult());
        assertEquals(expr, calculationResponse.getExpression());
    }


    @Test(expected = NoSuchElementException.class)
    public void resultIntegralCalculationResponseFunctionException2Test() throws ExecutionException, InterruptedException {
        int functionId = 1;
        IntegralRequest integralReqMock = PowerMockito.mock(IntegralRequest.class);

        when(integralReqMock.getFunctionId()).thenReturn(functionId);

        calculatorManager.processExponentialIntegralCalculation(integralReqMock, TOKEN).get();
    }

    @Test(expected = InputMismatchException.class)
    public void resultIntegralCalculationResponseBoundsException2Test() throws ExecutionException, InterruptedException {
        int functionId = 0;
        double lowerBound = 1;
        double upperBound = 0;
        IntegralRequest integralReqMock = PowerMockito.mock(IntegralRequest.class);

        when(integralReqMock.getFunctionId()).thenReturn(functionId);
        when(integralReqMock.getLowerBound()).thenReturn(lowerBound);
        when(integralReqMock.getUpperBound()).thenReturn(upperBound);

        calculatorManager.processExponentialIntegralCalculation(integralReqMock, TOKEN).get();

        verify(integralReqMock, times(1)).getFunctionId();
        verify(integralReqMock, times(1)).getLowerBound();
        verify(integralReqMock, times(1)).getUpperBound();
    }

    @Test(expected = InputMismatchException.class)
    public void resultIntegralCalculationNumThreadsHighException2Test() throws ExecutionException, InterruptedException {
        int functionId = 0;
        double lowerBound = 0;
        double upperBound = 1;
        int numThreads = 16;
        IntegralRequest integralReqMock = PowerMockito.mock(IntegralRequest.class);

        when(integralReqMock.getFunctionId()).thenReturn(functionId);
        when(integralReqMock.getLowerBound()).thenReturn(lowerBound);
        when(integralReqMock.getUpperBound()).thenReturn(upperBound);
        when(integralReqMock.getNumThreads()).thenReturn(numThreads);

        calculatorManager.processExponentialIntegralCalculation(integralReqMock, TOKEN).get();

        verify(integralReqMock, Mockito.times(1)).getNumThreads();
        verify(integralReqMock, Mockito.times(1)).getFunctionId();
        verify(integralReqMock, Mockito.times(1)).getLowerBound();
        verify(integralReqMock, Mockito.times(1)).getUpperBound();
    }

    @Test(expected = InputMismatchException.class)
    public void resultIntegralCalculationNumThreadsLowException2Test() throws ExecutionException, InterruptedException {
        int functionId = 0;
        double lowerBound = 0;
        double upperBound = 1;
        int numThreads = 0;
        IntegralRequest integralReqMock = PowerMockito.mock(IntegralRequest.class);

        when(integralReqMock.getFunctionId()).thenReturn(functionId);
        when(integralReqMock.getLowerBound()).thenReturn(lowerBound);
        when(integralReqMock.getUpperBound()).thenReturn(upperBound);
        when(integralReqMock.getNumThreads()).thenReturn(numThreads);

        calculatorManager.processExponentialIntegralCalculation(integralReqMock, TOKEN).get();

        verify(integralReqMock, Mockito.times(2)).getNumThreads();
        verify(integralReqMock, Mockito.times(1)).getFunctionId();
        verify(integralReqMock, Mockito.times(1)).getLowerBound();
        verify(integralReqMock, Mockito.times(1)).getUpperBound();
    }

    @Test(expected = InputMismatchException.class)
    public void resultIntegralCalculationRepeatedCalculationsException2Test() throws ExecutionException, InterruptedException {
        int functionId = 0;
        double lowerBound = 0;
        double upperBound = 0;
        int repeatedCalculations = -1;
        int numThreads = 1;
        IntegralRequest integralReqMock = PowerMockito.mock(IntegralRequest.class);

        when(integralReqMock.getFunctionId()).thenReturn(functionId);
        when(integralReqMock.getLowerBound()).thenReturn(lowerBound);
        when(integralReqMock.getUpperBound()).thenReturn(upperBound);
        when(integralReqMock.getNumThreads()).thenReturn(numThreads);
        when(integralReqMock.getRepeatedCalculations()).thenReturn(repeatedCalculations);

        calculatorManager.processExponentialIntegralCalculation(integralReqMock, TOKEN).get();

        verify(integralReqMock, Mockito.times(1)).getRepeatedCalculations();
        verify(integralReqMock, Mockito.times(2)).getNumThreads();
        verify(integralReqMock, Mockito.times(1)).getFunctionId();
        verify(integralReqMock, Mockito.times(1)).getLowerBound();
        verify(integralReqMock, Mockito.times(1)).getUpperBound();
    }

    @Test
    public void resultSimpleArithmeticResponse1Test() throws ExecutionException, InterruptedException {
        String expr = "6 - 10 * (2 - 5)";
        double res = 36;
        String resStr = "36";
        when(calculatorMock.solveArithmeticExpression(eq(expr))).thenReturn(res);

        CalculationResponse calculationResponse = (CalculationResponse) calculatorManager.processArithmeticCalculation(expr, TOKEN);

        assertEquals(resStr, calculationResponse.getResult());
        assertEquals(expr, calculationResponse.getExpression());

        verify(calculatorMock, times(1)).solveArithmeticExpression(eq(expr));

    }

}
