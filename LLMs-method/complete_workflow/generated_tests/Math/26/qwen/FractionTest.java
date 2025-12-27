package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testLoadExpIntA() {
        double[] result = FastMathLiteralArrays.loadExpIntA();
        
        assertNotNull("Result should not be null", result);
        assertNotSame("Should return a clone, not the original array", 
                     FastMathLiteralArrays.class.getDeclaredFields()[0].get(null), result);
        assertEquals("Array length should match", 1024, result.length);
        
        // Test some known values
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertEquals("Last non-NaN element before NaNs", 
                    2.7182817459106445E0d, result[512], 1e-15);
        assertTrue("Element after last valid should be NaN", Double.isNaN(result[1023]));
    }

    @Test
    public void testLoadExpIntB() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        assertNotNull("Result should not be null", result);
        assertNotSame("Should return a clone, not the original array", 
                     FastMathLiteralArrays.class.getDeclaredFields()[1].get(null), result);
        assertEquals("Array length should match", 1024, result.length);
        
        // Test some known values
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertEquals("Corresponding B value for e^1", 
                    0.0d, result[512], 1e-15);
        assertTrue("Element after last valid should be NaN", Double.isNaN(result[1023]));
        
        // Verify that at least one non-zero, non-NaN value exists
        boolean hasValidValues = false;
        for (int i = 1; i < result.length - 50; i++) {
            if (!Double.isNaN(result[i]) && result[i] != 0.0) {
                hasValidValues = true;
                break;
            }
        }
        assertTrue("Should contain valid non-zero, non-NaN values", hasValidValues);
    }

    @Test
    public void testLoadExpFracA() {
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        assertNotNull("Result should not be null", result);
        assertNotSame("Should return a clone, not the original array", 
                     FastMathLiteralArrays.class.getDeclaredFields()[2].get(null), result);
        assertEquals("Array length should match", 1024, result.length);
        
        // Test boundary values
        assertEquals("First element should be 1.0", 1.0, result[0], 0.0);
        assertEquals("Last element should be e^(1023/1024)", 
                    2.7182817459106445E0d, result[1023], 1e-15);
        
        // Test monotonicity
        for (int i = 1; i < result.length; i++) {
            if (!Double.isNaN(result[i]) && !Double.isNaN(result[i-1])) {
                assertTrue("Array should be monotonically increasing", 
                         result[i] >= result[i-1]);
            }
        }
    }

    @Test
    public void testLoadExpFracB() {
        double[] result = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("Result should not be null", result);
        assertNotSame("Should return a clone, not the original array", 
                     FastMathLiteralArrays.class.getDeclaredFields()[3].get(null), result);
        assertEquals("Array length should match", 1024, result.length);
        
        // Test boundary values
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertEquals("Last element should be very small correction", 
                    8.254840070367875E-8d, result[1023], 1e-15);
        
        // Test specific known value
        assertEquals("Second element should match expected", 
                    1.552583321178453E-10d, result[1], 1e-15);
        
        // Verify array contains both positive and negative values
        boolean hasPositive = false;
        boolean hasNegative = false;
        for (int i = 1; i < result.length - 1; i++) {
            if (!Double.isNaN(result[i])) {
                if (result[i] > 0.0) hasPositive = true;
                if (result[i] < 0.0) hasNegative = true;
            }
        }
        assertTrue("Should contain positive values", hasPositive);
        assertTrue("Should contain negative values", hasNegative);
    }

    @Test
    public void testLoadLnMant() {
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Result should not be null", result);
        assertNotSame("Should return a clone, not the original array", 
                     FastMathLiteralArrays.class.getDeclaredFields()[4].get(null), result);
        assertEquals("Array length should match", 1024, result.length);
        
        // Test first few elements
        assertNotNull("First subarray should not be null", result[0]);
        assertEquals("First subarray length should be 2", 2, result[0].length);
        assertEquals("First element of first subarray should be 0.0", 0.0, result[0][0], 0.0);
        assertEquals("Second element of first subarray should be 0.0", 0.0, result[0][1], 0.0);
        
        // Test a specific known value
        assertEquals("First element of index 1 should match", 
                    9.760860120877624E-4d, result[1][0], 1e-15);
        assertEquals("Second element of index 1 should match", 
                    -3.903230345984362E-11d, result[1][1], 1e-15);
        
        // Verify structure of all subarrays
        for (int i = 0; i < result.length; i++) {
            assertNotNull("Subarray at index " + i + " should not be null", result[i]);
            assertEquals("Subarray at index " + i + " should have length 2", 2, result[i].length);
        }
    }
}