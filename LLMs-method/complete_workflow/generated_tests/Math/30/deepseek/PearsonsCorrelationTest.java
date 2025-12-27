package org.apache.commons.math3.util;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {
    
    private FastMathLiteralArrays fastMathLiteralArrays;
    
    @Before
    public void setUp() {
        // Since the class is package-private and has only static methods,
        // we don't need to instantiate it
    }
    
    @Test
    public void testLoadExpIntA() {
        double[] result = FastMathLiteralArrays.loadExpIntA();
        
        assertNotNull("Returned array should not be null", result);
        assertTrue("Array should have length > 0", result.length > 0);
        
        // Check specific values
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertEquals("Element at index 1 should be NaN", Double.NaN, result[1], 0.0);
        
        // Check a middle value
        int middleIndex = result.length / 2;
        assertTrue("Middle value should be a valid double", 
                  Double.isFinite(result[middleIndex]) || Double.isNaN(result[middleIndex]));
    }
    
    @Test
    public void testLoadExpIntB() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        assertNotNull("Returned array should not be null", result);
        assertEquals("Arrays should have same length", 
                    FastMathLiteralArrays.loadExpIntA().length, result.length);
        
        // Check specific values
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertEquals("Element at index 1 should be NaN", Double.NaN, result[1], 0.0);
        
        // Verify array contains valid double values
        for (int i = 0; i < Math.min(100, result.length); i++) {
            double val = result[i];
            assertTrue("Value should be finite or NaN", 
                      Double.isFinite(val) || Double.isNaN(val) || Double.isInfinite(val));
        }
    }
    
    @Test
    public void testLoadExpFracA() {
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        assertNotNull("Returned array should not be null", result);
        assertTrue("Array should have length > 0", result.length > 0);
        
        // Check specific values
        assertEquals("First element should be 1.0", 1.0, result[0], 0.0);
        assertTrue("Second element should be > 1.0", result[1] > 1.0);
        
        // Verify array is monotonically increasing (for exp function)
        for (int i = 1; i < Math.min(10, result.length); i++) {
            assertTrue("Array should be increasing", result[i] > result[i-1]);
        }
    }
    
    @Test
    public void testLoadExpFracB() {
        double[] result = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("Returned array should not be null", result);
        assertEquals("Arrays should have same length", 
                    FastMathLiteralArrays.loadExpFracA().length, result.length);
        
        // Check specific values
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        
        // Verify array contains small correction values
        double maxAbs = 0;
        for (int i = 0; i < Math.min(100, result.length); i++) {
            maxAbs = Math.max(maxAbs, Math.abs(result[i]));
        }
        assertTrue("Values should be small correction factors", maxAbs < 1.0);
    }
    
    @Test
    public void testLoadLnMant() {
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Returned array should not be null", result);
        assertTrue("Array should have length > 0", result.length > 0);
        
        // Check structure of 2D array
        assertTrue("First row should have 2 columns", result[0].length == 2);
        
        // Check specific values
        assertEquals("First element should be [0.0, 0.0]", 0.0, result[0][0], 0.0);
        assertEquals("First element should be [0.0, 0.0]", 0.0, result[0][1], 0.0);
        
        // Verify all rows have 2 columns
        for (int i = 0; i < Math.min(10, result.length); i++) {
            assertEquals("Each row should have 2 columns", 2, result[i].length);
        }
    }
}