package org.apache.commons.math.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class FastMathTest {

    @Test
    public void testSin() {
        // Normal cases
        assertEquals(0.0, FastMath.sin(0.0), 1e-15);
        assertEquals(1.0, FastMath.sin(Math.PI / 2), 1e-15);
        assertEquals(0.0, FastMath.sin(Math.PI), 1e-15);
        
        // Boundary cases
        assertEquals(-1.0, FastMath.sin(3 * Math.PI / 2), 1e-15);
        assertTrue(Double.isNaN(FastMath.sin(Double.NaN)));
        assertEquals(0.0, FastMath.sin(Double.POSITIVE_INFINITY), 0.0);
    }
    
    @Test
    public void testCos() {
        // Normal cases
        assertEquals(1.0, FastMath.cos(0.0), 1e-15);
        assertEquals(0.0, FastMath.cos(Math.PI / 2), 1e-15);
        assertEquals(-1.0, FastMath.cos(Math.PI), 1e-15);
        
        // Boundary cases
        assertEquals(0.0, FastMath.cos(3 * Math.PI / 2), 1e-15);
        assertTrue(Double.isNaN(FastMath.cos(Double.NaN)));
        assertEquals(1.0, FastMath.cos(Double.POSITIVE_INFINITY), 1.0);
    }
    
    @Test
    public void testTan() {
        // Normal cases
        assertEquals(0.0, FastMath.tan(0.0), 1e-15);
        assertEquals(1.0, FastMath.tan(Math.PI / 4), 1e-15);
        
        // Exception cases
        assertTrue(Double.isInfinite(FastMath.tan(Math.PI / 2)));
        assertTrue(Double.isNaN(FastMath.tan(Double.NaN)));
        assertTrue(Double.isNaN(FastMath.tan(Double.POSITIVE_INFINITY)));
        assertEquals(0.0, FastMath.tan(Math.PI), 1e-15);
    }
    
    @Test
    public void testAsinAcos() {
        // Normal cases
        assertEquals(Math.PI / 2, FastMath.asin(1.0), 1e-15);
        assertEquals(0.0, FastMath.asin(0.0), 1e-15);
        assertEquals(Math.PI / 2, FastMath.acos(0.0), 1e-15);
        assertEquals(0.0, FastMath.acos(1.0), 1e-15);
        
        // Exception cases
        assertTrue(Double.isNaN(FastMath.asin(2.0)));
        assertTrue(Double.isNaN(FastMath.asin(-2.0)));
        assertTrue(Double.isNaN(FastMath.acos(2.0)));
        assertTrue(Double.isNaN(FastMath.acos(-2.0)));
        assertTrue(Double.isNaN(FastMath.asin(Double.NaN)));
    }
    
    @Test
    public void testExpLog() {
        // Normal cases
        assertEquals(1.0, FastMath.exp(0.0), 1e-15);
        assertEquals(Math.E, FastMath.exp(1.0), 1e-15);
        assertEquals(0.0, FastMath.exp(Double.NEGATIVE_INFINITY), 0.0);
        
        // Log tests
        assertEquals(0.0, FastMath.log(1.0), 1e-15);
        assertEquals(1.0, FastMath.log(Math.E), 1e-15);
        assertEquals(Double.NEGATIVE_INFINITY, FastMath.log(0.0), 0.0);
        assertTrue(Double.isNaN(FastMath.log(-1.0)));
    }
}