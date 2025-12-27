package org.apache.commons.math.util;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ComplexTest {

    private static final double EPSILON = 1e-10;

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
    }

    @Test
    public void testHyperbolicFunctions() {
        // Test cosh for normal cases
        assertEquals(1.0, FastMath.cosh(0.0), EPSILON);
        assertTrue(FastMath.cosh(1.0) > 1.0);
        assertEquals(FastMath.cosh(-1.0), FastMath.cosh(1.0), EPSILON);
        
        // Test sinh for normal cases
        assertEquals(0.0, FastMath.sinh(0.0), EPSILON);
        assertTrue(FastMath.sinh(1.0) > 0.0);
        assertEquals(-FastMath.sinh(1.0), FastMath.sinh(-1.0), EPSILON);
        
        // Test tanh for normal cases
        assertEquals(0.0, FastMath.tanh(0.0), EPSILON);
        assertTrue(FastMath.tanh(1.0) > 0.0 && FastMath.tanh(1.0) < 1.0);
        assertEquals(-FastMath.tanh(1.0), FastMath.tanh(-1.0), EPSILON);
    }

    @Test
    public void testInverseHyperbolicFunctions() {
        // Test asinh for normal cases
        assertEquals(0.0, FastMath.asinh(0.0), EPSILON);
        assertTrue(FastMath.asinh(1.0) > 0.0);
        assertEquals(-FastMath.asinh(1.0), FastMath.asinh(-1.0), EPSILON);
        
        // Test acosh for normal cases (domain: x >= 1)
        assertEquals(0.0, FastMath.acosh(1.0), EPSILON);
        assertTrue(FastMath.acosh(2.0) > 0.0);
        
        // Test atanh for normal cases (domain: -1 < x < 1)
        assertEquals(0.0, FastMath.atanh(0.0), EPSILON);
        assertTrue(FastMath.atanh(0.5) > 0.0);
        assertEquals(-FastMath.atanh(0.5), FastMath.atanh(-0.5), EPSILON);
    }

    @Test
    public void testExponentialAndLogarithmicFunctions() {
        // Test exp for normal cases
        assertEquals(1.0, FastMath.exp(0.0), EPSILON);
        assertTrue(FastMath.exp(1.0) > 2.7 && FastMath.exp(1.0) < 2.8);
        assertEquals(1.0/FastMath.exp(1.0), FastMath.exp(-1.0), EPSILON);
        
        // Test log for normal cases
        assertEquals(0.0, FastMath.log(1.0), EPSILON);
        assertTrue(FastMath.log(2.0) > 0.69 && FastMath.log(2.0) < 0.70);
        assertEquals(FastMath.log(0.5), -FastMath.log(2.0), EPSILON);
        
        // Test log10 for normal cases
        assertEquals(0.0, FastMath.log10(1.0), EPSILON);
        assertEquals(1.0, FastMath.log10(10.0), EPSILON);
        assertEquals(2.0, FastMath.log10(100.0), EPSILON);
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
        assertEquals(2.0, FastMath.sqrt(4.0), EPSILON);
        assertEquals(3.0, FastMath.sqrt(9.0), EPSILON);
        
        // Test cbrt for normal cases
        assertEquals(0.0, FastMath.cbrt(0.0), EPSILON);
        assertEquals(2.0, FastMath.cbrt(8.0), EPSILON);
        assertEquals(3.0, FastMath.cbrt(27.0), EPSILON);
        assertEquals(-2.0, FastMath.cbrt(-8.0), EPSILON);
    }

    @Test
    public void testSpecialCasesAndBoundaries() {
        // Test NaN handling
        assertTrue(Double.isNaN(FastMath.sin(Double.NaN)));
        assertTrue(Double.isNaN(FastMath.cos(Double.NaN)));
        assertTrue(Double.isNaN(FastMath.log(Double.NaN)));
        assertTrue(Double.isNaN(FastMath.exp(Double.NaN)));
        
        // Test infinity handling
        assertEquals(Double.NaN, FastMath.sin(Double.POSITIVE_INFINITY), EPSILON);
        assertEquals(Double.NaN, FastMath.cos(Double.POSITIVE_INFINITY), EPSILON);
        assertEquals(Double.POSITIVE_INFINITY, FastMath.log(Double.POSITIVE_INFINITY), EPSILON);
        assertEquals(Double.POSITIVE_INFINITY, FastMath.exp(Double.POSITIVE_INFINITY), EPSILON);
        assertEquals(0.0, FastMath.exp(Double.NEGATIVE_INFINITY), EPSILON);
        
        // Test signum function
        assertEquals(1.0, FastMath.signum(5.0), EPSILON);
        assertEquals(-1.0, FastMath.signum(-5.0), EPSILON);
        assertEquals(0.0, FastMath.signum(0.0), EPSILON);
        assertEquals(-0.0, FastMath.signum(-0.0), EPSILON);
        assertTrue(Double.isNaN(FastMath.signum(Double.NaN)));
        
        // Test abs function
        assertEquals(5.0, FastMath.abs(5.0), EPSILON);
        assertEquals(5.0, FastMath.abs(-5.0), EPSILON);
        assertEquals(0.0, FastMath.abs(0.0), EPSILON);
        assertEquals(0.0, FastMath.abs(-0.0), EPSILON);
    }

    @Test
    public void testInverseTrigonometricFunctions() {
        // Test asin for normal cases
        assertEquals(0.0, FastMath.asin(0.0), EPSILON);
        assertEquals(Math.PI/2, FastMath.asin(1.0), EPSILON);
        assertEquals(-Math.PI/2, FastMath.asin(-1.0), EPSILON);
        assertEquals(Math.PI/6, FastMath.asin(0.5), EPSILON);
        
        // Test acos for normal cases
        assertEquals(Math.PI/2, FastMath.acos(0.0), EPSILON);
        assertEquals(0.0, FastMath.acos(1.0), EPSILON);
        assertEquals(Math.PI, FastMath.acos(-1.0), EPSILON);
        assertEquals(Math.PI/3, FastMath.acos(0.5), EPSILON);
        
        // Test atan for normal cases
        assertEquals(0.0, FastMath.atan(0.0), EPSILON);
        assertEquals(Math.PI/4, FastMath.atan(1.0), EPSILON);
        assertEquals(-Math.PI/4, FastMath.atan(-1.0), EPSILON);
        
        // Test atan2 for normal cases
        assertEquals(0.0, FastMath.atan2(0.0, 1.0), EPSILON);
        assertEquals(Math.PI/2, FastMath.atan2(1.0, 0.0), EPSILON);
        assertEquals(Math.PI, FastMath.atan2(0.0, -1.0), EPSILON);
        assertEquals(-Math.PI/2, FastMath.atan2(-1.0, 0.0), EPSILON);
        assertEquals(Math.PI/4, FastMath.atan2(1.0, 1.0), EPSILON);
    }

    @Test
    public void testMinMaxFunctions() {
        // Test min for double
        assertEquals(2.0, FastMath.min(2.0, 3.0), EPSILON);
        assertEquals(2.0, FastMath.min(3.0, 2.0), EPSILON);
        assertEquals(-3.0, FastMath.min(-2.0, -3.0), EPSILON);
        assertTrue(Double.isNaN(FastMath.min(Double.NaN, 2.0)));
        assertTrue(Double.isNaN(FastMath.min(2.0, Double.NaN)));
        
        // Test max for double
        assertEquals(3.0, FastMath.max(2.0, 3.0), EPSILON);
        assertEquals(3.0, FastMath.max(3.0, 2.0), EPSILON);
        assertEquals(-2.0, FastMath.max(-2.0, -3.0), EPSILON);
        assertTrue(Double.isNaN(FastMath.max(Double.NaN, 2.0)));
        assertTrue(Double.isNaN(FastMath.max(2.0, Double.NaN)));
        
        // Test min for float
        assertEquals(2.0f, FastMath.min(2.0f, 3.0f), EPSILON);
        assertEquals(2.0f, FastMath.min(3.0f, 2.0f), EPSILON);
        
        // Test max for float
        assertEquals(3.0f, FastMath.max(2.0f, 3.0f), EPSILON);
        assertEquals(3.0f, FastMath.max(3.0f, 2.0f), EPSILON);
        
        // Test min for int
        assertEquals(2, FastMath.min(2, 3));
        assertEquals(2, FastMath.min(3, 2));
        
        // Test max for int
        assertEquals(3, FastMath.max(2, 3));
        assertEquals(3, FastMath.max(3, 2));
    }

    @Test
    public void testRoundingAndConversionFunctions() {
        // Test floor
        assertEquals(2.0, FastMath.floor(2.3), EPSILON);
        assertEquals(2.0, FastMath.floor(2.7), EPSILON);
        assertEquals(-3.0, FastMath.floor(-2.3), EPSILON);
        assertEquals(-3.0, FastMath.floor(-2.7), EPSILON);
        
        // Test ceil
        assertEquals(3.0, FastMath.ceil(2.3), EPSILON);
        assertEquals(3.0, FastMath.ceil(2.7), EPSILON);
        assertEquals(-2.0, FastMath.ceil(-2.3), EPSILON);
        assertEquals(-2.0, FastMath.ceil(-2.7), EPSILON);
        
        // Test rint
        assertEquals(2.0, FastMath.rint(2.3), EPSILON);
        assertEquals(3.0, FastMath.rint(2.7), EPSILON);
        assertEquals(2.0, FastMath.rint(2.5), EPSILON); // round to even
        assertEquals(4.0, FastMath.rint(3.5), EPSILON); // round to even
        
        // Test round for double
        assertEquals(2L, FastMath.round(2.3));
        assertEquals(3L, FastMath.round(2.7));
        assertEquals(-2L, FastMath.round(-2.3));
        assertEquals(-3L, FastMath.round(-2.7));
        
        // Test round for float
        assertEquals(2, FastMath.round(2.3f));
        assertEquals(3, FastMath.round(2.7f));
        assertEquals(-2, FastMath.round(-2.3f));
        assertEquals(-3, FastMath.round(-2.7f));
        
        // Test toDegrees and toRadians
        assertEquals(180.0, FastMath.toDegrees(Math.PI), EPSILON);
        assertEquals(360.0, FastMath.toDegrees(2*Math.PI), EPSILON);
        assertEquals(Math.PI, FastMath.toRadians(180.0), EPSILON);
        assertEquals(2*Math.PI, FastMath.toRadians(360.0), EPSILON);
    }
}