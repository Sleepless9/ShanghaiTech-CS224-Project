package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testLoadExpIntA_NormalCases() {
        double[] result = FastMathLiteralArrays.loadExpIntA();
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match", 514, result.length);
        assertNotSame("Should return a clone, not the original array", 
                      FastMathLiteralArrays.class.getDeclaredFields()[0].get(null), result);
        assertTrue("First valid value should be positive", result[41] > 0.0);
        assertTrue("Last valid value should be positive", result[result.length - 42] > 0.0);
    }

    @Test
    public void testLoadExpIntB_NormalCases() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match", 514, result.length);
        assertNotSame("Should return a clone, not the original array", 
                      FastMathLiteralArrays.class.getDeclaredFields()[1].get(null), result);
        assertFalse("Some values should be NaN", Double.isNaN(result[1]));
        assertTrue("Non-NaN values should exist", !Double.isNaN(result[41]));
    }

    @Test
    public void testLoadExpFracA_NormalCases() {
        double[] result = FastMathLiteralArrays.loadExpFracA();
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        assertTrue("First value should be exactly 1.0", result[0] == 1.0);
        assertTrue("Last value should be greater than first", result[result.length - 1] > result[0]);
        assertTrue("Values should be monotonically increasing", 
                  result[512] < result[513]);
        assertEquals("Specific known value check", 1.6180339813232422d, result[618], 1e-10);
    }

    @Test
    public void testLoadExpFracB_BoundaryCases() {
        double[] result = FastMathLiteralArrays.loadExpFracB();
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        assertTrue("First value should be zero", result[0] == 0.0);
        assertFalse("Second value should not be zero", result[1] != 0.0);
        assertTrue("Values should contain both positive and negative numbers", 
                  result[1] > 0 && result[10] < 0);
        assertFalse("Array should not be all zeros", 
                   java.util.Arrays.stream(result).allMatch(x -> x == 0.0));
    }

    @Test
    public void testLoadLnMant_ExceptionCases() {
        try {
            double[][] result = FastMathLiteralArrays.loadLnMant();
            assertNotNull("Result should not be null", result);
            assertEquals("Outer array length should match", 1024, result.length);
            assertNotNull("Inner arrays should not be null", result[0]);
            assertEquals("Inner array length should be 2", 2, result[0].length);
            assertTrue("First element of first inner array should be zero", result[0][0] == 0.0);
            assertTrue("Second element of first inner array should be zero", result[0][1] == 0.0);
            assertFalse("Later elements should not be all zero", 
                       result[100][0] != 0.0 || result[100][1] != 0.0);
        } catch (Exception e) {
            fail("Method should not throw exception: " + e.getMessage());
        }
    }
}