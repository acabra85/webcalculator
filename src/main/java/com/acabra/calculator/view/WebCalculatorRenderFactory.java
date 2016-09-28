package com.acabra.calculator.view;

/**
 * Created by Agustin on 9/28/2016.
 */
public class WebCalculatorRenderFactory {

    public static WebCalculatorRenderer createRenderer(RenderType renderType) {
        switch (renderType) {
            case HTML:
                return new WebCalculatorRendererHTML();
            default:
                throw new RuntimeException("invalid render type");
        }
    }
}
