package org.apache.commons.math3.util;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class CMAESOptimizerTest {
    
    private FastMathLiteralArrays fastMathLiteralArrays;
    
    @Before
    public void setUp() {
        // Since FastMathLiteralArrays is package-private, we can't instantiate it directly
        // We'll test the static methods instead
    }
    
    @Test
    public void testLoadExpIntA() {
        // Test that the method returns a non-null array
        double[] result = FastMathLiteralArrays.loadExpIntA();
        assertNotNull("EXP_INT_A array should not be null", result);
        
        // Test that the array has the expected length
        assertEquals("EXP_INT_A array length should be 1024", 1024, result.length);
        
        // Test specific values in the array
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertEquals("Element at index 512 should be 1.0", 1.0, result[512], 0.0);
        
        // Test that NaN values are present where expected
        assertTrue("Element at index 1 should be NaN", Double.isNaN(result[1]));
        assertTrue("Element at index 1023 should be NaN", Double.isNaN(result[1023]));
    }
    
    @Test
    public void testLoadExpIntB() {
        // Test that the method returns a non-null array
        double[] result = FastMathLiteralArrays.loadExpIntB();
        assertNotNull("EXP_INT_B array should not be null", result);
        
        // Test that the array has the expected length
        assertEquals("EXP_INT_B array length should be 1024", 1024, result.length);
        
        // Test specific values in the array
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        
        // Test that NaN values are present where expected
        assertTrue("Element at index 1 should be NaN", Double.isNaN(result[1]));
        assertTrue("Element at index 1023 should be NaN", Double.isNaN(result[1023]));
        
        // Test that the array is different from EXP_INT_A
        double[] expIntA = FastMathLiteralArrays.loadExpIntA();
        assertNotSame("EXP_INT_B should be a different array from EXP_INT_A", expIntA, result);
    }
    
    @Test
    public void testLoadExpFracA() {
        // Test that the method returns a non-null array
        double[] result = FastMathLiteralArrays.loadExpFracA();
        assertNotNull("EXP_FRAC_A array should not be null", result);
        
        // Test that the array has the expected length
        assertEquals("EXP_FRAC_A array length should be 1024", 1024, result.length);
        
        // Test specific values in the array
        assertEquals("First element should be 1.0", 1.0, result[0], 0.0);
        assertTrue("Last element should be approximately 2.71828", 
                   Math.abs(result[1023] - 2.7182817459106445) < 1e-10);
        
        // Test that values are monotonically increasing
        for (int i = 1; i < result.length; i++) {
            assertTrue("EXP_FRAC_A should be monotonically increasing", 
                       result[i] > result[i-1] || Math.abs(result[i] - result[i-1]) < 1e-15);
        }
    }
    
    @Test
    public void testLoadExpFracB() {
        // Test that the method returns a non-null array
        double[] result = FastMathLiteralArrays.loadExpFracB();
        assertNotNull("EXP_FRAC_B array should not be null", result);
        
        // Test that the array has the expected length
        assertEquals("EXP_FRAC_B array length should be 1024", 1024, result.length);
        
        // Test specific values in the array
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        
        // Test that the array contains both positive and negative values
        boolean hasPositive = false;
        boolean hasNegative = false;
        for (double value : result) {
            if (value > 0) hasPositive = true;
            if (value < 0) hasNegative = true;
        }
        assertTrue("EXP_FRAC_B should contain positive values", hasPositive);
        assertTrue("EXP_FRAC_B should contain negative values", hasNegative);
    }
    
    @Test
    public void testLoadLnMant() {
        // Test that the method returns a non-null array
        double[][] result = FastMathLiteralArrays.loadLnMant();
        assertNotNull("LN_MANT array should not be null", result);
        
        // Test that the array has the expected dimensions
        assertEquals("LN_MANT array should have 1024 rows", 1024, result.length);
        assertEquals("Each row should have 2 columns", 2, result[0].length);
        
        // Test specific values in the array
        assertEquals("First element should be [0.0, 0.0]", 0.0, result[0][0], 0.0);
        assertEquals("First element should be [0.0, 0.0]", 0.0, result[0][1], 0.0);
        
        // Test that values are reasonable
        for (int i = 0; i < result.length; i++) {
            assertNotNull("Row " + i + " should not be null", result[i]);
            assertEquals("Row " + i + " should have 2 elements", 2, result[i].length);
            
            // The first column should be positive and increasing
            if (i > 0) {
                assertTrue("First column should be increasing", 
                           result[i][0] > result[i-1][0] || 
                           Math.abs(result[i][0] - result[i-1][0]) < 1e-15);
            }
        }
    }
    
    @Test
    public void testArrayConsistency() {
        // Test that related arrays have consistent lengths
        double[] expIntA = FastMathLiteralArrays.loadExpIntA();
        double[] expIntB = FastMathLiteralArrays.loadExpIntB();
        double[] expFracA = FastMathLiteralArrays.loadExpFracA();
        double[] expFracB = FastMathLiteralArrays.loadExpFracB();
        double[][] lnMant = FastMathLiteralArrays.loadLnMant();
        
        assertEquals("EXP_INT_A and EXP_INT_B should have same length", 
                     expIntA.length, expIntB.length);
        assertEquals("EXP_FRAC_A and EXP_FRAC_B should have same length", 
                     expFracA.length, expFracB.length);
        assertEquals("All arrays should have length 1024 or 1024 rows", 
                     1024, expIntA.length);
        assertEquals("All arrays should have length 1024 or 1024 rows", 
                     1024, lnMant.length);
    }
}