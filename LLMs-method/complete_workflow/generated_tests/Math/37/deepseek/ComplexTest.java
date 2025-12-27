package org.apache.commons.math.util;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ComplexTest {
    
    private FastMathLiteralArrays fastMathLiteralArrays;
    
    @Before
    public void setUp() {
        // Since FastMathLiteralArrays is package-private, we can't instantiate it directly
        // We'll test the static methods directly
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
        
        // Check that it's a clone (not the original array)
        double[] secondCall = FastMathLiteralArrays.loadExpIntA();
        assertNotSame("Should return a new array each time", result, secondCall);
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
        assertEquals("EXP_INT_B should have same length as EXP_INT_A", 
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
        assertTrue("Second element should be greater than 1.0", result[1] > 1.0);
        
        // Verify monotonic increasing property (for most of the array)
        for (int i = 1; i < Math.min(100, result.length); i++) {
            assertTrue("Array should be increasing at index " + i, 
                      result[i] > result[i-1]);
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
        assertEquals("EXP_FRAC_B should have same length as EXP_FRAC_A", 
                     expFracA.length, result.length);
    }
    
    @Test
    public void testLoadLnMant() {
        // Test normal case
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Loaded array should not be null", result);
        assertTrue("Array should have positive length", result.length > 0);
        
        // Check structure of 2D array
        assertTrue("Should be a 2D array with inner arrays", result[0].length > 0);
        
        // Check specific values
        assertEquals("First element first dimension should be 0.0", 
                     0.0, result[0][0], 0.0);
        assertEquals("First element second dimension should be 0.0", 
                     0.0, result[0][1], 0.0);
        
        // Verify all inner arrays have 2 elements
        for (int i = 0; i < Math.min(100, result.length); i++) {
            assertEquals("Inner array at index " + i + " should have 2 elements",
                         2, result[i].length);
        }
        
        // Check that it's a clone
        double[][] secondCall = FastMathLiteralArrays.loadLnMant();
        assertNotSame("Should return a new array each time", result, secondCall);
    }
}