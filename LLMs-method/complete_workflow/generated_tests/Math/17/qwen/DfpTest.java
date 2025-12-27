package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class DfpTest {

    @Test
    public void testLoadExpIntA_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpIntA();
        
        assertNotNull("Result should not be null", result);
        assertTrue("Array should have positive length", result.length > 0);
        
        // Test a few known values from the beginning and end of the array
        assertEquals("First element should be 0.0", 0.0, result[0], 1e-15);
        assertEquals("Middle element at index 87 should be 1.0", 1.0, result[87], 1e-15);
        assertEquals("Last non-NaN element at index 174 should match expected value", 
                2.7182817459106445E0, result[174], 1e-15);
    }
    
    @Test
    public void testLoadExpIntB_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        assertNotNull("Result should not be null", result);
        assertTrue("Array should have positive length", result.length > 0);
        
        // Test a few known values from the beginning and end of the array
        assertEquals("First element should be 0.0", 0.0, result[0], 1e-15);
        assertEquals("Element at index 87 should be 0.0", 0.0, result[87], 1e-15);
        assertEquals("Last non-zero element at index 174 should match expected value", 
                8.254840070411029E-8, result[174], 1e-15);
    }
    
    @Test
    public void testLoadExpFracA_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array should have exactly 1024 elements", 1024, result.length);
        
        // Test key values from the exponential fraction table
        assertEquals("First element should be 1.0", 1.0, result[0], 1e-15);
        assertEquals("Second element should be approximately 1.000977", 
                1.0009770393371582d, result[1], 1e-15);
        assertEquals("Last element should be approximately 2.718", 
                2.7182817459106445d, result[1023], 1e-15);
    }
    
    @Test
    public void testLoadExpFracB_NormalCase() {
        double[] result = FastMathLiteralArrays.loadExpFracB();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array should have exactly 1024 elements", 1024, result.length);
        
        // Test key values from the exponential fraction table
        assertEquals("First element should be 0.0", 0.0, result[0], 1e-15);
        assertEquals("Second element should be small positive value", 
                1.552583321178453E-10d, result[1], 1e-15);
        assertEquals("Last element should be small positive value", 
                8.254840070367875E-8d, result[1023], 1e-15);
    }
    
    @Test
    public void testLoadLnMant_NormalCase() {
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        assertNotNull("Result should not be null", result);
        assertEquals("Array should have exactly 1024 rows", 1024, result.length);
        
        // Test that each row has exactly 2 elements
        for (int i = 0; i < result.length; i++) {
            assertNotNull("Each row should not be null", result[i]);
            assertEquals("Each row should have exactly 2 elements", 2, result[i].length);
        }
        
        // Test specific values
        assertEquals("First element of first row should be 0.0", 0.0, result[0][0], 1e-15);
        assertEquals("Second element of first row should be 0.0", 0.0, result[0][1], 1e-15);
        assertEquals("First element of second row should be approximately 9.76e-4", 
                9.760860120877624E-4d, result[1][0], 1e-15);
        assertEquals("Second element of second row should be negative small value", 
                -3.903230345984362E-11d, result[1][1], 1e-15);
    }
}