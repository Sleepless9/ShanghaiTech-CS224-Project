package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CMAESOptimizerTest {

    @Test
    public void testLoadExpIntA_NormalCase() {
        double[] array = FastMathLiteralArrays.loadExpIntA();
        assertNotNull("Array should not be null", array);
        assertTrue("Array should have more than 0 elements", array.length > 0);
        assertArrayEquals("First element should be 0.0", new double[]{0.0}, new double[]{array[0]}, 1e-15);
        assertEquals("Last valid non-NaN value should be at index 728", 2.7182817459106445E15, array[728], 1e-10);
    }

    @Test
    public void testLoadExpIntB_NormalCase() {
        double[] array = FastMathLiteralArrays.loadExpIntB();
        assertNotNull("Array should not be null", array);
        assertTrue("Array should have more than 0 elements", array.length > 0);
        assertEquals("Length should match EXP_INT_A", FastMathLiteralArrays.loadExpIntA().length, array.length);
        assertArrayEquals("First element should be 0.0", new double[]{0.0}, new double[]{array[0]}, 1e-15);
        assertEquals("First non-zero value should be at index 41", -1.76097684E-316d, array[41], 1e-20);
    }

    @Test
    public void testLoadExpFracA_NormalCase() {
        double[] array = FastMathLiteralArrays.loadExpFracA();
        assertNotNull("Array should not be null", array);
        assertTrue("Array should have 1024 elements", array.length == 1024);
        assertArrayEquals("First element should be 1.0", new double[]{1.0}, new double[]{array[0]}, 1e-15);
        assertEquals("Last element should be exp(1)", 2.7182817459106445d, array[1023], 1e-8);
        assertTrue("Values should be increasing", array[512] > array[256]);
    }

    @Test
    public void testLoadExpFracB_NormalCase() {
        double[] array = FastMathLiteralArrays.loadExpFracB();
        assertNotNull("Array should not be null", array);
        assertTrue("Array should have 1024 elements", array.length == 1024);
        assertArrayEquals("First element should be 0.0", new double[]{0.0}, new double[]{array[0]}, 1e-15);
        assertEquals("Last element should match the precision component", 8.254840070367875E-8d, array[1023], 1e-15);
        assertEquals("Length should match EXP_FRAC_A", FastMathLiteralArrays.loadExpFracA().length, array.length);
    }

    @Test
    public void testLoadLnMant_NormalCase() {
        double[][] array = FastMathLiteralArrays.loadLnMant();
        assertNotNull("Array should not be null", array);
        assertTrue("Array should have 1024 rows", array.length == 1024);
        assertTrue("Each row should have 2 elements", array[0].length == 2 && array[512].length == 2);
        assertNotNull("First row should not be null", array[0]);
        assertEquals("First element of first row should be 0.0", 0.0, array[0][0], 1e-15);
        assertEquals("Second element of first row should be 0.0", 0.0, array[0][1], 1e-15);
        assertEquals("First element of last row should be approximately ln(2)", 0.6931471805599453, array[1023][0], 1e-12);
    }
}