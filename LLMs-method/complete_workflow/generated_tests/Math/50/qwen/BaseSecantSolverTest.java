package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.commons.math.analysis.MultivariateRealFunction;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Test;

public class BaseSecantSolverTest {
    
    @Test
    public void testNormalCaseMinimize() {
        // Create optimizer with valid parameters
        BOBYQAOptimizer optimizer = new BOBYQAOptimizer(5);
        
        // Create a simple quadratic function to minimize
        MultivariateRealFunction function = new MultivariateRealFunction() {
            public double value(double[] point) {
                // f(x,y) = (x-1)^2 + (y-2)^2, minimum at (1,2)
                return Math.pow(point[0] - 1.0, 2) + Math.pow(point[1] - 2.0, 2);
            }
        };
        
        // Set up optimization problem
        double[] startPoint = {0.0, 0.0};
        double[] lowerBounds = {-10.0, -10.0};
        double[] upperBounds = {10.0, 10.0};
        
        RealPointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint, lowerBounds, upperBounds);
        
        // Verify results
        assertNotNull(result);
        assertNotNull(result.getPoint());
        assertEquals(2, result.getPoint().length);
        assertTrue(Math.abs(result.getPoint()[0] - 1.0) < 0.1);
        assertTrue(Math.abs(result.getPoint()[1] - 2.0) < 0.1);
        assertTrue(result.getValue() >= 0.0);
    }
    
    @Test
    public void testBoundaryConditions() {
        // Test dimension boundary conditions
        try {
            // Try with dimension 1 (too small)
            BOBYQAOptimizer optimizer1 = new BOBYQAOptimizer(4);
            double[] startPoint1 = {1.0};
            double[] lowerBounds1 = {0.0};
            double[] upperBounds1 = {2.0};
            
            assertThrows(NumberIsTooSmallException.class, () -> {
                optimizer1.optimize(10, mock(MultivariateRealFunction.class), 
                                   GoalType.MINIMIZE, startPoint1, lowerBounds1, upperBounds1);
            });
        } catch (Exception e) {
            fail("Should throw NumberIsTooSmallException for dimension 1");
        }
        
        // Test interpolation points boundary
        double[] startPoint = {1.0, 2.0};
        double[] lowerBounds = {0.0, 0.0};
        double[] upperBounds = {3.0, 3.0};
        
        // Valid case - should not throw exception
        BOBYQAOptimizer validOptimizer = new BOBYQAOptimizer(6); // n+2=4, (n+1)(n+2)/2=6
        assertNotNull(validOptimizer);
        
        // Test bounds mismatch
        BOBYQAOptimizer optimizer = new BOBYQAOptimizer(5);
        double[] badLowerBounds = {0.0}; // wrong dimension
        
        assertThrows(DimensionMismatchException.class, () -> {
            optimizer.optimize(10, mock(MultivariateRealFunction.class), 
                              GoalType.MINIMIZE, startPoint, badLowerBounds, upperBounds);
        });
    }
    
    @Test
    public void testInvalidInterpolationPoints() {
        double[] startPoint = {1.0, 2.0, 3.0}; // dimension = 3
        double[] lowerBounds = {0.0, 0.0, 0.0};
        double[] upperBounds = {4.0, 4.0, 4.0};
        
        // Test too few interpolation points (n+1 = 4, but we need at least n+2 = 5)
        assertThrows(OutOfRangeException.class, () -> {
            BOBYQAOptimizer optimizer = new BOBYQAOptimizer(4);
            optimizer.optimize(10, mock(MultivariateRealFunction.class), 
                              GoalType.MINIMIZE, startPoint, lowerBounds, upperBounds);
        });
        
        // Test too many interpolation points ((n+1)(n+2)/2 = 10, so 11 is too many)
        assertThrows(OutOfRangeException.class, () -> {
            BOBYQAOptimizer optimizer = new BOBYQAOptimizer(11);
            optimizer.optimize(10, mock(MultivariateRealFunction.class), 
                              GoalType.MINIMIZE, startPoint, lowerBounds, upperBounds);
        });
        
        // Test valid range
        BOBYQAOptimizer validOptimizer = new BOBYQAOptimizer(7);
        assertNotNull(validOptimizer);
    }
    
    @Test
    public void testStartPointOutOfBounds() {
        BOBYQAOptimizer optimizer = new BOBYQAOptimizer(5);
        MultivariateRealFunction function = mock(MultivariateRealFunction.class);
        
        double[] startPoint = {1.0, 2.0};
        double[] lowerBounds = {0.0, 0.0};
        double[] upperBounds = {1.5, 1.5}; // startPoint[1] = 2.0 is out of bounds
        
        assertThrows(OutOfRangeException.class, () -> {
            optimizer.optimize(10, function, GoalType.MINIMIZE, startPoint, lowerBounds, upperBounds);
        });
        
        // Test lower bound violation
        double[] startPoint2 = {-1.0, 1.0};
        double[] lowerBounds2 = {0.0, 0.0};
        double[] upperBounds2 = {2.0, 2.0};
        
        assertThrows(OutOfRangeException.class, () -> {
            optimizer.optimize(10, function, GoalType.MINIMIZE, startPoint2, lowerBounds2, upperBounds2);
        });
    }
}