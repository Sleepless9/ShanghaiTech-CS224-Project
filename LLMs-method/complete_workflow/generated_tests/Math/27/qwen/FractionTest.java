package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testLoadExpIntA_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpIntA();
        
        assertNotNull("Result array should not be null", result);
        assertEquals("Array length should match", 256, result.length);
        
        // Test first few values
        assertEquals("First element should be 0.0", 0.0, result[0], 1e-10);
        assertEquals("Second element should be NaN", Double.NaN, result[1], 0.0);
        assertEquals("Last element should be NaN", Double.NaN, result[result.length - 1], 0.0);
        
        // Test a known non-NaN value
        assertEquals("EXP_INT_A[100] should be correct", 
                   +2.4787522852420807E-7d, 
                   result[100], 
                   1e-15);
    }
    
    @Test
    public void testLoadExpIntB_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        assertNotNull("Result array should not be null", result);
        assertEquals("Array length should match", 256, result.length);
        
        // Test first few values
        assertEquals("First element should be 0.0", 0.0, result[0], 1e-10);
        assertEquals("Second element should be NaN", Double.NaN, result[1], 0.0);
        assertEquals("Last element should be NaN", Double.NaN, result[result.length - 1], 0.0);
        
        // Test a known non-NaN value
        assertEquals("EXP_INT_B[100] should be correct", 
                   +9.51750020881143E-7d, 
                   result[100], 
                   1e-15);
    }
    
    @Test
    public void testLoadExpFracA_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        assertNotNull("Result array should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        
        // Test boundary values
        assertEquals("First element should be 1.0", 1.0, result[0], 1e-10);
        assertEquals("Last element should be ~2.718", 2.7182817459106445d, result[1023], 1e-8);
        
        // Test a middle value
        assertEquals("EXP_FRAC_A[512] should be correct", 
                   1.6487212181091309d, 
                   result[512], 
                   1e-8);
    }
    
    @Test
    public void testLoadExpFracB_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("Result array should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        
        // Test boundary values
        assertEquals("First element should be 0.0", 0.0, result[0], 1e-10);
        assertEquals("Last element should be ~1.99e-8", 1.994572404486699d, result[1023], 1e-8);
        
        // Test a middle value
        assertTrue("EXP_FRAC_B[512] should be positive", result[512] > 0.0);
        assertEquals("EXP_FRAC_B[512] should be correct",
                   1.155135558327803e-8,
                   result[512],
                   1e-15);
    }
    
    @Test
    public void testLoadLnMant_NormalCase() {
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Result array should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        
        // Test first row
        assertNotNull("First row should not be null", result[0]);
        assertEquals("First row should have length 2", 2, result[0].length);
        assertEquals("LN_MANT[0][0] should be 0.0", 0.0, result[0][0], 1e-10);
        assertEquals("LN_MANT[0][1] should be 0.0", 0.0, result[0][1], 1e-10);
        
        // Test last row
        assertNotNull("Last row should not be null", result[1023]);
        assertEquals("Last row should have length 2", 2, result[1023].length);
        assertEquals("LN_MANT[1023][0] should be correct", 
                   0.6926587820053101d, 
                   result[1023][0], 
                   1e-8);
        assertEquals("LN_MANT[1023][1] should be correct", 
                   -1.943473623641502E-9d, 
                   result[1023][1], 
                   1e-15);
    }
}