package com.acabra.view;

import com.acabra.domain.response.CalculationResponse;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Agustin on 9/28/2016.
 */
public class WebCalculatorRendererHTML implements WebCalculatorRenderer {

    private  AtomicLong counter;

    public WebCalculatorRendererHTML() {
        this.counter = new AtomicLong();
    }

    @Override
    public TableHistoryResponse renderCalculationHistory(List<CalculationResponse> history, boolean descending) {
        String tableHTML = descending ? renderHistoryTable(reverseResults(history)) : renderHistoryTable(history);
        return new TableHistoryResponse(counter.incrementAndGet(), tableHTML);
    }

    private String renderHistoryTable(List<CalculationResponse> orderList) {
        StringBuilder sb = new StringBuilder("<table class=\"table table-striped\"><caption style=\"text-align: center\"><h4>History</h4></caption>");
        sb.append("<thead><tr></tr><th><b>Id.</b></th><th><b>Expression</b></th><th><b>Result</b></th><tr></tr></thead><tbody>");
        for (int i = 0; i < orderList.size(); i++) {
            extractRow(orderList, sb, i);
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    private void extractRow(List<CalculationResponse> reverseHistory, StringBuilder sb, int i) {
        sb.append("<tr>");
        sb.append("<td>" + reverseHistory.get(i).getId() + "</td>");
        sb.append("<td>" + reverseHistory.get(i).getExpression() + "</td>");
        sb.append("<td>" + reverseHistory.get(i).getResult() + "</td>");
        sb.append("</tr>");
    }

    private List<CalculationResponse> reverseResults(List<CalculationResponse> calculationResponses) {
        int size = calculationResponses.size();
        return IntStream.range(0, size)
                .mapToObj(i -> calculationResponses.get(size - i - 1))
                .collect(Collectors.toList());
    }

}
