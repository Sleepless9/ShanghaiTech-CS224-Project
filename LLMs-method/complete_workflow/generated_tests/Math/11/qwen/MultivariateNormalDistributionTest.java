package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testLoadExpIntA_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpIntA();
        
        assertNotNull("Result array should not be null", result);
        assertEquals("Array length should match original", 1025, result.length);
        
        // Test first few values
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertTrue("Second element should be NaN", Double.isNaN(result[1]));
        
        // Test middle value (around index 512)
        assertEquals("Middle value should be approximately 1.0", 1.0, result[512], 1e-10);
        
        // Test last few values
        assertEquals("Last element should be NaN", true, Double.isNaN(result[result.length - 1]));
    }

    @Test
    public void testLoadExpIntB_BoundaryCases() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        assertNotNull("Result array should not be null", result);
        assertEquals("Array length should match original", 1025, result.length);
        
        // Test boundary values
        assertEquals("First element should be 0.0", 0.0, result[0], 0.0);
        assertTrue("Values at beginning should be NaN", Double.isNaN(result[1]) && Double.isNaN(result[2]));
        
        // Test some non-NaN values in the middle range
        int expIntTableMaxIndex = 512; // Based on typical FastMath implementation
        int testIndex = expIntTableMaxIndex + 10;
        assertFalse("Value at test index should not be NaN", Double.isNaN(result[testIndex]));
        assertTrue("Value at test index should be positive", result[testIndex] > 0.0);
        
        // Test end values
        assertTrue("Last values should be NaN", 
                  Double.isNaN(result[result.length - 1]) && 
                  Double.isNaN(result[result.length - 2]));
    }

    @Test
    public void testLoadExpFracA_Comprehensive() {
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        assertNotNull("Result array should not be null", result);
        assertEquals("Array length should be 1024", 1024, result.length);
        
        // Test known values from the array
        assertEquals("First element should be 1.0", 1.0, result[0], 0.0);
        assertEquals("Second element should be ~1.000977", 1.0009770393371582d, result[1], 1e-15);
        assertEquals("Last element should be ~2.71828", 2.7182817459106445d, result[1023], 1e-15);
        
        // Test progression - values should be increasing
        assertTrue("Array values should be monotonically increasing",
                   result[512] > result[256] && result[768] > result[512]);
    }

    @Test
    public void testLoadExpFracB_ConsistencyWithA() {
        double[] fracA = FastMathLiteralArrays.loadExpFracA();
        double[] fracB = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("Both arrays should not be null", fracA);
        assertNotNull("Both arrays should not be null", fracB);
        assertEquals("Both arrays should have same length", fracA.length, fracB.length);
        
        // Test that some combined values are reasonable
        // For example, exp(0) = 1.0, so at index 0: A[0] + B[0] = 1.0 + 0.0 = 1.0
        assertEquals("exp(0) should be 1.0", 1.0, fracA[0] + fracB[0], 1e-10);
        
        // Test another point (index 100)
        double expApprox = fracA[100] + fracB[100];
        assertTrue("exp(100/1024) should be greater than 1.0", expApprox > 1.0);
        assertTrue("exp(100/1024) should be less than 2.0", expApprox < 2.0);
    }

    @Test
    public void testLoadLnMant_StructureAndContent() {
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Result array should not be null", result);
        assertEquals("Array should have 1024 rows", 1024, result.length);
        
        // Test structure of each row
        for (int i = 0; i < 10; i++) {
            assertNotNull("Row should not be null", result[i]);
            assertEquals("Each row should have 2 elements", 2, result[i].length);
        }
        
        // Test specific values
        assertEquals("First element of first row should be 0.0", 0.0, result[0][0], 0.0);
        assertEquals("Second element of first row should be 0.0", 0.0, result[0][1], 0.0);
        
        // Test a non-zero entry
        assertTrue("Second entry should have a small negative value", result[1][1] < 0.0);
        assertEquals("Second entry magnitude should be small", Math.abs(result[1][1]), 3.903230345984362E-11d, 1e-15);
    }
}