package com.acabra.calculator;

import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.integral.ExponentialIntegral;
import com.acabra.calculator.integral.IntegrableFunction;
import com.acabra.calculator.response.*;
import com.acabra.calculator.util.ResultFormatter;
import com.acabra.calculator.util.WebCalculatorConstants;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Agustin on 9/28/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({WebCalculatorManager.class, Calculator.class,
        WebCalculatorRendererHTML.class, WebCalculatorValidation.class,
        IntegralRequest.class, WebCalculatorFactorySimpleResponse.class,
        TokenResponse.class, TableHistoryResponse.class, ResultFormatter.class,
        ExponentialIntegral.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class WebCalculatorManagerTest {

    private WebCalculatorManager webCalculatorManager;
    private final static String TOKEN = "default-token";
    private final static String TOKEN2 = "default-token2";
    private final static String expr1 = "3 + 3";
    private final static double res1 = 6;
    private final static String expr2 = "4 + 4";
    private final static double res2 = 8;
    private final static String tableHeader = "<caption>History</caption><thead><tr><th>Id.</th><th>Expression</th><th>Result</th></tr></thead>";
    private final static String rowExpr1 = "<tr><td>1</td><td>3 + 3</td><td>6</td></tr>";
    private final static String rowExpr2 = "<tr><td>2</td><td>4 + 4</td><td>8</td></tr>";
    private final static String rowExpr3 = "<tr><td>1</td><td>sqrt ( 4 )</td><td>2</td></tr>";
    private final static String renderedTableEmpty = "<table>" + tableHeader + "</table>";
    private final static String renderedTable1 = "<table>" + tableHeader + "<tbody>" + rowExpr1 + "</tbody></table>";
    private final static String renderedTable2 = "<table>" + tableHeader + "<tbody>" + rowExpr1 + rowExpr2 + "</tbody></table>";
    private final static String renderedTable3 = "<table>" + tableHeader + "<tbody>" + rowExpr3 + "</tbody></table>";

    private Calculator calculatorMock;
    private WebCalculatorRenderer rendererMock;

    @Before
    public void prepare() throws Exception {
        this.calculatorMock = PowerMockito.mock(Calculator.class);
        this.rendererMock = PowerMockito.mock(WebCalculatorRendererHTML.class);

        whenNew(Calculator.class).withNoArguments().thenReturn(calculatorMock);

        webCalculatorManager = new WebCalculatorManager(rendererMock);

        verifyNew(Calculator.class).withNoArguments();
    }

    @Test
    public void provideSessionTokenTest() {
        long id = 0L;

        TokenResponse tokenResponseMock = PowerMockito.mock(TokenResponse.class);

        PowerMockito.mockStatic(WebCalculatorFactorySimpleResponse.class);
        when(WebCalculatorFactorySimpleResponse.createTokenResponse(id)).thenReturn(tokenResponseMock);

        assertTrue(tokenResponseMock == webCalculatorManager.provideSessionToken());

        PowerMockito.verifyStatic(Mockito.times(1));
        WebCalculatorFactorySimpleResponse.createTokenResponse(id);

    }

    @Test
    public void provideRenderedHistoryResultTest() {
        long id = 0L;
        String table = "tableHTML";
        int expectedSizeHistory = 0;
        List<CalculationResponse> emptyList = Collections.emptyList();

        TableHistoryResponse tableHistoryResponseMock = PowerMockito.mock(TableHistoryResponse.class);

        PowerMockito.mockStatic(WebCalculatorFactorySimpleResponse.class);
        when(WebCalculatorFactorySimpleResponse.createTableResponse(id, table)).thenReturn(tableHistoryResponseMock);

        when(rendererMock.renderCalculationHistory(eq(emptyList), eq(true))).thenReturn(table);

        assertTrue(tableHistoryResponseMock == webCalculatorManager.provideRenderedHistoryResult(TOKEN));
        assertEquals(expectedSizeHistory, webCalculatorManager.provideCalculationHistory(TOKEN).size());

        verify(rendererMock, times(1)).renderCalculationHistory(eq(emptyList), eq(true));

        PowerMockito.verifyStatic(times(1));
        WebCalculatorFactorySimpleResponse.createTableResponse(id, table);

    }

    @Test
    public void processCalculationTest() {
        int expectedSizeHistory = 2;

        when(calculatorMock.solveArithmeticExpression(eq(expr1))).thenReturn(res1);
        when(calculatorMock.solveArithmeticExpression(eq(expr2))).thenReturn(res2);

        CalculationResponse calculationResponse = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr1, TOKEN);

        assertEquals(0, calculationResponse.getId());
        assertEquals(calculationResponse.getExpression(), expr1);
        assertEquals(res1,  calculationResponse.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);

        CalculationResponse calculationResponse2 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr2, TOKEN);

        assertEquals(1, calculationResponse2.getId());
        assertEquals(expr2, calculationResponse2.getExpression());
        assertEquals(res2, calculationResponse2.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expectedSizeHistory, webCalculatorManager.provideCalculationHistory(TOKEN).size());

        verify(calculatorMock, times(1)).solveArithmeticExpression(eq(expr1));
        verify(calculatorMock, times(1)).solveArithmeticExpression(eq(expr2));
    }

    @Test
    public void provideCalculationHistoryTest() {
        int expectedSizeHistory = 2;

        when(calculatorMock.solveArithmeticExpression(eq(expr1))).thenReturn(res1);

        webCalculatorManager.processArithmeticCalculation(expr1, TOKEN);
        webCalculatorManager.processArithmeticCalculation(expr1, TOKEN);


        List<CalculationResponse> calculationHistory = webCalculatorManager.provideCalculationHistory(TOKEN2);

        assertEquals(0, calculationHistory.size());

        List<CalculationResponse> resultList = webCalculatorManager.provideCalculationHistory(TOKEN);

        assertEquals(expectedSizeHistory, resultList.size()); //valid size according to number of operations on that Token
        assertEquals(0, resultList.get(0).getId());
        assertEquals(1, resultList.get(1).getId());

        verify(calculatorMock, times(2)).solveArithmeticExpression(eq(expr1));
    }

    @Test
    public void provideCalculationHistory1Test() {
        assertNotNull(webCalculatorManager.provideCalculationHistory(TOKEN));
    }

    @Test
    public void provideRenderedHistoryResultEmptyTest() {
        int expectedSizeHistory = 0;
        when(rendererMock.renderCalculationHistory(eq(Collections.emptyList()), eq(true))).thenReturn(renderedTableEmpty);

        String emptyTable = ((TableHistoryResponse) webCalculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML();

        assertEquals(renderedTableEmpty, emptyTable);
        assertEquals(expectedSizeHistory, webCalculatorManager.provideCalculationHistory(TOKEN).size());

        verify(rendererMock, times(1)).renderCalculationHistory(eq(Collections.emptyList()), eq(true));
    }

    @Test
    public void provideRenderedHistoryResult1Test() {
        int expectedSizeHistory = 0;
        when(rendererMock.renderCalculationHistory(eq(Collections.emptyList()), eq(true))).thenReturn(renderedTable1);

        assertEquals(renderedTable1, ((TableHistoryResponse) webCalculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML());
        assertEquals(expectedSizeHistory, webCalculatorManager.provideCalculationHistory(TOKEN).size());

        verify(rendererMock, times(1)).renderCalculationHistory(eq(Collections.emptyList()), eq(true));
    }

    @Test
    public void provideRenderedHistoryResult2Test() {
        int expectedSizeHistory = 2;
        when(calculatorMock.solveArithmeticExpression(eq(expr1))).thenReturn(res1);
        when(calculatorMock.solveArithmeticExpression(eq(expr2))).thenReturn(res2);

        webCalculatorManager.processArithmeticCalculation(expr1, TOKEN);
        webCalculatorManager.processArithmeticCalculation(expr2, TOKEN);

        List<CalculationResponse> historyList = webCalculatorManager.provideCalculationHistory(TOKEN);
        when(rendererMock.renderCalculationHistory(eq(historyList), eq(true))).thenReturn(renderedTable2);

        assertEquals(renderedTable2, ((TableHistoryResponse) webCalculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML());
        assertEquals(expectedSizeHistory, historyList.size());

        verify(rendererMock, times(1)).renderCalculationHistory(eq(historyList), eq(true));
        verify(calculatorMock, times(1)).solveArithmeticExpression(eq(expr1));
        verify(calculatorMock, times(1)).solveArithmeticExpression(eq(expr2));
    }

    @Test
    public void provideRenderedHistoryResult3Test() {
        when(rendererMock.renderCalculationHistory(eq(Collections.emptyList()), eq(true))).thenReturn(renderedTable3);

        assertEquals(renderedTable3, ((TableHistoryResponse) webCalculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML());

        verify(rendererMock, times(1)).renderCalculationHistory(eq(Collections.emptyList()), eq(true));
    }

    @Test
    public void provideRenderedHistoryResult4Test() {
        String rowExpr4 = "<tr><td>5</td><td>10 - 5</td><td>5</td></tr>";
        String renderedTable4 = "<table>" + tableHeader + "<tbody>" + rowExpr4 + "</tbody></table>";

        when(rendererMock.renderCalculationHistory(eq(Collections.emptyList()), eq(true))).thenReturn(renderedTable4);

        assertEquals(renderedTable4, ((TableHistoryResponse) webCalculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML());

        verify(rendererMock, times(1)).renderCalculationHistory(eq(Collections.emptyList()), eq(true));
    }

    @Test
    public void provideRenderedHistoryResult5Test() {
        double res = -4;
        String rowExpr = "<tr><td>5</td><td>6 - 10</td><td>" + res + "</td></tr>";
        String renderedTable = "<table>" + tableHeader + "<tbody>" + rowExpr + "</tbody></table>";

        when(rendererMock.renderCalculationHistory(eq(Collections.emptyList()), eq(true))).thenReturn(renderedTable);

        assertEquals(renderedTable, ((TableHistoryResponse) webCalculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML());

        verify(rendererMock, times(1)).renderCalculationHistory(eq(Collections.emptyList()), eq(true));
    }

    @Test
    public void provideRenderedHistoryResult6Test() {
        String expr = "6 - 10 * (2 - 5)";
        double res = -4;
        String rowExpr = "<tr><td>5</td><td>" + expr + "</td><td>" + res + "</td></tr>";
        String renderedTable = "<table>" + tableHeader + "<tbody>" + rowExpr + "</tbody></table>";

        when(rendererMock.renderCalculationHistory(eq(Collections.emptyList()), eq(true))).thenReturn(renderedTable);

        assertEquals(renderedTable, ((TableHistoryResponse) webCalculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML());

        verify(rendererMock, times(1)).renderCalculationHistory(eq(Collections.emptyList()), eq(true));
    }

    @Test
    public void provideRenderedHistoryResultIntegral1Test() throws ExecutionException, InterruptedException {
        String expr = "integ{e^x}[0.00, 1.00]";
        String res = "1.71456";
        String rowExpr = "<tr><td>1</td><td>" + expr + "</td><td>" + res + "</td></tr>";
        String renderedTable = "<table>" + tableHeader + "<tbody>" + rowExpr + "</tbody></table>";

        when(rendererMock.renderCalculationHistory(eq(Collections.emptyList()), eq(true))).thenReturn(renderedTable);

        assertEquals(renderedTable, ((TableHistoryResponse) webCalculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML());

        verify(rendererMock, times(1)).renderCalculationHistory(eq(Collections.emptyList()), eq(true));

    }

    @Test
    public void provideRenderedHistoryResultIntegral2Test() throws ExecutionException, InterruptedException {
        String expr = "integ{e^x}[0.00, 0.00]";
        String res = "0";
        String rowExpr = "<tr><td>1</td><td>" + expr + "</td><td>" + res + "</td></tr>";
        String renderedTable = "<table>" + tableHeader + "<tbody>" + rowExpr + "</tbody></table>";

        when(rendererMock.renderCalculationHistory(eq(Collections.emptyList()), eq(true))).thenReturn(renderedTable);

        assertEquals(renderedTable, ((TableHistoryResponse) webCalculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML());

        verify(rendererMock, times(1)).renderCalculationHistory(eq(Collections.emptyList()), eq(true));

    }

    @Test
    public void provideRenderedHistoryResultIntegral3Test() throws Exception {
        String expr = "integ{e^x}[-10.0, -9.99]";
        String res = "0.00456E-5";
        String rowExpr = "<tr><td>1</td><td>" + expr + "</td><td>" + res + "</td></tr>";
        String renderedTable = "<table>" + tableHeader + "<tbody>" + rowExpr + "</tbody></table>";

        when(rendererMock.renderCalculationHistory(eq(Collections.emptyList()), eq(true))).thenReturn(renderedTable);

        String tableHTML = ((TableHistoryResponse) webCalculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML();
        assertEquals(renderedTable, tableHTML);

        verify(rendererMock, times(1)).renderCalculationHistory(eq(Collections.emptyList()), eq(true));


    }

    @Test
    public void resultIntegralCalculationResponse1Test() throws Exception {
        double lowerBound = 0;
        double upperBound = 1;
        double result = 1.71828;
        double approximation = 1.0;
        String expr = "Integ{e^x}[0, 1] #Rep=10 #Th=5";
        int repeatedCalculations = 10;
        boolean inscribed = true;
        int numThreads = 5;

        ExponentialIntegral expIntegralMock = PowerMockito.mock(ExponentialIntegral.class);

        when(expIntegralMock.getUpperBound()).thenReturn(upperBound);
        when(expIntegralMock.getLowerBound()).thenReturn(lowerBound);
        when(expIntegralMock.getLabel()).thenReturn(null);
        when(expIntegralMock.getResult()).thenReturn(result);
        when(expIntegralMock.getSequenceRiemannRectangle()).thenReturn(approximation);

        CompletableFuture<IntegrableFunction> futureSolvedIntegral = CompletableFuture.completedFuture(expIntegralMock);

        IntegralRequest integralRequestMock = PowerMockito.mock(IntegralRequest.class);
        when(integralRequestMock.getLowerBound()).thenReturn(lowerBound);
        when(integralRequestMock.getUpperBound()).thenReturn(upperBound);
        when(integralRequestMock.getRepeatedCalculations()).thenReturn(repeatedCalculations);
        when(integralRequestMock.getNumThreads()).thenReturn(numThreads);
        when(integralRequestMock.isAreaInscribed()).thenReturn(inscribed);

        when(calculatorMock.resolveIntegralApproximateRiemannSequenceRequest(any())).thenReturn(futureSolvedIntegral);

        PowerMockito.mockStatic(WebCalculatorValidation.class, ResultFormatter.class);
        IntegralRequest reqMock = PowerMockito.mock(IntegralRequest.class);

        PowerMockito.doNothing().when(WebCalculatorValidation.class, "validateIntegralRequest", reqMock);
        PowerMockito.when(ResultFormatter.formatIntegralRequest(futureSolvedIntegral.get().getLabel(), lowerBound + "", upperBound + "", repeatedCalculations, numThreads))
                .thenReturn(expr);

        IntegralCalculationResponse integralCalculationResponse = (IntegralCalculationResponse) webCalculatorManager.processExponentialIntegralCalculation(integralRequestMock, TOKEN).get();

        verify(calculatorMock, Mockito.times(1)).resolveIntegralApproximateRiemannSequenceRequest(any());

        PowerMockito.verifyStatic(Mockito.times(1));
        WebCalculatorValidation.validateIntegralRequest(integralRequestMock);

        PowerMockito.verifyStatic(Mockito.times(1));
        ResultFormatter.formatIntegralRequest(futureSolvedIntegral.get().getLabel(), lowerBound + "", upperBound + "", repeatedCalculations, numThreads);

        verify(integralRequestMock, times(1)).getLowerBound();
        verify(integralRequestMock, times(1)).getUpperBound();
        verify(integralRequestMock, times(1)).getNumThreads();
        verify(integralRequestMock, times(1)).getRepeatedCalculations();
        verify(integralRequestMock, times(0)).getFunctionId();
        verify(integralRequestMock, times(0)).isAreaInscribed();

        verify(expIntegralMock, times(3)).getLabel();
        verify(expIntegralMock, times(0)).getUpperBound();
        verify(expIntegralMock, times(0)).getLowerBound();
        verify(expIntegralMock, times(1)).getResult();
        verify(expIntegralMock, times(1)).getSequenceRiemannRectangle();

        assertEquals(approximation, integralCalculationResponse.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(result, integralCalculationResponse.getIntegralResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals("Integral", integralCalculationResponse.getDescription());
        assertEquals(expr, integralCalculationResponse.getExpression());
    }

    @Test
    public void resultIntegralCalculationResponse2Test() throws Exception {
        int functionId = 0;
        double lowerBound = 0;
        double upperBound = 1;
        double result = 1.0;
        int numThreads = 5;
        int repeatedCalculations = 6;
        String expr = "Integ{e^x}[0, 1] #Rep=6 #Th=5";
        String description = "Integral";
        double exactIntegral = 1.0;
        boolean inscribed = true;
        evaluateResultFormatting(functionId, lowerBound, upperBound, result, numThreads, repeatedCalculations, expr, description, exactIntegral, inscribed);
    }

    @Test
    public void resultIntegralCalculationResponse3Test() throws Exception {
        int functionId = 0;
        double lowerBound = -10;
        double upperBound = -9.99;
        double result = 4.5399929762483885E-7;
        int numThreads = 5;
        int repeatedCalculations = 6;
        double exactIntegral = 4.5399929762483885E-7;
        String expr = "Integ{e^x}[-10, -9.99] #Rep=6 #Th=5";
        String description = "Integral";
        boolean inscribed = true;
        evaluateResultFormatting(functionId, lowerBound, upperBound, result, numThreads, repeatedCalculations, expr, description, exactIntegral, inscribed);
    }

    @Test
    public void resultIntegralCalculationResponse4Test() throws Exception {
        int functionId = 0;
        double lowerBound = 0;
        double upperBound = 0;
        double result = 0.0;
        int numThreads = 5;
        int repeatedCalculations = 6;
        double exactIntegral = 0.0;
        String expr = "Integ{e^x}[0, 0] #Rep=6 #Th=5";
        String description = "Integral";
        boolean inscribed = true;
        evaluateResultFormatting(functionId, lowerBound, upperBound, result, numThreads, repeatedCalculations, expr, description, exactIntegral, inscribed);
    }

    @Test
    public void resultIntegralCalculationResponse5Test() throws Exception {
        int functionId = 0;
        double lowerBound = 0;
        double upperBound = 0;
        double result = 0.0;
        int numThreads = 5;
        int repeatedCalculations = 6;
        String expr = "Integ{e^x}[0, 0] #Rep=6 #Th=5";
        CompletableFuture<IntegrableFunction> integralResult = CompletableFuture.completedFuture(new ExponentialIntegral(lowerBound, upperBound, result));
        IntegralRequest integralRequest = new IntegralRequest.IntegralRequestBuilder()
                .withFunctionId(functionId)
                .withLowerBound(lowerBound)
                .withUpperBound(upperBound)
                .withNumThreads(numThreads)
                .withRepeatedCalculations(repeatedCalculations)
                .build();

        when(calculatorMock.resolveIntegralApproximateRiemannSequenceRequest(eq(integralRequest))).thenReturn(integralResult);

        CalculationResponse calculationResponse = webCalculatorManager.processExponentialIntegralCalculation(integralRequest, TOKEN).get();

        verify(calculatorMock, Mockito.times(1)).resolveIntegralApproximateRiemannSequenceRequest(eq(integralRequest));

        assertEquals(result, calculationResponse.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expr, calculationResponse.getExpression());
    }

    private void evaluateResultFormatting(int functionId, double lowerBound, double upperBound, double result, int numThreads,
                                          int repeatedCalculations, String expr, String description, double exactIntegral, boolean inscribed) throws Exception {


        ExponentialIntegral expIntegralMock = PowerMockito.mock(ExponentialIntegral.class);
        when(expIntegralMock.getUpperBound()).thenReturn(upperBound);
        when(expIntegralMock.getLowerBound()).thenReturn(lowerBound);
        when(expIntegralMock.getLabel()).thenReturn(null);
        when(expIntegralMock.getResult()).thenReturn(exactIntegral);
        when(expIntegralMock.getSequenceRiemannRectangle()).thenReturn(result);

        IntegralRequest integralReqMock = PowerMockito.mock(IntegralRequest.class);
        when(integralReqMock.getFunctionId()).thenReturn(functionId);
        when(integralReqMock.getLowerBound()).thenReturn(lowerBound);
        when(integralReqMock.getUpperBound()).thenReturn(upperBound);
        when(integralReqMock.getNumThreads()).thenReturn(numThreads);
        when(integralReqMock.getRepeatedCalculations()).thenReturn(repeatedCalculations);
        when(integralReqMock.isAreaInscribed()).thenReturn(inscribed);

        CompletableFuture<IntegrableFunction> futureSolvedIntegral = CompletableFuture.completedFuture(expIntegralMock);

        PowerMockito.mockStatic(WebCalculatorValidation.class, ResultFormatter.class);
        PowerMockito.doNothing().when(WebCalculatorValidation.class, "validateIntegralRequest", integralReqMock);
        PowerMockito.when(ResultFormatter.formatIntegralRequest(expIntegralMock.getLabel(), lowerBound + "", upperBound + "", repeatedCalculations, numThreads)).thenReturn(expr);

        when(calculatorMock.resolveIntegralApproximateRiemannSequenceRequest(eq(integralReqMock))).thenReturn(futureSolvedIntegral);

        IntegralCalculationResponse integralCalculationResponse = (IntegralCalculationResponse) webCalculatorManager.processExponentialIntegralCalculation(integralReqMock, TOKEN).get();

        PowerMockito.verifyStatic(Mockito.times(1));
        WebCalculatorValidation.validateIntegralRequest(integralReqMock);

        PowerMockito.verifyStatic(Mockito.times(1));
        ResultFormatter.formatIntegralRequest(expIntegralMock.getLabel(), lowerBound + "", upperBound + "", repeatedCalculations, numThreads);

        verify(integralReqMock, Mockito.times(1)).getRepeatedCalculations();
        verify(integralReqMock, Mockito.times(1)).getNumThreads();
        verify(integralReqMock, Mockito.times(0)).getFunctionId();
        verify(integralReqMock, Mockito.times(1)).getLowerBound();
        verify(integralReqMock, Mockito.times(1)).getUpperBound();
        verify(integralReqMock, Mockito.times(0)).isAreaInscribed();
        verify(calculatorMock, Mockito.times(1)).resolveIntegralApproximateRiemannSequenceRequest(eq(integralReqMock));

        verify(expIntegralMock, times(3)).getLabel();
        verify(expIntegralMock, times(0)).getUpperBound();
        verify(expIntegralMock, times(0)).getLowerBound();
        verify(expIntegralMock, times(1)).getResult();
        verify(expIntegralMock, times(1)).getSequenceRiemannRectangle();

        assertEquals(expr, integralCalculationResponse.getExpression());
        assertEquals(description, integralCalculationResponse.getDescription());
        assertEquals(result, integralCalculationResponse.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(exactIntegral, integralCalculationResponse.getIntegralResult(), WebCalculatorConstants.ACCURACY_EPSILON);
    }


    @Test(expected = NoSuchElementException.class)
    public void resultIntegralCalculationResponseFunctionException2Test() throws ExecutionException, InterruptedException {
        int functionId = 1;
        IntegralRequest integralReqMock = PowerMockito.mock(IntegralRequest.class);

        when(integralReqMock.getFunctionId()).thenReturn(functionId);

        webCalculatorManager.processExponentialIntegralCalculation(integralReqMock, TOKEN).get();
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

        webCalculatorManager.processExponentialIntegralCalculation(integralReqMock, TOKEN).get();

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

        webCalculatorManager.processExponentialIntegralCalculation(integralReqMock, TOKEN).get();

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

        webCalculatorManager.processExponentialIntegralCalculation(integralReqMock, TOKEN).get();

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

        webCalculatorManager.processExponentialIntegralCalculation(integralReqMock, TOKEN).get();

        verify(integralReqMock, Mockito.times(1)).getRepeatedCalculations();
        verify(integralReqMock, Mockito.times(2)).getNumThreads();
        verify(integralReqMock, Mockito.times(1)).getFunctionId();
        verify(integralReqMock, Mockito.times(1)).getLowerBound();
        verify(integralReqMock, Mockito.times(1)).getUpperBound();
    }

    @Test(expected = InputMismatchException.class)
    public void resultIntegralCalculationRepeatedCalculationsException3Test() throws ExecutionException, InterruptedException {
        int functionId = 0;
        double lowerBound = 0;
        double upperBound = 0;
        int repeatedCalculations = Integer.MAX_VALUE;
        int numThreads = 1;
        IntegralRequest integralReqMock = PowerMockito.mock(IntegralRequest.class);

        when(integralReqMock.getFunctionId()).thenReturn(functionId);
        when(integralReqMock.getLowerBound()).thenReturn(lowerBound);
        when(integralReqMock.getUpperBound()).thenReturn(upperBound);
        when(integralReqMock.getNumThreads()).thenReturn(numThreads);
        when(integralReqMock.getRepeatedCalculations()).thenReturn(repeatedCalculations);

        webCalculatorManager.processExponentialIntegralCalculation(integralReqMock, TOKEN).get();

        verify(integralReqMock, Mockito.times(1)).getRepeatedCalculations();
        verify(integralReqMock, Mockito.times(2)).getNumThreads();
        verify(integralReqMock, Mockito.times(1)).getFunctionId();
        verify(integralReqMock, Mockito.times(1)).getLowerBound();
        verify(integralReqMock, Mockito.times(1)).getUpperBound();
    }

    @Test
    public void resultSimpleArithmeticResponse1Test() throws ExecutionException, InterruptedException {
        String expr = "6 - 10 * ( 2 - 5 )";
        double res = 36;
        int expectedSizeHistory = 1;
        when(calculatorMock.solveArithmeticExpression(eq(expr))).thenReturn(res);

        CalculationResponse calculationResponse = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr, TOKEN);

        verify(calculatorMock, times(1)).solveArithmeticExpression(eq(expr));

        assertEquals(res, calculationResponse.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expr, calculationResponse.getExpression());
        assertEquals(expectedSizeHistory, webCalculatorManager.provideCalculationHistory(TOKEN).size());


    }

    @Test(expected = InputMismatchException.class)
    public void resultSimpleArithmeticFail1Test() {
        String expr = "6 - 10 * ( 2 - 5 ) &&";
        double res = 36;
        int expectedSizeHistory = 1;
        when(calculatorMock.solveArithmeticExpression(eq(expr))).thenReturn(res);

        CalculationResponse calculationResponse = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr, TOKEN);

        verify(calculatorMock, times(1)).solveArithmeticExpression(eq(expr));

        assertEquals(res, calculationResponse.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(expr, calculationResponse.getExpression());
        assertEquals(expectedSizeHistory, webCalculatorManager.provideCalculationHistory(TOKEN).size());
    }

    @Test
    public void cleanEntriesTest() {
        String expr = "1 + 0";
        double res = 1;
        int expectedSizeHistoryAfterCleanUp = 0;
        int expectedSizeHistoryBeforeCleanUp = 3;
        int expectedCleanedEntries = 3;

        when(calculatorMock.solveArithmeticExpression(any())).thenReturn(res);

        CalculationResponse calculationResponse1 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr, TOKEN);
        CalculationResponse calculationResponse2 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr, TOKEN2);
        CalculationResponse calculationResponse3 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr, TOKEN2);

        int before = webCalculatorManager.countHistorySize();
        CompletableFuture<Integer> cleanedFuture = webCalculatorManager.cleanExpiredEntries(LocalDateTime.now().minusMinutes(10), 0, ChronoUnit.SECONDS);

        assertEquals(expectedSizeHistoryBeforeCleanUp, before);
        cleanedFuture.thenAccept(cleanedEntries -> {
            assertEquals(expectedCleanedEntries, cleanedEntries.intValue());
            assertEquals(expectedSizeHistoryAfterCleanUp, webCalculatorManager.countHistorySize());
        });
        assertEquals(res, calculationResponse1.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(res, calculationResponse2.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(res, calculationResponse3.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);

        verify(calculatorMock, Mockito.times(3)).solveArithmeticExpression(expr);
    }

    @Test
    public void cleanEntriesNoneTest() {
        String expr = "2 + 1";
        double res = 3;
        int expectedSizeHistoryBeforeCleanUp = 3;
        int expectedCleanedEntries = 0;

        when(calculatorMock.solveArithmeticExpression(any())).thenReturn(res);

        CalculationResponse calculationResponse1 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr, TOKEN);
        CalculationResponse calculationResponse2 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr, TOKEN2);
        CalculationResponse calculationResponse3 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr, TOKEN2);

        int before = webCalculatorManager.countHistorySize();

        CompletableFuture<Integer> cleaningFuture = webCalculatorManager.cleanExpiredEntries(LocalDateTime.now().minusMinutes(15), 20, ChronoUnit.MINUTES);

        assertEquals(expectedSizeHistoryBeforeCleanUp, before);
        cleaningFuture.thenAccept(cleanedEntries -> {
            assertEquals(expectedCleanedEntries, cleanedEntries.intValue());
            assertEquals(expectedSizeHistoryBeforeCleanUp, webCalculatorManager.countHistorySize());
        });
        assertEquals(res, calculationResponse1.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(res, calculationResponse2.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(res, calculationResponse3.getResult(), WebCalculatorConstants.ACCURACY_EPSILON);

        verify(calculatorMock, Mockito.times(3)).solveArithmeticExpression(expr);
    }

}
