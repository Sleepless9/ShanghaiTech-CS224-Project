package org.apache.commons.math3.util;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    private FastMathLiteralArrays fastMathLiteralArrays;
    
    @Before
    public void setUp() {
        // Since FastMathLiteralArrays is package-private, we can't instantiate it directly
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
        assertNotSame("EXP_INT_A and EXP_INT_B should be different arrays", expIntA, result);
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
        
        // Test that values are monotonically increasing
        for (int i = 1; i < result.length; i++) {
            assertTrue("EXP_FRAC_A should be monotonically increasing", 
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
        
        // Test that the array is different from EXP_FRAC_A
        double[] expFracA = FastMathLiteralArrays.loadExpFracA();
        assertNotSame("EXP_FRAC_A and EXP_FRAC_B should be different arrays", expFracA, result);
        
        // Test that values are small (correction terms)
        for (int i = 0; i < result.length; i++) {
            assertTrue("EXP_FRAC_B values should be small correction terms", 
                      Math.abs(result[i]) < 1e-6 || Double.isNaN(result[i]));
        }
    }
    
    @Test
    public void testLoadLnMant() {
        // Test that the method returns a non-null array
        double[][] result = FastMathLiteralArrays.loadLnMant();
        assertNotNull("LN_MANT array should not be null", result);
        
        // Test that the array has the expected dimensions
        assertEquals("LN_MANT should have 1024 rows", 1024, result.length);
        assertEquals("Each row of LN_MANT should have 2 columns", 2, result[0].length);
        
        // Test specific values in the array
        assertEquals("First element should be [0.0, 0.0]", 0.0, result[0][0], 0.0);
        assertEquals("First element should be [0.0, 0.0]", 0.0, result[0][1], 0.0);
        
        // Test that the last row corresponds to ln(2)
        double lastValue = result[1023][0] + result[1023][1];
        assertEquals("Last element should approximate ln(2)", Math.log(2), lastValue, 1e-10);
        
        // Test that values are monotonically increasing
        for (int i = 1; i < result.length; i++) {
            double current = result[i][0] + result[i][1];
            double previous = result[i-1][0] + result[i-1][1];
            assertTrue("LN_MANT should be monotonically increasing", 
                      current > previous || Math.abs(current - previous) < 1e-15);
        }
    }
}