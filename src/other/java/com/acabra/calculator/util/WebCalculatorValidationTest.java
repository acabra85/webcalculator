package com.acabra.calculator.util;



import java.util.InputMismatchException;

/**
 * Created by Agustin on 10/2/2016.
 */
public class WebCalculatorValidationTest {

    @Test
    public void validateArithmeticExpressionValid1Test() {
        String expression = "6 + ( [ { 6 } ] )";
        WebCalculatorValidation.validateArithmeticExpression(expression);
    }

    @Test
    public void validateArithmeticExpressionValid2Test() {
        String expression = "-6 + ( [ { 6.0 } ] ) + 2.0";
        WebCalculatorValidation.validateArithmeticExpression(expression);
    }

    @Test
    public void validateArithmeticExpressionValid3Test() {
        String expression = "-6 + ( [ { -6.565 } ] ) + 2.0";
        WebCalculatorValidation.validateArithmeticExpression(expression);
    }

    @Test(expected = InputMismatchException.class)
    public void validateArithmeticExpressionWrongGrouping1Test() {
        String expression = "6 + ) [ { 6 } ] )";
        WebCalculatorValidation.validateArithmeticExpression(expression);
    }

    @Test(expected = InputMismatchException.class)
    public void validateArithmeticExpressionWrongGrouping2Test() {
        String expression = "[6 + ( [ { 6 } ] ) }";
        WebCalculatorValidation.validateArithmeticExpression(expression);
    }

    @Test(expected = InputMismatchException.class)
    public void validateArithmeticExpressionWrongGrouping3Test() {
        String expression = "{ 6 + ( [ { 6 } ] ) ]";
        WebCalculatorValidation.validateArithmeticExpression(expression);
    }
    @Test(expected = InputMismatchException.class)
    public void validateArithmeticExpressionWrongGrouping4Test() {
        String expression = "6 + ( [ { 6 } ] ) [";
        WebCalculatorValidation.validateArithmeticExpression(expression);
    }

    @Test(expected = InputMismatchException.class)
    public void validateArithmeticExpressionWrongContents1Test() {
        String expression = "{ 6 + ( [ { 6 . } ] ) ]";
        WebCalculatorValidation.validateArithmeticExpression(expression);
    }

    @Test(expected = InputMismatchException.class)
    public void validateArithmeticExpressionWrongContents2Test() {
        String expression = "{ 6 + ( [ { 6dd } ] ) }";
        WebCalculatorValidation.validateArithmeticExpression(expression);
    }

    @Test(expected = InputMismatchException.class)
    public void validateArithmeticExpressionWrongContents3Test() {
        String expression = "{ 6 + ( [ { 6 {} ] ) ]";
        WebCalculatorValidation.validateArithmeticExpression(expression);
    }

    @Test(expected = InputMismatchException.class)
    public void validateArithmeticExpressionWrongContents4Test() {
        String expression = " 5 + ) 3 - 2 (";
        WebCalculatorValidation.validateArithmeticExpression(expression);
    }

    @Test(expected = InputMismatchException.class)
    public void validateArithmeticExpressionWrongContents5Test() {
        String expression = " 5 + [ 3 - 2 )";
        WebCalculatorValidation.validateArithmeticExpression(expression);
    }

    @Test(expected = InputMismatchException.class)
    public void validateArithmeticExpressionWrongContents6Test() {
        String expression = " 5 + [ 3 - 2 ";
        WebCalculatorValidation.validateArithmeticExpression(expression);
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_wrong_parenthesis_closing_7() {
        String expression = "6 + ( [ { 6 ] } )";
        WebCalculatorValidation.validateArithmeticExpression(expression);
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_wrong_parenthesis_closing_8() {
        String expression = "{ [ ( ] ) }";
        WebCalculatorValidation.validateArithmeticExpression(expression);
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_wrong_parenthesis_closing_9() {
        String expression = " ) { [ ( ] ) }";
        WebCalculatorValidation.validateArithmeticExpression(expression);
    }
}
