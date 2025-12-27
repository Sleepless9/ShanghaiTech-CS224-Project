package org.apache.commons.math3.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class HarmonicFitterTest {

    @Test
    public void testLoadExpIntA() {
        // Test that the method returns a non-null array
        double[] result = FastMathLiteralArrays.loadExpIntA();
        assertNotNull(result);
        
        // Test that the array has the expected length
        assertEquals(1024, result.length);
        
        // Test specific values in the array
        assertEquals(0.0d, result[0], 0.0d);
        assertEquals(1.0d, result[512], 0.0d);
        assertTrue(Double.isNaN(result[1]));
        assertTrue(Double.isNaN(result[1023]));
    }

    @Test
    public void testLoadExpIntB() {
        // Test that the method returns a non-null array
        double[] result = FastMathLiteralArrays.loadExpIntB();
        assertNotNull(result);
        
        // Test that the array has the expected length
        assertEquals(1024, result.length);
        
        // Test specific values in the array
        assertEquals(0.0d, result[0], 0.0d);
        assertTrue(Double.isNaN(result[1]));
        assertTrue(Double.isNaN(result[1023]));
        
        // Test that the array is different from EXP_INT_A
        double[] expIntA = FastMathLiteralArrays.loadExpIntA();
        assertNotSame(expIntA, result);
    }

    @Test
    public void testLoadExpFracA() {
        // Test that the method returns a non-null array
        double[] result = FastMathLiteralArrays.loadExpFracA();
        assertNotNull(result);
        
        // Test that the array has the expected length
        assertEquals(1024, result.length);
        
        // Test specific values in the array
        assertEquals(1.0d, result[0], 0.0d);
        assertEquals(2.7182817459106445d, result[1023], 1e-10d);
        
        // Test monotonic increasing property
        for (int i = 1; i < result.length; i++) {
            assertTrue(result[i] > result[i-1]);
        }
    }

    @Test
    public void testLoadExpFracB() {
        // Test that the method returns a non-null array
        double[] result = FastMathLiteralArrays.loadExpFracB();
        assertNotNull(result);
        
        // Test that the array has the expected length
        assertEquals(1024, result.length);
        
        // Test specific values in the array
        assertEquals(0.0d, result[0], 0.0d);
        
        // Test that the array is different from EXP_FRAC_A
        double[] expFracA = FastMathLiteralArrays.loadExpFracA();
        assertNotSame(expFracA, result);
    }

    @Test
    public void testLoadLnMant() {
        // Test that the method returns a non-null array
        double[][] result = FastMathLiteralArrays.loadLnMant();
        assertNotNull(result);
        
        // Test that the array has the expected dimensions
        assertEquals(1024, result.length);
        assertEquals(2, result[0].length);
        
        // Test specific values in the array
        assertEquals(0.0d, result[0][0], 0.0d);
        assertEquals(0.0d, result[0][1], 0.0d);
        
        // Test that all rows have 2 columns
        for (int i = 0; i < result.length; i++) {
            assertEquals(2, result[i].length);
        }
        
        // Test that the array is a proper clone (not the same reference)
        double[][] original = FastMathLiteralArrays.loadLnMant();
        assertNotSame(original, result);
    }
}