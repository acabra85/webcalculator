package com.acabra.calculator;

import com.acabra.calculator.response.CalculationResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by Agustin on 10/6/2016.
 */
public class CalculationHistoryRecord {

    private final ArrayList<CalculationResponse> calculationHistory;
    private volatile LocalDateTime lastUsed;

    public CalculationHistoryRecord() {
        this.calculationHistory = new ArrayList<>();
        this.lastUsed = LocalDateTime.now();
    }

    public ArrayList getCalculationHistory() {
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
