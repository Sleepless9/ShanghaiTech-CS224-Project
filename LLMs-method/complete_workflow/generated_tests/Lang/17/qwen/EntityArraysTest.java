package org.apache.commons.lang3;

import static org.junit.Assert.*;
import org.junit.Test;

public class EntityArraysTest {

    @Test
    public void testIsEmptyAndIsNotEmpty() {
        // Test null and empty string
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty("abc"));
        
        // Test isNotEmpty (opposite of isEmpty)
        assertFalse(StringUtils.isNotEmpty(null));
        assertFalse(StringUtils.isNotEmpty(""));
        assertTrue(StringUtils.isNotEmpty(" "));
        assertTrue(StringUtils.isNotEmpty("abc"));
    }

    @Test
    public void testIsBlankAndIsNotBlank() {
        // Test blank strings (null, empty, whitespace only)
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertTrue(StringUtils.isBlank("   \t\n\r"));
        
        // Test non-blank strings
        assertFalse(StringUtils.isBlank("abc"));
        assertFalse(StringUtils.isBlank("  abc  "));
        
        // Test isNotBlank (opposite of isBlank)
        assertFalse(StringUtils.isNotBlank(null));
        assertFalse(StringUtils.isNotBlank(""));
        assertFalse(StringUtils.isNotBlank(" "));
        assertTrue(StringUtils.isNotBlank("abc"));
        assertTrue(StringUtils.isNotBlank("  abc  "));
    }

    @Test
    public void testTrimMethods() {
        String testString = "  hello world  ";
        
        // Test trim
        assertEquals("hello world", StringUtils.trim(testString));
        assertNull(StringUtils.trim(null));
        assertEquals("", StringUtils.trim(""));
        
        // Test trimToNull
        assertEquals("hello world", StringUtils.trimToNull(testString));
        assertNull(StringUtils.trimToNull(null));
        assertNull(StringUtils.trimToNull(""));
        assertNull(StringUtils.trimToNull("   "));
        
        // Test trimToEmpty
        assertEquals("hello world", StringUtils.trimToEmpty(testString));
        assertEquals("", StringUtils.trimToEmpty(null));
        assertEquals("", StringUtils.trimToEmpty(""));
        assertEquals("", StringUtils.trimToEmpty("   "));
    }

    @Test
    public void testEqualsMethods() {
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
        assertTrue(StringUtils.equalsIgnoreCase("abc", "ABC"));
        assertTrue(StringUtils.equalsIgnoreCase("AbC", "aBc"));
    }

    @Test
    public void testSubstringMethods() {
        String testString = "hello world";
        
        // Test substring with start index
        assertEquals("hello world", StringUtils.substring(testString, 0));
        assertEquals("world", StringUtils.substring(testString, 6));
        assertEquals("ld", StringUtils.substring(testString, -2));
        assertEquals("", StringUtils.substring(testString, 50));
        assertNull(StringUtils.substring(null, 0));
        
        // Test substring with start and end
        assertEquals("hello", StringUtils.substring(testString, 0, 5));
        assertEquals("worl", StringUtils.substring(testString, 6, 10));
        assertEquals("", StringUtils.substring(testString, 4, 2));
        assertEquals("o wo", StringUtils.substring(testString, -7, -2));
        assertNull(StringUtils.substring(null, 0, 5));
    }

    @Test
    public void testStartsWithEndsWith() {
        String testString = "hello world";
        
        // Test startsWith
        assertTrue(StringUtils.startsWith(testString, "hello"));
        assertFalse(StringUtils.startsWith(testString, "world"));
        assertTrue(StringUtils.startsWith("", ""));
        assertFalse(StringUtils.startsWith("hello", null));
        assertFalse(StringUtils.startsWith(null, "hello"));
        
        // Test startsWithIgnoreCase
        assertTrue(StringUtils.startsWithIgnoreCase("Hello World", "hello"));
        assertFalse(StringUtils.startsWithIgnoreCase("Hello World", "world"));
        
        // Test endsWith
        assertTrue(StringUtils.endsWith(testString, "world"));
        assertFalse(StringUtils.endsWith(testString, "hello"));
        assertTrue(StringUtils.endsWith("", ""));
        assertFalse(StringUtils.endsWith("hello", null));
        assertFalse(StringUtils.endsWith(null, "hello"));
        
        // Test endsWithIgnoreCase
        assertTrue(StringUtils.endsWithIgnoreCase("Hello World", "WORLD"));
        assertFalse(StringUtils.endsWithIgnoreCase("Hello World", "HELLO"));
    }
}