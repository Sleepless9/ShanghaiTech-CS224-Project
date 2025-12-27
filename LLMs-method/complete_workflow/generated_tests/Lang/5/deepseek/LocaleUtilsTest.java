package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testIsEmptyAndIsBlank() {
        // Test isEmpty
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty("abc"));
        
        // Test isBlank
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertTrue(StringUtils.isBlank("  \t\n\r"));
        assertFalse(StringUtils.isBlank("abc"));
        assertFalse(StringUtils.isBlank("  abc  "));
    }

    @Test
    public void testTrimAndStrip() {
        // Test trim
        assertNull(StringUtils.trim(null));
        assertEquals("", StringUtils.trim(""));
        assertEquals("", StringUtils.trim("   "));
        assertEquals("abc", StringUtils.trim("  abc  "));
        assertEquals("abc", StringUtils.trim("abc"));
        
        // Test strip
        assertNull(StringUtils.strip(null));
        assertEquals("", StringUtils.strip(""));
        assertEquals("", StringUtils.strip("   "));
        assertEquals("abc", StringUtils.strip("  abc  "));
        assertEquals("ab c", StringUtils.strip("  ab c  "));
        
        // Test strip with specific characters
        assertEquals("abc", StringUtils.strip("xxabcxx", "x"));
        assertEquals("  abc  ", StringUtils.strip("  abc  ", "x"));
        assertEquals("abc", StringUtils.strip("xyabcyx", "xyz"));
    }

    @Test
    public void testEqualsAndEqualsIgnoreCase() {
        // Test equals
        assertTrue(StringUtils.equals(null, null));
        assertFalse(StringUtils.equals(null, "abc"));
        assertFalse(StringUtils.equals("abc", null));
        assertTrue(StringUtils.equals("abc", "abc"));
        assertFalse(StringUtils.equals("abc", "ABC"));
        
        // Test equalsIgnoreCase
        assertTrue(StringUtils.equalsIgnoreCase(null, null));
        assertFalse(StringUtils.equalsIgnoreCase(null, "abc"));
        assertFalse(StringUtils.equalsIgnoreCase("abc", null));
        assertTrue(StringUtils.equalsIgnoreCase("abc", "abc"));
        assertTrue(StringUtils.equalsIgnoreCase("abc", "ABC"));
        assertFalse(StringUtils.equalsIgnoreCase("abc", "def"));
    }

    @Test
    public void testSubstringMethods() {
        // Test substring
        assertNull(StringUtils.substring(null, 0));
        assertEquals("", StringUtils.substring("", 0));
        assertEquals("abc", StringUtils.substring("abc", 0));
        assertEquals("c", StringUtils.substring("abc", 2));
        assertEquals("", StringUtils.substring("abc", 4));
        assertEquals("bc", StringUtils.substring("abc", -2));
        
        // Test substring with start and end
        assertNull(StringUtils.substring(null, 0, 2));
        assertEquals("", StringUtils.substring("", 0, 2));
        assertEquals("ab", StringUtils.substring("abc", 0, 2));
        assertEquals("", StringUtils.substring("abc", 2, 0));
        assertEquals("c", StringUtils.substring("abc", 2, 4));
        assertEquals("b", StringUtils.substring("abc", -2, -1));
        
        // Test left, right, mid
        assertEquals("ab", StringUtils.left("abc", 2));
        assertEquals("abc", StringUtils.left("abc", 5));
        assertEquals("bc", StringUtils.right("abc", 2));
        assertEquals("abc", StringUtils.right("abc", 5));
        assertEquals("bc", StringUtils.mid("abc", 1, 2));
        assertEquals("c", StringUtils.mid("abc", 2, 5));
    }

    @Test
    public void testContainsAndIndexOf() {
        // Test contains
        assertFalse(StringUtils.contains(null, 'a'));
        assertFalse(StringUtils.contains("", 'a'));
        assertTrue(StringUtils.contains("abc", 'a'));
        assertFalse(StringUtils.contains("abc", 'z'));
        
        // Test contains with CharSequence
        assertFalse(StringUtils.contains(null, "ab"));
        assertFalse(StringUtils.contains("abc", null));
        assertTrue(StringUtils.contains("abc", ""));
        assertTrue(StringUtils.contains("abc", "ab"));
        assertFalse(StringUtils.contains("abc", "xz"));
        
        // Test indexOf
        assertEquals(-1, StringUtils.indexOf(null, 'a'));
        assertEquals(-1, StringUtils.indexOf("", 'a'));
        assertEquals(0, StringUtils.indexOf("abc", 'a'));
        assertEquals(2, StringUtils.indexOf("abc", 'c'));
        assertEquals(-1, StringUtils.indexOf("abc", 'z'));
        
        // Test indexOf with start position
        assertEquals(2, StringUtils.indexOf("abab", 'a', 1));
        assertEquals(-1, StringUtils.indexOf("abab", 'a', 3));
        
        // Test containsIgnoreCase
        assertTrue(StringUtils.containsIgnoreCase("abc", "AB"));
        assertTrue(StringUtils.containsIgnoreCase("ABC", "ab"));
        assertFalse(StringUtils.containsIgnoreCase("abc", "xz"));
        assertFalse(StringUtils.containsIgnoreCase(null, "ab"));
        assertFalse(StringUtils.containsIgnoreCase("abc", null));
    }
}