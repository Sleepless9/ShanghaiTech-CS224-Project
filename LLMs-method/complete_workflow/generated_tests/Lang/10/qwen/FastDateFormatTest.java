package org.apache.commons.lang3;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class FastDateFormatTest {

    @Test
    public void testIsEmpty() {
        // Normal cases
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty("abc"));

        // Boundary cases
        assertFalse(StringUtils.isEmpty("a"));
        assertTrue(StringUtils.isEmpty(new StringBuilder().toString()));
    }

    @Test
    public void testIsBlank() {
        // Normal cases
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertTrue(StringUtils.isBlank("\t\n\r"));
        assertFalse(StringUtils.isBlank("abc"));
        assertFalse(StringUtils.isBlank("  abc  "));

        // Boundary cases
        assertTrue(StringUtils.isBlank(" \t "));
        assertFalse(StringUtils.isBlank(" a "));
    }

    @Test
    public void testTrim() {
        // Normal cases
        assertNull(StringUtils.trim(null));
        assertEquals("", StringUtils.trim(""));
        assertEquals("", StringUtils.trim("   "));
        assertEquals("abc", StringUtils.trim("abc"));
        assertEquals("abc", StringUtils.trim("   abc   "));

        // Boundary cases
        assertEquals("", StringUtils.trim("\t\n\r"));
        assertEquals("a", StringUtils.trim("a"));
    }

    @Test
    public void testEquals() {
        // Normal cases
        assertTrue(StringUtils.equals(null, null));
        assertFalse(StringUtils.equals(null, "abc"));
        assertFalse(StringUtils.equals("abc", null));
        assertTrue(StringUtils.equals("abc", "abc"));
        assertFalse(StringUtils.equals("abc", "ABC"));

        // Boundary cases
        assertTrue(StringUtils.equals("", ""));
        assertFalse(StringUtils.equals("a", "aa"));
    }

    @Test
    public void testSubstring() {
        // Normal cases
        assertNull(StringUtils.substring(null, 0));
        assertEquals("", StringUtils.substring("", 0));
        assertEquals("abc", StringUtils.substring("abc", 0));
        assertEquals("c", StringUtils.substring("abc", 2));
        assertEquals("", StringUtils.substring("abc", 10));

        // Boundary cases with negative indices
        assertEquals("bc", StringUtils.substring("abc", -2));
        assertEquals("abc", StringUtils.substring("abc", -10));
        assertEquals("", StringUtils.substring("abc", 5));
    }

    @Test
    public void testReplace() {
        // Normal cases
        assertNull(StringUtils.replace(null, "a", "b"));
        assertEquals("", StringUtils.replace("", "a", "b"));
        assertEquals("abc", StringUtils.replace("abc", null, "b"));
        assertEquals("abc", StringUtils.replace("abc", "x", null));
        assertEquals("bbc", StringUtils.replace("abc", "a", "b"));
        assertEquals("zzz", StringUtils.replace("aaa", "a", "z"));

        // Boundary cases
        assertEquals("", StringUtils.replace("", "", ""));
        assertEquals("bbb", StringUtils.replace("aaa", "a", "b"));
    }
}