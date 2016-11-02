package com.acabra.calculator.integral.definiteintegral;

import com.acabra.calculator.util.ResultFormatter;
import com.google.common.util.concurrent.AtomicDouble;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Agustin on 9/30/2016.
 */
public class DefiniteIntegralPolynomial extends DefiniteIntegralFunction {

    private static final FunctionDomain DOMAIN = FunctionDomainFactory.REAL_NUMBERS;
    private final int order;
    private final List<Double> coefficients;
    private String stringRepresentation;

    public DefiniteIntegralPolynomial(double lowerBound, double upperBound, List<Double> coefficients, Optional<Double> integrationResult, Optional<Double> approximation) {
        super(lowerBound, upperBound, integrationResult.orElse(null), IntegrableFunctionType.POLYNOMIAL.getLabel(), DOMAIN);
        this.approximation = approximation.orElse(null);
        this.coefficients = Collections.unmodifiableList(coefficients);
        this.order = this.coefficients.size();
        this.evaluatedLower = runEvaluation(this.lowerLimit);
        this.evaluatedUpper = runEvaluation(this.upperLimit);
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

    @Override
    public double evaluate(double domainPoint) {
        if (0 == order) {
            return 0.0;
        }
        return inFunctionsDomain(domainPoint) ?
                domainPoint == upperLimit ? evaluatedUpper :
                        domainPoint == lowerLimit ? evaluatedLower :
                                runEvaluation(domainPoint) :
                Double.NaN;
    }

    private double runEvaluation(double domainPoint) {
        double total = 0.0;
        for(int exponent = 0; exponent < order; exponent++) {
            if (coefficients.get(exponent) != 0) {
                total +=  Math.pow(domainPoint, exponent) * coefficients.get(exponent);
            }
        }
        return total;
    }

    protected Double executeIntegration() {
        double total = 0.0;
        for(int exponents = 0; exponents < order; exponents++) {
            if (coefficients.get(exponents) != 0) {
                total +=  evaluateIntegral(upperLimit, coefficients.get(exponents), exponents + 1) -
                        evaluateIntegral(lowerLimit, coefficients.get(exponents), exponents + 1);
            }
        }
        return total;
    }

    private double evaluateIntegral(double domainPoint, double coefficient, int newExponent) {
        return Math.pow(domainPoint, newExponent) * coefficient / newExponent;
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
    public double calculateDerivative(double evalPoint) {
        AtomicDouble total = new AtomicDouble();
        for (int i = 1; i < coefficients.size(); i++) {
            if (coefficients.get(i) != 0.0) {
                total.addAndGet(i * coefficients.get(i) * Math.pow(evalPoint, i-1));
            }
        }
        return total.get();
    }

    public static boolean inFunctionsDomain(double val) {
        return DOMAIN.belongsDomain(val);
    }
}
