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
            return String.format("%.5f x10^5", result*10000);
        }
        return String.format("%.5f", result);
    }
}
