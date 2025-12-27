package org.apache.commons.math3.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class PolygonsSetTest {

    @Test
    public void testLoadExpIntA() {
        // Test that the method returns a non-null array
        double[] result = FastMathLiteralArrays.loadExpIntA();
        assertNotNull(result);
        
        // Test that the array has the expected length
        assertEquals(1024, result.length);
        
        // Test specific values in the array
        assertEquals(0.0d, result[0], 0.0d);
        assertEquals(Double.NaN, result[1], 0.0d);
        assertEquals(1.0d, result[512], 0.0d);
        assertEquals(Double.NaN, result[1023], 0.0d);
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
        assertEquals(Double.NaN, result[1], 0.0d);
        assertEquals(0.7535752982147762d, result[512], 1e-15);
        assertEquals(Double.NaN, result[1023], 0.0d);
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
        assertEquals(1.0009770393371582d, result[1], 1e-15);
        assertEquals(2.7182817459106445d, result[1023], 1e-15);
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
        assertEquals(1.552583321178453E-10d, result[1], 1e-20);
        assertEquals(8.254840070367875E-8d, result[1023], 1e-15);
    }

    @Test
    public void testLoadLnMant() {
        // Test that the method returns a non-null array
        double[][] result = FastMathLiteralArrays.loadLnMant();
        assertNotNull(result);
        
        // Test that the array has the expected length
        assertEquals(1024, result.length);
        
        // Test that each inner array has 2 elements
        for (double[] innerArray : result) {
            assertEquals(2, innerArray.length);
        }
        
        // Test specific values in the array
        assertEquals(0.0d, result[0][0], 0.0d);
        assertEquals(0.0d, result[0][1], 0.0d);
        assertEquals(0.6926587820053101d, result[1023][0], 1e-15);
        assertEquals(-1.943473623641502E-9d, result[1023][1], 1e-20);
    }
}