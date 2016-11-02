package com.acabra.calculator.integral.definiteintegral;

import java.util.NoSuchElementException;

/**
 * Created by Agustin on 9/30/2016.
 */
public enum IntegrableFunctionType {
    EXPONENTIAL(0, "e^x"), POLYNOMIAL(1, "x^2"), LOGARITHMIC(2, "ln(x)"), INVERSE(3, "1/x"), COS(4, "cos(x)");

    final int type;
    final String label;

    IntegrableFunctionType(int p, String label) {
        this.type = p;
        this.label = label;
    }

    public static IntegrableFunctionType provideType(int functionId) {
        switch (functionId) {
            case 0:
                return EXPONENTIAL;
            case 1:
                return POLYNOMIAL;
            case 2:
                return LOGARITHMIC;
            case 3:
                return INVERSE;
            case 4:
                return COS;
            default:
                throw new NoSuchElementException("unable to find function type");
        }
    }

    public String getLabel() {
        return label;
    }
}
