package com.acabra.calculator;

import com.acabra.calculator.util.ShuntingYard;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Agustin on 9/30/2016.
 */
public class ShuntingYardTest {

    @Test
    public void solveInFixExpression1Test() {
        String expression = "1 + 2 + 3 - 4 + 8";
        List<String> expected = Arrays.asList("1 2 + 3 4 - + 8 +".split("\\s+"));
        assertEquals(expected, ShuntingYard.postfix(expression));
    }

    @Test
    public void solveInFixExpression2Test() {
        String expression = "2 @ ( 9 ) * 5";
        List<String> expected = Arrays.asList("2 9 @ 5 *".split("\\s+"));
        assertEquals(expected, ShuntingYard.postfix(expression));
    }

    @Test
    public void solveInFixExpression3Test() {
        String expression = "2  + @ ( 9 )";
        List<String> expected = Arrays.asList("2 9 @ +".split("\\s+"));
        assertEquals(expected, ShuntingYard.postfix(expression));
    }

    @Test
    public void solveInFixExpression4Test() {
        String expression = "1 + 2 * ( 3 - 4 ) + 8 / ( 1 + 1 )";
        List<String> expected = Arrays.asList("1 2 3 4 - * + 8 1 1 + / +".split("\\s+"));
        assertEquals(expected, ShuntingYard.postfix(expression));
    }

    @Test
    public void solveInFixExpression5Test() {
        String expression = "2  + @ ( 9 + 1 )";
        List<String> expected = Arrays.asList("2 9 1 + @ +".split("\\s+"));
        assertEquals(expected, ShuntingYard.postfix(expression));
    }

    @Test
    public void should_solve_using_shunting_yard_1() {
        String expression = "6 - ( 3 { 6 } )";
        List<String> expected = Arrays.asList("6 3 6 * -".split("\\s+"));
        assertEquals(expected, ShuntingYard.postfix(expression));
    }

    @Test
    public void should_solve_using_shunting_yard_2() {
        String expression = "6 - ( 3 { 4 [ 6 ( { 8 } ) ] } )";
        List<String> expected = Arrays.asList("6 3 4 6 8 * * * -".split("\\s+"));
        assertEquals(expected, ShuntingYard.postfix(expression));
    }

    @Test
    public void should_solve_using_shunting_yard_3() {
        String expression = "6 - ( { [ ( { 8 } ) 6 ] 4 } 3 )";
        List<String> expected = Arrays.asList("6 8 6 * 4 * 3 * -".split("\\s+"));
        assertEquals(expected, ShuntingYard.postfix(expression));
    }

    @Test
    public void should_solve_using_shunting_yard_4() {
        String expression = "6 - ( 3 * { 6 } )";
        List<String> expected = Arrays.asList("6 3 6 * -".split("\\s+"));
        assertEquals(expected, ShuntingYard.postfix(expression));
    }

    @Test
    public void should_solve_using_shunting_yard_5() {
        String expression = "6 - ( 3 { * 6 } )";
        List<String> expected = Arrays.asList("6 3 6 * * -".split("\\s+"));
        assertEquals(expected, ShuntingYard.postfix(expression));
    }

    @Test
    public void should_solve_using_shunting_yard_6() {
        String expression = "-26 -  (  - 6 )";
        List<String> expected = Arrays.asList("-26 6 - -".split("\\s+"));
        assertEquals(expected, ShuntingYard.postfix(expression));
    }
}
