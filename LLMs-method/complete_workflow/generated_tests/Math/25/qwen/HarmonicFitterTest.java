package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class HarmonicFitterTest {

    @Test
    public void testLoadExpIntA() {
        double[] result = FastMathLiteralArrays.loadExpIntA();
        
        // Verify array is not null and has expected length
        assertNotNull("Loaded array should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        
        // Test some known values from the array
        assertEquals("First element should be 0.0", 0.0, result[0], 1e-15);
        assertEquals("Second element should be NaN", Double.NaN, result[1], 1e-15);
        assertEquals("Last non-NaN value", 7.175096392165733E-66d, result[80], 1e-70);
        assertEquals("Final element should be NaN", Double.NaN, result[result.length - 1], 1e-15);
    }

    @Test
    public void testLoadExpIntB() {
        double[] result = FastMathLiteralArrays.loadExpIntB();
        
        // Verify array is not null and has expected length
        assertNotNull("Loaded array should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        
        // Test some known values from the array
        assertEquals("First element should be 0.0", 0.0, result[0], 1e-15);
        assertEquals("Second element should be NaN", Double.NaN, result[1], 1e-15);
        assertEquals("Non-zero value in middle", -1.4379095864E-313d, result[25], 1e-320);
        assertEquals("Final element should be NaN", Double.NaN, result[result.length - 1], 1e-15);
    }
    
    @Test
    public void testLoadExpFracA() {
        double[] result = FastMathLiteralArrays.loadExpFracA();
        
        // Verify array is not null and has expected length
        assertNotNull("Loaded array should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        
        // Test key values including boundaries and specific points
        assertEquals("First element should be 1.0", 1.0, result[0], 1e-15);
        assertEquals("Second element at index 1", 1.0009770393371582d, result[1], 1e-15);
        assertEquals("Middle element at index 512", 1.671417236328125d, result[512], 1e-15);
        assertEquals("Penultimate element", 2.7182817459106445d, result[1022], 1e-15);
        assertEquals("Last element should be exp(1)", Math.E, result[1023], 1e-7);
    }
    
    @Test
    public void testLoadExpFracB() {
        double[] result = FastMathLiteralArrays.loadExpFracB();
        
        // Verify array is not null and has expected length
        assertNotNull("Loaded array should not be null", result);
        assertEquals("Array length should match", 1024, result.length);
        
        // Test key values from the fractional part array
        assertEquals("First element should be 0.0", 0.0, result[0], 1e-15);
        assertEquals("Second element at index 1", 1.552583321178453E-10d, result[1], 1e-15);
        assertEquals("Positive value at index 100", 8.96162975425619E-8d, result[100], 1e-15);
        assertEquals("Negative value at index 150", -3.8077789783477783E-8d, result[150], 1e-15);
        assertEquals("Final element", 8.254840070367875E-8d, result[1023], 1e-15);
    }
    
    @Test
    public void testLoadLnMant() {
        double[][] result = FastMathLiteralArrays.loadLnMant();
        
        // Verify array is not null and has expected dimensions
        assertNotNull("Loaded array should not be null", result);
        assertEquals("Outer array length should match", 1024, result.length);
        
        // Test structure of the 2D array
        for (int i = 0; i < result.length; i++) {
            assertNotNull("Inner array elements should not be null", result[i]);
            assertEquals("Each inner array should have length 2", 2, result[i].length);
        }
        
        // Test specific values from the logarithm mantissa table
        double[] firstElement = result[0];
        assertEquals("First element first value should be 0.0", 0.0, firstElement[0], 1e-15);
        assertEquals("First element second value should be 0.0", 0.0, firstElement[1], 1e-15);
        
        double[] tenthElement = result[10];
        assertEquals("Tenth element first value", 0.00971824862062931d, tenthElement[0], 1e-15);
        assertEquals("Tenth element second value", 8.48292035519895E-10d, tenthElement[1], 1e-15);
    }
}