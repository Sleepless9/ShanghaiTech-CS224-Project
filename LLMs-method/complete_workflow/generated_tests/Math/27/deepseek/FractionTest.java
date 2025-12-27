package org.apache.commons.math3.util;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class FractionTest {
    
    private FastMathLiteralArrays fastMathLiteralArrays;
    
    @Before
    public void setUp() {
        // Since FastMathLiteralArrays is package-private, we can't instantiate it directly
        // We'll test the static methods instead
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
        
        // Check that it's a clone by verifying it's not the same reference
        double[] secondCall = FastMathLiteralArrays.loadExpIntA();
        assertNotSame("Should return a new clone each time", result, secondCall);
        assertEquals("Cloned arrays should have same length", result.length, secondCall.length);
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
        
        // Verify cloning behavior
        double[] secondCall = FastMathLiteralArrays.loadExpIntB();
        assertNotSame("Should return a new clone each time", result, secondCall);
    }
    
    @Test
    public void testLoadExpFracA() {
        // Test normal case
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        assertNotNull("Loaded array should not be null", result);
        assertTrue("Array should have positive length", result.length > 0);
        
        // Check specific values
        assertEquals("First element should be 1.0", 1.0, result[0], 0.0);
        assertEquals("Second element should be approximately 1.000977", 1.0009770393371582, result[1], 1e-12);
        
        // Verify cloning
        double[] secondCall = FastMathLiteralArrays.loadExpFracA();
        assertNotSame("Should return a new clone each time", result, secondCall);
        assertEquals("Cloned arrays should have same value at index 0", result[0], secondCall[0], 0.0);
    }
    
    @Test
    public void testLoadExpFracB() {
        // Test normal case
        double[] result = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("Loaded array should not be null", result);
        assertTrue("Array should have positive length", result.length > 0);
        
        // Check specific values
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertEquals("Second element should be approximately 1.552583e-10", 1.552583321178453E-10, result[1], 1e-20);
        
        // Verify cloning
        double[] secondCall = FastMathLiteralArrays.loadExpFracB();
        assertNotSame("Should return a new clone each time", result, secondCall);
    }
    
    @Test
    public void testLoadLnMant() {
        // Test normal case for 2D array
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Loaded array should not be null", result);
        assertTrue("Array should have positive length", result.length > 0);
        assertTrue("Inner arrays should exist", result[0].length > 0);
        
        // Check specific values
        assertEquals("First element first inner array should be 0.0", 0.0, result[0][0], 0.0);
        assertEquals("First element second inner array should be 0.0", 0.0, result[0][1], 0.0);
        
        // Check structure of the 2D array
        assertEquals("Should be a 2D array with 2 columns", 2, result[0].length);
        
        // Verify cloning
        double[][] secondCall = FastMathLiteralArrays.loadLnMant();
        assertNotSame("Should return a new clone each time", result, secondCall);
        assertNotSame("Inner arrays should also be cloned", result[0], secondCall[0]);
    }
}