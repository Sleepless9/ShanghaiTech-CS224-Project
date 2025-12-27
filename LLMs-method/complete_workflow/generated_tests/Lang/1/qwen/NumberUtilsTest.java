package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberUtilsTest {

    @Test
    public void testIsEmptyAndIsNotEmpty() {
        // Test isEmpty - null and empty cases
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty("abc"));
        
        // Test isNotEmpty - opposite of isEmpty
        assertFalse(StringUtils.isNotEmpty(null));
        assertFalse(StringUtils.isNotEmpty(""));
        assertTrue(StringUtils.isNotEmpty(" "));
        assertTrue(StringUtils.isNotEmpty("abc"));
        
        // Additional boundary cases
        assertFalse(StringUtils.isNotEmpty("\t\n"));
        assertTrue(StringUtils.isNotEmpty("   "));
    }

    @Test
    public void testIsBlankAndIsNotBlank() {
        // Test isBlank - null, empty, whitespace only
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertTrue(StringUtils.isBlank("  \t\n  "));
        assertFalse(StringUtils.isBlank("abc"));
        assertFalse(StringUtils.isBlank(" a "));
        
        // Test isNotBlank - opposite of isBlank
        assertFalse(StringUtils.isNotBlank(null));
        assertFalse(StringUtils.isNotBlank(""));
        assertFalse(StringUtils.isNotBlank(" "));
        assertFalse(StringUtils.isNotBlank("  \t\n  "));
        assertTrue(StringUtils.isNotBlank("abc"));
        assertTrue(StringUtils.isNotBlank(" a "));
    }

    @Test
    public void testTrimMethods() {
        // Test trim - removes control chars (<=32)
        assertNull(StringUtils.trim(null));
        assertEquals("", StringUtils.trim(""));
        assertEquals("", StringUtils.trim(" \t\n\r ")); // all control chars
        assertEquals("abc", StringUtils.trim("  abc  "));
        assertEquals("abc", StringUtils.trim("  abc\t\n\r"));
        
        // Test trimToNull - returns null for empty/whitespace-only
        assertNull(StringUtils.trimToNull(null));
        assertNull(StringUtils.trimToNull(""));
        assertNull(StringUtils.trimToNull("  \t\n  "));
        assertEquals("abc", StringUtils.trimToNull("  abc  "));
        
        // Test trimToEmpty - returns empty string for null input
        assertEquals("", StringUtils.trimToEmpty(null));
        assertEquals("", StringUtils.trimToEmpty(""));
        assertEquals("", StringUtils.trimToEmpty(" \t\n\r "));
        assertEquals("abc", StringUtils.trimToEmpty("  abc  "));
    }

    @Test
    public void testStripMethods() {
        // Test strip - removes whitespace as defined by Character.isWhitespace
        assertNull(StringUtils.strip(null));
        assertEquals("", StringUtils.strip(""));
        assertEquals("", StringUtils.strip(" \t\n\r\u00A0")); // various whitespace
        assertEquals("abc", StringUtils.strip("  abc  "));
        assertEquals("ab c", StringUtils.strip(" ab c "));
        
        // Test stripToNull - returns null if result would be empty
        assertNull(StringUtils.stripToNull(null));
        assertNull(StringUtils.stripToNull(""));
        assertNull(StringUtils.stripToNull(" \t\n\r "));
        assertEquals("abc", StringUtils.stripToNull("  abc  "));
        
        // Test stripToEmpty - returns empty string for null input
        assertEquals("", StringUtils.stripToEmpty(null));
        assertEquals("", StringUtils.stripToEmpty(""));
        assertEquals("", StringUtils.stripToEmpty(" \t\n\r "));
        assertEquals("abc", StringUtils.stripToEmpty("  abc  "));
    }

    @Test
    public void testEqualsMethods() {
        // Test equals - case sensitive comparison
        assertTrue(StringUtils.equals(null, null));
        assertFalse(StringUtils.equals(null, "abc"));
        assertFalse(StringUtils.equals("abc", null));
        assertTrue(StringUtils.equals("abc", "abc"));
        assertFalse(StringUtils.equals("abc", "ABC"));
        assertTrue(StringUtils.equals("", ""));
        
        // Test equalsIgnoreCase - case insensitive comparison
        assertTrue(StringUtils.equalsIgnoreCase(null, null));
        assertFalse(StringUtils.equalsIgnoreCase(null, "abc"));
        assertFalse(StringUtils.equalsIgnoreCase("abc", null));
        assertTrue(StringUtils.equalsIgnoreCase("abc", "ABC"));
        assertTrue(StringUtils.equalsIgnoreCase("AbC", "aBc"));
        assertFalse(StringUtils.equalsIgnoreCase("abc", "def"));
    }
}