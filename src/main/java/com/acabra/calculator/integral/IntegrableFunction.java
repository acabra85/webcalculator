package com.acabra.calculator.integral;

import com.acabra.calculator.util.ResultFormatter;
import org.apache.log4j.Logger;

/**
 * Created by Agustin on 9/29/2016.
 */
public abstract class IntegrableFunction {

    protected static final String STRING_REPRESENTATION_FORMAT = "Integ{%s}[%s, %s]";
    protected final double lowerBound;
    protected final double upperBound;
    private final String label;
    protected Double result;
    protected volatile Double sequenceRiemannRectangle;
    protected static final Logger logger = Logger.getLogger(IntegrableFunction.class);

    public IntegrableFunction(double lowerBound, double upperBound, Double result, String label) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.result = result;
        this.label = label;
        this.sequenceRiemannRectangle = null;
    }

    /**
     * This method evaluates the integral using the
     * integration techniques for the specified function.
     *
     * Recommended to cache the result of this method to be stored as result.
     * Since the bounds of this object are final the result is not meant to change.
     *
     * @return the result of integration.
     */
    public synchronized double solve() {
        if (null == result) {
            result = executeIntegration();
        }
        return result;
    }

    /**
     * This method is meant to provide the integration solution using the integration
     * techniques for this function
     * @return the result of integration between limits
     */
    protected abstract Double executeIntegration();

    /**
     * Calculates the area of the Riemann Sequence rectangle
     * @param inscribed if true the height of the rectangle will be the result of
     *                  evaluating the function on the lowerLimit or upperLimit if false.
     * @see <a href="https://en.wikipedia.org/wiki/Riemann_integral">Riemann Integral<a/>
     * @return
     */
    public Double calculateRiemannSequenceRectangleArea(boolean inscribed) {
        /*TODO Areas under the x-axis should be taken in count
               this runs on the assumption that the evaluations on all points on this
               function are positive values on y-axis, to solve discrepancy a split
               of ranges is required
          */
        double width = (upperBound - lowerBound);
        double evaluatedLower = Math.abs(evaluate(lowerBound));
        double evaluatedUpper = Math.abs(evaluate(upperBound));
        double height = inscribed ? Math.min(evaluatedLower, evaluatedUpper) : Math.max(evaluatedLower, evaluatedUpper);
        return width * height;
    }

    /**
     * This method evaluates the integral using
     * the Riemann Sequences technique by approximating either inscribed or
     * circumscribed rectangles from the graph.
     *
     * Recommended to cache the result of this method to be stored as result.
     * Since the bounds of this object are final the result is not meant to change.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Riemann_integral">Riemann Integral<a/>
     * @return the area of the calculated rectangle
     */
    protected abstract double solveIntegralWithRiemannSequences(boolean inscribedRectangle);

    /**
     * Evaluates the function on a single domainPoint, it is recommended to
     * watch for domain constraints of the function.
     * @param domainPoint a domainPoint belonging to the domain of the function.
     * @return the evaluated function on the requested domainPoint
     */
    protected abstract double evaluate(double domainPoint);

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public double getResult() {
        return solve();
    }

    public synchronized Double getSequenceRiemannRectangle() {
        if (sequenceRiemannRectangle == null ) { //if no sequence rectangle was given provide one
            sequenceRiemannRectangle = calculateRiemannSequenceRectangleArea(true);
        }
        return sequenceRiemannRectangle;
    }

    @Override
    public String toString() {
        return String.format(STRING_REPRESENTATION_FORMAT,
                IntegralFunctionType.EXPONENTIAL.getLabel(),
                ResultFormatter.trimIntegerResults(lowerBound + ""),
                ResultFormatter.trimIntegerResults(upperBound + ""));
    }

}
