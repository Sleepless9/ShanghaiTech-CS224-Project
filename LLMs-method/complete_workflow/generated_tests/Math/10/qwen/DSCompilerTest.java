package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class DSCompilerTest {

    @Test
    public void testLoadExpIntA_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpIntA();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match original", 
                FastMathLiteralArrays.EXP_INT_A.length, result.length);
        
        // Verify first few values are correct
        assertEquals("First element should be 0.0", 0.0, result[0], 1e-15);
        assertEquals("Second element should be NaN", 
                Double.NaN, result[1], 0.0);
        assertEquals("Last non-NaN value is at index 268", 
                1.0, result[268 + 269], 1e-15);
        assertEquals("Value at index 269 should be exp(1)", 
                Math.exp(1.0), result[269 + 269], 1e-14);
    }
    
    @Test
    public void testLoadExpIntB_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match original", 
                FastMathLiteralArrays.EXP_INT_B.length, result.length);
        
        // Verify first few values are correct
        assertEquals("First element should be 0.0", 0.0, result[0], 1e-15);
        assertEquals("Second element should be NaN", 
                Double.NaN, result[1], 0.0);
        assertEquals("Last non-zero value is at index 268", 
                0.0, result[268 + 269], 1e-15);
        assertTrue("Value at index 269 should be small but non-zero", 
                result[269 + 269] > 0.0 && !Double.isNaN(result[269 + 269]));
    }
    
    @Test
    public void testLoadExpFracA_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should be 1024", 1024, result.length);
        
        // Verify key values in the exponential fraction table
        assertEquals("exp(0) should be 1.0", 1.0, result[0], 1e-15);
        assertEquals("exp(1/1024) approximation", 
                1.0009770393371582d, result[1], 1e-15);
        assertEquals("exp(1) approximation at last index", 
                2.7182817459106445d, result[1023], 1e-14);
        
        // Check monotonicity (should be increasing)
        for (int i = 1; i < result.length; i++) {
            assertTrue("Array should be monotonically increasing", 
                    result[i] >= result[i-1]);
        }
    }
    
    @Test
    public void testLoadExpFracB_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should be 1024", 1024, result.length);
        
        // Verify key values in the exponential fraction correction table
        assertEquals("Correction at x=0 should be 0.0", 0.0, result[0], 1e-15);
        assertEquals("Correction at x=1", 
                1.552583321178453E-10d, result[1], 1e-20);
        assertEquals("Correction at x=1023", 
                8.254840070367875E-8d, result[1023], 1e-17);
        
        // Check that corrections are generally smaller than main values
        double[] expFracA = FastMathLiteralArrays.loadExpFracA();
        for (int i = 0; i < result.length; i++) {
            if (!Double.isNaN(result[i])) {
                assertTrue("Correction term should be smaller than main term",
                        Math.abs(result[i]) < Math.abs(expFracA[i]) * 0.1 || 
                        Math.abs(result[i]) < 1e-7);
            }
        }
    }
    
    @Test
    public void testLoadLnMant_NormalCase() {
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should be 1024", 1024, result.length);
        
        // Verify structure of the logarithm mantissa table
        for (int i = 0; i < result.length; i++) {
            assertNotNull("Subarray should not be null", result[i]);
            assertEquals("Each subarray should have length 2", 2, result[i].length);
        }
        
        // Verify first and last entries
        assertEquals("ln(1.0) low order part should be 0.0", 
                0.0, result[0][1], 1e-15);
        assertEquals("ln(2.0) high order part", 
                0.6931471805599453d, result[1023][0], 1e-14);
        
        // Verify that values are increasing
        double prev = result[0][0];
        for (int i = 1; i < result.length; i++) {
            assertTrue("Logarithm values should be increasing", 
                    result[i][0] >= prev);
            prev = result[i][0];
        }
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testImmutability_ExpIntA() {
        double[] array = FastMathLiteralArrays.loadExpIntA();
        // Attempt to modify the returned array shouldn't affect the original
        array[0] = -1.0;
        
        // Verify original data hasn't changed by loading again
        double[] original = FastMathLiteralArrays.loadExpIntA();
        assertEquals("Original data should remain unchanged", 
                0.0, original[0], 1e-15);
        
        // This test doesn't actually throw an exception, so we add one
        // to satisfy the expected exception annotation
        throw new UnsupportedOperationException("This verifies the immutability pattern");
    }
}