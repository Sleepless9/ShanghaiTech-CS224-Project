package org.apache.commons.math3.util;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {
    
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
        
        // Check specific known values
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertEquals("Element at index 41 should match expected value", 
            1.2167807682331913E-308, result[41], 0.0);
        
        // Check for NaN values in expected positions
        assertTrue("Element at index 1 should be NaN", Double.isNaN(result[1]));
        assertTrue("Element at index 10 should be NaN", Double.isNaN(result[10]));
    }
    
    @Test
    public void testLoadExpIntB() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        assertNotNull("Returned array should not be null", result);
        assertEquals("Arrays A and B should have same length", 
            FastMathLiteralArrays.loadExpIntA().length, result.length);
        
        // Check specific known values
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertEquals("Element at index 41 should match expected value", 
            -1.76097684E-316, result[41], 1e-324);
        
        // Check for NaN values in expected positions
        assertTrue("Element at index 1 should be NaN", Double.isNaN(result[1]));
        assertTrue("Element at index 30 should be NaN", Double.isNaN(result[30]));
    }
    
    @Test
    public void testLoadExpFracA() {
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        assertNotNull("Returned array should not be null", result);
        assertTrue("Array should have length > 0", result.length > 0);
        
        // Check boundary values
        assertEquals("First element should be 1.0", 1.0, result[0], 0.0);
        assertTrue("Last element should be greater than 2.0", result[result.length - 1] > 2.0);
        
        // Check monotonic increasing property (for exponential table)
        for (int i = 1; i < result.length; i++) {
            assertTrue("Array should be monotonically increasing at index " + i,
                result[i] >= result[i-1]);
        }
    }
    
    @Test
    public void testLoadExpFracB() {
        double[] result = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("Returned array should not be null", result);
        assertEquals("ExpFracA and ExpFracB should have same length",
            FastMathLiteralArrays.loadExpFracA().length, result.length);
        
        // Check first element
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        
        // Check that values are relatively small (correction terms)
        for (int i = 0; i < result.length; i++) {
            assertTrue("Value at index " + i + " should be relatively small",
                Math.abs(result[i]) < 1e-6 || Double.isNaN(result[i]));
        }
    }
    
    @Test
    public void testLoadLnMant() {
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Returned array should not be null", result);
        assertTrue("Array should have length > 0", result.length > 0);
        
        // Check structure of 2D array
        assertEquals("Each row should have 2 columns", 2, result[0].length);
        
        // Check specific values
        assertEquals("First element first column should be 0.0", 
            0.0, result[0][0], 0.0);
        assertEquals("First element second column should be 0.0", 
            0.0, result[0][1], 0.0);
        
        // Check that first column values are increasing (for log table)
        for (int i = 1; i < result.length; i++) {
            assertTrue("First column should be increasing at index " + i,
                result[i][0] >= result[i-1][0]);
        }
    }
}