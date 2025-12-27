package org.apache.commons.math3.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class CMAESOptimizerTest {

    @Test
    public void testLoadExpIntA() {
        // Test that the method returns a non-null array
        double[] result = FastMathLiteralArrays.loadExpIntA();
        assertNotNull("Returned array should not be null", result);
        
        // Test that the array has the expected length
        assertEquals("Array length should match original", 1024, result.length);
        
        // Test specific values at boundaries
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertTrue("Element at index 1 should be NaN", Double.isNaN(result[1]));
        
        // Test a known value from the middle of the array
        assertEquals("Element at index 512 should be 1.0", 1.0, result[512], 1e-10);
        
        // Test that it's a clone (not the same reference)
        double[] secondCall = FastMathLiteralArrays.loadExpIntA();
        assertNotSame("Should return a new array each time", result, secondCall);
        assertArrayEquals("Cloned arrays should have equal content", result, secondCall, 0.0);
    }

    @Test
    public void testLoadExpIntB() {
        // Test basic properties of the returned array
        double[] result = FastMathLiteralArrays.loadExpIntB();
        assertNotNull("Returned array should not be null", result);
        
        // Test array length
        assertEquals("Array length should match original", 1024, result.length);
        
        // Test specific values
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertTrue("Element at index 1 should be NaN", Double.isNaN(result[1]));
        
        // Test a non-zero, non-NaN value
        assertFalse("Element at index 41 should not be NaN", Double.isNaN(result[41]));
        assertNotEquals("Element at index 41 should not be 0", 0.0, result[41], 0.0);
        
        // Test cloning behavior
        double[] secondCall = FastMathLiteralArrays.loadExpIntB();
        assertArrayEquals("Cloned arrays should have equal content", result, secondCall, 0.0);
    }

    @Test
    public void testLoadExpFracA() {
        // Test the exponential fraction table A
        double[] result = FastMathLiteralArrays.loadExpFracA();
        assertNotNull("Returned array should not be null", result);
        
        // Test array length
        assertEquals("Array length should be 1024", 1024, result.length);
        
        // Test specific values
        assertEquals("First element should be 1.0", 1.0, result[0], 1e-10);
        assertEquals("Last element should be approximately 2.71828", 2.7182817459106445, result[1023], 1e-10);
        
        // Test monotonic increase (exponential property)
        for (int i = 1; i < result.length; i++) {
            assertTrue("Array should be monotonically increasing", result[i] > result[i-1] || Math.abs(result[i] - result[i-1]) < 1e-15);
        }
        
        // Test cloning
        double[] clone = FastMathLiteralArrays.loadExpFracA();
        assertNotSame("Should return a clone", result, clone);
    }

    @Test
    public void testLoadExpFracB() {
        // Test the exponential fraction table B
        double[] result = FastMathLiteralArrays.loadExpFracB();
        assertNotNull("Returned array should not be null", result);
        
        // Test array length
        assertEquals("Array length should be 1024", 1024, result.length);
        
        // Test specific values
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        
        // Test that most values are very small (correction terms)
        int nonZeroCount = 0;
        for (double value : result) {
            if (Math.abs(value) > 1e-10) {
                nonZeroCount++;
            }
        }
        assertTrue("Should contain many non-zero correction terms", nonZeroCount > 100);
        
        // Test cloning
        double[] secondCall = FastMathLiteralArrays.loadExpFracB();
        assertArrayEquals("Cloned arrays should be equal", result, secondCall, 0.0);
    }

    @Test
    public void testLoadLnMant() {
        // Test the logarithm mantissa table
        double[][] result = FastMathLiteralArrays.loadLnMant();
        assertNotNull("Returned array should not be null", result);
        
        // Test array dimensions
        assertEquals("Should have 1024 rows", 1024, result.length);
        assertEquals("Each row should have 2 columns", 2, result[0].length);
        
        // Test specific values
        assertArrayEquals("First row should be [0.0, 0.0]", new double[]{0.0, 0.0}, result[0], 0.0);
        
        // Test that first column values are increasing
        for (int i = 1; i < result.length; i++) {
            assertTrue("First column should be increasing", result[i][0] > result[i-1][0] || Math.abs(result[i][0] - result[i-1][0]) < 1e-15);
        }
        
        // Test that second column values are small (correction terms)
        double maxAbsSecondCol = 0;
        for (double[] row : result) {
            maxAbsSecondCol = Math.max(maxAbsSecondCol, Math.abs(row[1]));
        }
        assertTrue("Second column should contain small correction terms", maxAbsSecondCol < 1e-6);
        
        // Test cloning
        double[][] clone = FastMathLiteralArrays.loadLnMant();
        assertNotSame("Should return a clone", result, clone);
        assertEquals("Clone should have same number of rows", result.length, clone.length);
    }
}