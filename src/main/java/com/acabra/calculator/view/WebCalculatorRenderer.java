package com.acabra.calculator.view;

import com.acabra.calculator.response.CalculationResponse;
import com.acabra.calculator.response.SimpleResponse;

import java.util.List;

/**
 * Created by Agustin on 9/28/2016.
 */
public interface WebCalculatorRenderer {

    SimpleResponse renderCalculationHistory(List<CalculationResponse> calculationResponseList, boolean descendingOrder);
}
