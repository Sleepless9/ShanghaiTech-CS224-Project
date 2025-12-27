package org.apache.commons.math.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class GaussianFitterTest {

    @Test
    public void testSinhNormalCases() {
        assertEquals(0.0, FastMath.sinh(0.0), 1e-15);
        assertEquals(Math.sinh(1.0), FastMath.sinh(1.0), 1e-15);
        assertEquals(Math.sinh(-1.0), FastMath.sinh(-1.0), 1e-15);
        
        double largeValue = 2.0;
        double expected = Math.exp(largeValue) / 2.0;
        assertEquals(expected, FastMath.sinh(largeValue), 1e-15);
    }

    @Test
    public void testCoshNormalCases() {
        assertEquals(1.0, FastMath.cosh(0.0), 1e-15);
        assertEquals(Math.cosh(1.0), FastMath.cosh(1.0), 1e-15);
        assertEquals(Math.cosh(-1.0), FastMath.cosh(-1.0), 1e-15);
        
        double largeValue = 2.0;
        double expected = Math.exp(largeValue) / 2.0;
        assertEquals(expected, FastMath.cosh(largeValue), 1e-15);
    }

    @Test
    public void testTanhNormalCases() {
        assertEquals(0.0, FastMath.tanh(0.0), 1e-15);
        assertEquals(1.0, FastMath.tanh(20.0), 1e-15);
        assertEquals(-1.0, FastMath.tanh(-20.0), 1e-15);
        assertEquals(Math.tanh(1.0), FastMath.tanh(1.0), 1e-15);
        
        assertTrue(FastMath.tanh(0.5) > 0.0);
        assertTrue(FastMath.tanh(-0.5) < 0.0);
    }
    
    @Test
    public void testAsinhBoundaryCases() {
        assertEquals(0.0, FastMath.asinh(0.0), 1e-15);
        assertEquals(Double.NaN, FastMath.asinh(Double.NaN), 0.0);
        
        double x = 1.0;
        double expected = Math.log(Math.sqrt(x * x + 1) + x);
        assertEquals(expected, FastMath.asinh(x), 1e-15);
        
        double negX = -1.0;
        assertEquals(-expected, FastMath.asinh(negX), 1e-15);
    }
    
    @Test
    public void testAtanhExceptionalCases() {
        assertEquals(0.0, FastMath.atanh(0.0), 1e-15);
        assertEquals(Double.NaN, FastMath.atanh(Double.NaN), 0.0);
        assertEquals(Double.NaN, FastMath.atanh(2.0), 0.0);
        assertEquals(Double.NaN, FastMath.atanh(-2.0), 0.0);
        
        double smallValue = 0.01;
        double expected = 0.5 * Math.log((1 + smallValue) / (1 - smallValue));
        assertEquals(expected, FastMath.atanh(smallValue), 1e-15);
        
        double negSmallValue = -0.01;
        assertEquals(-expected, FastMath.atanh(negSmallValue), 1e-15);
    }
}