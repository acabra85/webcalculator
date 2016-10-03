package com.acabra.calculator.util;

/**
 * Created by Agustin on 9/30/2016.
 */
public class ResultFormatter {

    static double epsilon = 0.000001d;

    public static String formatResult(double result) {
        if (result == 0) {
            return "0";
        }
        else if (Math.abs(result) < epsilon) {
            return String.format("%.5fE-6", result * 1000000);
        }
        return (Double.parseDouble(String.format("%.6f", result))) + "";
    }

    public static String trimIntegerResults(String result) {
        return result.endsWith(".0") ? result.substring(0, result.length() - 2) : result;
    }

    public static String formatIntegralRequest(String label, String lowerBound, String upperBound, int repeatedCalculations, int numberThreads) {
        return String.format(WebCalculatorConstants.INTEGRAL_REQ_FORMAT,
                label,
                ResultFormatter.trimIntegerResults(lowerBound),
                ResultFormatter.trimIntegerResults(upperBound),
                repeatedCalculations,
                numberThreads);
    }
}
