package org.apache.commons.math3.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class LineTest {

    @Test
    public void testLoadExpIntA() {
        double[] result = FastMathLiteralArrays.loadExpIntA();
        assertNotNull(result);
        assertTrue(result.length > 0);
        
        // Check first element
        assertEquals(0.0d, result[0], 0.0);
        
        // Check some middle element
        assertEquals(1.0d, result[512], 0.0);
        
        // Check last element
        assertTrue(Double.isNaN(result[result.length - 1]));
    }

    @Test
    public void testLoadExpIntB() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        assertNotNull(result);
        assertEquals(FastMathLiteralArrays.loadExpIntA().length, result.length);
        
        // Check first element
        assertEquals(0.0d, result[0], 0.0);
        
        // Check some middle element
        assertEquals(0.7535752982147762d, result[512], 1e-15);
        
        // Check last element
        assertTrue(Double.isNaN(result[result.length - 1]));
    }

    @Test
    public void testLoadExpFracA() {
        double[] result = FastMathLiteralArrays.loadExpFracA();
        assertNotNull(result);
        assertEquals(1024, result.length);
        
        // Check first element
        assertEquals(1.0d, result[0], 0.0);
        
        // Check middle element
        assertEquals(2.7182817459106445d, result[1023], 1e-15);
        
        // Check monotonic increase
        for (int i = 1; i < result.length; i++) {
            assertTrue(result[i] > result[i-1]);
        }
    }

    @Test
    public void testLoadExpFracB() {
        double[] result = FastMathLiteralArrays.loadExpFracB();
        assertNotNull(result);
        assertEquals(1024, result.length);
        
        // Check first element
        assertEquals(0.0d, result[0], 0.0);
        
        // Check last element
        assertEquals(8.254840070367875E-8d, result[1023], 1e-15);
        
        // Verify it's not all zeros
        boolean hasNonZero = false;
        for (double value : result) {
            if (value != 0.0) {
                hasNonZero = true;
                break;
            }
        }
        assertTrue(hasNonZero);
    }

    @Test
    public void testLoadLnMant() {
        double[][] result = FastMathLiteralArrays.loadLnMant();
        assertNotNull(result);
        assertEquals(1024, result.length);
        
        // Check first element
        assertArrayEquals(new double[]{0.0d, 0.0d}, result[0], 0.0);
        
        // Check middle element
        assertArrayEquals(new double[]{0.40546512603759766d, -1.7929433274271985E-8d}, 
                         result[512], 1e-15);
        
        // Check last element
        assertArrayEquals(new double[]{0.6926587820053101d, -1.943473623641502E-9d}, 
                         result[1023], 1e-15);
        
        // Verify each row has 2 elements
        for (double[] row : result) {
            assertEquals(2, row.length);
        }
    }
}