package org.apache.commons.math3.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class CMAESOptimizerTest {

    @Test
    public void testLoadExpIntA_NormalCases() {
        double[] result = FastMathLiteralArrays.loadExpIntA();
        
        assertNotNull("Result should not be null", result);
        assertThat("Array length should match original", result.length, is(FastMathLiteralArrays.EXP_INT_A.length));
        assertEquals("First element should be 0.0", 0.0, result[0], 1e-15);
        assertEquals("Last non-NaN element should match", 
                FastMathLiteralArrays.EXP_INT_A[FastMathLiteralArrays.EXP_INT_A.length - 42], 
                result[FastMathLiteralArrays.EXP_INT_A.length - 42], 1e-15);
    }
    
    @Test
    public void testLoadExpIntB_BoundaryCases() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        assertNotNull("Result should not be null", result);
        assertThat("Array length should match original", result.length, is(FastMathLiteralArrays.EXP_INT_B.length));
        assertEquals("First element should be 0.0", 0.0, result[0], 1e-15);
        assertTrue("Middle element should be normal value", 
                Double.isFinite(result[result.length / 2]));
        assertTrue("Last valid element before NaNs should be finite",
                Double.isFinite(result[FastMathLiteralArrays.EXP_INT_B.length - 42]));
    }
    
    @Test
    public void testLoadExpFracA_ValuesAndConsistency() {
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        assertNotNull("Result should not be null", result);
        assertThat("Array length should match original", result.length, is(FastMathLiteralArrays.EXP_FRAC_A.length));
        assertEquals("First element should be 1.0", 1.0, result[0], 1e-15);
        assertEquals("Last element should match", 
                FastMathLiteralArrays.EXP_FRAC_A[FastMathLiteralArrays.EXP_FRAC_A.length - 1], 
                result[FastMathLiteralArrays.EXP_FRAC_A.length - 1], 1e-15);
        
        // Test that the array has reasonable values (monotonically increasing)
        for (int i = 1; i < result.length; i++) {
            if (Double.isFinite(result[i-1]) && Double.isFinite(result[i])) {
                assertTrue("Array should be monotonically increasing at index " + i,
                        result[i] >= result[i-1]);
            }
        }
    }
    
    @Test
    public void testLoadExpFracB_NonNullAndLength() {
        double[] result = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("Result should not be null", result);
        assertThat("Array length should match original", result.length, is(FastMathLiteralArrays.EXP_FRAC_B.length));
        assertEquals("First element should be 0.0", 0.0, result[0], 1e-15);
        assertThat("Second element should be small positive", result[1], greaterThan(0.0));
        assertThat("Last few elements should have large magnitude", 
                Math.abs(result[result.length - 1]), greaterThan(1e-9));
    }
    
    @Test
    public void testLoadLnMant_StructureAndValues() {
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Result should not be null", result);
        assertThat("Array length should match original", result.length, is(FastMathLiteralArrays.LN_MANT.length));
        assertNotNull("First sub-array should not be null", result[0]);
        assertEquals("First sub-array should have length 2", 2, result[0].length);
        assertEquals("Second sub-array should have length 2", 2, result[1].length);
        
        // Test first few entries
        assertEquals("LN_MANT[0][0] should be 0.0", 0.0, result[0][0], 1e-15);
        assertEquals("LN_MANT[0][1] should be 0.0", 0.0, result[0][1], 1e-15);
        assertEquals("LN_MANT[1][0] should match", 
                FastMathLiteralArrays.LN_MANT[1][0], result[1][0], 1e-15);
        assertEquals("LN_MANT[1][1] should match", 
                FastMathLiteralArrays.LN_MANT[1][1], result[1][1], 1e-15);
    }
}