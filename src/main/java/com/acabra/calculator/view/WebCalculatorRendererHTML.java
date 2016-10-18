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


    private static final String APPROX_METHOD_LABEL = "Method";
    private static final String APPROX_LABEL = "Approximated";
    private static final String REAL_VALUE_LABEL = "Real Value";
    private static final String ACCURACY_LABEL = "Accuracy";

    private static final String ACCURATE_CLASS = "accurate-approximation";
    private static final String NOT_ACCURATE_CLASS = "not-accurate-approximation";

    private static final String TABLE_CSS = "<style>"
            + "." + NOT_ACCURATE_CLASS + " {color: red;}\n"
            + "." + ACCURATE_CLASS + " {color: green;}\n"
            + "</style>";

    protected static final String TABLE_HEADER = TABLE_CSS +"<table class=\"table table-striped\">" +
            "<caption style=\"text-align: center\"><h4>History</h4></caption><thead>" +
            "<tr>" +
            "<th><b>Id.</b></th>" +
            "<th><b>Expression</b></th>" +
            "<th><b>Result</b></th>" +
            "<th><b>Response Time</b></th>" +
            "</tr></thead><tbody>";

    protected static final String INTEGRAL_RESULT_DETAIL_TABLE = "<div class=\"integral-detail-div\"><table class=\"integral-subtable\">"
            + "<tr><th>" + APPROX_METHOD_LABEL + "</th><td class=\"integral-subtable-result\">%s</td></tr>"
            + "<tr><th>" + APPROX_LABEL + "</th><td class=\"integral-subtable-result\">%s</td></tr>"
            + "<tr><th>" + REAL_VALUE_LABEL + "</th><td class=\"integral-subtable-result\">%s</td></tr>"
            + "<tr><th>" + ACCURACY_LABEL + "</th><td class=\"integral-subtable-result %s\">%s</td></tr>"
            + "</table></div>";

    public WebCalculatorRendererHTML() {}

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
        if (calculationResponse instanceof IntegralCalculationResponse) {
            return createIntegralComparativeTable((IntegralCalculationResponse) calculationResponse);
        }
        return ResultFormatter.trimIntegerResults(calculationResponse.getResult());
    }

    private String createIntegralComparativeTable(IntegralCalculationResponse integralCalculationResponse) {
        String accuracyFormatted = ResultFormatter.formatPercentage(integralCalculationResponse.getAccuracy());
        String integralFormatted = ResultFormatter.formatResult(integralCalculationResponse.getIntegralResult());
        String approxFormatted = ResultFormatter.formatResult(Double.valueOf(integralCalculationResponse.getResult()));
        return String.format(INTEGRAL_RESULT_DETAIL_TABLE, integralCalculationResponse.getDescription(),
                approxFormatted, integralFormatted, colorAccuracy(integralCalculationResponse.getAccuracy()),
                accuracyFormatted);
    }

    protected static String colorAccuracy(double accuracy) {
        if (accuracy < 90) {
            return NOT_ACCURATE_CLASS;
        } else if (accuracy > 98.5) {
            return ACCURATE_CLASS;
        }
        return "";
    }

}
