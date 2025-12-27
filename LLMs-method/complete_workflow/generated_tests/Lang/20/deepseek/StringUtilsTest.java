package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class StringUtilsTest {

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
        assertTrue(StringUtils.isBlank("  \t\n\r\f"));
        assertFalse(StringUtils.isBlank("abc"));
        assertFalse(StringUtils.isBlank("  abc  "));
        
        // Test isNotEmpty and isNotBlank
        assertFalse(StringUtils.isNotEmpty(null));
        assertFalse(StringUtils.isNotEmpty(""));
        assertTrue(StringUtils.isNotEmpty(" "));
        
        assertFalse(StringUtils.isNotBlank(null));
        assertFalse(StringUtils.isNotBlank(""));
        assertFalse(StringUtils.isNotBlank(" "));
        assertTrue(StringUtils.isNotBlank("abc"));
    }

    @Test
    public void testTrimAndStrip() {
        // Test trim
        assertNull(StringUtils.trim(null));
        assertEquals("", StringUtils.trim(""));
        assertEquals("", StringUtils.trim("   "));
        assertEquals("abc", StringUtils.trim("abc"));
        assertEquals("abc", StringUtils.trim("  abc  "));
        
        // Test trimToNull and trimToEmpty
        assertNull(StringUtils.trimToNull(null));
        assertNull(StringUtils.trimToNull(""));
        assertNull(StringUtils.trimToNull("   "));
        assertEquals("abc", StringUtils.trimToNull("  abc  "));
        
        assertEquals("", StringUtils.trimToEmpty(null));
        assertEquals("", StringUtils.trimToEmpty(""));
        assertEquals("", StringUtils.trimToEmpty("   "));
        assertEquals("abc", StringUtils.trimToEmpty("  abc  "));
        
        // Test strip
        assertNull(StringUtils.strip(null));
        assertEquals("", StringUtils.strip(""));
        assertEquals("", StringUtils.strip("   "));
        assertEquals("abc", StringUtils.strip("abc"));
        assertEquals("abc", StringUtils.strip("  abc  "));
        assertEquals("ab c", StringUtils.strip(" ab c "));
        
        // Test strip with specific characters
        assertEquals("abc", StringUtils.strip("  abc  ", null));
        assertEquals("  abc", StringUtils.strip("  abcyx", "xyz"));
        assertEquals("abc", StringUtils.strip("yxabc  ", "xyz"));
    }

    @Test
    public void testEqualsAndContains() {
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
        
        // Test contains
        assertFalse(StringUtils.contains(null, 'a'));
        assertFalse(StringUtils.contains("", 'a'));
        assertTrue(StringUtils.contains("abc", 'a'));
        assertFalse(StringUtils.contains("abc", 'z'));
        
        assertFalse(StringUtils.contains(null, "ab"));
        assertFalse(StringUtils.contains("abc", null));
        assertTrue(StringUtils.contains("abc", ""));
        assertTrue(StringUtils.contains("abc", "a"));
        assertFalse(StringUtils.contains("abc", "z"));
        
        // Test containsIgnoreCase
        assertFalse(StringUtils.containsIgnoreCase(null, "ab"));
        assertFalse(StringUtils.containsIgnoreCase("abc", null));
        assertTrue(StringUtils.containsIgnoreCase("abc", ""));
        assertTrue(StringUtils.containsIgnoreCase("abc", "A"));
        assertFalse(StringUtils.containsIgnoreCase("abc", "Z"));
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
        assertNull(StringUtils.left(null, 2));
        assertEquals("", StringUtils.left("abc", -1));
        assertEquals("ab", StringUtils.left("abc", 2));
        assertEquals("abc", StringUtils.left("abc", 4));
        
        assertNull(StringUtils.right(null, 2));
        assertEquals("", StringUtils.right("abc", -1));
        assertEquals("bc", StringUtils.right("abc", 2));
        assertEquals("abc", StringUtils.right("abc", 4));
        
        assertNull(StringUtils.mid(null, 0, 2));
        assertEquals("", StringUtils.mid("abc", 4, 2));
        assertEquals("ab", StringUtils.mid("abc", 0, 2));
        assertEquals("c", StringUtils.mid("abc", 2, 4));
        assertEquals("ab", StringUtils.mid("abc", -2, 2));
    }

    @Test
    public void testIndexOfAndLastIndexOf() {
        // Test indexOf
        assertEquals(-1, StringUtils.indexOf(null, 'a'));
        assertEquals(-1, StringUtils.indexOf("", 'a'));
        assertEquals(0, StringUtils.indexOf("abc", 'a'));
        assertEquals(2, StringUtils.indexOf("abc", 'c'));
        assertEquals(-1, StringUtils.indexOf("abc", 'z'));
        
        assertEquals(-1, StringUtils.indexOf(null, "ab"));
        assertEquals(-1, StringUtils.indexOf("abc", null));
        assertEquals(0, StringUtils.indexOf("abc", ""));
        assertEquals(0, StringUtils.indexOf("abc", "a"));
        assertEquals(1, StringUtils.indexOf("abc", "bc"));
        assertEquals(-1, StringUtils.indexOf("abc", "z"));
        
        // Test lastIndexOf
        assertEquals(-1, StringUtils.lastIndexOf(null, 'a'));
        assertEquals(-1, StringUtils.lastIndexOf("", 'a'));
        assertEquals(2, StringUtils.lastIndexOf("abcabc", 'c'));
        assertEquals(3, StringUtils.lastIndexOf("abcabc", 'a', 4));
        
        // Test indexOfIgnoreCase
        assertEquals(-1, StringUtils.indexOfIgnoreCase(null, "ab"));
        assertEquals(-1, StringUtils.indexOfIgnoreCase("abc", null));
        assertEquals(0, StringUtils.indexOfIgnoreCase("abc", ""));
        assertEquals(0, StringUtils.indexOfIgnoreCase("abc", "A"));
        assertEquals(1, StringUtils.indexOfIgnoreCase("abc", "BC"));
    }
}