package org.apache.commons.math.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class VarianceTest {

    @Test
    public void testLoadExpIntA_NormalCases() {
        double[] expIntA = FastMathLiteralArrays.loadExpIntA();
        
        // Test known values at specific indices
        assertEquals(0.0, expIntA[0], 1e-15);
        assertEquals(1.0, expIntA[54], 1e-15);
        assertEquals(2.7182817459106445d, expIntA[55], 1e-15);
        assertEquals(7.389056205749512d, expIntA[56], 1e-15);
        
        // Test array length
        assertEquals(205, expIntA.length);
    }
    
    @Test
    public void testLoadExpIntB_BoundaryCases() {
        double[] expIntB = FastMathLiteralArrays.loadExpIntB();
        
        // Test boundary values
        assertEquals(0.0, expIntB[0], 1e-15);
        assertEquals(Double.NaN, expIntB[1], 0.0);
        assertEquals(Double.NaN, expIntB[expIntB.length - 1], 0.0);
        
        // Test a few non-NaN values
        assertTrue(expIntB[40] < 0.0); // Should be negative
        assertTrue(expIntB[41] > 0.0);   // Should be positive
        
        // Verify array length matches EXP_INT_A
        assertEquals(FastMathLiteralArrays.loadExpIntA().length, expIntB.length);
    }
    
    @Test
    public void testLoadExpFracA_ValuesAndLength() {
        double[] expFracA = FastMathLiteralArrays.loadExpFracA();
        
        // Test first and last values
        assertEquals(1.0, expFracA[0], 1e-15);
        assertEquals(2.7182817459106445d, expFracA[1023], 1e-15);
        
        // Test middle value
        assertEquals(1.5, expFracA[512], 0.01);
        
        // Test array length (should be 1024 values for 2^10 increments)
        assertEquals(1024, expFracA.length);
    }
    
    @Test
    public void testLoadExpFracB_NonZeroValues() {
        double[] expFracB = FastMathLiteralArrays.loadExpFracB();
        
        // Test first value is zero
        assertEquals(0.0, expFracB[0], 1e-15);
        
        // Test that other values are not zero
        assertNotEquals(0.0, expFracB[1]);
        assertNotEquals(0.0, expFracB[512]);
        assertNotEquals(0.0, expFracB[1023]);
        
        // Test array length matches EXP_FRAC_A
        assertEquals(FastMathLiteralArrays.loadExpFracA().length, expFracB.length);
    }
    
    @Test
    public void testLoadLnMant_StructureAndValues() {
        double[][] lnMant = FastMathLiteralArrays.loadLnMant();
        
        // Test array dimensions
        assertEquals(1024, lnMant.length);
        assertEquals(2, lnMant[0].length);
        
        // Test first value (index 0 should be zeros)
        assertEquals(0.0, lnMant[0][0], 1e-15);
        assertEquals(0.0, lnMant[0][1], 1e-15);
        
        // Test a few sample values
        assertEquals(9.760860120877624E-4d, lnMant[1][0], 1e-15);
        assertEquals(-3.903230345984362E-11d, lnMant[1][1], 1e-15);
        
        // Test last value
        assertEquals(0.6926587820053101d, lnMant[1023][0], 1e-15);
        assertEquals(-1.943473623641502E-9d, lnMant[1023][1], 1e-15);
    }
}