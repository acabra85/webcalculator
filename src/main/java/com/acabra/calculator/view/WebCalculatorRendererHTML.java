package com.acabra.calculator.view;

import com.acabra.calculator.response.CalculationResponse;
import com.acabra.calculator.response.IntegralCalculationResponse;
import com.acabra.calculator.util.ResultFormatter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Agustin on 9/28/2016.
 */
public class WebCalculatorRendererHTML implements WebCalculatorRenderer {

    public WebCalculatorRendererHTML() {

    }

    private final String TABLE_HEADER = "<table class=\"table table-striped\">" +
            "<caption style=\"text-align: center\"><h4>History</h4></caption><thead>" +
            "<tr>" +
            "<th><b>Id.</b></th>" +
            "<th><b>Expression</b></th>" +
            "<th><b>Result</b></th>" +
            "<th><b>Response Time</b></th>" +
            "</tr></thead><tbody>";

    private final String APPROX_LABEL = "Approximated";
    private final String REAL_VALUE_LABEL = "Real Value";
    private final String ACCURACY_LABEL = "Accuracy";

    @Override
    public String renderCalculationHistory(List<CalculationResponse> history, boolean descending) {
        return descending ? renderHistoryTable(reverseResults(history)) : renderHistoryTable(history);
    }

    private String renderHistoryTable(List<CalculationResponse> orderList) {
        StringBuilder sb = new StringBuilder(TABLE_HEADER);
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
        sb.append("<tr>")
            .append("<td>").append(calculationResponse.getId()).append("</td>")
            .append("<td>").append(calculationResponse.getExpression()).append("</td>")
            .append("<td>").append(provideFormatting(calculationResponse)).append("</td>")
            .append("<td>").append(ResultFormatter.formatNanoSeconds(calculationResponse.getResponseTime())).append("</td>")
        .append("</tr>");
    }

    private String provideFormatting(CalculationResponse calculationResponse) {
        String result = ResultFormatter.trimIntegerResults(Double.toString(calculationResponse.getResult()));
        if (calculationResponse instanceof IntegralCalculationResponse) {
            return createIntegralComparativeTable((IntegralCalculationResponse) calculationResponse);
        }
        return result;
    }

    private String createIntegralComparativeTable(IntegralCalculationResponse integralCalculationResponse) {
        String accuracyFormatted = ResultFormatter.formatPercentage(integralCalculationResponse.getAccuracy());
        String integralFormatted = ResultFormatter.formatResult(integralCalculationResponse.getIntegralResult());
        String approxFormatted = ResultFormatter.formatResult(integralCalculationResponse.getResult());
        return "<div class=\"integral-detail-div\"><table class=\"integral-subtable\">"
                + "<tr><th>" + APPROX_LABEL + "</th><td style=\"text-align: right;\">"
                    + approxFormatted + "</td></tr>"
                + "<tr><th>" + REAL_VALUE_LABEL + "</th><td style=\"text-align: right;\">"
                    + integralFormatted + "</td></tr>"
                + "<tr><th>" + ACCURACY_LABEL + "</th><td style=\"text-align: right;\">"
                    + accuracyFormatted + "</td></tr>"
                + "</table></div>";
    }

}
