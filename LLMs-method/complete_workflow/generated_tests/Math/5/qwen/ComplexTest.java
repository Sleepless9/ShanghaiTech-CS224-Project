package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class ComplexTest {

    @Test
    public void testLoadExpIntA_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpIntA();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match", 512, result.length);
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertEquals("Last valid element should match", 2.7182817459106445d, result[512], 0.0);
    }
    
    @Test
    public void testLoadExpIntB_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match", 512, result.length);
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertEquals("Last valid element should match", 2.7182817459106445d, result[512], 0.0);
    }
    
    @Test
    public void testLoadExpFracA_BoundaryCase() {
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should be 1024", 1024, result.length);
        assertEquals("First element should be 1.0", 1.0, result[0], 0.0);
        assertEquals("Middle element should be approximately 1.65", 1.6567160606384277, result[512], 0.000001);
        assertEquals("Last element should be approximately 2.718", 2.7182817459106445d, result[1023], 0.000001);
    }
    
    @Test
    public void testLoadExpFracB_BoundaryCase() {
        double[] result = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should be 1024", 1024, result.length);
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertEquals("Middle element should be approximately 8.7e-8", 8.759667154796094E-8d, result[512], 1.0E-9);
        assertEquals("Last element should be approximately 8.25e-8", 8.254840070367875E-8d, result[1023], 1.0E-9);
    }
    
    @Test
    public void testLoadLnMant_ExceptionalCase() {
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should be 1024", 1024, result.length);
        assertEquals("First sub-array length should be 2", 2, result[0].length);
        assertEquals("First element of first array should be 0.0", 0.0, result[0][0], 0.0);
        assertEquals("Second element of first array should be 0.0", 0.0, result[0][1], 0.0);
        assertEquals("First element of last array should be approximately 0.69", 0.6926587820053101d, result[1023][0], 0.000001);
    }
}