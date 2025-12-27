package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CMAESOptimizerTest {

    @Test
    public void testLoadExpIntA() {
        double[] result = FastMathLiteralArrays.loadExpIntA();
        
        // Verify array is not null and has expected length
        assertNotNull("loadExpIntA should not return null", result);
        assertEquals("EXP_INT_A array length mismatch", 1024, result.length);
        
        // Verify first few elements are correct
        assertArrayEquals(new double[]{0.0d, Double.NaN, Double.NaN, Double.NaN}, 
                        java.util.Arrays.copyOf(result, 4), 0.0);
        
        // Verify last few elements are correct
        assertArrayEquals(new double[]{Double.NaN, Double.NaN, Double.NaN, Double.NaN}, 
                        java.util.Arrays.copyOfRange(result, result.length - 4, result.length), 0.0);
        
        // Verify a known non-NaN value in the middle
        assertEquals("Expected specific expIntA value at index 768", 
                   5.987414E176d, result[768], 1.0e165d);
    }
    
    @Test
    public void testLoadExpIntB() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        // Verify array is not null and has expected length
        assertNotNull("loadExpIntB should not return null", result);
        assertEquals("EXP_INT_B array length mismatch", 1024, result.length);
        
        // Verify first few elements are correct
        assertArrayEquals(new double[]{0.0d, Double.NaN, Double.NaN, Double.NaN}, 
                        java.util.Arrays.copyOf(result, 4), 0.0);
        
        // Verify last few elements are correct
        assertArrayEquals(new double[]{Double.NaN, Double.NaN, Double.NaN, Double.NaN}, 
                        java.util.Arrays.copyOfRange(result, result.length - 4, result.length), 0.0);
        
        // Verify a known non-zero value in the middle
        assertTrue("Value at index 1000 should be positive", result[1000] > 0.0);
    }
    
    @Test
    public void testLoadExpFracA() {
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        // Verify array is not null and has expected length
        assertNotNull("loadExpFracA should not return null", result);
        assertEquals("EXP_FRAC_A array length mismatch", 1024, result.length);
        
        // Verify first element is exactly 1.0
        assertEquals("First element of EXP_FRAC_A should be 1.0", 
                   1.0d, result[0], 0.0);
        
        // Verify last element matches expected final value
        assertEquals("Last element of EXP_FRAC_A should match", 
                   2.7182817459106445d, result[result.length - 1], 0.0);
        
        // Verify some intermediate values
        assertTrue("Middle values should be increasing", 
                   result[512] > result[256] && result[768] > result[512]);
    }
    
    @Test
    public void testLoadExpFracB() {
        double[] result = FastMathLiteralArrays.loadExpFracB();
        
        // Verify array is not null and has expected length
        assertNotNull("loadExpFracB should not return null", result);
        assertEquals("EXP_FRAC_B array length mismatch", 1024, result.length);
        
        // Verify first element is zero
        assertEquals("First element of EXP_FRAC_B should be 0.0", 
                   0.0d, result[0], 0.0);
        
        // Verify last element is NaN (based on inspection)
        assertTrue("Last element should be NaN", 
                   Double.isNaN(result[result.length - 1]));
        
        // Verify some values are not all zero
        boolean hasNonZero = false;
        for (int i = 1; i < 10; i++) {
            if (result[i] != 0.0 && !Double.isNaN(result[i])) {
                hasNonZero = true;
                break;
            }
        }
        assertTrue("EXP_FRAC_B should have non-zero/non-NaN values", hasNonZero);
    }
    
    @Test
    public void testLoadLnMant() {
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        // Verify array is not null and has expected length
        assertNotNull("loadLnMant should not return null", result);
        assertEquals("LN_MANT array length mismatch", 1024, result.length);
        
        // Verify each sub-array has exactly 2 elements
        for (int i = 0; i < result.length; i++) {
            assertNotNull("Sub-array should not be null", result[i]);
            assertEquals("Each sub-array should have 2 elements", 2, result[i].length);
        }
        
        // Verify first sub-array contains zeros
        assertEquals("First sub-array first element should be 0.0", 
                   0.0d, result[0][0], 0.0);
        assertEquals("First sub-array second element should be 0.0", 
                   0.0d, result[0][1], 0.0);
        
        // Verify last sub-array contains specific values
        assertFalse("Last sub-array first element should not be NaN", 
                    Double.isNaN(result[result.length - 1][0]));
        assertEquals("Last sub-array second element should be negative", 
                   -1.943473623641502E-9d, result[result.length - 1][1], 5.0e-10d);
    }
}