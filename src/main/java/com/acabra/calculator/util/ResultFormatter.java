package com.acabra.calculator.util;

/**
 * Created by Agustin on 9/30/2016.
 */
public class ResultFormatter {

    static double epsilon = 0.00001d;

    public static String formatResult(double result) {
        if (result == 0) {
            return result + "";
        }
        else if (Math.abs(result) < epsilon) {
            return String.format("%.5fE-5", result * 10000);
        }
        return String.format("%.5f", result);
    }

    public static String trimIntegerResults(String result) {
        return result.endsWith(".0") ? result.substring(0, result.length() - 2) : result;
    }
}
