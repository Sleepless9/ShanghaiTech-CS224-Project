package org.apache.commons.math3.util;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class FractionTest {
    
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
        
        // Check first element
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        
        // Check a known value in the middle
        assertEquals("Element at index 512 should be 1.0", 1.0, result[512], 1e-10);
        
        // Check last element is NaN
        assertTrue("Last element should be NaN", Double.isNaN(result[result.length - 1]));
    }
    
    @Test
    public void testLoadExpIntB() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        assertNotNull("Returned array should not be null", result);
        assertEquals("Array should have same length as EXP_INT_A", 
                     FastMathLiteralArrays.loadExpIntA().length, result.length);
        
        // Check first element
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        
        // Check that array contains NaN values
        boolean hasNaN = false;
        for (double value : result) {
            if (Double.isNaN(value)) {
                hasNaN = true;
                break;
            }
        }
        assertTrue("Array should contain NaN values", hasNaN);
    }
    
    @Test
    public void testLoadExpFracA() {
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        assertNotNull("Returned array should not be null", result);
        assertTrue("Array should have length > 0", result.length > 0);
        
        // Check first element
        assertEquals("First element should be 1.0", 1.0, result[0], 1e-10);
        
        // Check last element
        double lastValue = result[result.length - 1];
        assertTrue("Last element should be approximately 2.7182817459106445", 
                   Math.abs(lastValue - 2.7182817459106445) < 1e-10);
        
        // Verify array is monotonically increasing
        for (int i = 1; i < result.length; i++) {
            assertTrue("Array should be monotonically increasing", 
                       result[i] > result[i-1] || Math.abs(result[i] - result[i-1]) < 1e-15);
        }
    }
    
    @Test
    public void testLoadExpFracB() {
        double[] result = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("Returned array should not be null", result);
        assertEquals("Array should have same length as EXP_FRAC_A", 
                     FastMathLiteralArrays.loadExpFracA().length, result.length);
        
        // Check first element
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        
        // Check that values are relatively small (correction terms)
        double maxAbsValue = 0.0;
        for (double value : result) {
            maxAbsValue = Math.max(maxAbsValue, Math.abs(value));
        }
        assertTrue("Values should be small correction terms", maxAbsValue < 1e-6);
    }
    
    @Test
    public void testLoadLnMant() {
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Returned array should not be null", result);
        assertEquals("Array should have 1024 rows", 1024, result.length);
        
        // Check first row
        assertNotNull("First row should not be null", result[0]);
        assertEquals("First row should have 2 columns", 2, result[0].length);
        assertEquals("First element of first row should be 0.0", 0.0, result[0][0], 0.0);
        assertEquals("Second element of first row should be 0.0", 0.0, result[0][1], 0.0);
        
        // Check last row
        assertNotNull("Last row should not be null", result[result.length - 1]);
        assertEquals("Last row should have 2 columns", 2, result[result.length - 1].length);
        
        // Check that all rows have 2 columns
        for (int i = 0; i < result.length; i++) {
            assertEquals("Row " + i + " should have 2 columns", 2, result[i].length);
        }
    }
}