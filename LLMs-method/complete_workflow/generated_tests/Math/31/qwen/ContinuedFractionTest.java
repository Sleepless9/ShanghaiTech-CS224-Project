package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ContinuedFractionTest {

    @Test
    public void testLoadExpIntA() {
        double[] result = FastMathLiteralArrays.loadExpIntA();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        
        // Test first few elements
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertEquals("Second element should be NaN", Double.NaN, result[1], 0.0);
        
        // Test some middle values
        assertTrue("Element at index 50 should be positive", result[50] > 0.0);
        assertTrue("Element at index 50 should not be NaN", !Double.isNaN(result[50]));
        
        // Test last few elements
        assertEquals("Last element should be NaN", Double.NaN, result[result.length - 1], 0.0);
        assertEquals("Second to last should be NaN", Double.NaN, result[result.length - 2], 0.0);
    }
    
    @Test
    public void testLoadExpIntB() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        
        // Test first few elements
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertEquals("Second element should be NaN", Double.NaN, result[1], 0.0);
        
        // Test specific known values
        assertTrue("Element at index 10 should not be NaN", !Double.isNaN(result[10]));
        assertTrue("Element at index 10 should be negative", result[10] < 0.0);
        
        // Test last few elements
        assertEquals("Last element should be NaN", Double.NaN, result[result.length - 1], 0.0);
        assertEquals("Second to last should be NaN", Double.NaN, result[result.length - 2], 0.0);
    }
    
    @Test
    public void testLoadExpFracA() {
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        
        // Test first and last elements
        assertEquals("First element should be 1.0", 1.0, result[0], 0.0);
        assertEquals("Last element should be approximately 2.718", 2.718, result[result.length - 1], 0.01);
        
        // Test that values are increasing
        for (int i = 1; i < result.length; i++) {
            if (!Double.isNaN(result[i-1]) && !Double.isNaN(result[i])) {
                assertTrue("Values should be non-decreasing", result[i] >= result[i-1]);
            }
        }
        
        // Test specific indices
        assertTrue("Element at index 100 should be greater than 1.1", result[100] > 1.1);
        assertTrue("Element at index 500 should be greater than 1.6", result[500] > 1.6);
    }
    
    @Test
    public void testLoadExpFracB() {
        double[] result = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        
        // Test first element
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        
        // Test that the array has valid values (not all NaN)
        int nonNaNCount = 0;
        for (double value : result) {
            if (!Double.isNaN(value)) {
                nonNaNCount++;
            }
        }
        assertTrue("Should have many non-NaN values", nonNaNCount > 900);
        
        // Test specific indices
        assertTrue("Element at index 10 should not be zero", Math.abs(result[10]) > 1e-10);
        assertTrue("Element at index 100 should not be zero", Math.abs(result[100]) > 1e-10);
    }
    
    @Test
    public void testLoadLnMant() {
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Outer array length should match", 1024, result.length);
        
        // Test each inner array has length 2
        for (int i = 0; i < result.length; i++) {
            assertNotNull("Inner array should not be null", result[i]);
            assertEquals("Inner array should have length 2", 2, result[i].length);
        }
        
        // Test first element
        assertEquals("First element of first array should be 0.0", 0.0, result[0][0], 0.0);
        assertEquals("Second element of first array should be 0.0", 0.0, result[0][1], 0.0);
        
        // Test that values are reasonable (logarithms between 0 and ~0.7)
        for (int i = 1; i < result.length; i++) {
            if (!Double.isNaN(result[i][0])) {
                assertTrue("First component should be positive", result[i][0] >= 0.0);
                assertTrue("First component should be less than 1.0", result[i][0] < 1.0);
            }
        }
    }
}