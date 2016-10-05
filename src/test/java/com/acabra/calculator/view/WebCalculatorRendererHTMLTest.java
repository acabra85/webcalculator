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


    private final String TABLE_OPEN = "<table class=\"table table-striped\">" +
            "<caption style=\"text-align: center\"><h4>History</h4></caption><thead>" +
            "<tr>" +
            "<th><b>Id.</b></th>" +
            "<th><b>Expression</b></th>" +
            "<th><b>Result</b></th>" +
            "<th><b>Response Time</b></th>" +
            "</tr></thead><tbody>";

    private final String TABLE_CLOSE = "</tbody></table>";
    private final String row1 = "<tr><td>1</td><td>aa</td><td>1.1</td><td>1</td></tr>";
    private final String row2 = "<tr><td>2</td><td>bb</td><td>0</td><td>1</td></tr>";
    private String approximationLabel = "Approximated";
    private String realValLabel = "Real Value";
    private String subRow3 = "<table class=\"integral-subtable\"><tr><th>" + approximationLabel +"</th><td style=\"text-align: right;\">0.456</td></tr><tr><th>" + realValLabel + "</th><td style=\"text-align: right;\">0.01</td></tr></table>";
    private final String row3 = "<tr><td>3</td><td>Integ</td><td><div style=\"width:auto; height: 40px;\">" + subRow3 + "</div></td><td>1</td></tr>";
    private String subRow4 = "<table class=\"integral-subtable\"><tr><th>" + approximationLabel + "</th><td style=\"text-align: right;\">0.04560E-6</td></tr><tr><th>" + realValLabel + "</th><td style=\"text-align: right;\">0.01</td></tr></table>";
    private final String row4 = "<tr><td>4</td><td>Integ</td><td><div style=\"width:auto; height: 40px;\">" + subRow4 + "</div></td><td>1</td></tr>";

    private final List<CalculationResponse> listToRender = Arrays.asList(
            new CalculationResponse(1, "aa", 1.1, 1L,"dd"),
            new CalculationResponse(2, "bb", 0.0, 1L, "ee"),
            new CalculationResponse(3, WebCalculatorConstants.INTEGRAL_PREFIX, 0.456, 1L, "0.01"),
            new CalculationResponse(4, WebCalculatorConstants.INTEGRAL_PREFIX, 0.0000000456, 1L, "0.01")
    );

    @Before
    public void prepare() {

    }

    @Test
    public void renderTableHTMLAscendingTest() {
        WebCalculatorRendererHTML webCalculatorRendererHTML = new WebCalculatorRendererHTML();
        String resultHTML = TABLE_OPEN + row1 + row2 + row3 + row4 + TABLE_CLOSE;
        assertEquals(resultHTML, webCalculatorRendererHTML.renderCalculationHistory(listToRender, false));
    }

    @Test
    public void renderTableHTMLDescendingTest() {
        WebCalculatorRendererHTML webCalculatorRendererHTML = new WebCalculatorRendererHTML();
        String resultHTML = TABLE_OPEN + row4 + row3 + row2 + row1 + TABLE_CLOSE;
        String tableHTML  = webCalculatorRendererHTML.renderCalculationHistory(listToRender, true);
        assertEquals(resultHTML, tableHTML);
    }
}
