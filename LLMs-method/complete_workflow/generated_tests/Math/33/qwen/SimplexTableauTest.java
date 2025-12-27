package org.apache.commons.math3.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testLoadExpIntArrays_NormalCase() {
        double[] expIntA = FastMathLiteralArrays.loadExpIntA();
        double[] expIntB = FastMathLiteralArrays.loadExpIntB();
        
        assertNotNull("EXP_INT_A array should not be null", expIntA);
        assertNotNull("EXP_INT_B array should not be null", expIntB);
        assertEquals("Arrays should have same length", expIntA.length, expIntB.length);
        assertEquals("Array length should be 512", 512, expIntA.length);
        
        // Check first and last non-NaN values
        assertEquals("First value of EXP_INT_A should be 0.0", 0.0, expIntA[0], 1e-15);
        assertEquals("Last value of EXP_INT_A should be near 2.7e308", 
                     2.7182817459106445E308d, expIntA[expIntA.length - 1], 1e-15);
    }
    
    @Test
    public void testLoadExpFracArrays_BoundaryValues() {
        double[] expFracA = FastMathLiteralArrays.loadExpFracA();
        double[] expFracB = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("EXP_FRAC_A array should not be null", expFracA);
        assertNotNull("EXP_FRAC_B array should not be null", expFracB);
        assertEquals("Arrays should have same length", expFracA.length, expFracB.length);
        assertEquals("Array length should be 1024", 1024, expFracA.length);
        
        // Check boundary values
        assertEquals("First value of EXP_FRAC_A should be 1.0", 1.0, expFracA[0], 1e-15);
        assertEquals("First value of EXP_FRAC_B should be 0.0", 0.0, expFracB[0], 1e-15);
        assertEquals("Last value of EXP_FRAC_A should be ~2.7", 2.7182817459106445d, expFracA[1023], 1e-15);
        assertEquals("Last value of EXP_FRAC_B should be ~8.25e-8", 8.254840070367875E-8d, expFracB[1023], 1e-15);
    }
    
    @Test
    public void testLoadLnMant_ArrayStructure() {
        double[][] lnMant = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("LN_MANT array should not be null", lnMant);
        assertEquals("LN_MANT should have 1024 rows", 1024, lnMant.length);
        
        // Check structure of each row
        for (int i = 0; i < lnMant.length; i++) {
            assertNotNull("Row " + i + " should not be null", lnMant[i]);
            assertEquals("Each row should have exactly 2 elements", 2, lnMant[i].length);
        }
        
        // Check specific known values
        assertEquals("First element of first row should be 0.0", 0.0, lnMant[0][0], 1e-15);
        assertEquals("Second element of first row should be 0.0", 0.0, lnMant[0][1], 1e-15);
        assertEquals("First element of last row should be ~0.69266", 0.6926587820053101d, lnMant[1023][0], 1e-15);
        assertEquals("Second element of last row should be ~-1.94e-9", -1.943473623641502E-9d, lnMant[1023][1], 1e-15);
    }
}