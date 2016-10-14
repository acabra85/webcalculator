package com.acabra.calculator.integral;

import com.acabra.calculator.util.ResultFormatter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Agustin on 9/30/2016.
 */
public class FPolynomial extends IntegrableFunction {

    private static final FunctionDomain DOMAIN = FunctionDomainFactory.REAL_NUMBERS;
    private final int order;
    private final List<Double> coefficients;
    protected String stringRepresentation;

    public FPolynomial(double lowerBound, double upperBound, List<Double> coefficients, Optional<Double> integrationResult, Optional<Double> approximation) {
        super(lowerBound, upperBound, integrationResult.orElse(null), IntegralFunctionType.POLYNOMIAL.getLabel());
        this.sequenceRiemannRectangle = approximation.orElse(null);
        this.coefficients = Collections.unmodifiableList(coefficients);
        this.order = this.coefficients.size();
        this.evaluatedLower = runEvaluation(this.lowerBound);
        this.evaluatedUpper = runEvaluation(this.upperBound);
    }

    @Override
    protected void validateBounds() {
        if (!DOMAIN.belongsDomain(this.lowerBound) || !DOMAIN.belongsDomain(this.upperBound)
                || DOMAIN.doesRangeContainsNonDomainPoints(this.requestedIntegrationRange)) {
            throw new UnsupportedOperationException("unable to create function, bounds not in function's domain");
        }
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
    protected double evaluate(double domainPoint) {
        if (0 == order) {
            return 0.0;
        }
        return inFunctionsDomain(domainPoint) ?
                domainPoint == upperBound ? evaluatedUpper :
                        domainPoint == lowerBound ? evaluatedLower :
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

    @Override
    protected synchronized double solveIntegralWithRiemannSequences(boolean inscribedRectangle) {
        if (sequenceRiemannRectangle == null) {
            sequenceRiemannRectangle = calculateRiemannSequenceRectangleArea(inscribedRectangle);
        }
        return sequenceRiemannRectangle;
    }

    protected Double executeIntegration() {
        double total = 0.0;
        for(int exponents = 0; exponents < order; exponents++) {
            if (coefficients.get(exponents) != 0) {
                total +=  evaluateIntegral(upperBound, coefficients.get(exponents), exponents + 1) -
                        evaluateIntegral(lowerBound, coefficients.get(exponents), exponents + 1);
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
                ResultFormatter.trimIntegerResults(lowerBound + ""),
                ResultFormatter.trimIntegerResults(upperBound + ""));
    }

    public synchronized String getStringRepresentation() {
        if (null == stringRepresentation) {
            stringRepresentation = provideStringRepresentation();
        }
        return stringRepresentation;
    }

    public static boolean inFunctionsDomain(double val) {
        return DOMAIN.belongsDomain(val);
    }
}
