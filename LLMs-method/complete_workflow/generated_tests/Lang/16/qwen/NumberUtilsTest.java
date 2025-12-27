package org.apache.commons.lang3;

import static org.junit.Assert.*;
import org.junit.Test;

public class NumberUtilsTest {

    @Test
    public void testIsEmpty() {
        // Test normal cases
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty("abc"));
        
        // Test boundary cases
        assertTrue(StringUtils.isEmpty(new StringBuilder()));
        assertFalse(StringUtils.isEmpty("   "));
    }
    
    @Test
    public void testIsBlank() {
        // Test normal cases
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertTrue(StringUtils.isBlank("\t\n\r"));
        assertFalse(StringUtils.isBlank("abc"));
        assertFalse(StringUtils.isBlank("  abc  "));
        
        // Test boundary cases
        assertTrue(StringUtils.isBlank(" \t "));
        assertFalse(StringUtils.isBlank(" a "));
    }
    
    @Test
    public void testTrim() {
        // Test normal cases
        assertNull(StringUtils.trim(null));
        assertEquals("", StringUtils.trim(""));
        assertEquals("", StringUtils.trim("     "));
        assertEquals("abc", StringUtils.trim("    abc    "));
        
        // Test boundary cases
        assertEquals("abc", StringUtils.trim("abc"));
        assertEquals("", StringUtils.trim(" \u0000 "));
        
        // Test exception case - non-whitespace control characters
        assertEquals("\u0001abc\u0002", StringUtils.trim("\u0001abc\u0002"));
    }
    
    @Test
    public void testEquals() {
        // Test normal cases
        assertTrue(StringUtils.equals(null, null));
        assertTrue(StringUtils.equals("abc", "abc"));
        assertFalse(StringUtils.equals(null, "abc"));
        assertFalse(StringUtils.equals("abc", null));
        assertFalse(StringUtils.equals("abc", "ABC"));
        
        // Test boundary cases
        assertTrue(StringUtils.equals("", ""));
        assertFalse(StringUtils.equals("a", "aa"));
        assertTrue(StringUtils.equals(new StringBuilder("test"), new StringBuilder("test")));
    }
    
    @Test
    public void testIndexOf() {
        // Test normal cases
        assertEquals(-1, StringUtils.indexOf(null, 'a'));
        assertEquals(-1, StringUtils.indexOf("", 'a'));
        assertEquals(0, StringUtils.indexOf("abc", 'a'));
        assertEquals(1, StringUtils.indexOf("abc", 'b'));
        assertEquals(-1, StringUtils.indexOf("abc", 'd'));
        
        // Test boundary cases
        assertEquals(0, StringUtils.indexOf("aaa", 'a'));
        assertEquals(-1, StringUtils.indexOf("abc", 'a', 5));
        assertEquals(2, StringUtils.indexOf("abcabc", 'c', 3));
    }
}