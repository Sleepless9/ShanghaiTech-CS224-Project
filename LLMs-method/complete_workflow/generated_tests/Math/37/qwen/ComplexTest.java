package org.apache.commons.math.util;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testLoadExpIntA_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpIntA();
        
        assertNotNull("Result should not be null", result);
        assertThat("Array length should match original", result.length, is(FastMathLiteralArrays.EXP_INT_A.length));
        assertNotSame("Should return a clone, not the same array", result, FastMathLiteralArrays.EXP_INT_A);
        
        // Check some known values from beginning and end
        assertEquals("First element should be 0.0", 0.0, result[0], 1e-15);
        assertEquals("Last non-NaN element at index 279", 1.8184484008569103E67d, result[279], 1e-15);
    }
    
    @Test
    public void testLoadExpIntB_BoundaryCases() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        assertNotNull("Result should not be null", result);
        assertTrue("Array should contain NaN values", Double.isNaN(result[1]));
        assertTrue("Array should contain NaN values at end", Double.isNaN(result[result.length - 1]));
        
        // Verify specific known values
        assertEquals("Element at index 279 should be correct", 1.1308138082184872E68d, result[279], 1e-15);
        assertEquals("Element at index 280 should be correct", -1.1358681362673942E68d, result[280], 1e-15);
    }
    
    @Test
    public void testLoadExpFracA_And_ExpFracB_Consistency() {
        double[] fracA = FastMathLiteralArrays.loadExpFracA();
        double[] fracB = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("FracA result should not be null", fracA);
        assertNotNull("FracB result should not be null", fracB);
        assertThat("Both arrays should have same length", fracA.length, is(fracB.length));
        
        // Test first few values for expected range (exp(0) = 1.0)
        assertEquals("exp(0) A component", 1.0, fracA[0], 1e-15);
        assertEquals("exp(0) B component", 0.0, fracB[0], 1e-15);
        
        // Test that exp(x/1024) = A[x] + B[x] for x=1
        double expectedExpSmall = Math.exp(1.0 / 1024.0);
        double actualExpSmall = fracA[1] + fracB[1];
        assertEquals("exp(1/1024) should be accurate", expectedExpSmall, actualExpSmall, 1e-10);
        
        // Test middle value
        double expectedExpMid = Math.exp(512.0 / 1024.0); // exp(0.5)
        double actualExpMid = fracA[512] + fracB[512];
        assertEquals("exp(0.5) should be accurate", expectedExpMid, actualExpMid, 1e-10);
    }
    
    @Test
    public void testLoadLnMant_NormalCase() {
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Result should not be null", result);
        assertThat("Array length should match original", result.length, is(FastMathLiteralArrays.LN_MANT.length));
        assertNotSame("Should return a clone, not the same array", result, FastMathLiteralArrays.LN_MANT);
        
        // Check structure of returned array (should be 2D with second dimension size 2)
        for (int i = 0; i < result.length; i++) {
            assertNotNull("Subarray elements should not be null", result[i]);
            assertThat("Each subarray should have length 2", result[i].length, is(2));
        }
        
        // Check known values
        assertEquals("First mantissa value at index 0", 0.0, result[0][0], 1e-15);
        assertEquals("Second mantissa value at index 0", 0.0, result[0][1], 1e-15);
        assertEquals("First mantissa value at index 10", 0.00971824862062931d, result[10][0], 1e-15);
        assertEquals("Second mantissa value at index 10", 8.48292035519895E-10d, result[10][1], 1e-15);
    }
    
    @Test
    public void testAllLoadingMethods_ReturnClones() {
        double[] expIntA = FastMathLiteralArrays.loadExpIntA();
        double[] expIntB = FastMathLiteralArrays.loadExpIntB();
        double[] expFracA = FastMathLiteralArrays.loadExpFracA();
        double[] expFracB = FastMathLiteralArrays.loadExpFracB();
        double[][] lnMant = FastMathLiteralArrays.loadLnMant();
        
        // Verify none are null
        assertNotNull("loadExpIntA should not return null", expIntA);
        assertNotNull("loadExpIntB should not return null", expIntB);
        assertNotNull("loadExpFracA should not return null", expFracA);
        assertNotNull("loadExpFracB should not return null", expFracB);
        assertNotNull("loadLnMant should not return null", lnMant);
        
        // Verify they are clones, not references to internal arrays
        assertNotSame("loadExpIntA should return clone", expIntA, FastMathLiteralArrays.EXP_INT_A);
        assertNotSame("loadExpIntB should return clone", expIntB, FastMathLiteralArrays.EXP_INT_B);
        assertNotSame("loadExpFracA should return clone", expFracA, FastMathLiteralArrays.EXP_FRAC_A);
        assertNotSame("loadExpFracB should return clone", expFracB, FastMathLiteralArrays.EXP_FRAC_B);
        assertNotSame("loadLnMant should return clone", lnMant, FastMathLiteralArrays.LN_MANT);
    }
}