package com.acabra.calculator.view;

import com.acabra.calculator.response.CalculationResponse;
import com.acabra.calculator.util.WebCalculatorConstants;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 9/30/2016.
 */
public class WebCalculatorRendererHTMLTest {

    private final String row1 = "<tr><td>1</td><td>aa</td><td>1.1</td></tr>";
    private final String row2 = "<tr><td>2</td><td>bb</td><td>0</td></tr>";
    private final String row3 = "<tr><td>3</td><td>Integ</td><td>0.456</td></tr>";
    private final String row4 = "<tr><td>4</td><td>Integ</td><td>0.04560E-6</td></tr>";

    private final List<CalculationResponse> listToRender = Arrays.asList(
            new CalculationResponse(1, "aa", 1.1, "dd"),
            new CalculationResponse(2, "bb", 0.0, "ee"),
            new CalculationResponse(3, WebCalculatorConstants.INTEGRAL_PREFIX, 0.456, ""),
            new CalculationResponse(4, WebCalculatorConstants.INTEGRAL_PREFIX, 0.0000000456, "")
    );

    @Before
    public void prepare() {

    }

    @Test
    public void renderTableHTMLAscendingTest() {
        WebCalculatorRendererHTML webCalculatorRendererHTML = new WebCalculatorRendererHTML();
        String resultHTML = "<table class=\"table table-striped\"><caption style=\"text-align: center\"><h4>History</h4></caption><thead><tr></tr><th><b>Id.</b></th><th><b>Expression</b></th><th><b>Result</b></th><tr></tr></thead>" +
                "<tbody>" + row1 + row2 + row3 + row4 +
                "</tbody></table>";
        String tableHTML  = webCalculatorRendererHTML.renderCalculationHistory(listToRender, false);
        assertEquals(resultHTML, tableHTML);
    }

    @Test
    public void renderTableHTMLDescendingTest() {
        WebCalculatorRendererHTML webCalculatorRendererHTML = new WebCalculatorRendererHTML();
        String resultHTML = "<table class=\"table table-striped\"><caption style=\"text-align: center\"><h4>History</h4></caption><thead><tr></tr><th><b>Id.</b></th><th><b>Expression</b></th><th><b>Result</b></th><tr></tr></thead>" +
                "<tbody>" + row4 + row3 + row2 + row1 +
                "</tbody></table>";
        String tableHTML  = webCalculatorRendererHTML.renderCalculationHistory(listToRender, true);
        assertEquals(resultHTML, tableHTML);
    }
}
