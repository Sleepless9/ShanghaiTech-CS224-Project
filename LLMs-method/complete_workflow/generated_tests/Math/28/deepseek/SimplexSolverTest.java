package org.apache.commons.math3.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testLoadExpIntA() {
        // Test that the method returns a non-null array
        double[] result = FastMathLiteralArrays.loadExpIntA();
        assertNotNull("Returned array should not be null", result);
        
        // Test that the array has the expected length
        assertEquals("Array length should match original", 1024, result.length);
        
        // Test specific values at known positions
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertEquals("Element at index 512 should be 1.0", 1.0, result[512], 0.0);
        
        // Test that NaN values are preserved
        assertTrue("Element at index 1 should be NaN", Double.isNaN(result[1]));
        assertTrue("Element at index 1023 should be NaN", Double.isNaN(result[1023]));
    }

    @Test
    public void testLoadExpIntB() {
        // Test that the method returns a non-null array
        double[] result = FastMathLiteralArrays.loadExpIntB();
        assertNotNull("Returned array should not be null", result);
        
        // Test that the array has the expected length
        assertEquals("Array length should match original", 1024, result.length);
        
        // Test specific values at known positions
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        
        // Test that NaN values are preserved
        assertTrue("Element at index 1 should be NaN", Double.isNaN(result[1]));
        assertTrue("Element at index 1023 should be NaN", Double.isNaN(result[1023]));
        
        // Test a non-zero, non-NaN value
        assertFalse("Element at index 512 should not be NaN", Double.isNaN(result[512]));
    }

    @Test
    public void testLoadExpFracA() {
        // Test that the method returns a non-null array
        double[] result = FastMathLiteralArrays.loadExpFracA();
        assertNotNull("Returned array should not be null", result);
        
        // Test that the array has the expected length
        assertEquals("Array length should match original", 1024, result.length);
        
        // Test specific values at known positions
        assertEquals("First element should be 1.0", 1.0, result[0], 0.0);
        assertEquals("Last element should be approximately 2.71828", 2.7182817459106445, result[1023], 1e-12);
        
        // Test monotonic increase (exponential property)
        for (int i = 1; i < result.length; i++) {
            assertTrue("Array should be monotonically increasing", result[i] > result[i-1]);
        }
    }

    @Test
    public void testLoadExpFracB() {
        // Test that the method returns a non-null array
        double[] result = FastMathLiteralArrays.loadExpFracB();
        assertNotNull("Returned array should not be null", result);
        
        // Test that the array has the expected length
        assertEquals("Array length should match original", 1024, result.length);
        
        // Test specific values at known positions
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        
        // Test that values are small (correction terms)
        for (int i = 0; i < result.length; i++) {
            assertTrue("Values should be small correction terms", Math.abs(result[i]) < 1e-6 || Double.isNaN(result[i]));
        }
    }

    @Test
    public void testLoadLnMant() {
        // Test that the method returns a non-null array
        double[][] result = FastMathLiteralArrays.loadLnMant();
        assertNotNull("Returned array should not be null", result);
        
        // Test that the array has the expected dimensions
        assertEquals("Array should have 1024 rows", 1024, result.length);
        assertEquals("Each row should have 2 columns", 2, result[0].length);
        
        // Test specific values at known positions
        assertEquals("First element first column should be 0.0", 0.0, result[0][0], 0.0);
        assertEquals("First element second column should be 0.0", 0.0, result[0][1], 0.0);
        
        // Test that last row has expected values
        assertEquals("Last row first column should be approximately 0.692658", 0.6926587820053101, result[1023][0], 1e-12);
        assertTrue("Last row second column should be negative", result[1023][1] < 0);
        
        // Test that first column is monotonically increasing
        for (int i = 1; i < result.length; i++) {
            assertTrue("First column should be monotonically increasing", result[i][0] > result[i-1][0]);
        }
    }
}