package org.apache.commons.math.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {

    @Test
    public void testSqrt() {
        // Test normal cases
        assertEquals(2.0, FastMath.sqrt(4.0), 1e-12);
        assertEquals(3.0, FastMath.sqrt(9.0), 1e-12);
        
        // Test edge cases
        assertEquals(0.0, FastMath.sqrt(0.0), 1e-12);
        assertTrue(Double.isNaN(FastMath.sqrt(-1.0)));
        
        // Test with large and small numbers
        assertEquals(1e150, FastMath.sqrt(1e300), 1e135);
        assertEquals(1e-150, FastMath.sqrt(1e-300), 1e-165);
    }

    @Test
    public void testHyperbolicFunctions() {
        // Test cosh
        assertEquals(1.0, FastMath.cosh(0.0), 1e-12);
        assertTrue(FastMath.cosh(1.0) > 1.0);
        
        // Test sinh
        assertEquals(0.0, FastMath.sinh(0.0), 1e-12);
        assertTrue(FastMath.sinh(1.0) > 0.0);
        assertTrue(FastMath.sinh(-1.0) < 0.0);
        
        // Test tanh
        assertEquals(0.0, FastMath.tanh(0.0), 1e-12);
        assertTrue(FastMath.tanh(1.0) > 0.0 && FastMath.tanh(1.0) < 1.0);
        assertEquals(-1.0, FastMath.tanh(-20.0), 1e-12);
        assertEquals(1.0, FastMath.tanh(20.0), 1e-12);
    }

    @Test
    public void testInverseHyperbolicFunctions() {
        // Test asinh
        assertEquals(0.0, FastMath.asinh(0.0), 1e-12);
        assertTrue(FastMath.asinh(1.0) > 0.0);
        assertEquals(-FastMath.asinh(1.0), FastMath.asinh(-1.0), 1e-12);
        
        // Test acosh
        assertEquals(0.0, FastMath.acosh(1.0), 1e-12);
        assertTrue(FastMath.acosh(2.0) > 0.0);
        assertTrue(Double.isNaN(FastMath.acosh(0.5))); // Domain error
        
        // Test atanh
        assertEquals(0.0, FastMath.atanh(0.0), 1e-12);
        assertTrue(FastMath.atanh(0.5) > 0.0);
        assertEquals(-FastMath.atanh(0.5), FastMath.atanh(-0.5), 1e-12);
        assertTrue(Double.isNaN(FastMath.atanh(2.0))); // Domain error
    }

    @Test
    public void testExponentialFunctions() {
        // Test exp
        assertEquals(1.0, FastMath.exp(0.0), 1e-12);
        assertEquals(Math.E, FastMath.exp(1.0), 1e-12);
        assertTrue(FastMath.exp(10.0) > 1.0);
        assertEquals(0.0, FastMath.exp(-1000.0), 1e-12); // Underflow to 0
        
        // Test expm1
        assertEquals(0.0, FastMath.expm1(0.0), 1e-12);
        assertEquals(Math.E - 1.0, FastMath.expm1(1.0), 1e-12);
        assertTrue(FastMath.expm1(0.001) > 0.0);
        assertEquals(-1.0, FastMath.expm1(-1000.0), 1e-12); // Approaches -1
    }

    @Test
    public void testLogarithmicFunctions() {
        // Test log
        assertEquals(0.0, FastMath.log(1.0), 1e-12);
        assertEquals(1.0, FastMath.log(Math.E), 1e-12);
        assertTrue(Double.isInfinite(FastMath.log(0.0)));
        assertTrue(FastMath.log(0.5) < 0.0);
        
        // Test log10
        assertEquals(0.0, FastMath.log10(1.0), 1e-12);
        assertEquals(1.0, FastMath.log10(10.0), 1e-12);
        assertEquals(2.0, FastMath.log10(100.0), 1e-12);
        
        // Test log1p
        assertEquals(0.0, FastMath.log1p(0.0), 1e-12);
        assertEquals(FastMath.log(1.1), FastMath.log1p(0.1), 1e-12);
        assertTrue(Double.isInfinite(FastMath.log1p(-1.0)));
    }

    @Test
    public void testPowerFunctions() {
        // Test pow
        assertEquals(1.0, FastMath.pow(5.0, 0.0), 1e-12);
        assertEquals(25.0, FastMath.pow(5.0, 2.0), 1e-12);
        assertEquals(0.04, FastMath.pow(5.0, -2.0), 1e-12);
        assertEquals(8.0, FastMath.pow(2.0, 3.0), 1e-12);
        
        // Test cbrt
        assertEquals(0.0, FastMath.cbrt(0.0), 1e-12);
        assertEquals(1.0, FastMath.cbrt(1.0), 1e-12);
        assertEquals(2.0, FastMath.cbrt(8.0), 1e-12);
        assertEquals(-2.0, FastMath.cbrt(-8.0), 1e-12);
    }

    @Test
    public void testTrigonometricFunctions() {
        // Test sin
        assertEquals(0.0, FastMath.sin(0.0), 1e-12);
        assertEquals(1.0, FastMath.sin(Math.PI/2), 1e-12);
        assertEquals(0.0, FastMath.sin(Math.PI), 1e-12);
        
        // Test cos
        assertEquals(1.0, FastMath.cos(0.0), 1e-12);
        assertEquals(0.0, FastMath.cos(Math.PI/2), 1e-12);
        assertEquals(-1.0, FastMath.cos(Math.PI), 1e-12);
        
        // Test tan
        assertEquals(0.0, FastMath.tan(0.0), 1e-12);
        assertTrue(Math.abs(FastMath.tan(Math.PI/4) - 1.0) < 1e-12);
    }

    @Test
    public void testInverseTrigonometricFunctions() {
        // Test asin
        assertEquals(0.0, FastMath.asin(0.0), 1e-12);
        assertEquals(Math.PI/2, FastMath.asin(1.0), 1e-12);
        assertEquals(-Math.PI/2, FastMath.asin(-1.0), 1e-12);
        assertTrue(Double.isNaN(FastMath.asin(2.0))); // Domain error
        
        // Test acos
        assertEquals(Math.PI/2, FastMath.acos(0.0), 1e-12);
        assertEquals(0.0, FastMath.acos(1.0), 1e-12);
        assertEquals(Math.PI, FastMath.acos(-1.0), 1e-12);
        
        // Test atan
        assertEquals(0.0, FastMath.atan(0.0), 1e-12);
        assertEquals(Math.PI/4, FastMath.atan(1.0), 1e-12);
        assertEquals(-Math.PI/4, FastMath.atan(-1.0), 1e-12);
        
        // Test atan2
        assertEquals(0.0, FastMath.atan2(0.0, 1.0), 1e-12);
        assertEquals(Math.PI/2, FastMath.atan2(1.0, 0.0), 1e-12);
        assertEquals(Math.PI, FastMath.atan2(0.0, -1.0), 1e-12);
        assertEquals(-Math.PI/2, FastMath.atan2(-1.0, 0.0), 1e-12);
    }

    @Test
    public void testSpecialFunctions() {
        // Test abs
        assertEquals(5.0, FastMath.abs(5.0), 1e-12);
        assertEquals(5.0, FastMath.abs(-5.0), 1e-12);
        assertEquals(0.0, FastMath.abs(0.0), 1e-12);
        assertEquals(5, FastMath.abs(-5));
        
        // Test signum
        assertEquals(1.0, FastMath.signum(5.0), 1e-12);
        assertEquals(-1.0, FastMath.signum(-5.0), 1e-12);
        assertEquals(0.0, FastMath.signum(0.0), 1e-12);
        assertEquals(1.0f, FastMath.signum(5.0f), 1e-12f);
        
        // Test min/max
        assertEquals(2.0, FastMath.min(2.0, 5.0), 1e-12);
        assertEquals(-5.0, FastMath.min(2.0, -5.0), 1e-12);
        assertEquals(5.0, FastMath.max(2.0, 5.0), 1e-12);
        assertEquals(2.0, FastMath.max(2.0, -5.0), 1e-12);
        
        // Test floor/ceil/rint
        assertEquals(2.0, FastMath.floor(2.7), 1e-12);
        assertEquals(2.0, FastMath.floor(2.0), 1e-12);
        assertEquals(3.0, FastMath.ceil(2.3), 1e-12);
        assertEquals(2.0, FastMath.ceil(2.0), 1e-12);
        assertEquals(2.0, FastMath.rint(2.3), 1e-12);
        assertEquals(2.0, FastMath.rint(1.5), 1e-12); // Round to even
        assertEquals(4.0, FastMath.rint(3.5), 1e-12); // Round to even
    }

    @Test
    public void testConstants() {
        // Test PI and E constants
        assertTrue(FastMath.PI > 3.14159 && FastMath.PI < 3.14160);
        assertTrue(FastMath.E > 2.71828 && FastMath.E < 2.71829);
        
        // Test conversion functions
        assertEquals(Math.PI/4, FastMath.toRadians(45.0), 1e-12);
        assertEquals(180.0, FastMath.toDegrees(Math.PI), 1e-12);
        assertEquals(0.0, FastMath.toRadians(0.0), 1e-12);
        assertEquals(0.0, FastMath.toDegrees(0.0), 1e-12);
    }

    @Test
    public void testEdgeCasesAndSpecialValues() {
        // Test NaN handling
        assertTrue(Double.isNaN(FastMath.sqrt(Double.NaN)));
        assertTrue(Double.isNaN(FastMath.log(Double.NaN)));
        assertTrue(Double.isNaN(FastMath.sin(Double.NaN)));
        
        // Test infinity handling
        assertTrue(Double.isInfinite(FastMath.exp(1000.0)));
        assertEquals(Double.POSITIVE_INFINITY, FastMath.log(Double.POSITIVE_INFINITY), 1e-12);
        assertTrue(Double.isNaN(FastMath.sin(Double.POSITIVE_INFINITY)));
        
        // Test signed zero
        assertEquals(-0.0, FastMath.sin(-0.0), 1e-12);
        assertEquals(0.0, FastMath.sin(0.0), 1e-12);
        
        // Test ulp
        assertTrue(FastMath.ulp(1.0) > 0.0);
        assertEquals(Double.MIN_VALUE, FastMath.ulp(0.0), 1e-12);
        
        // Test nextAfter
        assertEquals(Math.nextUp(1.0), FastMath.nextAfter(1.0, 2.0), 1e-12);
        assertEquals(Math.nextAfter(1.0, 0.0), FastMath.nextAfter(1.0, 0.0), 1e-12);
        
        // Test scalb
        assertEquals(2.0, FastMath.scalb(1.0, 1), 1e-12);
        assertEquals(0.5, FastMath.scalb(1.0, -1), 1e-12);
        assertEquals(4.0f, FastMath.scalb(1.0f, 2), 1e-12f);
    }
}