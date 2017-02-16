package com.acabra.calculator.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Agustin Cabra on 2/16/2017.
 */
public class ExplicitMultiplicationParserTest {

    @Test
    public void should_explicitly_define_multiplication_symbol_1() {
        String input = "6 - ( 3 { 6 } )";
        String expected = "6 - ( 3 * { 6 } )";
        Assert.assertArrayEquals(StringUtils.split(expected, " "), ExplicitMultiplicationParser.makeMultiplicationExplicit(input).toArray());
    }

    @Test
    public void should_explicitly_define_multiplication_symbol_2() {
        String input = "6 - ( { 6 } 3 )";
        String expected = "6 - ( { 6 } * 3 )";
        Assert.assertArrayEquals(StringUtils.split(expected, " "), ExplicitMultiplicationParser.makeMultiplicationExplicit(input).toArray());
    }

    @Test
    public void should_explicitly_define_three_multiplication_symbols_1() {
        String input = "6 - ( { [ ( { 8 } ) 6 ] 4 } 3 )";
        String expected = "6 - ( { [ ( { 8 } ) * 6 ] * 4 } * 3 )";
        Assert.assertArrayEquals(StringUtils.split(expected, " "), ExplicitMultiplicationParser.makeMultiplicationExplicit(input).toArray());
    }

    @Test
    public void should_explicitly_define_three_multiplication_symbols_2() {
        String input = "6 - ( 3 { 4 [ 6 ( { 8 } ) ] } )";
        String expected = "6 - ( 3 * { 4 * [ 6 * ( { 8 } ) ] } )";
        Assert.assertArrayEquals(StringUtils.split(expected, " "), ExplicitMultiplicationParser.makeMultiplicationExplicit(input).toArray());
    }
}
