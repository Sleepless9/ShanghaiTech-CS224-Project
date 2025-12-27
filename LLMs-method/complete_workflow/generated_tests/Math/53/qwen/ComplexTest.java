package org.apache.commons.math.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class ComplexTest {

    @Test
    public void testSinhNormalCases() {
        assertEquals(0.0, FastMath.sinh(0.0), 1e-15);
        assertEquals(Math.sinh(1.0), FastMath.sinh(1.0), 1e-15);
        assertEquals(Math.sinh(-1.0), FastMath.sinh(-1.0), 1e-15);
        
        double largeValue = Math.sinh(2.0);
        assertEquals(largeValue, FastMath.sinh(2.0), 1e-15);
        assertEquals(-largeValue, FastMath.sinh(-2.0), 1e-15);
    }

    @Test
    public void testSinhBoundaryCases() {
        assertEquals(Double.POSITIVE_INFINITY, FastMath.sinh(21.0), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, FastMath.sinh(-21.0), 0.0);
        
        assertTrue(Double.isNaN(FastMath.sinh(Double.NaN)));
        assertEquals(0.0, FastMath.sinh(0.0), 0.0);
        assertEquals(-0.0, FastMath.sinh(-0.0), 0.0);
    }

    @Test
    public void testAsinhNormalCases() {
        assertEquals(0.0, FastMath.asinh(0.0), 1e-15);
        assertEquals(Math.log(1.0 + Math.sqrt(2.0)), FastMath.asinh(1.0), 1e-15);
        assertEquals(-Math.log(1.0 + Math.sqrt(2.0)), FastMath.asinh(-1.0), 1e-15);
        
        double x = 0.5;
        assertEquals(Math.log(x + Math.sqrt(x*x + 1)), FastMath.asinh(x), 1e-15);
    }

    @Test
    public void testAsinhBoundaryCases() {
        assertEquals(Double.POSITIVE_INFINITY, FastMath.asinh(Double.POSITIVE_INFINITY), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, FastMath.asinh(Double.NEGATIVE_INFINITY), 0.0);
        
        assertTrue(Double.isNaN(FastMath.asinh(Double.NaN)));
        assertEquals(0.0, FastMath.asinh(0.0), 0.0);
        assertEquals(-0.0, FastMath.asinh(-0.0), 0.0);
    }

    @Test
    public void testConstantsAccuracy() {
        assertEquals(Math.PI, FastMath.PI, 1e-9);
        assertEquals(Math.E, FastMath.E, 1e-9);
        
        assertTrue(FastMath.PI > 3.141592653589793);
        assertTrue(FastMath.E > 2.718281828459045);
    }
}