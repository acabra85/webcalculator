package com.acabra.calculator.util;

/**
 * Created by Agustin on 9/30/2016.
 */
public class ResultFormatter {

    static double epsilon = 0.000001d;

    /**
     * Provides formatting of double values to present the information for rendering purposes.
     * @param result a double value to be formatted
     * @return a string representing the formated result.
     */
    public static String formatResult(double result) {
        if (result == 0) {
            return "0";
        }
        else if (Math.abs(result) < epsilon) {
            return String.format("%.5fE-6", result * 1000000);
        }
        return (Double.parseDouble(String.format("%.6f", result))) + "";
    }

    /**
     * Removes the trailing zeros for integer values.
     * @param result a string containing a result
     * @return the result value without trailing zeros.
     */
    public static String trimIntegerResults(String result) {
        return result.endsWith(".0") ? result.substring(0, result.length() - 2) : result;
    }


    /**
     * Prints using the default format defined for an integrable function and the parameters chosen
     * to make the calculation
     * @param label the label of the function requested
     * @param lowerBound the lower limit
     * @param upperBound the upper limit
     * @param repeatedCalculations how many times to split the main range to approximate results
     * @param numberThreads the amount of threads to use
     * @return a string representation for rendering purposes
     */
    public static String formatIntegralRequest(String label, String lowerBound, String upperBound, long repeatedCalculations, int numberThreads) {
        return String.format(WebCalculatorConstants.INTEGRAL_REQ_FORMAT,
                label,
                ResultFormatter.trimIntegerResults(lowerBound),
                ResultFormatter.trimIntegerResults(upperBound),
                repeatedCalculations,
                numberThreads);
    }

    public static String formatPercentage(double accuracy) {
        if (accuracy < WebCalculatorConstants.ACCURACY_EPSILON) {
            return "0.00%";
        }
        return Double.valueOf(String.format("%.4f", accuracy)).toString() + "%";
    }

    public static String formatNanoSeconds(long nanoSecs) {
        return Double.valueOf(String.format("%.3f", nanoSecs / 1000000000.0)) + "s";
    }
}
