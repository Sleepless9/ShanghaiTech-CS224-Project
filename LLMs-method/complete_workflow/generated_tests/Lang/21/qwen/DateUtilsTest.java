package org.apache.commons.lang3;

import static org.junit.Assert.*;
import org.junit.Test;

public class DateUtilsTest {

    @Test
    public void testIsEmptyAndIsNotEmpty() {
        // Test null and empty string cases
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
        assertTrue(StringUtils.isBlank("  \t\n  "));
        assertFalse(StringUtils.isBlank("abc"));
        assertFalse(StringUtils.isBlank("  abc  "));
        
        // Test isNotBlank (opposite of isBlank)
        assertFalse(StringUtils.isNotBlank(null));
        assertFalse(StringUtils.isNotBlank(""));
        assertFalse(StringUtils.isNotBlank(" "));
        assertFalse(StringUtils.isNotBlank("  \t\n  "));
        assertTrue(StringUtils.isNotBlank("abc"));
        assertTrue(StringUtils.isNotBlank("  abc  "));
    }
    
    @Test
    public void testTrimMethods() {
        // Test trim variants with various whitespace combinations
        assertNull(StringUtils.trim(null));
        assertEquals("", StringUtils.trim(""));
        assertEquals("", StringUtils.trim("   \t\n   "));
        assertEquals("abc", StringUtils.trim("   abc   "));
        assertEquals("abc def", StringUtils.trim("   abc def   "));
        
        // Test trimToNull
        assertNull(StringUtils.trimToNull(null));
        assertNull(StringUtils.trimToNull(""));
        assertNull(StringUtils.trimToNull("   "));
        assertEquals("abc", StringUtils.trimToNull("   abc   "));
        
        // Test trimToEmpty
        assertEquals("", StringUtils.trimToEmpty(null));
        assertEquals("", StringUtils.trimToEmpty(""));
        assertEquals("", StringUtils.trimToEmpty("   "));
        assertEquals("abc", StringUtils.trimToEmpty("   abc   "));
    }
    
    @Test
    public void testEqualsMethods() {
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
    }
    
    @Test
    public void testSubstringMethods() {
        // Test substring with various start/end positions
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
        assertEquals("b", StringUtils.substring("abc", -2, -1));
    }
}