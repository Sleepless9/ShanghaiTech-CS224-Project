package org.apache.commons.math3.util;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class DSCompilerTest {
    
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
        assertTrue("Element at index 513 should be approximately 2.718", 
                   Math.abs(result[513] - 2.7182817459106445) < 1e-10);
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
        assertEquals("Element at index 512 should be 0.0", 0.0, result[512], 0.0);
        
        // Test that some values are NaN as expected from the data
        assertTrue("Element at index 1 should be NaN", Double.isNaN(result[1]));
        assertTrue("Element at index 1023 should be NaN", Double.isNaN(result[1023]));
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
        assertTrue("Last element should be approximately 2.718", 
                   Math.abs(result[1023] - 2.7182817459106445) < 1e-10);
        
        // Test that values are monotonically increasing
        for (int i = 1; i < result.length; i++) {
            assertTrue("Array should be monotonically increasing at index " + i,
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
        
        // Test that the values are relatively small (correction terms)
        for (int i = 0; i < result.length; i++) {
            assertTrue("Values should be small correction terms at index " + i,
                       Math.abs(result[i]) < 1e-6 || Double.isNaN(result[i]));
        }
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
        
        // Test that the first column values are increasing
        for (int i = 1; i < result.length; i++) {
            assertTrue("First column should be increasing at index " + i,
                       result[i][0] > result[i-1][0] || 
                       Math.abs(result[i][0] - result[i-1][0]) < 1e-15);
        }
    }
}