package org.apache.commons.math3.util;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {
    
    private FastMathLiteralArrays fastMathLiteralArrays;
    
    @Before
    public void setUp() {
        // Since FastMathLiteralArrays is package-private, we can't instantiate it directly
        // We'll test the static methods instead
    }
    
    @Test
    public void testLoadExpIntA() {
        // Test normal case: array should be loaded and cloned
        double[] result = FastMathLiteralArrays.loadExpIntA();
        
        assertNotNull("Loaded array should not be null", result);
        assertTrue("Array should have elements", result.length > 0);
        
        // Verify it's a clone by checking it's not the same reference
        double[] secondCall = FastMathLiteralArrays.loadExpIntA();
        assertNotSame("Should return a new clone each time", result, secondCall);
        
        // Verify content is the same
        assertEquals("Cloned arrays should have same length", result.length, secondCall.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals("Element at index " + i + " should match", 
                         result[i], secondCall[i], 0.0);
        }
    }
    
    @Test
    public void testLoadExpIntB() {
        // Test normal case: array should be loaded and cloned
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        assertNotNull("Loaded array should not be null", result);
        assertTrue("Array should have elements", result.length > 0);
        
        // Verify it's a clone
        double[] secondCall = FastMathLiteralArrays.loadExpIntB();
        assertNotSame("Should return a new clone each time", result, secondCall);
        
        // Check for NaN values in the array (expected based on source)
        boolean foundNaN = false;
        for (double value : result) {
            if (Double.isNaN(value)) {
                foundNaN = true;
                break;
            }
        }
        assertTrue("Array should contain NaN values", foundNaN);
    }
    
    @Test
    public void testLoadExpFracA() {
        // Test normal case
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        assertNotNull("Loaded array should not be null", result);
        assertTrue("Array should have elements", result.length > 0);
        
        // Verify first element is 1.0 as per source
        assertEquals("First element should be 1.0", 1.0, result[0], 1e-15);
        
        // Verify monotonic increase (exponential property)
        for (int i = 1; i < Math.min(100, result.length); i++) {
            assertTrue("Array should be monotonically increasing at index " + i,
                      result[i] > result[i-1]);
        }
    }
    
    @Test
    public void testLoadExpFracB() {
        // Test normal case
        double[] result = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("Loaded array should not be null", result);
        assertEquals("Array should have same length as EXP_FRAC_A", 
                     FastMathLiteralArrays.loadExpFracA().length, result.length);
        
        // Verify first element is 0.0 as per source
        assertEquals("First element should be 0.0", 0.0, result[0], 1e-15);
        
        // Check array contains both positive and negative values
        boolean hasPositive = false;
        boolean hasNegative = false;
        for (int i = 0; i < Math.min(100, result.length); i++) {
            if (result[i] > 0) hasPositive = true;
            if (result[i] < 0) hasNegative = true;
        }
        assertTrue("Array should contain positive values", hasPositive);
        assertTrue("Array should contain negative values", hasNegative);
    }
    
    @Test
    public void testLoadLnMant() {
        // Test normal case: 2D array should be loaded and cloned
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Loaded array should not be null", result);
        assertTrue("Array should have elements", result.length > 0);
        
        // Verify it's a clone
        double[][] secondCall = FastMathLiteralArrays.loadLnMant();
        assertNotSame("Should return a new clone each time", result, secondCall);
        
        // Verify structure: each element should be an array of 2 doubles
        for (int i = 0; i < Math.min(10, result.length); i++) {
            assertNotNull("Row " + i + " should not be null", result[i]);
            assertEquals("Row " + i + " should have 2 columns", 2, result[i].length);
            
            // First column should be positive (mantissa)
            assertTrue("First column should be non-negative at row " + i, 
                      result[i][0] >= 0);
        }
        
        // Verify first row is [0.0, 0.0] as per source
        assertEquals("First row, first column should be 0.0", 
                     0.0, result[0][0], 1e-15);
        assertEquals("First row, second column should be 0.0", 
                     0.0, result[0][1], 1e-15);
    }
}