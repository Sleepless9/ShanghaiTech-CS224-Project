package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LineTest {

    @Test
    public void testLoadExpIntA_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpIntA();
        
        assertNotNull("Result array should not be null", result);
        assertEquals("Array length should match original", 1024, result.length);
        
        // Check first few values are as expected
        assertEquals("First value should be 0.0", 0.0, result[0], 1e-15);
        assertTrue("Second value should be NaN", Double.isNaN(result[1]));
        
        // Check a known non-NaN value in the middle
        assertEquals("Value at index 60 should be ~1.0", 1.0, result[60], 1e-15);
    }
    
    @Test
    public void testLoadExpIntB_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        assertNotNull("Result array should not be null", result);
        assertEquals("Array length should match original", 1024, result.length);
        
        // Check first few values are as expected
        assertEquals("First value should be 0.0", 0.0, result[0], 1e-15);
        assertTrue("Second value should be NaN", Double.isNaN(result[1]));
        
        // Verify that returned array is a clone (independent copy)
        double[] original = FastMathLiteralArrays.loadExpIntB();
        result[0] = 999.999;
        assertEquals("Original array should not be modified", 0.0, original[0], 1e-15);
    }
    
    @Test
    public void testLoadExpFracA_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        assertNotNull("Result array should not be null", result);
        assertEquals("Array length should match original", 1024, result.length);
        
        // Check boundary values
        assertEquals("First value should be 1.0", 1.0, result[0], 1e-15);
        assertEquals("Last value should be ~2.718", 2.7182817459106445d, result[result.length - 1], 1e-15);
        
        // Verify it's a proper clone
        double[] original = FastMathLiteralArrays.loadExpFracA();
        assertArrayEquals("Cloned arrays should be equal", original, result, 1e-15);
    }
    
    @Test
    public void testLoadExpFracB_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("Result array should not be null", result);
        assertEquals("Array length should match original", 1024, result.length);
        
        // Check boundary values
        assertEquals("First value should be 0.0", 0.0, result[0], 1e-15);
        assertEquals("Last value should be positive", 8.254840070367875E-8d, result[result.length - 1], 1e-15);
        
        // Verify independence of clone
        double[] firstCall = FastMathLiteralArrays.loadExpFracB();
        double[] secondCall = FastMathLiteralArrays.loadExpFracB();
        secondCall[0] = -1.0;
        assertEquals("First call result should not be affected", 0.0, firstCall[0], 1e-15);
    }
    
    @Test
    public void testLoadLnMant_NormalCase() {
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Result array should not be null", result);
        assertEquals("Array length should match original", 1024, result.length);
        
        // Check structure and values
        assertNotNull("Subarray at index 0 should not be null", result[0]);
        assertEquals("Subarray at index 0 should have length 2", 2, result[0].length);
        
        // Check specific known values
        assertEquals("LN_MANT[1][0] should be ~9.76e-4", 9.760860120877624E-4d, result[1][0], 1e-15);
        assertEquals("LN_MANT[1][1] should be ~-3.90e-11", -3.903230345984362E-11d, result[1][1], 1e-15);
        
        // Verify it's a deep clone by modifying a value
        double[][] original = FastMathLiteralArrays.loadLnMant();
        result[0][0] = 999.999;
        assertEquals("Original array should not be modified", 0.0, original[0][0], 1e-15);
    }
}