package org.apache.commons.math3.util;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ContinuedFractionTest {
    
    private FastMathLiteralArrays fastMathLiteralArrays;
    
    @Before
    public void setUp() {
        // FastMathLiteralArrays is package-private, so we can't instantiate it directly
        // We'll test the static methods instead
    }
    
    @Test
    public void testLoadExpIntA() {
        // Test normal case: array should be loaded and have correct properties
        double[] result = FastMathLiteralArrays.loadExpIntA();
        
        assertNotNull("Loaded array should not be null", result);
        assertTrue("Array should have positive length", result.length > 0);
        
        // Check specific values in the array
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertEquals("Element at index 1 should be NaN", Double.NaN, result[1], 0.0);
        
        // Check that it's a proper clone (not the same reference)
        double[] result2 = FastMathLiteralArrays.loadExpIntA();
        assertNotSame("Should return a new array each time", result, result2);
        assertEquals("Arrays should have same length", result.length, result2.length);
    }
    
    @Test
    public void testLoadExpIntB() {
        // Test normal case
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        assertNotNull("Loaded array should not be null", result);
        assertTrue("Array should have positive length", result.length > 0);
        
        // Check specific values
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertEquals("Element at index 1 should be NaN", Double.NaN, result[1], 0.0);
        
        // Verify array length matches EXP_INT_A
        double[] expIntA = FastMathLiteralArrays.loadExpIntA();
        assertEquals("EXP_INT_A and EXP_INT_B should have same length", 
                     expIntA.length, result.length);
    }
    
    @Test
    public void testLoadExpFracA() {
        // Test normal case
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        assertNotNull("Loaded array should not be null", result);
        assertTrue("Array should have positive length", result.length > 0);
        
        // Check specific values
        assertEquals("First element should be 1.0", 1.0, result[0], 0.0);
        assertTrue("Second element should be > 1.0", result[1] > 1.0);
        
        // Verify monotonic increasing property (for most of the array)
        for (int i = 1; i < Math.min(100, result.length); i++) {
            assertTrue("Array should be increasing at index " + i, 
                      result[i] > result[i-1] || Math.abs(result[i] - result[i-1]) < 1e-10);
        }
    }
    
    @Test
    public void testLoadExpFracB() {
        // Test normal case
        double[] result = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("Loaded array should not be null", result);
        assertTrue("Array should have positive length", result.length > 0);
        
        // Check specific values
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        
        // Verify array length matches EXP_FRAC_A
        double[] expFracA = FastMathLiteralArrays.loadExpFracA();
        assertEquals("EXP_FRAC_A and EXP_FRAC_B should have same length", 
                     expFracA.length, result.length);
    }
    
    @Test
    public void testLoadLnMant() {
        // Test normal case
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Loaded array should not be null", result);
        assertTrue("Array should have positive length", result.length > 0);
        
        // Check structure of 2D array
        assertTrue("First dimension should have at least 1024 elements", result.length >= 1024);
        
        // Check first element
        assertNotNull("First row should not be null", result[0]);
        assertEquals("First row should have 2 elements", 2, result[0].length);
        assertEquals("First element should be 0.0", 0.0, result[0][0], 0.0);
        assertEquals("Second element should be 0.0", 0.0, result[0][1], 0.0);
        
        // Check last element
        int lastIdx = result.length - 1;
        assertNotNull("Last row should not be null", result[lastIdx]);
        assertEquals("Last row should have 2 elements", 2, result[lastIdx].length);
        
        // Verify it's a proper clone
        double[][] result2 = FastMathLiteralArrays.loadLnMant();
        assertNotSame("Should return a new array each time", result, result2);
    }
}