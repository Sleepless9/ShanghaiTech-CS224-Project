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

        // Test isNotEmpty which is opposite of isEmpty
        assertFalse(StringUtils.isNotEmpty(null));
        assertFalse(StringUtils.isNotEmpty(""));
        assertTrue(StringUtils.isNotEmpty(" "));
        assertTrue(StringUtils.isNotEmpty("abc"));
        
        // Verify the relationship between isEmpty and isNotEmpty
        String testStr = "test";
        assertEquals(!StringUtils.isEmpty(testStr), StringUtils.isNotEmpty(testStr));
    }

    @Test
    public void testIsBlankAndIsNotBlank() {
        // Test blank cases (null, empty, whitespace only)
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertTrue(StringUtils.isBlank("   \t\n"));
        
        // Test not blank cases
        assertFalse(StringUtils.isBlank("abc"));
        assertFalse(StringUtils.isBlank("  abc  "));
        
        // Test isNotBlank which is opposite of isBlank
        assertFalse(StringUtils.isNotBlank(null));
        assertFalse(StringUtils.isNotBlank(""));
        assertFalse(StringUtils.isNotBlank(" "));
        assertTrue(StringUtils.isNotBlank("abc"));
        assertTrue(StringUtils.isNotBlank("  abc  "));
    }

    @Test
    public void testTrimMethods() {
        // Test trim - removes control chars (<=32)
        assertNull(StringUtils.trim(null));
        assertEquals("", StringUtils.trim(""));
        assertEquals("", StringUtils.trim("   "));
        assertEquals("abc", StringUtils.trim("   abc   "));
        assertEquals("abc", StringUtils.trim("\t\n\rabc\t\n\r"));
        
        // Test trimToNull - returns null for empty results
        assertNull(StringUtils.trimToNull(null));
        assertNull(StringUtils.trimToNull(""));
        assertNull(StringUtils.trimToNull("   "));
        assertEquals("abc", StringUtils.trimToNull("   abc   "));
        
        // Test trimToEmpty - returns empty string for null input
        assertEquals("", StringUtils.trimToEmpty(null));
        assertEquals("", StringUtils.trimToEmpty(""));
        assertEquals("", StringUtils.trimToEmpty("   "));
        assertEquals("abc", StringUtils.trimToEmpty("   abc   "));
    }

    @Test
    public void testEqualsAndCompareMethods() {
        // Test equals with null handling
        assertTrue(StringUtils.equals(null, null));
        assertFalse(StringUtils.equals(null, "abc"));
        assertFalse(StringUtils.equals("abc", null));
        assertTrue(StringUtils.equals("abc", "abc"));
        assertFalse(StringUtils.equals("abc", "ABC"));
        assertTrue(StringUtils.equalsIgnoreCase("abc", "ABC"));
        assertTrue(StringUtils.equalsIgnoreCase(null, null));
        
        // Test startsWith and endsWith
        assertTrue(StringUtils.startsWith("abcdef", "abc"));
        assertFalse(StringUtils.startsWith("abcdef", "def"));
        assertTrue(StringUtils.startsWith(null, null));
        assertFalse(StringUtils.startsWith("abc", null));
        
        assertTrue(StringUtils.endsWith("abcdef", "def"));
        assertFalse(StringUtils.endsWith("abcdef", "abc"));
        assertTrue(StringUtils.endsWith(null, null));
        assertFalse(StringUtils.endsWith("abc", null));
        
        // Case insensitive tests
        assertTrue(StringUtils.startsWithIgnoreCase("ABCDEF", "abc"));
        assertTrue(StringUtils.endsWithIgnoreCase("ABCDEF", "def"));
    }

    @Test
    public void testSubstringMethods() {
        // Test basic substring extraction
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
        assertEquals("b", StringUtils.substring("abc", -2, -1));
        
        // Test substring before/after
        assertEquals("abc", StringUtils.substringBefore("abc.def", "."));
        assertEquals("def", StringUtils.substringAfter("abc.def", "."));
        assertEquals("abc.def", StringUtils.substringBefore("abc.def", "xyz"));
        assertEquals("", StringUtils.substringAfter("abc.def", "xyz"));
        assertEquals("abc", StringUtils.substringBeforeLast("abc.def.ghi", "."));
        assertEquals("ghi", StringUtils.substringAfterLast("abc.def.ghi", "."));
    }
}