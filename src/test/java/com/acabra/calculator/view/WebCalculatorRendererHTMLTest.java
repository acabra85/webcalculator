package com.acabra.calculator.view;

import com.acabra.calculator.response.CalculationResponse;
import com.acabra.calculator.response.TableHistoryResponse;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 9/30/2016.
 */
public class WebCalculatorRendererHTMLTest {

    @Test
    public void renderTableHTMLAscendingTest() {
        WebCalculatorRendererHTML webCalculatorRendererHTML = new WebCalculatorRendererHTML();
        List<CalculationResponse> list = Arrays.asList(
                new CalculationResponse(1, "aa", 1.1, "dd"),
                new CalculationResponse(2, "bb", 0.0, "ee")
        );
        String resultHTML = "<table class=\"table table-striped\"><caption style=\"text-align: center\"><h4>History</h4></caption><thead><tr></tr><th><b>Id.</b></th><th><b>Expression</b></th><th><b>Result</b></th><tr></tr></thead>" +
                "<tbody><tr><td>1</td><td>aa</td><td>1.1</td></tr>" +
                "<tr><td>2</td><td>bb</td><td>0</td></tr></tbody></table>";
        TableHistoryResponse tableHistoryResponse = webCalculatorRendererHTML.renderCalculationHistory(list, false);
        String tableHTML = tableHistoryResponse.getTableHTML();
        assertEquals(resultHTML, tableHTML);
    }

    @Test
    public void renderTableHTMLDescendingTest() {
        WebCalculatorRendererHTML webCalculatorRendererHTML = new WebCalculatorRendererHTML();
        List<CalculationResponse> list = Arrays.asList(
                new CalculationResponse(1, "aa", 1.1, "dd"),
                new CalculationResponse(2, "bb", 0.0, "ee")
        );
        String resultHTML = "<table class=\"table table-striped\"><caption style=\"text-align: center\"><h4>History</h4></caption><thead><tr></tr><th><b>Id.</b></th><th><b>Expression</b></th><th><b>Result</b></th><tr></tr></thead>" +
                "<tbody>" +
                "<tr><td>2</td><td>bb</td><td>0</td></tr>" +
                "<tr><td>1</td><td>aa</td><td>1.1</td></tr>" +
                "</tbody></table>";
        TableHistoryResponse tableHistoryResponse = webCalculatorRendererHTML.renderCalculationHistory(list, true);
        String tableHTML = tableHistoryResponse.getTableHTML();
        assertEquals(resultHTML, tableHTML);
    }
}
