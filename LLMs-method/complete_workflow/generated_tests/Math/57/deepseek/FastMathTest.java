package org.apache.commons.math.util;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class FastMathTest {

    private static final double EPSILON = 1e-12;

    @Test
    public void testBasicTrigonometricFunctions() {
        // Test sin for normal cases
        assertEquals(0.0, FastMath.sin(0.0), EPSILON);
        assertEquals(1.0, FastMath.sin(Math.PI/2), EPSILON);
        assertEquals(0.0, FastMath.sin(Math.PI), EPSILON);
        assertEquals(-1.0, FastMath.sin(3*Math.PI/2), EPSILON);
        
        // Test cos for normal cases
        assertEquals(1.0, FastMath.cos(0.0), EPSILON);
        assertEquals(0.0, FastMath.cos(Math.PI/2), EPSILON);
        assertEquals(-1.0, FastMath.cos(Math.PI), EPSILON);
        assertEquals(0.0, FastMath.cos(3*Math.PI/2), EPSILON);
        
        // Test tan for normal cases
        assertEquals(0.0, FastMath.tan(0.0), EPSILON);
        assertEquals(Math.tan(Math.PI/4), FastMath.tan(Math.PI/4), EPSILON);
        assertTrue(Math.abs(FastMath.tan(Math.PI/3) - Math.sqrt(3)) < EPSILON);
    }

    @Test
    public void testHyperbolicFunctions() {
        // Test sinh for normal cases
        assertEquals(0.0, FastMath.sinh(0.0), EPSILON);
        assertEquals(1.1752011936438014, FastMath.sinh(1.0), EPSILON);
        assertEquals(-1.1752011936438014, FastMath.sinh(-1.0), EPSILON);
        
        // Test cosh for normal cases
        assertEquals(1.0, FastMath.cosh(0.0), EPSILON);
        assertEquals(1.5430806348152437, FastMath.cosh(1.0), EPSILON);
        assertEquals(1.5430806348152437, FastMath.cosh(-1.0), EPSILON);
        
        // Test tanh for normal cases
        assertEquals(0.0, FastMath.tanh(0.0), EPSILON);
        assertEquals(0.7615941559557649, FastMath.tanh(1.0), EPSILON);
        assertEquals(-0.7615941559557649, FastMath.tanh(-1.0), EPSILON);
        
        // Test boundary cases for tanh
        assertEquals(1.0, FastMath.tanh(100.0), EPSILON);
        assertEquals(-1.0, FastMath.tanh(-100.0), EPSILON);
    }

    @Test
    public void testInverseHyperbolicFunctions() {
        // Test asinh for normal cases
        assertEquals(0.0, FastMath.asinh(0.0), EPSILON);
        assertEquals(0.881373587019543, FastMath.asinh(1.0), EPSILON);
        assertEquals(-0.881373587019543, FastMath.asinh(-1.0), EPSILON);
        
        // Test acosh for normal cases
        assertEquals(0.0, FastMath.acosh(1.0), EPSILON);
        assertEquals(1.3169578969248166, FastMath.acosh(2.0), EPSILON);
        
        // Test atanh for normal cases
        assertEquals(0.0, FastMath.atanh(0.0), EPSILON);
        assertEquals(0.5493061443340549, FastMath.atanh(0.5), EPSILON);
        assertEquals(-0.5493061443340549, FastMath.atanh(-0.5), EPSILON);
        
        // Test boundary case for acosh
        assertTrue(Double.isNaN(FastMath.acosh(0.5))); // Input < 1 should return NaN
    }

    @Test
    public void testExponentialAndLogarithmicFunctions() {
        // Test exp for normal cases
        assertEquals(1.0, FastMath.exp(0.0), EPSILON);
        assertEquals(Math.E, FastMath.exp(1.0), EPSILON);
        assertEquals(0.36787944117144233, FastMath.exp(-1.0), EPSILON);
        
        // Test expm1 for normal cases
        assertEquals(0.0, FastMath.expm1(0.0), EPSILON);
        assertEquals(Math.E - 1.0, FastMath.expm1(1.0), EPSILON);
        assertEquals(-0.6321205588285577, FastMath.expm1(-1.0), EPSILON);
        
        // Test log for normal cases
        assertEquals(0.0, FastMath.log(1.0), EPSILON);
        assertEquals(1.0, FastMath.log(Math.E), EPSILON);
        assertEquals(2.302585092994046, FastMath.log(10.0), EPSILON);
        
        // Test log10 for normal cases
        assertEquals(0.0, FastMath.log10(1.0), EPSILON);
        assertEquals(1.0, FastMath.log10(10.0), EPSILON);
        assertEquals(2.0, FastMath.log10(100.0), EPSILON);
        
        // Test log1p for normal cases
        assertEquals(0.0, FastMath.log1p(0.0), EPSILON);
        assertEquals(0.6931471805599453, FastMath.log1p(1.0), EPSILON);
        assertEquals(-0.6931471805599453, FastMath.log1p(-0.5), EPSILON);
    }

    @Test
    public void testPowerAndRootFunctions() {
        // Test pow for normal cases
        assertEquals(1.0, FastMath.pow(5.0, 0.0), EPSILON);
        assertEquals(25.0, FastMath.pow(5.0, 2.0), EPSILON);
        assertEquals(0.04, FastMath.pow(5.0, -2.0), EPSILON);
        assertEquals(2.0, FastMath.pow(4.0, 0.5), EPSILON);
        
        // Test sqrt for normal cases
        assertEquals(0.0, FastMath.sqrt(0.0), EPSILON);
        assertEquals(1.0, FastMath.sqrt(1.0), EPSILON);
        assertEquals(2.0, FastMath.sqrt(4.0), EPSILON);
        assertEquals(3.0, FastMath.sqrt(9.0), EPSILON);
        
        // Test cbrt for normal cases
        assertEquals(0.0, FastMath.cbrt(0.0), EPSILON);
        assertEquals(1.0, FastMath.cbrt(1.0), EPSILON);
        assertEquals(2.0, FastMath.cbrt(8.0), EPSILON);
        assertEquals(3.0, FastMath.cbrt(27.0), EPSILON);
        assertEquals(-2.0, FastMath.cbrt(-8.0), EPSILON);
        
        // Test hypot for normal cases
        assertEquals(5.0, FastMath.hypot(3.0, 4.0), EPSILON);
        assertEquals(13.0, FastMath.hypot(5.0, 12.0), EPSILON);
        assertEquals(Math.sqrt(2.0), FastMath.hypot(1.0, 1.0), EPSILON);
    }

    @Test
    public void testSpecialCasesAndBoundaries() {
        // Test NaN handling
        assertTrue(Double.isNaN(FastMath.sin(Double.NaN)));
        assertTrue(Double.isNaN(FastMath.cos(Double.NaN)));
        assertTrue(Double.isNaN(FastMath.exp(Double.NaN)));
        assertTrue(Double.isNaN(FastMath.log(Double.NaN)));
        
        // Test infinity handling
        assertEquals(Double.POSITIVE_INFINITY, FastMath.exp(Double.POSITIVE_INFINITY), EPSILON);
        assertEquals(0.0, FastMath.exp(Double.NEGATIVE_INFINITY), EPSILON);
        assertTrue(Double.isNaN(FastMath.log(Double.NEGATIVE_INFINITY)));
        
        // Test signum
        assertEquals(1.0, FastMath.signum(5.0), EPSILON);
        assertEquals(-1.0, FastMath.signum(-5.0), EPSILON);
        assertEquals(0.0, FastMath.signum(0.0), EPSILON);
        assertEquals(-0.0, FastMath.signum(-0.0), EPSILON);
        assertTrue(Double.isNaN(FastMath.signum(Double.NaN)));
        
        // Test abs
        assertEquals(5.0, FastMath.abs(5.0), EPSILON);
        assertEquals(5.0, FastMath.abs(-5.0), EPSILON);
        assertEquals(0.0, FastMath.abs(0.0), EPSILON);
        assertEquals(0.0, FastMath.abs(-0.0), EPSILON);
        
        // Test min/max
        assertEquals(3.0, FastMath.min(3.0, 5.0), EPSILON);
        assertEquals(3.0, FastMath.min(5.0, 3.0), EPSILON);
        assertEquals(5.0, FastMath.max(3.0, 5.0), EPSILON);
        assertEquals(5.0, FastMath.max(5.0, 3.0), EPSILON);
        
        // Test floor/ceil/rint
        assertEquals(3.0, FastMath.floor(3.7), EPSILON);
        assertEquals(-4.0, FastMath.floor(-3.7), EPSILON);
        assertEquals(4.0, FastMath.ceil(3.2), EPSILON);
        assertEquals(-3.0, FastMath.ceil(-3.2), EPSILON);
        assertEquals(4.0, FastMath.rint(3.7), EPSILON);
        assertEquals(4.0, FastMath.rint(4.2), EPSILON);
        assertEquals(4.0, FastMath.rint(3.5), EPSILON); // Round to even
        assertEquals(2.0, FastMath.rint(2.5), EPSILON); // Round to even
    }

    @Test
    public void testAngleConversionFunctions() {
        // Test toRadians
        assertEquals(0.0, FastMath.toRadians(0.0), EPSILON);
        assertEquals(Math.PI, FastMath.toRadians(180.0), EPSILON);
        assertEquals(Math.PI/2, FastMath.toRadians(90.0), EPSILON);
        assertEquals(Math.PI/4, FastMath.toRadians(45.0), EPSILON);
        
        // Test toDegrees
        assertEquals(0.0, FastMath.toDegrees(0.0), EPSILON);
        assertEquals(180.0, FastMath.toDegrees(Math.PI), EPSILON);
        assertEquals(90.0, FastMath.toDegrees(Math.PI/2), EPSILON);
        assertEquals(45.0, FastMath.toDegrees(Math.PI/4), EPSILON);
        
        // Test inverse trigonometric functions
        assertEquals(0.0, FastMath.asin(0.0), EPSILON);
        assertEquals(Math.PI/2, FastMath.asin(1.0), EPSILON);
        assertEquals(-Math.PI/2, FastMath.asin(-1.0), EPSILON);
        
        assertEquals(Math.PI/2, FastMath.acos(0.0), EPSILON);
        assertEquals(0.0, FastMath.acos(1.0), EPSILON);
        assertEquals(Math.PI, FastMath.acos(-1.0), EPSILON);
        
        assertEquals(0.0, FastMath.atan(0.0), EPSILON);
        assertEquals(Math.PI/4, FastMath.atan(1.0), EPSILON);
        assertEquals(-Math.PI/4, FastMath.atan(-1.0), EPSILON);
        
        // Test atan2
        assertEquals(0.0, FastMath.atan2(0.0, 1.0), EPSILON);
        assertEquals(Math.PI/2, FastMath.atan2(1.0, 0.0), EPSILON);
        assertEquals(Math.PI, FastMath.atan2(0.0, -1.0), EPSILON);
        assertEquals(-Math.PI/2, FastMath.atan2(-1.0, 0.0), EPSILON);
        assertEquals(Math.PI/4, FastMath.atan2(1.0, 1.0), EPSILON);
    }

    @Test
    public void testNextAfterAndUlpFunctions() {
        // Test nextAfter
        assertEquals(Math.nextUp(1.0), FastMath.nextAfter(1.0, 2.0), EPSILON);
        assertEquals(Math.nextDown(1.0), FastMath.nextAfter(1.0, 0.0), EPSILON);
        assertEquals(1.0, FastMath.nextAfter(1.0, 1.0), EPSILON);
        
        // Test nextUp
        assertEquals(Math.nextUp(1.0), FastMath.nextUp(1.0), EPSILON);
        assertEquals(Math.nextUp(-1.0), FastMath.nextUp(-1.0), EPSILON);
        
        // Test ulp
        assertEquals(Math.ulp(1.0), FastMath.ulp(1.0), EPSILON);
        assertEquals(Math.ulp(0.0), FastMath.ulp(0.0), EPSILON);
        assertEquals(Math.ulp(Double.MAX_VALUE), FastMath.ulp(Double.MAX_VALUE), EPSILON);
        
        // Test scalb
        assertEquals(2.0, FastMath.scalb(1.0, 1), EPSILON);
        assertEquals(0.5, FastMath.scalb(1.0, -1), EPSILON);
        assertEquals(4.0, FastMath.scalb(1.0, 2), EPSILON);
        assertEquals(0.25, FastMath.scalb(1.0, -2), EPSILON);
    }

    @Test
    public void testCopySignAndGetExponent() {
        // Test copySign for double
        assertEquals(5.0, FastMath.copySign(5.0, 1.0), EPSILON);
        assertEquals(-5.0, FastMath.copySign(5.0, -1.0), EPSILON);
        assertEquals(5.0, FastMath.copySign(-5.0, 1.0), EPSILON);
        assertEquals(-5.0, FastMath.copySign(-5.0, -1.0), EPSILON);
        
        // Test copySign for float
        assertEquals(5.0f, FastMath.copySign(5.0f, 1.0f), EPSILON);
        assertEquals(-5.0f, FastMath.copySign(5.0f, -1.0f), EPSILON);
        assertEquals(5.0f, FastMath.copySign(-5.0f, 1.0f), EPSILON);
        assertEquals(-5.0f, FastMath.copySign(-5.0f, -1.0f), EPSILON);
        
        // Test getExponent for double
        assertEquals(0, FastMath.getExponent(1.0));
        assertEquals(1, FastMath.getExponent(2.0));
        assertEquals(2, FastMath.getExponent(4.0));
        assertEquals(-1, FastMath.getExponent(0.5));
        
        // Test getExponent for float
        assertEquals(0, FastMath.getExponent(1.0f));
        assertEquals(1, FastMath.getExponent(2.0f));
        assertEquals(2, FastMath.getExponent(4.0f));
        assertEquals(-1, FastMath.getExponent(0.5f));
    }
}