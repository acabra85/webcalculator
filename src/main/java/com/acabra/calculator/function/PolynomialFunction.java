package com.acabra.calculator.function;

import com.acabra.calculator.integral.definiteintegral.FunctionDomain;
import com.acabra.calculator.integral.definiteintegral.FunctionDomainFactory;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.DoubleAdder;

/**
 * Created by Agustin on 11/2/2016.
 */
public class PolynomialFunction extends RealFunction {

    public static final FunctionDomain DOMAIN = FunctionDomainFactory.REAL_NUMBERS;
    private final List<Double> coefficients;
    private final int order;

    public PolynomialFunction(List<Double> coefficients) {
        super(PolynomialFunction.DOMAIN);
        this.coefficients = coefficients == null ? Collections.emptyList() : Collections.unmodifiableList(coefficients);
        this.order = this.coefficients.size();
        if (coefficients != null && !coefficients.stream().allMatch(domain::belongsDomain))
            throw new UnsupportedOperationException("Coefficients not belonging to the domain found");
    }

    @Override
    public Double apply(Double evalPoint) {
        if (0 == order) {
            return 0.0;
        }
        if (domain.belongsDomain(evalPoint)) {
            DoubleAdder total = new DoubleAdder();
            for(int exponent = 0; exponent < coefficients.size(); exponent++) {
                if (coefficients.get(exponent) != 0) {
                    total.add(coefficients.get(exponent) * Math.pow(evalPoint, exponent));
                }
            }
            return total.doubleValue();
        }
        throw new UnsupportedOperationException(String.format("%.4f does not belong to functions DOMAIN", evalPoint));
    }

    @Override
    public Double calculateDerivative(Double evalPoint) {
        if (domain.belongsDomain(evalPoint)) {
            DoubleAdder total = new DoubleAdder();
            for (int i = 1; i < coefficients.size(); i++) {
                if (coefficients.get(i) != 0.0) {
                    total.add(i * coefficients.get(i) * Math.pow(evalPoint, i-1));
                }
            }
            return total.sum();
        }
        throw new UnsupportedOperationException(String.format("%.4f does not belong to functions DOMAIN", evalPoint));
    }

    private double evaluateIntegral(double coefficient, double domainPoint, int newExponent) {
        return coefficient * Math.pow(domainPoint, newExponent) / newExponent;
    }

    @Override
    public Double calculateIntegral(Double evalPoint) {
        DoubleAdder total = new DoubleAdder();
        for(int exponents = 0; exponents < order; exponents++) {
            if (coefficients.get(exponents) != 0) {
                total.add(evaluateIntegral(coefficients.get(exponents), evalPoint, exponents + 1));
            }
        }
        return total.sum();
    }
}
