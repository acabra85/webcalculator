package com.acabra.calculator.util;

import com.acabra.calculator.Operator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Agustin Cabra on 2/16/2017.
 */
public class ExplicitMultiplicationParser {

    private static final Logger logger = Logger.getLogger(ExplicitMultiplicationParser.class);

    /**
     * Replaces implicit multiplication defined in the input string and places
     * explicit multiplication symbols.
     *
     * e.g.
     *     6 ( 7 )  =>  6 * ( 7 )
     *     6 ( 3 { 8 } 7 )  =>  6 * ( 3 * { 8 } * 7 )
     *
     * @param input the arithmetic expression
     * @return a list of strings defining the explicit multiplication symbols
     */
    public static List<String> makeMultiplicationExplicit(String input) {
        String[] split = StringUtils.split(input, " ");
        int size = split.length;
        List<String> result = new ArrayList<>(size);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size - 1; i++) {
            result.add(split[i]);
            sb.append(split[i]).append(' ');
            applyChangesIfRequired(split[i], split[i + 1], result, sb);
        }
        result.add(split[size - 1]);
        sb.append(split[size - 1]);
        logger.debug("with explicit multiplication: " + sb.toString());
        return result;
    }

    private static void applyChangesIfRequired(String current, String next, List<String> result, StringBuilder sb) {
        if (NumberUtils.isNumber(current) && Operator.OPEN_GROUP_SYMBOLS_SET.contains(next)
                || NumberUtils.isNumber(next) && Operator.CLOSE_GROUP_SYMBOLS_SET.contains(current)
                ) {
            result.add(Operator.MULTIPLY.getLabel());
            sb.append(Operator.MULTIPLY.getLabel()).append(' ');
        }
    }
}
