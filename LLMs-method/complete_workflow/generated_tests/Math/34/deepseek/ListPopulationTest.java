package org.apache.commons.math3.util;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ListPopulationTest {
    
    private FastMathLiteralArrays fastMathLiteralArrays;
    
    @Before
    public void setUp() {
        // FastMathLiteralArrays is package-private, so we can't instantiate it directly
        // We'll test the static methods instead
    }
    
    @Test
    public void testLoadExpIntA() {
        // Test normal case: array is loaded correctly
        double[] result = FastMathLiteralArrays.loadExpIntA();
        
        assertNotNull("Loaded array should not be null", result);
        assertTrue("Array should have elements", result.length > 0);
        
        // Check specific values
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertEquals("Element at index 1 should be NaN", Double.NaN, result[1], 0.0);
        
        // Check that it's a clone (not the same reference)
        double[] secondCall = FastMathLiteralArrays.loadExpIntA();
        assertNotSame("Should return a new array each time", result, secondCall);
        assertEquals("Arrays should have same length", result.length, secondCall.length);
    }
    
    @Test
    public void testLoadExpIntB() {
        // Test normal case: array is loaded correctly
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        assertNotNull("Loaded array should not be null", result);
        assertTrue("Array should have elements", result.length > 0);
        
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
        // Test normal case: array is loaded correctly
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        assertNotNull("Loaded array should not be null", result);
        assertTrue("Array should have elements", result.length > 0);
        
        // Check specific values
        assertEquals("First element should be 1.0", 1.0, result[0], 0.0);
        assertTrue("Second element should be > 1.0", result[1] > 1.0);
        
        // Verify monotonic increasing (for exp function)
        for (int i = 1; i < result.length; i++) {
            assertTrue("Array should be monotonically increasing", 
                       result[i] >= result[i-1]);
        }
    }
    
    @Test
    public void testLoadExpFracB() {
        // Test normal case: array is loaded correctly
        double[] result = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("Loaded array should not be null", result);
        assertTrue("Array should have elements", result.length > 0);
        
        // Check specific values
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        
        // Verify array length matches EXP_FRAC_A
        double[] expFracA = FastMathLiteralArrays.loadExpFracA();
        assertEquals("EXP_FRAC_B should have same length as EXP_FRAC_A", 
                     expFracA.length, result.length);
    }
    
    @Test
    public void testLoadLnMant() {
        // Test normal case: 2D array is loaded correctly
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Loaded array should not be null", result);
        assertTrue("Array should have elements", result.length > 0);
        
        // Check structure of 2D array
        assertEquals("First row should have 2 columns", 2, result[0].length);
        assertEquals("First element should be 0.0", 0.0, result[0][0], 0.0);
        assertEquals("Second element should be 0.0", 0.0, result[0][1], 0.0);
        
        // Check that all rows have 2 columns
        for (int i = 0; i < result.length; i++) {
            assertEquals("All rows should have 2 columns", 2, result[i].length);
        }
        
        // Verify it's a clone
        double[][] secondCall = FastMathLiteralArrays.loadLnMant();
        assertNotSame("Should return a new array each time", result, secondCall);
    }
}