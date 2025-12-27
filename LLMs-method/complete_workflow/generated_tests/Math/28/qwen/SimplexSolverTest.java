package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testLoadExpIntA_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpIntA();
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        assertEquals("First element should be 0.0", 0.0, result[0], 1e-15);
        assertEquals("Last element should be NaN", Double.NaN, result[result.length - 1], 0.0);
    }

    @Test
    public void testLoadExpIntB_BoundaryValues() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        assertNotNull("Result should not be null", result);
        assertTrue("Second element should be NaN", Double.isNaN(result[1]));
        assertTrue("Element at index 41 should be negative", result[41] < 0);
        assertEquals("Element at index 709 should be positive", 
                2.6884484008569103E67d, result[709], 1e-10);
    }

    @Test
    public void testLoadExpFracA_ValuesAndCloning() {
        double[] resultA = FastMathLiteralArrays.loadExpFracA();
        double[] resultB = FastMathLiteralArrays.loadExpFracA();
        
        assertNotNull("Result A should not be null", resultA);
        assertNotNull("Result B should not be null", resultB);
        assertNotSame("Arrays should be different instances", resultA, resultB);
        assertArrayEquals("Arrays should have same values", resultA, resultB, 1e-15);
        
        assertEquals("First element should be 1.0", 1.0, resultA[0], 1e-15);
        assertEquals("Last element should be exp(1)", 2.7182817459106445d, resultA[1023], 1e-10);
    }

    @Test
    public void testLoadExpFracB_LnMant_CloningAndIntegrity() {
        double[] expFracB = FastMathLiteralArrays.loadExpFracB();
        double[][] lnMant = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("expFracB should not be null", expFracB);
        assertNotNull("lnMant should not be null", lnMant);
        assertEquals("lnMant should have 1024 rows", 1024, lnMant.length);
        assertEquals("Each lnMant row should have 2 elements", 2, lnMant[0].length);
        
        // Test that cloning works (different references)
        double[][] lnMant2 = FastMathLiteralArrays.loadLnMant();
        assertNotSame("lnMant arrays should be different instances", lnMant, lnMant2);
        assertArrayEquals("lnMant[0] values should match", lnMant[0], lnMant2[0], 1e-15);
    }

    @Test
    public void testLoadLnMant_SpecificValues() {
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should be 1024", 1024, result.length);
        
        // Test specific known values from the array
        assertEquals("LN_MANT[1][0] first value", 9.760860120877624E-4d, result[1][0], 1e-15);
        assertEquals("LN_MANT[1][1] second value", -3.903230345984362E-11d, result[1][1], 1e-15);
        
        assertEquals("LN_MANT[256][0] first value", 0.2231435477733612d, result[256][0], 1e-15);
        assertEquals("LN_MANT[256][1] second value", 3.5408485497116107E-9d, result[256][1], 1e-15);
    }
}