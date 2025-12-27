package org.apache.commons.math3.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

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
        
        // Test that it's a clone (not the same reference)
        double[] secondCall = FastMathLiteralArrays.loadExpIntA();
        assertNotSame("Should return a new array each time", result, secondCall);
    }

    @Test
    public void testLoadExpIntB() {
        // Test basic functionality
        double[] result = FastMathLiteralArrays.loadExpIntB();
        assertNotNull("Returned array should not be null", result);
        
        // Test array length
        assertEquals("Array length should match original", 1024, result.length);
        
        // Test specific values
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertTrue("Element at index 512 should be non-zero", Math.abs(result[512]) > 0);
        
        // Test cloning behavior
        double[] secondCall = FastMathLiteralArrays.loadExpIntB();
        assertArrayEquals("Cloned arrays should have equal content", result, secondCall, 0.0);
        assertNotSame("Should return a new array each time", result, secondCall);
    }

    @Test
    public void testLoadExpFracA() {
        // Test basic functionality
        double[] result = FastMathLiteralArrays.loadExpFracA();
        assertNotNull("Returned array should not be null", result);
        
        // Test array length
        assertEquals("Array length should match original", 1024, result.length);
        
        // Test specific values
        assertEquals("First element should be 1.0", 1.0, result[0], 0.0);
        assertTrue("Last element should be > 2.0", result[1023] > 2.0);
        
        // Test monotonic increase (property of exponential)
        for (int i = 1; i < result.length; i++) {
            assertTrue("Array should be monotonically increasing", result[i] > result[i-1]);
        }
    }

    @Test
    public void testLoadExpFracB() {
        // Test basic functionality
        double[] result = FastMathLiteralArrays.loadExpFracB();
        assertNotNull("Returned array should not be null", result);
        
        // Test array length
        assertEquals("Array length should match original", 1024, result.length);
        
        // Test specific values
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        
        // Test that values are relatively small (correction terms)
        for (int i = 0; i < result.length; i++) {
            assertTrue("Correction terms should be small", Math.abs(result[i]) < 1.0);
        }
        
        // Test cloning
        double[] clone = FastMathLiteralArrays.loadExpFracB();
        assertNotSame("Should return a clone", result, clone);
    }

    @Test
    public void testLoadLnMant() {
        // Test basic functionality
        double[][] result = FastMathLiteralArrays.loadLnMant();
        assertNotNull("Returned array should not be null", result);
        
        // Test array dimensions
        assertEquals("Should have 1024 rows", 1024, result.length);
        assertEquals("Each row should have 2 columns", 2, result[0].length);
        
        // Test specific values
        assertEquals("First element first column should be 0.0", 0.0, result[0][0], 0.0);
        assertEquals("First element second column should be 0.0", 0.0, result[0][1], 0.0);
        
        // Test that first column values increase (logarithm table)
        for (int i = 1; i < result.length; i++) {
            assertTrue("First column should be increasing", result[i][0] > result[i-1][0]);
        }
        
        // Test cloning behavior
        double[][] secondCall = FastMathLiteralArrays.loadLnMant();
        assertNotSame("Should return a new array each time", result, secondCall);
        assertEquals("Cloned array should have same dimensions", result.length, secondCall.length);
    }
}