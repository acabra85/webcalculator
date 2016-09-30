package com.acabra.calculator;

import com.acabra.calculator.response.CalculationResponse;
import com.acabra.calculator.response.SimpleResponse;
import com.acabra.calculator.response.TableHistoryResponse;
import com.acabra.calculator.view.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
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
@PrepareForTest({CalculatorManager.class, Calculator.class, WebCalculatorRendererHTML.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class CalculatorManagerTest {

    private CalculatorManager calculatorManager;
    private final static String TOKEN = "default-token";
    private final static String TOKEN2 = "default-token2";
    private final static String expr1 = "3 + 3";
    private final static String res1 = "6";
    private final static String expr2 = "4 + 4";
    private final static String res2 = "8";
    private final static String expr3 = "sqrt ( 4 )";
    private final static String res3 = "8";
    private final static String tableHeader = "<caption>History</caption><thead><tr><th>Id.</th><th>Expression</th><th>Result</th></tr></thead>";
    private final static String renderedTableEmpty = "<table>" + tableHeader + "</table>";
    private final static String rowExpr1 = "<tr><td>1</td><td>3 + 3</td><td>6</td></tr>";
    private final static String rowExpr2 = "<tr><td>2</td><td>4 + 4</td><td>8</td></tr>";
    private final static String rowExpr3 = "<tr><td>1</td><td>sqrt ( 4 )</td><td>2</td></tr>";
    private final static String renderedTable1 = "<table>" + tableHeader + "<tbody>" + rowExpr1 + "</tbody></table>";
    private final static String renderedTable2 = "<table>" + tableHeader + "<tbody>" + rowExpr1 + rowExpr2 + "</tbody></table>";
    private final static String renderedTable3 = "<table>" + tableHeader + "<tbody>" + rowExpr3 + "</tbody></table>";
    SimpleResponse defaultTableResponseEmpty = new TableHistoryResponse(0L, renderedTableEmpty);
    SimpleResponse defaultTableResponse1 = new TableHistoryResponse(1L, renderedTable1);
    SimpleResponse defaultTableResponse2 = new TableHistoryResponse(2L, renderedTable2);
    SimpleResponse defaultTableResponse3 = new TableHistoryResponse(3L, renderedTable3);

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

        when(calculatorMock.makeCalculation(expr1)).thenReturn(res1);
        when(calculatorMock.makeCalculation(expr2)).thenReturn(res2);

        CalculationResponse calculationResponse = (CalculationResponse) calculatorManager.processCalculation(expr1, TOKEN);

        assertEquals(calculationResponse.getId(), 1);
        assertEquals(calculationResponse.getExpression(), expr1);
        assertEquals(calculationResponse.getResult(), res1);

        CalculationResponse calculationResponse2 = (CalculationResponse) calculatorManager.processCalculation(expr2, TOKEN);

        assertEquals(calculationResponse2.getId(), 2);
        assertEquals(calculationResponse2.getExpression(), expr2);
        assertEquals(calculationResponse2.getResult(), res2);

        verify(calculatorMock, times(1)).makeCalculation(expr1);
        verify(calculatorMock, times(1)).makeCalculation(expr2);
    }

    @Test
    public void provideCalculationHistoryTest() {

        when(calculatorMock.makeCalculation(expr1)).thenReturn(res1);

        calculatorManager.processCalculation(expr1, TOKEN);
        calculatorManager.processCalculation(expr1, TOKEN);


        List<CalculationResponse> calculationHistory = calculatorManager.provideCalculationHistory(TOKEN2);

        assertEquals(calculationHistory.size(), 0);

        List<CalculationResponse> resultList = calculatorManager.provideCalculationHistory(TOKEN);

        assertEquals(resultList.size(), 2); //valid size according to number of operations on that Token
        assertEquals(resultList.get(0).getId(), 1);
        assertEquals(resultList.get(1).getId(), 2);

        verify(calculatorMock, times(2)).makeCalculation(expr1);
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
        testSingleRowOperation(expr1, res1, defaultTableResponse1, renderedTable1);
    }

    @Test
    public void provideRenderedHistoryResult2Test() {
        when(calculatorMock.makeCalculation(expr1)).thenReturn(res1);
        when(calculatorMock.makeCalculation(expr2)).thenReturn(res2);

        calculatorManager.processCalculation(expr1, TOKEN);
        calculatorManager.processCalculation(expr2, TOKEN);

        List<CalculationResponse> historyList = calculatorManager.provideCalculationHistory(TOKEN);
        when(rendererMock.renderCalculationHistory(eq(historyList), eq(true))).thenReturn(defaultTableResponse2);

        assertEquals(renderedTable2, ((TableHistoryResponse) calculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML());
        verify(rendererMock, times(1)).renderCalculationHistory(eq(historyList), eq(true));
    }

    @Test
    public void provideRenderedHistoryResult3Test() {
        testSingleRowOperation(expr3, res3, defaultTableResponse3, renderedTable3);
    }

    private void testSingleRowOperation(String expr, String res, SimpleResponse defaultTableResponse, String renderedTable) {
        when(calculatorMock.makeCalculation(expr)).thenReturn(res);
        calculatorManager.processCalculation(expr, TOKEN);
        List<CalculationResponse> historyList = calculatorManager.provideCalculationHistory(TOKEN);
        when(rendererMock.renderCalculationHistory(eq(historyList), eq(true))).thenReturn(defaultTableResponse);
        assertEquals(renderedTable, ((TableHistoryResponse) calculatorManager.provideRenderedHistoryResult(TOKEN)).getTableHTML());
        verify(rendererMock, times(1)).renderCalculationHistory(eq(historyList), eq(true));
    }

}
