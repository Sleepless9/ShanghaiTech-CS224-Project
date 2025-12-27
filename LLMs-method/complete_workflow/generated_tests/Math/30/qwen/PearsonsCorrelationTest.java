package org.apache.commons.math3.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class PearsonsCorrelationTest {

    @Test
    public void testLoadExpIntA_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpIntA();
        
        assertNotNull("Result should not be null", result);
        assertThat("Array length should match", result.length, is(FastMathLiteralArrays.EXP_INT_A.length));
        assertTrue("First element should be 0.0", result[0] == 0.0);
        assertTrue("Last non-NaN element should match", 
                result[result.length - 100] == FastMathLiteralArrays.EXP_INT_A[result.length - 100]);
        assertTrue("Array should be a clone (not same reference)", 
                result != FastMathLiteralArrays.EXP_INT_A);
    }
    
    @Test
    public void testLoadExpIntB_BoundaryCase() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should be correct", 769, result.length);
        assertTrue("First element should be 0.0", result[0] == 0.0);
        assertTrue("Second element should be NaN", Double.isNaN(result[1]));
        assertTrue("Middle element should not be NaN", !Double.isNaN(result[50]));
        assertTrue("Array should contain NaN values as expected", 
                Double.isNaN(result[result.length - 2]));
        assertTrue("Returned array should be independent copy", 
                result != FastMathLiteralArrays.EXP_INT_B);
    }
    
    @Test
    public void testLoadExpFracA_ExceptionalCase() {
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        assertNotNull("Result should not be null", result);
        assertThat("Array length should be 1024", result.length, is(1024));
        assertTrue("First element should be exactly 1.0", result[0] == 1.0);
        assertTrue("Last element should be approximately 2.08", 
                Math.abs(result[result.length - 1] - 2.08) < 0.01);
        assertTrue("Array elements should be in ascending order (except for anomalies)", 
                result[result.length/2] > result[result.length/4]);
        assertTrue("Returned array should be a clone", 
                result != FastMathLiteralArrays.EXP_FRAC_A);
    }
    
    @Test
    public void testLoadExpFracB_NormalValues() {
        double[] result = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("Result should not be null", result);
        assertThat("Array length should match", result.length, is(FastMathLiteralArrays.EXP_FRAC_B.length));
        assertTrue("First element should be 0.0", result[0] == 0.0);
        assertFalse("Second element should not be zero", result[1] == 0.0);
        assertTrue("Second element should be positive", result[1] > 0.0);
        assertTrue("Element at index 100 should not be NaN", !Double.isNaN(result[100]));
        assertTrue("Array should be a proper clone", result != FastMathLiteralArrays.EXP_FRAC_B);
    }
    
    @Test
    public void testLoadLnMant_StructureAndValues() {
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Result should not be null", result);
        assertThat("Array length should be 1024", result.length, is(1024));
        assertThat("Each subarray should have length 2", result[0].length, is(2));
        assertThat("Subarray at index 1 should have length 2", result[1].length, is(2));
        assertTrue("First element of first subarray should be 0.0", result[0][0] == 0.0);
        assertTrue("Second element of first subarray should be 0.0", result[0][1] == 0.0);
        assertTrue("First element of last subarray should not be NaN", !Double.isNaN(result[result.length-1][0]));
        assertTrue("Returned array should be a clone", result != FastMathLiteralArrays.LN_MANT);
    }
}