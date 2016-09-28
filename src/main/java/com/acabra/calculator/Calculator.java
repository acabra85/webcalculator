package com.acabra.calculator;

import org.apache.log4j.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Agustin on 9/27/2016.
 */
public class Calculator {

    private static final String ENGINE_NAME = "JavaScript";
    private final ScriptEngine engine;
    private final AtomicLong counter;
    private static final Logger logger = Logger.getLogger(Calculator.class);

    public Calculator() {
        ScriptEngineManager manager = new ScriptEngineManager();
        this.engine = manager.getEngineByName(ENGINE_NAME);
        counter = new AtomicLong();
    }

    public String makeCalculation(String expression) {
        try {
            logger.info("parsing expression " + expression );
            logger.info("shunting yard applied " + ShuntingYard.postfix(expression) );
            double result = Double.parseDouble(engine.eval(getFilteredExpression(expression) + ";") + "");
            counter.incrementAndGet();
            return trimTrailingZeros(result + "");
        } catch (ScriptException e) {
            logger.error(e);
            return "NaN";
        }
    }

    private String trimTrailingZeros(String result) {
        return result.endsWith(".0") ? result.substring(0, result.length() - 2) : result;
    }

    private String getFilteredExpression(String expression) {
        return expression.replace("sqrt", "Math.sqrt");
    }

    public long calculationsPerformed() {
        return counter.get();
    }
}
