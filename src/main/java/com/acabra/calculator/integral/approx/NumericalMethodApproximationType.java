package com.acabra.calculator.integral.approx;

import java.util.NoSuchElementException;

/**
 * Created by Agustin on 10/8/2016.
 */
public enum NumericalMethodApproximationType {
    RIEMANN(0, "Riemann"), SIMPSON(1, "Simpson"), GAUSS(2, "Gauss"), ROMBERG(3, "Romberg");

    final int methodId;
    final String label;

    NumericalMethodApproximationType(int p, String label) {
        this.methodId = p;
        this.label = label;
    }

    public static NumericalMethodApproximationType provideType(int methodId) {
        switch (methodId) {
            case 0:
                return RIEMANN;
            case 1:
                return SIMPSON;
            case 2:
                return GAUSS;
            default:
                throw new NoSuchElementException("unable to find result method methodId");
        }
    }

    public String getLabel() {
        return label;
    }
}
