package com.acabra.calculator.view;

import java.util.NoSuchElementException;

/**
 * Created by Agustin on 9/28/2016.
 */
public class WebCalculatorRenderFactory {

    public static WebCalculatorRenderer createRenderer(RenderType renderType) {
        switch (renderType) {
            case HTML:
                return new WebCalculatorRendererHTML();
            default:
                throw new NoSuchElementException("not yet implemented render type");
        }
    }
}
