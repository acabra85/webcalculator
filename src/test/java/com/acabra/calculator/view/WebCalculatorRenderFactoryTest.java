package com.acabra.calculator.view;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertTrue;

/**
 * Created by Agustin on 10/2/2016.
 */
public class WebCalculatorRenderFactoryTest {

    @Test
    public void createRendererTest() {
        WebCalculatorRenderer renderer = WebCalculatorRenderFactory.createRenderer(RenderType.HTML);
        assertTrue(renderer instanceof WebCalculatorRendererHTML);
    }

    @Test(expected = NoSuchElementException.class)
    public void createRendererFailTest() {
        WebCalculatorRenderFactory.createRenderer(RenderType.XML);
    }
}
