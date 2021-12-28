package com.acabra.calculator;

import com.google.common.base.Stopwatch;
import com.acabra.calculator.domain.CalculationHistoryRecord;
import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.domain.IntegralRequestBuilder;
import com.acabra.calculator.integral.approx.NumericalMethodAreaApproximationType;
import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralExponential;
import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralFunction;
import com.acabra.calculator.integral.definiteintegral.DefiniteIntegralFunctionFactory;
import com.acabra.calculator.response.CalculationResponse;
import com.acabra.calculator.response.IntegralCalculationResponse;
import com.acabra.calculator.response.RenderedHistoryResponse;
import com.acabra.calculator.response.TokenResponse;
import com.acabra.calculator.response.WebCalculatorFactoryResponse;
import com.acabra.calculator.response.WebCalculatorFactorySimpleResponse;
import com.acabra.calculator.util.ResultFormatter;
import com.acabra.calculator.util.WebCalculatorConstants;
import com.acabra.calculator.util.WebCalculatorValidation;
import com.acabra.calculator.view.WebCalculatorRenderer;
import com.acabra.calculator.view.WebCalculatorRendererHTML;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.CheckReturnValue;
import org.junit.Assert;



import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Agustin on 9/28/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({WebCalculatorManager.class, Calculator.class,
        WebCalculatorRendererHTML.class, WebCalculatorValidation.class,
        IntegralRequest.class, WebCalculatorFactorySimpleResponse.class,
        WebCalculatorFactoryResponse.class,
        TokenResponse.class, RenderedHistoryResponse.class, ResultFormatter.class,
        DefiniteIntegralExponential.class, CalculationHistoryRecord.class, Stopwatch.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class WebCalculatorManagerTest {

    private WebCalculatorManager webCalculatorManager;
    private final static String TOKEN = "default-token";
    private final static String TOKEN2 = "default-token2";
    private final static String expr1 = "3 + 3";
    private final static String res1 = "6";
    private final static String expr2 = "4 + 4";
    private final static String res2 = "8";
    private final static String expr3 = "@ ( 4 )";
    private final static String res3 = "2";
    private final static String tableHeader = "<caption>History</caption><thead><tr><th>Id.</th><th>Expression</th><th>Result</th></tr></thead>";
    private final static String rowExpr1 = "<tr><td>1</td><td>3 + 3</td><td>6</td></tr>";
    private final static String rowExpr2 = "<tr><td>2</td><td>4 + 4</td><td>8</td></tr>";
    private final static String rowExpr3 = "<tr><td>1</td><td>sqrt ( 4 )</td><td>2</td></tr>";
    private final static String renderedTable2 = "<table>" + tableHeader + "<tbody>" + rowExpr1 + rowExpr2 + "</tbody></table>";
    private final static String renderedTable3 = "<table>" + tableHeader + "<tbody>" + rowExpr3 + "</tbody></table>";

    private Calculator calculatorMock;
    private WebCalculatorRenderer rendererMock;

    @BeforeEach
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

    @Test @CheckReturnValue
    public void provideRenderedHistoryResultTest() {
        long id = 1L;
        String table = "renderedTable";
        int expectedSizeHistory = 1;

        RenderedHistoryResponse renderedHistoryResponseMock = PowerMockito.mock(RenderedHistoryResponse.class);

        PowerMockito.mockStatic(WebCalculatorFactorySimpleResponse.class);
        when(WebCalculatorFactorySimpleResponse.createTableResponse(id, table)).thenReturn(renderedHistoryResponseMock);

        when(rendererMock.renderCalculationHistory(any(), eq(true))).thenReturn(table);
        PowerMockito.when(calculatorMock.solveArithmeticExpression(any())).thenReturn(new BigDecimal("0"));

        webCalculatorManager.processArithmeticCalculation("2 - 2", TOKEN);
        assertTrue(renderedHistoryResponseMock == webCalculatorManager.provideRenderedHistoryResponse(TOKEN));
        assertEquals(expectedSizeHistory, webCalculatorManager.provideRawHistory(TOKEN).size());

        verify(rendererMock, times(1)).renderCalculationHistory(any(), eq(true));
        verify(calculatorMock, times(1)).solveArithmeticExpression(any());

        PowerMockito.verifyStatic(times(1));
        WebCalculatorFactorySimpleResponse.createTableResponse(id, table);

    }

    @Test
    public void processCalculationTest() {
        int expectedSizeHistory = 2;

        when(calculatorMock.solveArithmeticExpression(eq(expr1))).thenReturn(new BigDecimal(res1));
        when(calculatorMock.solveArithmeticExpression(eq(expr2))).thenReturn(new BigDecimal(res2));

        CalculationResponse calculationResponse = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr1, TOKEN);

        assertEquals(0, calculationResponse.getId());
        assertEquals(calculationResponse.getExpression(), expr1);
        assertEquals(res1,  calculationResponse.getResult());

        CalculationResponse calculationResponse2 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr2, TOKEN);

        assertEquals(1, calculationResponse2.getId());
        assertEquals(expr2, calculationResponse2.getExpression());
        assertEquals(res2, calculationResponse2.getResult());
        assertEquals(expectedSizeHistory, webCalculatorManager.provideRawHistory(TOKEN).size());

        verify(calculatorMock, times(1)).solveArithmeticExpression(eq(expr1));
        verify(calculatorMock, times(1)).solveArithmeticExpression(eq(expr2));
    }

    @Test
    public void provideCalculationHistoryTest() {
        int expectedSizeHistory = 2;

        when(calculatorMock.solveArithmeticExpression(eq(expr1))).thenReturn(new BigDecimal(res1));

        webCalculatorManager.processArithmeticCalculation(expr1, TOKEN);
        webCalculatorManager.processArithmeticCalculation(expr1, TOKEN);

        boolean exceptionToken2 = false;

        List<CalculationResponse> calculationHistory;
        try {
            calculationHistory = webCalculatorManager.provideRawHistory(TOKEN2);
        } catch (NoSuchElementException nsee) {
            calculationHistory = Collections.emptyList();
            exceptionToken2 = true;
        }

        assertTrue(exceptionToken2);
        assertEquals(0, calculationHistory.size());

        List<CalculationResponse> resultList = webCalculatorManager.provideRawHistory(TOKEN);

        assertEquals(expectedSizeHistory, resultList.size()); //valid size according to number of operations on that Token
        assertEquals(0, resultList.get(0).getId());
        assertEquals(1, resultList.get(1).getId());

        verify(calculatorMock, times(2)).solveArithmeticExpression(eq(expr1));
    }

    @Test(expected = NoSuchElementException.class)
    public void provideRenderedHistoryResultEmptyTest() {
        webCalculatorManager.provideRenderedHistoryResponse(TOKEN);
    }

    @Test
    public void provideRenderedHistoryResult2Test() {
        int expectedSizeHistory = 2;
        when(calculatorMock.solveArithmeticExpression(eq(expr1))).thenReturn(new BigDecimal(res1));
        when(calculatorMock.solveArithmeticExpression(eq(expr2))).thenReturn(new BigDecimal(res2));

        webCalculatorManager.processArithmeticCalculation(expr1, TOKEN);
        webCalculatorManager.processArithmeticCalculation(expr2, TOKEN);

        List<CalculationResponse> historyList = webCalculatorManager.provideRawHistory(TOKEN);
        when(rendererMock.renderCalculationHistory(eq(historyList), eq(true))).thenReturn(renderedTable2);

        assertEquals(renderedTable2, ((RenderedHistoryResponse) webCalculatorManager.provideRenderedHistoryResponse(TOKEN)).getRenderedTable());
        assertEquals(expectedSizeHistory, historyList.size());

        verify(rendererMock, times(1)).renderCalculationHistory(eq(historyList), eq(true));
        verify(calculatorMock, times(1)).solveArithmeticExpression(eq(expr1));
        verify(calculatorMock, times(1)).solveArithmeticExpression(eq(expr2));
    }

    @Test
    public void provideRenderedHistoryResult3Test() {
        when(rendererMock.renderCalculationHistory(any(), eq(true))).thenReturn(renderedTable3);

        when(calculatorMock.solveArithmeticExpression(eq(expr3))).thenReturn(new BigDecimal(res3));

        webCalculatorManager.processArithmeticCalculation(expr3, TOKEN);
        assertEquals(renderedTable3, ((RenderedHistoryResponse) webCalculatorManager.provideRenderedHistoryResponse(TOKEN)).getRenderedTable());


        verify(rendererMock, times(1)).renderCalculationHistory(any(), eq(true));
        verify(calculatorMock, times(1)).solveArithmeticExpression(eq(expr3));
    }

    @Test
    public void provideRenderedHistoryResult4Test() {
        String expr4 = "10 - 5";
        String result4 = "5";
        String rowExpr4 = "<tr><td>1</td><td>" + expr4 + "</td><td>" + result4 + "</td></tr>";
        String renderedTable4 = "<table>" + tableHeader + "<tbody>" + rowExpr4 + "</tbody></table>";

        when(rendererMock.renderCalculationHistory(any(), eq(true))).thenReturn(renderedTable4);
        PowerMockito.when(calculatorMock.solveArithmeticExpression(expr4)).thenReturn(new BigDecimal(result4));

        assertEquals(result4, ((CalculationResponse)webCalculatorManager.processArithmeticCalculation(expr4, TOKEN)).getResult());
        assertEquals(renderedTable4, ((RenderedHistoryResponse) webCalculatorManager.provideRenderedHistoryResponse(TOKEN)).getRenderedTable());

        verify(rendererMock, times(1)).renderCalculationHistory(any(), eq(true));
    }

    @Test
    public void provideRenderedHistoryResult5Test() {
        String res = "-4";
        String expression = "6 - 10";
        String rowExpr = "<tr><td>5</td><td>" + expression + "</td><td>" + res + "</td></tr>";
        String renderedTable = "<table>" + tableHeader + "<tbody>" + rowExpr + "</tbody></table>";

        when(rendererMock.renderCalculationHistory(any(), eq(true))).thenReturn(renderedTable);
        PowerMockito.when(calculatorMock.solveArithmeticExpression(expression)).thenReturn(new BigDecimal(res));

        assertEquals(res, ((CalculationResponse)webCalculatorManager.processArithmeticCalculation(expression, TOKEN)).getResult());
        assertEquals(renderedTable, ((RenderedHistoryResponse) webCalculatorManager.provideRenderedHistoryResponse(TOKEN)).getRenderedTable());

        verify(rendererMock, times(1)).renderCalculationHistory(any(), eq(true));
        verify(calculatorMock, times(1)).solveArithmeticExpression(eq(expression));
    }

    @Test
    public void provideRenderedHistoryResult6Test() {
        String expr = "6 - 10 * ( 2 - 5 ) ";
        String res = "-4";
        String rowExpr = "<tr><td>5</td><td>" + expr + "</td><td>" + res + "</td></tr>";
        String renderedTable = "<table>" + tableHeader + "<tbody>" + rowExpr + "</tbody></table>";

        when(rendererMock.renderCalculationHistory(any(), eq(true))).thenReturn(renderedTable);
        PowerMockito.when(calculatorMock.solveArithmeticExpression(expr)).thenReturn(new BigDecimal(res));

        assertEquals(res, ((CalculationResponse)webCalculatorManager.processArithmeticCalculation(expr, TOKEN)).getResult());
        assertEquals(renderedTable, ((RenderedHistoryResponse) webCalculatorManager.provideRenderedHistoryResponse(TOKEN)).getRenderedTable());

        verify(rendererMock, times(1)).renderCalculationHistory(any(), eq(true));
        verify(calculatorMock, times(1)).solveArithmeticExpression(eq(expr));
    }

    @Test
    public void provideRenderedHistoryResultIntegral1Test() throws Exception {
        int sizeBefore = 0;
        int sizeAfter = 1;
        String expr = "integ{e^x}[0.00, 1.00]";
        String res = "1.71456";
        String rowExpr = "<tr><td>1</td><td>" + expr + "</td><td>" + res + "</td></tr>";
        String renderedTable = "<table>" + tableHeader + "<tbody>" + rowExpr + "</tbody></table>";
        String integralStrRep = "integralStrRep";
        IntegralRequest integralRequest = new IntegralRequest(0, 1, 1, 1, 0, 0, true, Collections.emptyList());

        when(rendererMock.renderCalculationHistory(any(), eq(true))).thenReturn(renderedTable);

        CalculationResponse calculationResponseMock = PowerMockito.mock(CalculationResponse.class);

        DefiniteIntegralFunction solvedIntegralMock = PowerMockito.mock(DefiniteIntegralFunction.class);
        when(solvedIntegralMock.getApproximation()).thenReturn(1.1);
        when(solvedIntegralMock.getResult()).thenReturn(1.2);
        when(solvedIntegralMock.toString()).thenReturn(integralStrRep);

        CompletableFuture<DefiniteIntegralFunction> future = CompletableFuture.completedFuture(solvedIntegralMock);

        PowerMockito.mockStatic(ResultFormatter.class);
        PowerMockito.when(ResultFormatter.formatIntegralRequest(anyString(), anyInt(), anyInt())).thenReturn("formatted");

        PowerMockito.mockStatic(WebCalculatorValidation.class);
        PowerMockito.doNothing().when(WebCalculatorValidation.class, "validateIntegralRequest", integralRequest);

        PowerMockito.mockStatic(WebCalculatorFactoryResponse.class);
        PowerMockito.when(WebCalculatorFactoryResponse.createCalculationResponse(anyLong(), anyString(), anyLong(), eq(solvedIntegralMock), anyString())).thenReturn(calculationResponseMock);

        when(calculatorMock.approximateAreaUnderCurve(any())).thenReturn(future);
        assertEquals(sizeBefore, webCalculatorManager.countHistorySize());
        CalculationResponse solved = webCalculatorManager.processIntegralCalculation(integralRequest, TOKEN).get();
        Assert.assertEquals(solved, calculationResponseMock);
        assertEquals(sizeAfter, webCalculatorManager.countHistorySize());

        assertEquals(renderedTable, ((RenderedHistoryResponse) webCalculatorManager.provideRenderedHistoryResponse(TOKEN)).getRenderedTable());
        verify(rendererMock, times(1)).renderCalculationHistory(any(), eq(true));
        verify(calculatorMock, times(1)).approximateAreaUnderCurve(any());

        PowerMockito.verifyStatic(times(1));
        ResultFormatter.formatIntegralRequest(integralStrRep, 1, 1);
        PowerMockito.verifyStatic(times(1));
        WebCalculatorValidation.validateIntegralRequest(integralRequest);
        PowerMockito.verifyStatic(times(1));
        WebCalculatorFactoryResponse.createCalculationResponse(anyLong(), anyString(), anyLong(), eq(solvedIntegralMock), anyString());

    }

    @Test
    public void provideRenderedHistoryResultIntegral2Test() throws Exception {
        int sizeBefore = 0;
        int sizeAfter = 1;
        String expr = "integ{e^x}[0.00, 0.00]";
        String res = "0";
        String rowExpr = "<tr><td>1</td><td>" + expr + "</td><td>" + res + "</td></tr>";
        String renderedTable = "<table>" + tableHeader + "<tbody>" + rowExpr + "</tbody></table>";
        IntegralRequest integralRequest = new IntegralRequest(0, 1, 1, 1, 0, 0, true, Collections.emptyList());
        CalculationResponse calculationResponseMock = PowerMockito.mock(CalculationResponse.class);
        String integralStrRep = "integralStrRep";

        when(rendererMock.renderCalculationHistory(any(), eq(true))).thenReturn(renderedTable);

        DefiniteIntegralFunction solvedIntegralMock = PowerMockito.mock(DefiniteIntegralFunction.class);
        when(solvedIntegralMock.getApproximation()).thenReturn(1.1);
        when(solvedIntegralMock.getResult()).thenReturn(1.2);
        when(solvedIntegralMock.toString()).thenReturn(integralStrRep);

        CompletableFuture<DefiniteIntegralFunction> future = CompletableFuture.completedFuture(solvedIntegralMock);

        PowerMockito.mockStatic(ResultFormatter.class);
        PowerMockito.when(ResultFormatter.formatIntegralRequest(anyString(), anyInt(), anyInt())).thenReturn("formatted");

        PowerMockito.mockStatic(WebCalculatorValidation.class);
        PowerMockito.doNothing().when(WebCalculatorValidation.class, "validateIntegralRequest", integralRequest);

        PowerMockito.mockStatic(WebCalculatorFactoryResponse.class);
        PowerMockito.when(WebCalculatorFactoryResponse.createCalculationResponse(anyLong(), anyString(), anyLong(), eq(solvedIntegralMock), anyString())).thenReturn(calculationResponseMock);

        when(calculatorMock.approximateAreaUnderCurve(any())).thenReturn(future);

        assertEquals(sizeBefore, webCalculatorManager.countHistorySize());
        CalculationResponse solved = webCalculatorManager.processIntegralCalculation(integralRequest, TOKEN).get();
        Assert.assertEquals(solved, calculationResponseMock);
        assertEquals(sizeAfter, webCalculatorManager.countHistorySize());

        assertEquals(renderedTable, ((RenderedHistoryResponse) webCalculatorManager.provideRenderedHistoryResponse(TOKEN)).getRenderedTable());
        verify(rendererMock, times(1)).renderCalculationHistory(any(), eq(true));
        verify(calculatorMock, times(1)).approximateAreaUnderCurve(any());

        PowerMockito.verifyStatic(times(1));
        ResultFormatter.formatIntegralRequest(integralStrRep, 1, 1);
        PowerMockito.verifyStatic(times(1));
        WebCalculatorValidation.validateIntegralRequest(integralRequest);
        PowerMockito.verifyStatic(times(1));
        WebCalculatorFactoryResponse.createCalculationResponse(anyLong(), anyString(), anyLong(), eq(solvedIntegralMock), anyString());

    }

    @Test
    public void provideRenderedHistoryResultIntegral3Test() throws Exception {
        int sizeBefore = 0;
        int sizeAfter = 1;
        String expr = "integ{e^x}[-10.0, -9.99]";
        String res = "0.00456E-5";
        String rowExpr = "<tr><td>1</td><td>" + expr + "</td><td>" + res + "</td></tr>";
        String renderedTable = "<table>" + tableHeader + "<tbody>" + rowExpr + "</tbody></table>";
        IntegralRequest integralRequest = new IntegralRequest(0, 1, 1, 1, 0, 0, true, Collections.emptyList());
        String integralStrRep = "integralStrRep";
        CalculationResponse calculationResponseMock = PowerMockito.mock(CalculationResponse.class);

        when(rendererMock.renderCalculationHistory(any(), eq(true))).thenReturn(renderedTable);

        DefiniteIntegralFunction solvedIntegralMock = PowerMockito.mock(DefiniteIntegralFunction.class);
        when(solvedIntegralMock.toString()).thenReturn(integralStrRep);

        CompletableFuture<DefiniteIntegralFunction> future = CompletableFuture.completedFuture(solvedIntegralMock);

        PowerMockito.mockStatic(ResultFormatter.class);
        PowerMockito.when(ResultFormatter.formatIntegralRequest(anyString(), anyInt(), anyInt())).thenReturn("formatted");

        PowerMockito.mockStatic(WebCalculatorValidation.class);
        PowerMockito.doNothing().when(WebCalculatorValidation.class, "validateIntegralRequest", integralRequest);

        PowerMockito.mockStatic(WebCalculatorFactoryResponse.class);
        PowerMockito.when(WebCalculatorFactoryResponse.createCalculationResponse(anyLong(), anyString(), anyLong(), eq(solvedIntegralMock), anyString())).thenReturn(calculationResponseMock);

        when(calculatorMock.approximateAreaUnderCurve(any())).thenReturn(future);

        assertEquals(sizeBefore, webCalculatorManager.countHistorySize());
        CalculationResponse solved = webCalculatorManager.processIntegralCalculation(integralRequest, TOKEN).get();
        Assert.assertEquals(solved, calculationResponseMock);
        assertEquals(sizeAfter, webCalculatorManager.countHistorySize());

        assertEquals(renderedTable, ((RenderedHistoryResponse) webCalculatorManager.provideRenderedHistoryResponse(TOKEN)).getRenderedTable());
        verify(rendererMock, times(1)).renderCalculationHistory(any(), eq(true));
        verify(calculatorMock, times(1)).approximateAreaUnderCurve(any());

        PowerMockito.verifyStatic(times(1));
        ResultFormatter.formatIntegralRequest(integralStrRep, 1, 1);
        PowerMockito.verifyStatic(times(1));
        WebCalculatorValidation.validateIntegralRequest(integralRequest);
        PowerMockito.verifyStatic(times(1));
        WebCalculatorFactoryResponse.createCalculationResponse(anyLong(), anyString(), anyLong(), eq(solvedIntegralMock), anyString());


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
        int approximationMethodId = 0;
        int sizeBefore = 0;
        int sizeAfter = 1;

        DefiniteIntegralExponential expIntegralMock = PowerMockito.mock(DefiniteIntegralExponential.class);

        when(expIntegralMock.getUpperLimit()).thenReturn(upperBound);
        when(expIntegralMock.getLowerLimit()).thenReturn(lowerBound);
        when(expIntegralMock.toString()).thenReturn("");
        when(expIntegralMock.getResult()).thenReturn(result);
        when(expIntegralMock.getApproximation()).thenReturn(approximation);

        CompletableFuture<DefiniteIntegralFunction> futureSolvedIntegral = CompletableFuture.completedFuture(expIntegralMock);

        IntegralRequest integralRequestMock = PowerMockito.mock(IntegralRequest.class);
        when(integralRequestMock.getLowerLimit()).thenReturn(lowerBound);
        when(integralRequestMock.getUpperLimit()).thenReturn(upperBound);
        when(integralRequestMock.getRepeatedCalculations()).thenReturn(repeatedCalculations);
        when(integralRequestMock.getNumThreads()).thenReturn(numThreads);
        when(integralRequestMock.isAreaInscribed()).thenReturn(inscribed);
        when(integralRequestMock.getApproximationMethodId()).thenReturn(approximationMethodId);

        when(calculatorMock.approximateAreaUnderCurve(any())).thenReturn(futureSolvedIntegral);

        PowerMockito.mockStatic(WebCalculatorValidation.class, ResultFormatter.class);
        IntegralRequest reqMock = PowerMockito.mock(IntegralRequest.class);

        PowerMockito.doNothing().when(WebCalculatorValidation.class, "validateIntegralRequest", reqMock);
        PowerMockito.when(ResultFormatter.formatIntegralRequest(expIntegralMock.toString(), repeatedCalculations, numThreads))
                .thenReturn(expr);

        assertEquals(sizeBefore, webCalculatorManager.countHistorySize());
        CalculationResponse calculationResponse = webCalculatorManager.processIntegralCalculation(integralRequestMock, TOKEN).get();
        IntegralCalculationResponse integralCalculationResponse = (IntegralCalculationResponse) calculationResponse;

        verify(calculatorMock, Mockito.times(1)).approximateAreaUnderCurve(any());

        PowerMockito.verifyStatic(Mockito.times(1));
        WebCalculatorValidation.validateIntegralRequest(integralRequestMock);

        PowerMockito.verifyStatic(Mockito.times(1));
        ResultFormatter.formatIntegralRequest(expIntegralMock.toString(), repeatedCalculations, numThreads);

        verify(integralRequestMock, times(0)).getLowerLimit();
        verify(integralRequestMock, times(0)).getUpperLimit();
        verify(integralRequestMock, times(1)).getNumThreads();
        verify(integralRequestMock, times(1)).getRepeatedCalculations();
        verify(integralRequestMock, times(0)).getFunctionId();
        verify(integralRequestMock, times(1)).getApproximationMethodId();
        verify(integralRequestMock, times(0)).isAreaInscribed();

        verify(expIntegralMock, times(0)).getUpperLimit();
        verify(expIntegralMock, times(0)).getLowerLimit();
        verify(expIntegralMock, times(1)).getResult();
        verify(expIntegralMock, times(1)).getApproximation();

        assertEquals("1.0", integralCalculationResponse.getResult());
        assertEquals(result, integralCalculationResponse.getIntegralResult(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(DefiniteIntegralFunctionFactory.evaluateApproximationMethodType(approximationMethodId).getLabel(), integralCalculationResponse.getDescription());
        assertEquals(expr, integralCalculationResponse.getExpression());
        assertEquals(sizeAfter, webCalculatorManager.countHistorySize());
    }

    @Test
    public void resultIntegralCalculationResponse2Test() throws Exception {
        int functionId = 0;
        int approximationMethodId = 0;
        double lowerBound = 0;
        double upperBound = 1;
        String result = "1.0";
        int numThreads = 5;
        int repeatedCalculations = 6;
        String expr = "Integ{e^x}[0, 1] #Rep=6 #Th=5";
        String description = NumericalMethodAreaApproximationType.RIEMANN.getLabel();
        String exactIntegral = "1.0";
        boolean inscribed = true;
        evaluateResultFormatting(approximationMethodId,
                functionId, lowerBound, upperBound, result, numThreads, repeatedCalculations, expr, description, exactIntegral, inscribed);
    }

    @Test
    public void resultIntegralCalculationResponse3Test() throws Exception {
        int functionId = 0;
        double lowerBound = -10;
        double upperBound = -9.99;
        String result = "4.5399929762483885E-7";
        int numThreads = 5;
        int repeatedCalculations = 6;
        String exactIntegral = "4.5399929762483885E-7";
        String expr = "Integ{e^x}[-10, -9.99] #Rep=6 #Th=5";
        int approximationMethodId = 0;
        String description = NumericalMethodAreaApproximationType.RIEMANN.getLabel();
        boolean inscribed = true;
        evaluateResultFormatting(approximationMethodId, functionId, lowerBound, upperBound, result, numThreads, repeatedCalculations, expr, description, exactIntegral, inscribed);
    }

    @Test
    public void resultIntegralCalculationResponse4Test() throws Exception {
        int functionId = 0;
        int approximationMethodId = 0;
        double lowerBound = 0;
        double upperBound = 0;
        String result = "0.0";
        int numThreads = 5;
        int repeatedCalculations = 6;
        String exactIntegral = "0.0";
        String expr = "Integ{e^x}[0, 0] #Rep=6 #Th=5";
        String description = NumericalMethodAreaApproximationType.RIEMANN.getLabel();
        boolean inscribed = true;
        evaluateResultFormatting(approximationMethodId, functionId, lowerBound, upperBound, result, numThreads, repeatedCalculations, expr, description, exactIntegral, inscribed);
    }

    @Test
    public void resultIntegralCalculationResponse5Test() throws Exception {
        int sizeBeforeProcess = 0;
        int sizeAfterProcess = 1;
        int functionId = 0;
        double lowerBound = 0;
        double upperBound = 0;
        String result = "0.0";
        int numThreads = 5;
        int repeatedCalculations = 6;
        String expr = "Integ{e^x}[0, 0] #Rep=6 #Th=5";
        int approximationMethodId = 0;
        String description = NumericalMethodAreaApproximationType.RIEMANN.getLabel();
        CompletableFuture<DefiniteIntegralFunction> integralResult = CompletableFuture.completedFuture(new DefiniteIntegralExponential(lowerBound, upperBound, Optional.of(Double.valueOf(result)), Optional.empty()));
        IntegralRequest integralRequest = new IntegralRequestBuilder()
                .withFunctionId(functionId)
                .withApproximationMethodId(approximationMethodId)
                .withLowerBound(lowerBound)
                .withUpperBound(upperBound)
                .withNumThreads(numThreads)
                .withRepeatedCalculations(repeatedCalculations)
                .build();

        when(calculatorMock.approximateAreaUnderCurve(any())).thenReturn(integralResult);

        assertEquals(sizeBeforeProcess, webCalculatorManager.countHistorySize());

        CalculationResponse calculationResponse = webCalculatorManager.processIntegralCalculation(integralRequest, TOKEN).get();

        IntegralCalculationResponse integralCalculationResponse = (IntegralCalculationResponse) calculationResponse;
        verify(calculatorMock, Mockito.times(1)).approximateAreaUnderCurve(any());
        assertEquals("0", integralCalculationResponse.getResult());
        assertEquals(expr, integralCalculationResponse.getExpression());
        assertEquals(description, integralCalculationResponse.getDescription());
        assertEquals(NumericalMethodAreaApproximationType.RIEMANN, DefiniteIntegralFunctionFactory.evaluateApproximationMethodType(integralRequest.getApproximationMethodId()));
        assertEquals(sizeAfterProcess, webCalculatorManager.countHistorySize());
    }

    private void evaluateResultFormatting(int approximationMethodId, int functionId, double lowerBound, double upperBound, String result, int numThreads,
                                          int repeatedCalculations, String expr, String description, String exactIntegral, boolean inscribed) throws Exception {

        int sizeBeforeProcess = 0;
        int sizeAfterProcess = 1;

        DefiniteIntegralExponential expIntegralMock = PowerMockito.mock(DefiniteIntegralExponential.class);
        when(expIntegralMock.getUpperLimit()).thenReturn(upperBound);
        when(expIntegralMock.getLowerLimit()).thenReturn(lowerBound);
        when(expIntegralMock.toString()).thenReturn("");
        when(expIntegralMock.getResult()).thenReturn(Double.valueOf(exactIntegral));
        when(expIntegralMock.getApproximation()).thenReturn(Double.valueOf(result));

        IntegralRequest integralReqMock = PowerMockito.mock(IntegralRequest.class);
        when(integralReqMock.getFunctionId()).thenReturn(functionId);
        when(integralReqMock.getApproximationMethodId()).thenReturn(approximationMethodId);
        when(integralReqMock.getLowerLimit()).thenReturn(lowerBound);
        when(integralReqMock.getUpperLimit()).thenReturn(upperBound);
        when(integralReqMock.getNumThreads()).thenReturn(numThreads);
        when(integralReqMock.getRepeatedCalculations()).thenReturn(repeatedCalculations);
        when(integralReqMock.isAreaInscribed()).thenReturn(inscribed);

        CompletableFuture<DefiniteIntegralFunction> futureSolvedIntegral = CompletableFuture.completedFuture(expIntegralMock);

        PowerMockito.mockStatic(WebCalculatorValidation.class, ResultFormatter.class);
        PowerMockito.doNothing().when(WebCalculatorValidation.class, "validateIntegralRequest", integralReqMock);
        PowerMockito.when(ResultFormatter.formatIntegralRequest(expIntegralMock.toString(), repeatedCalculations, numThreads)).thenReturn(expr);

        when(calculatorMock.approximateAreaUnderCurve(eq(integralReqMock))).thenReturn(futureSolvedIntegral);

        assertEquals(sizeBeforeProcess, webCalculatorManager.countHistorySize());

        CalculationResponse calculationResponse = webCalculatorManager.processIntegralCalculation(integralReqMock, TOKEN).get();
        IntegralCalculationResponse integralCalculationResponse = (IntegralCalculationResponse) calculationResponse;
        PowerMockito.verifyStatic(Mockito.times(1));
        WebCalculatorValidation.validateIntegralRequest(integralReqMock);

        PowerMockito.verifyStatic(Mockito.times(1));
        ResultFormatter.formatIntegralRequest(expIntegralMock.toString(), repeatedCalculations, numThreads);

        verify(integralReqMock, Mockito.times(1)).getRepeatedCalculations();
        verify(integralReqMock, Mockito.times(1)).getNumThreads();
        verify(integralReqMock, Mockito.times(0)).getFunctionId();
        verify(integralReqMock, Mockito.times(1)).getApproximationMethodId();
        verify(integralReqMock, Mockito.times(0)).getLowerLimit();
        verify(integralReqMock, Mockito.times(0)).getUpperLimit();
        verify(integralReqMock, Mockito.times(0)).isAreaInscribed();
        verify(calculatorMock, Mockito.times(1)).approximateAreaUnderCurve(eq(integralReqMock));

        verify(expIntegralMock, times(0)).getUpperLimit();
        verify(expIntegralMock, times(0)).getLowerLimit();
        verify(expIntegralMock, times(1)).getResult();
        verify(expIntegralMock, times(1)).getApproximation();

        assertEquals(expr, integralCalculationResponse.getExpression());
        assertEquals(description, integralCalculationResponse.getDescription());
        assertEquals(result, integralCalculationResponse.getResult());
        assertEquals(exactIntegral, integralCalculationResponse.getIntegralResult() + "");
        assertEquals(sizeAfterProcess, webCalculatorManager.countHistorySize());
    }

    @Test(expected = NoSuchElementException.class)
    public void resultIntegralCalculationResponseFunctionException2Test() throws ExecutionException, InterruptedException {
        int functionId = 1;
        IntegralRequest integralReqMock = PowerMockito.mock(IntegralRequest.class);
        when(integralReqMock.getFunctionId()).thenReturn(functionId);
        webCalculatorManager.processIntegralCalculation(integralReqMock, TOKEN);
    }

    @Test(expected = NoSuchElementException.class)
    public void resultIntegralCalculationResponseFunctionException3Test() throws ExecutionException, InterruptedException {
        int functionId = 0;
        int approximationMethodId = 1;
        IntegralRequest integralReqMock = PowerMockito.mock(IntegralRequest.class);
        when(integralReqMock.getFunctionId()).thenReturn(functionId);
        when(integralReqMock.getApproximationMethodId()).thenReturn(approximationMethodId);
        webCalculatorManager.processIntegralCalculation(integralReqMock, TOKEN);
    }

    @Test(expected = InputMismatchException.class)
    public void resultIntegralCalculationResponseBoundsException2Test() throws ExecutionException, InterruptedException {
        int functionId = 0;
        double lowerBound = 1;
        double upperBound = 0;
        int approximationMethodId = 0;
        IntegralRequest integralReqMock = PowerMockito.mock(IntegralRequest.class);

        when(integralReqMock.getFunctionId()).thenReturn(functionId);
        when(integralReqMock.getApproximationMethodId()).thenReturn(approximationMethodId);
        when(integralReqMock.getLowerLimit()).thenReturn(lowerBound);
        when(integralReqMock.getUpperLimit()).thenReturn(upperBound);

        webCalculatorManager.processIntegralCalculation(integralReqMock, TOKEN);
    }

    @Test(expected = InputMismatchException.class)
    public void resultIntegralCalculationNumThreadsHighException2Test() throws ExecutionException, InterruptedException {
        int functionId = 0;
        int approximationMethod = 0;
        double lowerBound = 0;
        double upperBound = 1;
        int numThreads = 16;
        IntegralRequest integralReqMock = PowerMockito.mock(IntegralRequest.class);

        when(integralReqMock.getFunctionId()).thenReturn(functionId);
        when(integralReqMock.getApproximationMethodId()).thenReturn(approximationMethod);
        when(integralReqMock.getLowerLimit()).thenReturn(lowerBound);
        when(integralReqMock.getUpperLimit()).thenReturn(upperBound);
        when(integralReqMock.getNumThreads()).thenReturn(numThreads);

        webCalculatorManager.processIntegralCalculation(integralReqMock, TOKEN);
    }

    @Test(expected = InputMismatchException.class)
    public void resultIntegralCalculationNumThreadsLowException2Test() throws ExecutionException, InterruptedException {
        int functionId = 0;
        int approximationMethodId = 0;
        double lowerBound = 0;
        double upperBound = 1;
        int numThreads = 0;
        IntegralRequest integralReqMock = PowerMockito.mock(IntegralRequest.class);

        when(integralReqMock.getFunctionId()).thenReturn(functionId);
        when(integralReqMock.getApproximationMethodId()).thenReturn(approximationMethodId);
        when(integralReqMock.getLowerLimit()).thenReturn(lowerBound);
        when(integralReqMock.getUpperLimit()).thenReturn(upperBound);
        when(integralReqMock.getNumThreads()).thenReturn(numThreads);

        webCalculatorManager.processIntegralCalculation(integralReqMock, TOKEN);
    }

    @Test(expected = InputMismatchException.class)
    public void resultIntegralCalculationRepeatedCalculationsException2Test() throws ExecutionException, InterruptedException {
        int functionId = 0;
        int approximationMethodId = 0;
        double lowerBound = 0;
        double upperBound = 0;
        int repeatedCalculations = -1;
        int numThreads = 1;
        IntegralRequest integralReqMock = PowerMockito.mock(IntegralRequest.class);

        when(integralReqMock.getFunctionId()).thenReturn(functionId);
        when(integralReqMock.getApproximationMethodId()).thenReturn(approximationMethodId);
        when(integralReqMock.getLowerLimit()).thenReturn(lowerBound);
        when(integralReqMock.getUpperLimit()).thenReturn(upperBound);
        when(integralReqMock.getNumThreads()).thenReturn(numThreads);
        when(integralReqMock.getRepeatedCalculations()).thenReturn(repeatedCalculations);

        webCalculatorManager.processIntegralCalculation(integralReqMock, TOKEN);
    }

    @Test(expected = InputMismatchException.class)
    public void resultIntegralCalculationRepeatedCalculationsException3Test() throws ExecutionException, InterruptedException {
        int functionId = 0;
        double lowerBound = 0;
        double upperBound = 0;
        int repeatedCalculations = Integer.MAX_VALUE;
        int approximationMethodId = 0;
        int numThreads = 1;
        IntegralRequest integralReqMock = PowerMockito.mock(IntegralRequest.class);

        when(integralReqMock.getFunctionId()).thenReturn(functionId);
        when(integralReqMock.getApproximationMethodId()).thenReturn(approximationMethodId);
        when(integralReqMock.getLowerLimit()).thenReturn(lowerBound);
        when(integralReqMock.getUpperLimit()).thenReturn(upperBound);
        when(integralReqMock.getNumThreads()).thenReturn(numThreads);
        when(integralReqMock.getRepeatedCalculations()).thenReturn(repeatedCalculations);

        webCalculatorManager.processIntegralCalculation(integralReqMock, TOKEN);
    }

    @Test
    public void resultSimpleArithmeticResponse1Test() throws ExecutionException, InterruptedException {
        String expr = "6 - 10 * ( 2 - 5 )";
        String res = "36";
        int expectedSizeHistory = 1;
        when(calculatorMock.solveArithmeticExpression(eq(expr))).thenReturn(new BigDecimal(res));

        CalculationResponse calculationResponse = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr, TOKEN);

        verify(calculatorMock, times(1)).solveArithmeticExpression(eq(expr));

        assertEquals(res, calculationResponse.getResult());
        assertEquals(expr, calculationResponse.getExpression());
        assertEquals(expectedSizeHistory, webCalculatorManager.provideRawHistory(TOKEN).size());


    }

    @Test(expected = InputMismatchException.class)
    public void resultSimpleArithmeticFail1Test() {
        String expr = "6 - 10 * ( 2 - 5 ) &&";
        String res = "36";
        int expectedSizeHistory = 1;
        when(calculatorMock.solveArithmeticExpression(eq(expr))).thenReturn(new BigDecimal(res));

        CalculationResponse calculationResponse = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr, TOKEN);

        verify(calculatorMock, times(1)).solveArithmeticExpression(eq(expr));

        assertEquals(res, calculationResponse.getResult());
        assertEquals(expr, calculationResponse.getExpression());
        assertEquals(expectedSizeHistory, webCalculatorManager.provideRawHistory(TOKEN).size());
    }

    @Test
    public void cleanEntriesTest() throws ExecutionException, InterruptedException {
        String expr = "1 + 0";
        String res = "1";
        int expectedSizeHistoryAfterCleanUp = 0;
        int expectedSizeHistoryBeforeCleanUp = 3;
        int expectedCleanedEntries = 3;

        when(calculatorMock.solveArithmeticExpression(any())).thenReturn(new BigDecimal(res));

        CalculationResponse calculationResponse1 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr, TOKEN);
        CalculationResponse calculationResponse2 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr, TOKEN2);
        CalculationResponse calculationResponse3 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr, TOKEN2 + TOKEN);

        verify(calculatorMock, Mockito.times(3)).solveArithmeticExpression(expr);
        assertEquals(res, calculationResponse1.getResult());
        assertEquals(res, calculationResponse2.getResult());
        assertEquals(res, calculationResponse3.getResult());


        assertEquals(expectedSizeHistoryBeforeCleanUp, webCalculatorManager.countHistorySize());
        Integer cleanedEntries = webCalculatorManager.cleanExpiredEntries(LocalDateTime.now().minusMinutes(10), 0, ChronoUnit.SECONDS).get();
        assertEquals(expectedCleanedEntries, cleanedEntries.intValue());
        assertEquals(expectedSizeHistoryAfterCleanUp, webCalculatorManager.countHistorySize());

    }

    @Test
    public void cleanEntriesNoneTest() throws ExecutionException, InterruptedException {
        String expr = "2 + 1";
        String res = "3";
        int expectedSizeHistoryBeforeCleanUp = 2;
        int expectedCleanedEntries = 0;

        when(calculatorMock.solveArithmeticExpression(any())).thenReturn(new BigDecimal(res));

        CalculationResponse calculationResponse1 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr, TOKEN);
        CalculationResponse calculationResponse2 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr, TOKEN2);
        CalculationResponse calculationResponse3 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr, TOKEN2);

        assertEquals(expectedSizeHistoryBeforeCleanUp, webCalculatorManager.countHistorySize());

        Integer cleanedEntries = webCalculatorManager.cleanExpiredEntries(LocalDateTime.now().minusMinutes(15), 20, ChronoUnit.MINUTES).get();

        assertEquals(expectedCleanedEntries, cleanedEntries.intValue());
        assertEquals(expectedSizeHistoryBeforeCleanUp - cleanedEntries, webCalculatorManager.countHistorySize());

        assertEquals(res, calculationResponse1.getResult());
        assertEquals(res, calculationResponse2.getResult());
        assertEquals(res, calculationResponse3.getResult());

        verify(calculatorMock, Mockito.times(3)).solveArithmeticExpression(expr);
    }

    @Test
    public void cleanSomeEntriesTest() throws Exception {
        String expr1 = "2 + 1";
        String expr2 = "1 + 1";
        String expr3 = "4 + 1";
        String res1 = "3";
        String res2 = "2";
        String res3 = "5";
        int expectedSizeHistoryBeforeCleanUp = 2;
        int expectedCleanedEntries = 1;
        long secs = 1000000000L;
        LocalDateTime cleaningTime = LocalDateTime.of(2016, Month.DECEMBER, 10, 9, 0);
        int expirationPolicy = 20;
        LocalDateTime historyLastUsed1 = cleaningTime.minusMinutes(expirationPolicy);
        LocalDateTime historyLastUsed2 = cleaningTime.minusMinutes(expirationPolicy + 1);
        int sizeToken = 1;
        int sizeToken2 = 2;

        when(calculatorMock.solveArithmeticExpression(eq(expr1))).thenReturn(new BigDecimal(res1));
        when(calculatorMock.solveArithmeticExpression(eq(expr2))).thenReturn(new BigDecimal(res2));
        when(calculatorMock.solveArithmeticExpression(eq(expr3))).thenReturn(new BigDecimal(res3));

        CalculationResponse calcResponseMock1 = PowerMockito.mock(CalculationResponse.class);
        CalculationResponse calcResponseMock2 = PowerMockito.mock(CalculationResponse.class);
        CalculationResponse calcResponseMock3 = PowerMockito.mock(CalculationResponse.class);
        when(calcResponseMock1.getResult()).thenReturn(res1);
        when(calcResponseMock2.getResult()).thenReturn(res2);
        when(calcResponseMock3.getResult()).thenReturn(res3);
        when(calcResponseMock1.getResponseTime()).thenReturn(secs);
        when(calcResponseMock2.getResponseTime()).thenReturn(secs);
        when(calcResponseMock3.getResponseTime()).thenReturn(secs);

        CalculationHistoryRecord historyRecordMock1 = PowerMockito.mock(CalculationHistoryRecord.class);
        CalculationHistoryRecord historyRecordMock2 = PowerMockito.mock(CalculationHistoryRecord.class);
        PowerMockito.when(historyRecordMock1.getLastUsed()).thenReturn(historyLastUsed1);
        PowerMockito.when(historyRecordMock1.getCalculationHistory()).thenReturn(Collections.singletonList(calcResponseMock1));
        PowerMockito.when(historyRecordMock2.getLastUsed()).thenReturn(historyLastUsed2);
        PowerMockito.when(historyRecordMock2.getCalculationHistory()).thenReturn(Arrays.asList(calcResponseMock2, calcResponseMock3));

        PowerMockito.mockStatic(WebCalculatorFactoryResponse.class);
        PowerMockito.when(WebCalculatorFactoryResponse.createCalculationResponse(0L, expr1, res1, secs, "Arithmetic")).thenReturn(calcResponseMock1);
        PowerMockito.when(WebCalculatorFactoryResponse.createCalculationResponse(1L, expr2, res2, secs, "Arithmetic")).thenReturn(calcResponseMock2);
        PowerMockito.when(WebCalculatorFactoryResponse.createCalculationResponse(2L, expr3, res3, secs, "Arithmetic")).thenReturn(calcResponseMock3);

        Stopwatch stopwatchMock = PowerMockito.mock(Stopwatch.class);
        PowerMockito.mockStatic(Stopwatch.class);
        PowerMockito.when(Stopwatch.createStarted()).thenReturn(stopwatchMock);
        PowerMockito.when(stopwatchMock.elapsed(eq(TimeUnit.NANOSECONDS))).thenReturn(secs);

        PowerMockito.whenNew(CalculationHistoryRecord.class).withArguments(eq(calcResponseMock1)).thenReturn(historyRecordMock1);
        PowerMockito.whenNew(CalculationHistoryRecord.class).withArguments(eq(calcResponseMock2)).thenReturn(historyRecordMock2);

        CalculationResponse calculationResponse1 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr1, TOKEN);
        CalculationResponse calculationResponse2 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr2, TOKEN2);
        CalculationResponse calculationResponse3 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr3, TOKEN2);

        assertEquals(expectedSizeHistoryBeforeCleanUp, webCalculatorManager.countHistorySize());
        assertEquals(sizeToken, webCalculatorManager.provideRawHistory(TOKEN).size());
        assertEquals(sizeToken2, webCalculatorManager.provideRawHistory(TOKEN2).size());

        Integer cleanedEntries = webCalculatorManager.cleanExpiredEntries(cleaningTime, expirationPolicy, ChronoUnit.MINUTES).get();

        assertEquals(expectedCleanedEntries, cleanedEntries.intValue());
        assertEquals(expectedSizeHistoryBeforeCleanUp - cleanedEntries, webCalculatorManager.countHistorySize());
        assertEquals(sizeToken, webCalculatorManager.provideRawHistory(TOKEN).size());

        boolean exceptionHistoryNotFound = false;
        try {
            webCalculatorManager.provideRawHistory(TOKEN2);
        } catch (NoSuchElementException nse) {
            exceptionHistoryNotFound = true;
        }

        assertTrue(exceptionHistoryNotFound);

        verify(historyRecordMock1, Mockito.times(1)).getLastUsed();
        verify(historyRecordMock2, Mockito.times(1)).getLastUsed();

        assertEquals(res1, calculationResponse1.getResult());
        assertEquals(res2, calculationResponse2.getResult());
        assertEquals(res3, calculationResponse3.getResult());
        assertEquals(secs, calculationResponse1.getResponseTime(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(secs, calculationResponse2.getResponseTime(), WebCalculatorConstants.ACCURACY_EPSILON);
        assertEquals(secs, calculationResponse3.getResponseTime(), WebCalculatorConstants.ACCURACY_EPSILON);

        verify(calculatorMock, Mockito.times(1)).solveArithmeticExpression(expr1);
        verify(calculatorMock, Mockito.times(1)).solveArithmeticExpression(expr2);
        verify(calculatorMock, Mockito.times(1)).solveArithmeticExpression(expr3);

        PowerMockito.verifyNew(CalculationHistoryRecord.class, Mockito.times(1)).withArguments(eq(calcResponseMock1));
        PowerMockito.verifyNew(CalculationHistoryRecord.class, Mockito.times(1)).withArguments(eq(calcResponseMock2));

        PowerMockito.verifyStatic(Mockito.times(1));
        WebCalculatorFactoryResponse.createCalculationResponse(0L, expr1, res1, secs, "Arithmetic");
        PowerMockito.verifyStatic(Mockito.times(1));
        WebCalculatorFactoryResponse.createCalculationResponse(1L, expr2, res2, secs, "Arithmetic");
        PowerMockito.verifyStatic(Mockito.times(1));
        WebCalculatorFactoryResponse.createCalculationResponse(2L, expr3, res3, secs, "Arithmetic");

        PowerMockito.verifyStatic(Mockito.times(3));
        Stopwatch.createStarted();

        verify(stopwatchMock, Mockito.times(3)).elapsed(any());
        verify(calcResponseMock1, Mockito.times(1)).getResult();
        verify(calcResponseMock2, Mockito.times(1)).getResult();
        verify(calcResponseMock3, Mockito.times(1)).getResult();
        verify(calcResponseMock1, Mockito.times(1)).getResponseTime();
        verify(calcResponseMock2, Mockito.times(1)).getResponseTime();
        verify(calcResponseMock3, Mockito.times(1)).getResponseTime();
    }

    @Test
    public void testAppendHistoryRetrievalTest() {
        int expectedSize = 2;
        long secs = 1L;
        int sizeToken = 2;
        int sizeToken2 = 2;

        when(calculatorMock.solveArithmeticExpression(eq(expr1))).thenReturn(new BigDecimal(res1));
        when(calculatorMock.solveArithmeticExpression(eq(expr2))).thenReturn(new BigDecimal(res2));

        Stopwatch stopwatchMock = PowerMockito.mock(Stopwatch.class);
        PowerMockito.mockStatic(Stopwatch.class);
        PowerMockito.when(Stopwatch.createStarted()).thenReturn(stopwatchMock);
        PowerMockito.when(stopwatchMock.elapsed(any())).thenReturn(secs);

        CalculationResponse calculationResponse0 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr1, TOKEN);
        CalculationResponse calculationResponse1 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr1, TOKEN);
        CalculationResponse calculationResponse2 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr2, TOKEN2);
        CalculationResponse calculationResponse3 = (CalculationResponse) webCalculatorManager.processArithmeticCalculation(expr2, TOKEN2);

        assertEquals(res1, calculationResponse0.getResult());
        assertEquals(res1, calculationResponse1.getResult());
        assertEquals(res2, calculationResponse2.getResult());
        assertEquals(res2, calculationResponse3.getResult());
        assertEquals(0L, calculationResponse0.getId());
        assertEquals(1L, calculationResponse1.getId());
        assertEquals(2L, calculationResponse2.getId());
        assertEquals(3L, calculationResponse3.getId());
        assertEquals(secs, calculationResponse0.getResponseTime());
        assertEquals(secs, calculationResponse1.getResponseTime());
        assertEquals(secs, calculationResponse2.getResponseTime());
        assertEquals(secs, calculationResponse3.getResponseTime());

        assertEquals(expectedSize, webCalculatorManager.countHistorySize());
        assertEquals(sizeToken, webCalculatorManager.provideRawHistory(TOKEN).size());
        assertEquals(sizeToken2, webCalculatorManager.provideRawHistory(TOKEN2).size());

        verify(calculatorMock, Mockito.times(2)).solveArithmeticExpression(eq(expr1));
        verify(calculatorMock, Mockito.times(2)).solveArithmeticExpression(eq(expr2));

        verifyStatic(Mockito.times(4));
        Stopwatch.createStarted();

        verify(stopwatchMock, Mockito.times(4)).elapsed(any());
    }

}
