package org.apache.commons.math3.util;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class DfpTest {
    
    private FastMathLiteralArrays fastMathLiteralArrays;
    
    @Before
    public void setUp() {
        // FastMathLiteralArrays is package-private, so we can't instantiate it directly
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
        assertEquals("Last element should be approximately e", Math.E, result[1023], 1e-10);
        
        // Test monotonic increasing property (for most of the array)
        for (int i = 1; i < result.length - 1; i++) {
            assertTrue("Array should be monotonically increasing at index " + i, 
                      result[i] >= result[i-1] || Math.abs(result[i] - result[i-1]) < 1e-15);
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
        for (int i = 1; i < Math.min(100, result.length); i++) {
            if (result[i] > 0) hasPositive = true;
            if (result[i] < 0) hasNegative = true;
        }
        assertTrue("Array should contain positive values", hasPositive);
        assertTrue("Array should contain negative values", hasNegative);
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
        assertEquals("First element first column should be 0.0", 0.0, result[0][0], 0.0);
        assertEquals("First element second column should be 0.0", 0.0, result[0][1], 0.0);
        
        // Test that the last row corresponds to ln(2) approximation
        // ln(2) ≈ 0.6931471805599453
        double ln2Approx = result[1023][0] + result[1023][1];
        assertEquals("Last row should approximate ln(2)", Math.log(2), ln2Approx, 1e-10);
        
        // Test that all rows have 2 elements
        for (int i = 0; i < result.length; i++) {
            assertEquals("Row " + i + " should have 2 elements", 2, result[i].length);
        }
    }
}