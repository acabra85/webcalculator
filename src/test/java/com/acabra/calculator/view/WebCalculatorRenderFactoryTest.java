package com.acabra.calculator.view;

import org.junit.Test;

import java.util.NoSuchElementException;

/**
 * Created by Agustin on 10/2/2016.
 */
public class WebCalculatorRenderFactoryTest {

    @Test
    public void createRendererTest() {
        WebCalculatorRenderFactory.createRenderer(RenderType.HTML);
    }

    @Test(expected = NoSuchElementException.class)
    public void createRendererFailTest() {
        WebCalculatorRenderFactory.createRenderer(RenderType.XML);
    }
}
