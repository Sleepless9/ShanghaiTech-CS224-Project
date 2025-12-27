package org.apache.commons.math.util;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class VarianceTest {

    @Test
    public void testLoadExpIntA() {
        double[] array = FastMathLiteralArrays.loadExpIntA();
        assertNotNull("Array should not be null", array);
        assertTrue("Array should have elements", array.length > 0);
        assertEquals("First element should be 0.0", 0.0, array[0], 0.0);
        assertEquals("Element at index 41 should match", 1.2167807682331913E-308, array[41], 0.0);
    }

    @Test
    public void testLoadExpIntB() {
        double[] array = FastMathLiteralArrays.loadExpIntB();
        assertNotNull("Array should not be null", array);
        assertEquals("Array length should match EXP_INT_A", 
                     FastMathLiteralArrays.loadExpIntA().length, array.length);
        assertEquals("First element should be 0.0", 0.0, array[0], 0.0);
        assertTrue("Array should contain NaN values", Double.isNaN(array[1]));
    }

    @Test
    public void testLoadExpFracA() {
        double[] array = FastMathLiteralArrays.loadExpFracA();
        assertNotNull("Array should not be null", array);
        assertEquals("Array should have 1024 elements", 1024, array.length);
        assertEquals("First element should be 1.0", 1.0, array[0], 0.0);
        assertEquals("Last element should be approximately e", 2.7182817459106445, array[1023], 1e-10);
    }

    @Test
    public void testLoadExpFracB() {
        double[] array = FastMathLiteralArrays.loadExpFracB();
        assertNotNull("Array should not be null", array);
        assertEquals("Array should have same length as EXP_FRAC_A", 
                     FastMathLiteralArrays.loadExpFracA().length, array.length);
        assertEquals("First element should be 0.0", 0.0, array[0], 0.0);
        assertTrue("Array should contain non-zero values", Math.abs(array[1]) > 0);
    }

    @Test
    public void testLoadLnMant() {
        double[][] array = FastMathLiteralArrays.loadLnMant();
        assertNotNull("Array should not be null", array);
        assertEquals("Array should have 1024 rows", 1024, array.length);
        assertEquals("Each row should have 2 columns", 2, array[0].length);
        assertEquals("First element should be 0.0", 0.0, array[0][0], 0.0);
        assertEquals("Second element of first row should be 0.0", 0.0, array[0][1], 0.0);
        assertTrue("Last row should have non-zero values", array[1023][0] > 0);
    }
}