package com.acabra.calculator.view;

import com.acabra.calculator.response.CalculationResponse;
import com.acabra.calculator.util.ResultFormatter;
import com.acabra.calculator.util.WebCalculatorConstants;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Agustin on 9/28/2016.
 */
public class WebCalculatorRendererHTML implements WebCalculatorRenderer {

    public WebCalculatorRendererHTML() {

    }

    @Override
    public String renderCalculationHistory(List<CalculationResponse> history, boolean descending) {
        return descending ? renderHistoryTable(reverseResults(history)) : renderHistoryTable(history);
    }

    private String renderHistoryTable(List<CalculationResponse> orderList) {
        StringBuilder sb = new StringBuilder("<table class=\"table table-striped\"><caption style=\"text-align: center\"><h4>History</h4></caption>");
        sb.append("<thead><tr></tr><th><b>Id.</b></th><th><b>Expression</b></th><th><b>Result</b></th><tr></tr></thead><tbody>");
        for (int i = 0; i < orderList.size(); i++) {
            extractRow(orderList.get(i), sb);
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    private List<CalculationResponse> reverseResults(List<CalculationResponse> calculationResponses) {
        int size = calculationResponses.size();
        return IntStream.range(0, size)
                .mapToObj(i -> calculationResponses.get(size - i - 1))
                .collect(Collectors.toList());
    }

    private void extractRow(CalculationResponse calculationResponse, StringBuilder sb) {
        sb.append("<tr><td>").append(calculationResponse.getId()).append("</td>");
        sb.append("<td>").append(calculationResponse.getExpression()).append("</td>");
        sb.append("<td>").append(provideFormatting(calculationResponse)).append("</td>");
        sb.append("</tr>");
    }

    private String provideFormatting(CalculationResponse calculationResponse) {
        String result = ResultFormatter.trimIntegerResults(Double.toString(calculationResponse.getResult()));
        if (calculationResponse.getExpression().startsWith(WebCalculatorConstants.INTEGRAL_PREFIX)) {
            return createIntegralComparativeTable(ResultFormatter.formatResult(calculationResponse.getResult()), calculationResponse.getDescription());
        }
        return result;
    }

    private String createIntegralComparativeTable(String approximation, String actualValue) {
        String approxLabel = "Approximated";
        String realValLabel = "Real Value";
        return "<div style=\"width:auto; height: 40px;\"><table class=\"integral-subtable\">"
                + "<tr><th>" + approxLabel + "</th><td style=\"text-align: right;\">" + approximation +"</td></tr>"
                + "<tr><th>" + realValLabel + "</th><td style=\"text-align: right;\">" + ResultFormatter.formatResult(Double.parseDouble(actualValue)) +"</td></tr>"
        +"</table></div>";

    }

}
