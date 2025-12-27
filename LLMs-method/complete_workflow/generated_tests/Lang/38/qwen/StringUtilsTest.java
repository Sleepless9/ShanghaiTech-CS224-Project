package org.apache.commons.lang3;

import static org.junit.Assert.*;
import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void testIsEmptyAndIsNotEmpty() {
        // Test null and empty string cases
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty("abc"));
        
        // Test isNotEmpty which is inverse of isEmpty
        assertFalse(StringUtils.isNotEmpty(null));
        assertFalse(StringUtils.isNotEmpty(""));
        assertTrue(StringUtils.isNotEmpty(" "));
        assertTrue(StringUtils.isNotEmpty("abc"));
        
        // Additional boundary cases
        assertFalse(StringUtils.isNotEmpty("\t"));
        assertTrue(StringUtils.isNotEmpty("   "));
    }

    @Test
    public void testIsBlankAndIsNotBlank() {
        // Test blank detection (null, empty, whitespace only)
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertTrue(StringUtils.isBlank("  \t \n  "));
        
        // Test not blank cases
        assertFalse(StringUtils.isBlank("abc"));
        assertFalse(StringUtils.isBlank(" a "));
        assertFalse(StringUtils.isBlank("abc "));
        
        // Test isNotBlank (inverse of isBlank)
        assertFalse(StringUtils.isNotBlank(null));
        assertFalse(StringUtils.isNotBlank(""));
        assertFalse(StringUtils.isNotBlank("   "));
        assertTrue(StringUtils.isNotBlank("abc"));
        assertTrue(StringUtils.isNotBlank(" a "));
    }

    @Test
    public void testTrimAndStripMethods() {
        // Test trim methods
        assertNull(StringUtils.trim(null));
        assertEquals("", StringUtils.trim(""));
        assertEquals("", StringUtils.trim("   \t\n   "));
        assertEquals("abc", StringUtils.trim("  abc  "));
        assertEquals("abc", StringUtils.trim("\tabc\n"));
        
        // Test trimToNull
        assertNull(StringUtils.trimToNull(null));
        assertNull(StringUtils.trimToNull(""));
        assertNull(StringUtils.trimToNull("   "));
        assertEquals("abc", StringUtils.trimToNull("  abc  "));
        
        // Test trimToEmpty
        assertEquals("", StringUtils.trimToEmpty(null));
        assertEquals("", StringUtils.trimToEmpty(""));
        assertEquals("", StringUtils.trimToEmpty("   "));
        assertEquals("abc", StringUtils.trimToEmpty("  abc  "));
        
        // Test strip methods
        assertNull(StringUtils.strip(null));
        assertEquals("", StringUtils.strip(""));
        assertEquals("", StringUtils.strip("   \t\n   "));
        assertEquals("abc", StringUtils.strip("  abc  "));
        assertEquals("ab c", StringUtils.strip(" ab c "));
    }

    @Test
    public void testEqualsAndCompareMethods() {
        // Test equals with null handling
        assertTrue(StringUtils.equals(null, null));
        assertFalse(StringUtils.equals(null, "abc"));
        assertFalse(StringUtils.equals("abc", null));
        assertTrue(StringUtils.equals("abc", "abc"));
        assertFalse(StringUtils.equals("abc", "ABC"));
        
        // Test equalsIgnoreCase
        assertTrue(StringUtils.equalsIgnoreCase(null, null));
        assertFalse(StringUtils.equalsIgnoreCase(null, "abc"));
        assertFalse(StringUtils.equalsIgnoreCase("abc", null));
        assertTrue(StringUtils.equalsIgnoreCase("abc", "ABC"));
        assertTrue(StringUtils.equalsIgnoreCase("AbC", "aBc"));
        assertFalse(StringUtils.equalsIgnoreCase("abc", "def"));
        
        // Test startsWith and endsWith
        assertTrue(StringUtils.startsWith("abcdef", "abc"));
        assertFalse(StringUtils.startsWith("abcdef", "def"));
        assertFalse(StringUtils.startsWith(null, "abc"));
        assertTrue(StringUtils.startsWith(null, null));
        
        assertTrue(StringUtils.endsWith("abcdef", "def"));
        assertFalse(StringUtils.endsWith("abcdef", "abc"));
        assertFalse(StringUtils.endsWith(null, "def"));
        assertTrue(StringUtils.endsWith(null, null));
        
        // Case insensitive versions
        assertTrue(StringUtils.startsWithIgnoreCase("ABCDEF", "abc"));
        assertTrue(StringUtils.endsWithIgnoreCase("ABCDEF", "def"));
        assertFalse(StringUtils.startsWithIgnoreCase("abcdef", "XYZ"));
    }

    @Test
    public void testSubstringAndIndexMethods() {
        // Test substring methods
        assertNull(StringUtils.substring(null, 0));
        assertEquals("", StringUtils.substring("", 0));
        assertEquals("abc", StringUtils.substring("abc", 0));
        assertEquals("bc", StringUtils.substring("abc", 1));
        assertEquals("c", StringUtils.substring("abc", 2));
        assertEquals("", StringUtils.substring("abc", 5));
        assertEquals("bc", StringUtils.substring("abc", -2));
        
        // Test substring with start and end
        assertNull(StringUtils.substring(null, 0, 1));
        assertEquals("", StringUtils.substring("", 0, 1));
        assertEquals("ab", StringUtils.substring("abc", 0, 2));
        assertEquals("", StringUtils.substring("abc", 2, 0));
        assertEquals("c", StringUtils.substring("abc", 2, 4));
        assertEquals("", StringUtils.substring("abc", 4, 6));
        assertEquals("", StringUtils.substring("abc", 2, 2));
        
        // Test indexOf methods
        assertEquals(-1, StringUtils.indexOf(null, 'a'));
        assertEquals(-1, StringUtils.indexOf("", 'a'));
        assertEquals(0, StringUtils.indexOf("abc", 'a'));
        assertEquals(1, StringUtils.indexOf("abc", 'b'));
        assertEquals(-1, StringUtils.indexOf("abc", 'd'));
        
        // Test contains methods
        assertFalse(StringUtils.contains(null, 'a'));
        assertFalse(StringUtils.contains("", 'a'));
        assertTrue(StringUtils.contains("abc", 'a'));
        assertFalse(StringUtils.contains("abc", 'd'));
        assertTrue(StringUtils.contains("abc", "ab"));
        assertFalse(StringUtils.contains("abc", "xyz"));
    }
}