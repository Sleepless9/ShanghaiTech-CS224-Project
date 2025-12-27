package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testLoadExpIntA_NormalCases() {
        double[] expIntA = FastMathLiteralArrays.loadExpIntA();
        assertNotNull("EXP_INT_A should not be null", expIntA);
        assertEquals("EXP_INT_A should have expected length", 512, expIntA.length);
        
        // Test some known values from the array
        assertEquals(0.0d, expIntA[0], 0.0);
        assertEquals(1.0d, expIntA[510], 0.0);
        assertEquals(Double.NaN, expIntA[1], 0.0);
        assertEquals(+2.7182817459106445d, expIntA[511], 1e-10);
    }

    @Test
    public void testLoadExpIntB_BoundaryCases() {
        double[] expIntB = FastMathLiteralArrays.loadExpIntB();
        assertNotNull("EXP_INT_B should not be null", expIntB);
        assertEquals("EXP_INT_B should have expected length", 512, expIntB.length);
        
        // Test boundary values
        assertEquals(0.0d, expIntB[0], 0.0);
        assertEquals(Double.NaN, expIntB[1], 0.0);
        assertEquals(Double.NaN, expIntB[expIntB.length - 1], 0.0);
        assertEquals(Double.NaN, expIntB[expIntB.length - 2], 0.0);
    }
    
    @Test
    public void testLoadExpFracA_NormalValues() {
        double[] expFracA = FastMathLiteralArrays.loadExpFracA();
        assertNotNull("EXP_FRAC_A should not be null", expFracA);
        assertEquals("EXP_FRAC_A should have expected length", 1024, expFracA.length);
        
        // Test some known values
        assertEquals(1.0d, expFracA[0], 0.0);
        assertEquals(1.0009770393371582d, expFracA[1], 1e-10);
        assertEquals(2.7182817459106445d, expFracA[1023], 1e-10);
        
        // Verify the array is properly filled (not all zeros or NaNs)
        boolean hasNonZero = false;
        boolean hasNonNaN = false;
        for (double value : expFracA) {
            if (value != 0.0d) hasNonZero = true;
            if (!Double.isNaN(value)) hasNonNaN = true;
        }
        assertTrue("EXP_FRAC_A should contain non-zero values", hasNonZero);
        assertTrue("EXP_FRAC_A should contain non-NaN values", hasNonNaN);
    }
    
    @Test
    public void testLoadExpFracB_ExceptionalCases() {
        double[] expFracB = FastMathLiteralArrays.loadExpFracB();
        assertNotNull("EXP_FRAC_B should not be null", expFracB);
        assertEquals("EXP_FRAC_B should have expected length", 1024, expFracB.length);
        
        // Test specific values including zero and non-zero cases
        assertEquals(0.0d, expFracB[0], 0.0);
        assertEquals(1.552583321178453E-10d, expFracB[1], 1e-15);
        assertEquals(8.254840070367875E-8d, expFracB[1023], 1e-10);
        
        // Ensure returned array is a clone (independent instance)
        double[] expFracBClone = FastMathLiteralArrays.loadExpFracB();
        assertNotSame("loadExpFracB should return a clone", expFracB, expFracBClone);
        assertEquals("Cloned arrays should have same values", expFracB.length, expFracBClone.length);
        for (int i = 0; i < expFracB.length; i++) {
            assertEquals("Array values should match at index " + i, expFracB[i], expFracBClone[i], 1e-15);
        }
    }
    
    @Test
    public void testLoadLnMant_StructureAndValues() {
        double[][] lnMant = FastMathLiteralArrays.loadLnMant();
        assertNotNull("LN_MANT should not be null", lnMant);
        assertEquals("LN_MANT should have expected length", 1024, lnMant.length);
        
        // Test structure of the 2D array
        for (int i = 0; i < lnMant.length; i++) {
            assertNotNull("LN_MANT[" + i + "] should not be null", lnMant[i]);
            assertEquals("LN_MANT[" + i + "] should have length 2", 2, lnMant[i].length);
        }
        
        // Test specific values
        assertEquals(0.0d, lnMant[0][0], 0.0);
        assertEquals(0.0d, lnMant[0][1], 0.0);
        assertEquals(9.760860120877624E-4d, lnMant[1][0], 1e-10);
        assertEquals(-3.903230345984362E-11d, lnMant[1][1], 1e-15);
        assertEquals(0.6926587820053101d, lnMant[1023][0], 1e-10);
        assertEquals(-1.943473623641502E-9d, lnMant[1023][1], 1e-15);
    }
}