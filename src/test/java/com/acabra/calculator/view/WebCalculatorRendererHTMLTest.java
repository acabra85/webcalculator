package com.acabra.calculator.view;

import com.acabra.calculator.response.CalculationResponse;
import com.acabra.calculator.response.IntegralCalculationResponse;
import com.acabra.calculator.util.ResultFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyString;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Agustin on 9/30/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ResultFormatter.class, WebCalculatorRendererHTML.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class WebCalculatorRendererHTMLTest {


    private static final String TABLE_CLOSE = "</tbody></table>";

    private double approx3 = 0.444;
    private String approxStr3 = "0.444";
    private double integralResult3 = 0.5555;
    private String integralResultStr3 = "0.5555";
    private String percentageStr3 = "88.88%";
    private double percentage3 = 100.0 -  20.07200720072007;

    private double approx4 = 0.0000000456;
    private String approxStr4 = "4.56E-8";
    private double integralResult4 = 0.88888;
    private String integralResultStr4 = "0.88888";
    private double percentage4 = 100 - 99.9999948699487;
    private String percentageStr4 = "99.99%";

    private static final String row1 = "<tr><td>1</td><td>aa</td><td>1.1</td><td>0.001s</td></tr>";
    private static final String row2 = "<tr><td>2</td><td>bb</td><td>0</td><td>0.001s</td></tr>";
    private String METHOD_RIEMANN_LABEL = "Riemann";
    private String subRow3 = String.format(WebCalculatorRendererHTML.INTEGRAL_RESULT_DETAIL_TABLE,
            METHOD_RIEMANN_LABEL, approx3, integralResult3,
            WebCalculatorRendererHTML.colorAccuracy(percentage3), percentageStr3);
    private String subRow4 = String.format(WebCalculatorRendererHTML.INTEGRAL_RESULT_DETAIL_TABLE,
            METHOD_RIEMANN_LABEL, approx4, integralResult4,
            WebCalculatorRendererHTML.colorAccuracy(percentage4), percentageStr4);
    private final String row3 = "<tr><td>3</td><td>Integ</td><td>" + subRow3 + "</td><td>0.001s</td></tr>";
    private final String row4 = "<tr><td>4</td><td>Integ</td><td>" + subRow4 + "</td><td>0.001s</td></tr>";

    private final List<CalculationResponse> listToRender = Arrays.asList(
            new CalculationResponse(1, true, "aa", "1.1", 1L,"arithmetic1"),
            new CalculationResponse(2, true, "bb", "0.0", 1L, "arithmetic2"),
            new IntegralCalculationResponse(3, false, "Integ", approx3, integralResult3, 1L, "Riemann"),
            new IntegralCalculationResponse(4, false, "Integ", approx4, integralResult4, 1L, "Riemann")
    );

    @Test
    public void renderTableHTMLAscendingTest() {
        WebCalculatorRendererHTML webCalculatorRendererHTML = new WebCalculatorRendererHTML();
        String resultHTML = WebCalculatorRendererHTML.TABLE_HEADER + row1 + row2 + row3 + row4 + TABLE_CLOSE;
        long secs = 1L;

        PowerMockito.mockStatic(ResultFormatter.class);
        when(ResultFormatter.formatPercentage(percentage3)).thenReturn(percentageStr3);
        when(ResultFormatter.formatPercentage(percentage4)).thenReturn(percentageStr4);
        when(ResultFormatter.formatResult(integralResult3)).thenReturn(integralResultStr3);
        when(ResultFormatter.formatResult(integralResult4)).thenReturn(integralResultStr4);
        when(ResultFormatter.formatResult(approx4)).thenReturn(approxStr4);
        when(ResultFormatter.formatResult(approx3)).thenReturn(approxStr3);
        when(ResultFormatter.formatNanoSeconds(secs)).thenReturn("0.001s");
        when(ResultFormatter.trimIntegerResults("1.1")).thenReturn("1.1");
        when(ResultFormatter.trimIntegerResults("0.0")).thenReturn("0");

        assertEquals(resultHTML, webCalculatorRendererHTML.renderCalculationHistory(listToRender, false));

        PowerMockito.verifyStatic(times(2));
        ResultFormatter.formatPercentage(anyDouble());

        PowerMockito.verifyStatic(times(4));
        ResultFormatter.formatResult(anyDouble());

        PowerMockito.verifyStatic(times(4));
        ResultFormatter.formatNanoSeconds(secs);

        PowerMockito.verifyStatic(times(2));
        ResultFormatter.trimIntegerResults(anyString());
    }

    @Test
    public void renderTableHTMLDescendingTest() {
        WebCalculatorRendererHTML webCalculatorRendererHTML = new WebCalculatorRendererHTML();
        String resultHTML = WebCalculatorRendererHTML.TABLE_HEADER + row4 + row3 + row2 + row1 + TABLE_CLOSE;
        long secs = 1L;

        PowerMockito.mockStatic(ResultFormatter.class);
        when(ResultFormatter.formatPercentage(percentage3)).thenReturn(percentageStr3);
        when(ResultFormatter.formatPercentage(percentage4)).thenReturn(percentageStr4);
        when(ResultFormatter.formatResult(integralResult3)).thenReturn(integralResultStr3);
        when(ResultFormatter.formatResult(integralResult4)).thenReturn(integralResultStr4);
        when(ResultFormatter.formatResult(approx4)).thenReturn(approxStr4);
        when(ResultFormatter.formatResult(approx3)).thenReturn(approxStr3);
        when(ResultFormatter.formatNanoSeconds(secs)).thenReturn("0.001s");
        when(ResultFormatter.trimIntegerResults("1.1")).thenReturn("1.1");
        when(ResultFormatter.trimIntegerResults("0.0")).thenReturn("0");

        assertEquals(resultHTML, webCalculatorRendererHTML.renderCalculationHistory(listToRender, true));

        PowerMockito.verifyStatic(times(2));
        ResultFormatter.formatPercentage(anyDouble());

        PowerMockito.verifyStatic(times(4));
        ResultFormatter.formatResult(anyDouble());

        PowerMockito.verifyStatic(times(4));
        ResultFormatter.formatNanoSeconds(secs);

        PowerMockito.verifyStatic(times(2));
        ResultFormatter.trimIntegerResults(anyString());
    }
}
