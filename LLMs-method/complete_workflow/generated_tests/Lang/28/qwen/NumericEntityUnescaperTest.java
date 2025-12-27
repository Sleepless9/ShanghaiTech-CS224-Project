package org.apache.commons.lang3;

import static org.junit.Assert.*;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsEmpty() {
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty("abc"));
        
        assertTrue(StringUtils.isEmpty((CharSequence) null));
        assertTrue(StringUtils.isEmpty((CharSequence) ""));
        assertFalse(StringUtils.isEmpty((CharSequence) " "));
        assertFalse(StringUtils.isEmpty((CharSequence) "abc"));
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertTrue(StringUtils.isBlank("\t\n"));
        assertFalse(StringUtils.isBlank("abc"));
        assertFalse(StringUtils.isBlank("  abc  "));
        
        assertTrue(StringUtils.isBlank((CharSequence) null));
        assertTrue(StringUtils.isBlank((CharSequence) ""));
        assertTrue(StringUtils.isBlank((CharSequence) " "));
        assertFalse(StringUtils.isBlank((CharSequence) "abc"));
    }

    @Test
    public void testTrim() {
        assertNull(StringUtils.trim(null));
        assertEquals("", StringUtils.trim(""));
        assertEquals("", StringUtils.trim("   "));
        assertEquals("abc", StringUtils.trim("abc"));
        assertEquals("abc", StringUtils.trim("  abc  "));
        assertEquals("a b c", StringUtils.trim(" a b c "));
        
        assertEquals("abc def", StringUtils.trim("  abc def  "));
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
        assertFalse(StringUtils.equals((CharSequence) "abc", (CharSequence) "ABC"));
    }

    @Test
    public void testSubstring() {
        assertNull(StringUtils.substring(null, 0));
        assertEquals("", StringUtils.substring("", 0));
        assertEquals("abc", StringUtils.substring("abc", 0));
        assertEquals("c", StringUtils.substring("abc", 2));
        assertEquals("", StringUtils.substring("abc", 4));
        assertEquals("bc", StringUtils.substring("abc", -1));
        assertEquals("abc", StringUtils.substring("abc", -4));
        
        assertEquals("ab", StringUtils.substring("abc", 0, 2));
        assertEquals("", StringUtils.substring("abc", 2, 0));
        assertEquals("c", StringUtils.substring("abc", 2, 4));
        assertEquals("", StringUtils.substring("abc", 4, 6));
        assertEquals("", StringUtils.substring("abc", 2, 2));
        assertEquals("b", StringUtils.substring("abc", -2, -1));
    }
}