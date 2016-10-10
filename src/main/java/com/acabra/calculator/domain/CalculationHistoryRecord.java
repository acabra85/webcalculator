package com.acabra.calculator.domain;

import com.acabra.calculator.response.CalculationResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agustin on 10/6/2016.
 */
public class CalculationHistoryRecord {

    private final List<CalculationResponse> calculationHistory;
    private volatile LocalDateTime lastUsed;

    public CalculationHistoryRecord(CalculationResponse calculationResponse) {
        this.calculationHistory = new ArrayList<>();
        this.calculationHistory.add(calculationResponse);
        this.lastUsed = LocalDateTime.now();
    }

    public List<CalculationResponse> getCalculationHistory() {
        refresh();
        return calculationHistory;
    }

    private synchronized void refresh() {
        this.lastUsed = LocalDateTime.now();
    }

    public LocalDateTime getLastUsed() {
        return lastUsed;
    }

    public void append(CalculationResponse calculationResponse) {
        refresh();
        this.calculationHistory.add(calculationResponse);
    }
}
