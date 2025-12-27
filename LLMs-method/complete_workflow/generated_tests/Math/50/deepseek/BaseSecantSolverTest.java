package org.apache.commons.math.optimization.direct;

import org.apache.commons.math.analysis.MultivariateRealFunction;
import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testConstructorValidNumberOfInterpolationPoints() {
        BOBYQAOptimizer optimizer = new BOBYQAOptimizer(5);
        assertNotNull(optimizer);
    }

    @Test(expected = OutOfRangeException.class)
    public void testConstructorInvalidNumberOfInterpolationPointsTooSmall() {
        new BOBYQAOptimizer(1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testConstructorInvalidNumberOfInterpolationPointsTooLarge() {
        new BOBYQAOptimizer(100);
    }

    @Test
    public void testOptimizeWithBounds() {
        MultivariateRealFunction sphere = new MultivariateRealFunction() {
            public double value(double[] point) {
                double sum = 0;
                for (double v : point) {
                    sum += v * v;
                }
                return sum;
            }
        };
        
        double[] startPoint = {1.0, 2.0};
        double[] lowerBound = {-2.0, -2.0};
        double[] upperBound = {2.0, 2.0};
        
        BOBYQAOptimizer optimizer = new BOBYQAOptimizer(5, lowerBound, upperBound);
        optimizer.setMaxEvaluations(1000);
        
        RealPointValuePair result = optimizer.optimize(sphere, GoalType.MINIMIZE, startPoint);
        
        assertNotNull(result);
        double[] optimum = result.getPoint();
        assertEquals(2, optimum.length);
        assertTrue(optimum[0] >= lowerBound[0] && optimum[0] <= upperBound[0]);
        assertTrue(optimum[1] >= lowerBound[1] && optimum[1] <= upperBound[1]);
        assertTrue(result.getValue() >= 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testOptimizeWithIncorrectBoundsDimension() {
        double[] startPoint = {1.0, 2.0};
        double[] lowerBound = {-2.0};
        double[] upperBound = {2.0, 2.0, 3.0};
        
        BOBYQAOptimizer optimizer = new BOBYQAOptimizer(5, lowerBound, upperBound);
        optimizer.setMaxEvaluations(100);
        
        MultivariateRealFunction sphere = new MultivariateRealFunction() {
            public double value(double[] point) {
                double sum = 0;
                for (double v : point) {
                    sum += v * v;
                }
                return sum;
            }
        };
        
        optimizer.optimize(sphere, GoalType.MINIMIZE, startPoint);
    }

    @Test
    public void testOptimizeWithoutBounds() {
        MultivariateRealFunction rosenbrock = new MultivariateRealFunction() {
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return (1 - x) * (1 - x) + 100 * (y - x * x) * (y - x * x);
            }
        };
        
        double[] startPoint = {-1.0, 1.0};
        
        BOBYQAOptimizer optimizer = new BOBYQAOptimizer(6);
        optimizer.setMaxEvaluations(2000);
        
        RealPointValuePair result = optimizer.optimize(rosenbrock, GoalType.MINIMIZE, startPoint);
        
        assertNotNull(result);
        double[] optimum = result.getPoint();
        assertEquals(2, optimum.length);
        assertTrue(result.getValue() >= 0.0);
        assertTrue(Double.isFinite(result.getValue()));
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testOptimizeWithStartPointOutsideBounds() {
        MultivariateRealFunction sphere = new MultivariateRealFunction() {
            public double value(double[] point) {
                double sum = 0;
                for (double v : point) {
                    sum += v * v;
                }
                return sum;
            }
        };
        
        double[] startPoint = {5.0, 5.0};
        double[] lowerBound = {0.0, 0.0};
        double[] upperBound = {2.0, 2.0};
        
        BOBYQAOptimizer optimizer = new BOBYQAOptimizer(5, lowerBound, upperBound);
        optimizer.setMaxEvaluations(100);
        
        optimizer.optimize(sphere, GoalType.MINIMIZE, startPoint);
    }
}