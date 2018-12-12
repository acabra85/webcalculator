package com.acabra.calculator.integral.definiteintegral;

import com.acabra.calculator.function.PolynomialFunction;
import com.acabra.calculator.util.ResultFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Agustin on 9/30/2016.
 */
public final class DefiniteIntegralPolynomial extends DefiniteIntegralFunction {

    public static final FunctionDomain DOMAIN = FunctionDomainFactory.REAL_NUMBERS;
    private final int order;
    private final List<Double> coefficients;
    private String stringRepresentation;

    public DefiniteIntegralPolynomial(double lowerBound, double upperBound, List<Double> coefficients, Optional<Double> integrationResult, Optional<Double> approximation) {
        super(lowerBound, upperBound, integrationResult.orElse(null), IntegrableFunctionType.POLYNOMIAL.getLabel(),
                new PolynomialFunction(Collections.unmodifiableList(coefficients)));
        this.approximation = approximation.orElse(null);
        this.coefficients = Collections.unmodifiableList(coefficients);
        this.order = this.coefficients.size();
    }

    private String provideStringRepresentation() {
        if(coefficients.isEmpty()) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        boolean appended = false;
        for (int exp = 0; exp < coefficients.size(); exp++) {
            Double coeff = coefficients.get(exp);
            if(coeff != 0) {
                double abs = Math.abs(coeff);
                if (coeff < 0) {
                    sb.append("-");
                    appended = true;
                } else {
                    sb.append(appended?"+":"");
                    appended = true;
                }
                if(exp==0) {
                    sb.append(ResultFormatter.trimIntegerResults(abs+""));
                }
                if(exp == 1) {
                    if(abs!=1) sb.append(ResultFormatter.trimIntegerResults(abs+""));
                    sb.append("x");
                } else if(exp>1) {
                    if(abs!=1) sb.append(ResultFormatter.trimIntegerResults(abs+""));
                    sb.append("x^").append(exp);
                }
            }
        }
        return sb.toString();
    }

    protected Double executeDefiniteIntegration() {
        return function.calculateIntegral(upperLimit) - function.calculateIntegral(lowerLimit);
    }

    public int getOrder() {
        return order;
    }

    public List<Double> getCoefficients() {
        return coefficients;
    }

    @Override
    public String toString() {
        return String.format(STRING_REPRESENTATION_FORMAT,
                getStringRepresentation(),
                ResultFormatter.trimIntegerResults(lowerLimit + ""),
                ResultFormatter.trimIntegerResults(upperLimit + ""));
    }

    private synchronized String getStringRepresentation() {
        if (null == stringRepresentation) {
            stringRepresentation = provideStringRepresentation();
        }
        return stringRepresentation;
    }

    @Override
    public Double evaluateOnBaseFunction(Double evalPoint) {
        return function.apply(evalPoint);
    }
}
