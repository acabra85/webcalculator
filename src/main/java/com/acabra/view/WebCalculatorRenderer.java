package com.acabra.view;

import com.acabra.domain.response.CalculationResponse;
import com.acabra.domain.response.SimpleResponse;

import java.util.List;

/**
 * Created by Agustin on 9/28/2016.
 */
public interface WebCalculatorRenderer {

    SimpleResponse renderCalculationHistory(List<CalculationResponse> calculationResponseList, boolean descendingOrder);
}
