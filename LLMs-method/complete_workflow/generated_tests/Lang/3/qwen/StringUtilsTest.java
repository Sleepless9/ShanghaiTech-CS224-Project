package org.apache.commons.lang3;

import static org.junit.Assert.*;
import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void testIsEmpty() {
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty("abc"));
        assertFalse(StringUtils.isEmpty("  abc  "));
        
        assertEquals(true, StringUtils.isEmpty((CharSequence) null));
        assertEquals(true, StringUtils.isEmpty((CharSequence) ""));
        assertEquals(false, StringUtils.isEmpty((CharSequence) " "));
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertTrue(StringUtils.isBlank("\t\n\f\r"));
        assertFalse(StringUtils.isBlank("abc"));
        assertFalse(StringUtils.isBlank("  abc  "));
        
        assertEquals(true, StringUtils.isBlank((CharSequence) null));
        assertEquals(true, StringUtils.isBlank((CharSequence) " \t\n\f\r "));
    }

    @Test
    public void testTrim() {
        assertNull(StringUtils.trim(null));
        assertEquals("", StringUtils.trim(""));
        assertEquals("", StringUtils.trim("     "));
        assertEquals("abc", StringUtils.trim("abc"));
        assertEquals("abc", StringUtils.trim("    abc    "));
        assertEquals("ab c", StringUtils.trim(" ab c "));
    }

    @Test
    public void testEquals() {
        assertTrue(StringUtils.equals(null, null));
        assertFalse(StringUtils.equals(null, "abc"));
        assertFalse(StringUtils.equals("abc", null));
        assertTrue(StringUtils.equals("abc", "abc"));
        assertFalse(StringUtils.equals("abc", "ABC"));
        assertFalse(StringUtils.equals("abc", "abcd"));
        
        assertTrue(StringUtils.equals((CharSequence) null, (CharSequence) null));
        assertTrue(StringUtils.equals((CharSequence) "abc", (CharSequence) "abc"));
    }

    @Test
    public void testSubstring() {
        assertNull(StringUtils.substring(null, 0));
        assertEquals("", StringUtils.substring("", 0));
        assertEquals("abc", StringUtils.substring("abc", 0));
        assertEquals("c", StringUtils.substring("abc", 2));
        assertEquals("", StringUtils.substring("abc", 4));
        assertEquals("bc", StringUtils.substring("abc", -2));
        assertEquals("abc", StringUtils.substring("abc", -4));
        
        assertNull(StringUtils.substring(null, 0, 1));
        assertEquals("", StringUtils.substring("", 0, 1));
        assertEquals("ab", StringUtils.substring("abc", 0, 2));
        assertEquals("", StringUtils.substring("abc", 2, 0));
        assertEquals("c", StringUtils.substring("abc", 2, 4));
        assertEquals("", StringUtils.substring("abc", 4, 6));
        assertEquals("", StringUtils.substring("abc", 2, 2));
        assertEquals("b", StringUtils.substring("abc", -2, -1));
        assertEquals("ab", StringUtils.substring("abc", -4, 2));
    }
}