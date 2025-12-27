package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class PolygonsSetTest {

    @Test
    public void testLoadExpIntA_NormalCases() {
        double[] result = FastMathLiteralArrays.loadExpIntA();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        assertEquals("First element should be 0.0", 0.0, result[0], 1e-15);
        assertEquals("Last non-NaN element before gap should be correct", 
                     +7.175096392165733E-66d, result[960], 1e-70);
        assertEquals("Element at index 512 should be correct", 
                     +5.873970727302894E-297d, result[512], 1e-300);
    }
    
    @Test
    public void testLoadExpIntB_NormalCases() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        assertEquals("First element should be 0.0", 0.0, result[0], 1e-15);
        assertEquals("Last non-NaN element before gap should be correct", 
                     -9.441842771290538E-300d, result[960], 1e-304);
        assertEquals("Element at index 512 should be correct", 
                     -4.9831202931544113E-298d, result[512], 1e-302);
    }
    
    @Test
    public void testLoadExpFracA_NormalCases() {
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        assertEquals("First element should be 1.0", 1.0, result[0], 1e-15);
        assertEquals("Last element should be exp(1)", 
                     2.7182817459106445d, result[1023], 1e-15);
        assertEquals("Middle element should be correct", 
                     1.653559684753418, result[512], 1e-10);
    }
    
    @Test
    public void testLoadExpFracB_NormalCases() {
        double[] result = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        assertEquals("First element should be 0.0", 0.0, result[0], 1e-15);
        assertEquals("Last element should be small correction", 
                     8.254840070367875E-8d, result[1023], 1e-15);
        assertEquals("Middle element should be correct", 
                     8.750367485925089E-8d, result[512], 1e-10);
    }
    
    @Test
    public void testLoadLnMant_NormalCases() {
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        assertNotNull("Subarray at index 0 should not be null", result[0]);
        assertEquals("Subarray at index 0 should have length 2", 2, result[0].length);
        assertEquals("First subarray first element should be 0.0", 0.0, result[0][0], 1e-15);
        assertEquals("First subarray second element should be 0.0", 0.0, result[0][1], 1e-15);
        assertNotNull("Subarray at index 512 should not be null", result[512]);
        assertEquals("Subarray at index 512 should have length 2", 2, result[512].length);
        assertEquals("Element at [512][0] should be correct", 
                     0.40546512603759766d, result[512][0], 1e-10);
    }
}