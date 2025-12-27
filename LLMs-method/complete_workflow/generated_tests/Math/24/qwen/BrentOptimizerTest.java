package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testLoadExpIntA_Normal() {
        double[] result = FastMathLiteralArrays.loadExpIntA();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        
        // Test first few values
        assertEquals("First value should be 0.0", 0.0, result[0], 1e-15);
        assertEquals("Second value should be NaN", Double.NaN, result[1], 1e-15);
        assertEquals("Last value should be NaN", Double.NaN, result[result.length - 1], 1e-15);
        
        // Test a known non-zero, non-NaN value (index 41)
        assertEquals("Value at index 41 should match expected", 
                     1.2167807682331913E-308d, result[41], 1e-310);
    }
    
    @Test
    public void testLoadExpIntB_Boundary() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        
        // Test boundary values
        assertEquals("First value should be 0.0", 0.0, result[0], 1e-15);
        assertEquals("Last value should be NaN", Double.NaN, result[result.length - 1], 1e-15);
        
        // Verify that the clone is independent
        double[] original = FastMathLiteralArrays.loadExpIntB();
        result[0] = 999.999;
        assertNotSame("Changing cloned array should not affect original", 
                      result[0], original[0]);
    }
    
    @Test
    public void testLoadExpFracA_Values() {
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        
        // Test known values
        assertEquals("First value should be 1.0", 1.0, result[0], 1e-15);
        assertEquals("Second value should be ~1.000977", 
                     1.0009770393371582d, result[1], 1e-15);
        assertEquals("Middle value should be ~1.65", 
                     1.6584100778608032d, result[512], 1e-15);
        assertEquals("Last value should be ~2.718", 
                     2.7182817459106445d, result[1023], 1e-15);
    }
    
    @Test
    public void testLoadExpFracB_Cloning() {
        double[] result1 = FastMathLiteralArrays.loadExpFracB();
        double[] result2 = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("Result should not be null", result1);
        assertNotNull("Second result should not be null", result2);
        assertEquals("Array lengths should match", result1.length, result2.length);
        
        // Verify arrays are equal but independent
        assertArrayEquals("Arrays should have same values", result1, result2, 1e-15);
        assertNotSame("Arrays should be different instances", result1, result2);
        
        // Modify one array and ensure other is unchanged
        result1[0] = -1.0;
        assertEquals("Other array should not be affected", 0.0, result2[0], 1e-15);
    }
    
    @Test
    public void testLoadLnMant_StructureAndValues() {
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        
        // Test structure
        assertNotNull("First sub-array should not be null", result[0]);
        assertEquals("Sub-array should have length 2", 2, result[0].length);
        
        // Test specific values
        assertEquals("First sub-array first value should be 0.0", 
                     0.0, result[0][0], 1e-15);
        assertEquals("First sub-array second value should be 0.0", 
                     0.0, result[0][1], 1e-15);
        
        // Test a non-zero entry (index 1)
        assertEquals("Index 1, value 0 should match", 
                     9.760860120877624E-4d, result[1][0], 1e-15);
        assertEquals("Index 1, value 1 should match", 
                     -3.903230345984362E-11d, result[1][1], 1e-15);
        
        // Test last entry
        assertEquals("Last entry first value should match",
                     0.6926587820053101d, result[1023][0], 1e-15);
        assertEquals("Last entry second value should match", 
                     -1.943473623641502E-9d, result[1023][1], 1e-15);
    }
}