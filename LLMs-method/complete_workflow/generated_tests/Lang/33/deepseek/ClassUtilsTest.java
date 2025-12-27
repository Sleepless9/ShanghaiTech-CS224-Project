package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClassUtilsTest {

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
        assertEquals("abc", StringUtils.strip("abc"));
        
        // Test strip with custom characters
        assertEquals("abc", StringUtils.strip("xxabcxx", "x"));
        assertEquals("abc", StringUtils.strip("yzabczy", "yz"));
        assertEquals("  abc  ", StringUtils.strip("  abc  ", "x"));
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
        
        // Test contains with String
        assertFalse(StringUtils.contains(null, "abc"));
        assertFalse(StringUtils.contains("abc", null));
        assertTrue(StringUtils.contains("", ""));
        assertTrue(StringUtils.contains("abc", ""));
        assertTrue(StringUtils.contains("abc", "a"));
        assertFalse(StringUtils.contains("abc", "z"));
    }

    @Test
    public void testSubstringMethods() {
        // Test substring
        assertNull(StringUtils.substring(null, 0));
        assertEquals("", StringUtils.substring("", 0));
        assertEquals("abc", StringUtils.substring("abc", 0));
        assertEquals("bc", StringUtils.substring("abc", 1));
        assertEquals("c", StringUtils.substring("abc", 2));
        assertEquals("", StringUtils.substring("abc", 3));
        assertEquals("bc", StringUtils.substring("abc", -2));
        
        // Test left
        assertNull(StringUtils.left(null, 2));
        assertEquals("", StringUtils.left("", 2));
        assertEquals("ab", StringUtils.left("abc", 2));
        assertEquals("abc", StringUtils.left("abc", 5));
        assertEquals("", StringUtils.left("abc", -1));
        assertEquals("", StringUtils.left("abc", 0));
        
        // Test right
        assertNull(StringUtils.right(null, 2));
        assertEquals("", StringUtils.right("", 2));
        assertEquals("bc", StringUtils.right("abc", 2));
        assertEquals("abc", StringUtils.right("abc", 5));
        assertEquals("", StringUtils.right("abc", -1));
        assertEquals("", StringUtils.right("abc", 0));
    }

    @Test
    public void testSplitAndJoin() {
        // Test split
        assertNull(StringUtils.split(null));
        assertArrayEquals(new String[]{}, StringUtils.split(""));
        assertArrayEquals(new String[]{"ab", "cd", "ef"}, StringUtils.split("ab cd ef"));
        assertArrayEquals(new String[]{"ab", "cd", "ef"}, StringUtils.split("ab   cd ef"));
        assertArrayEquals(new String[]{"ab", "cd", "ef"}, StringUtils.split("ab\t\ncd\ref"));
        
        // Test split with separator
        assertNull(StringUtils.split(null, ','));
        assertArrayEquals(new String[]{}, StringUtils.split("", ','));
        assertArrayEquals(new String[]{"a", "b", "c"}, StringUtils.split("a,b,c", ','));
        assertArrayEquals(new String[]{"a", "b", "c"}, StringUtils.split("a,,b,c", ','));
        assertArrayEquals(new String[]{"a:b:c"}, StringUtils.split("a:b:c", ','));
        
        // Test join
        assertNull(StringUtils.join((Object[])null));
        assertEquals("", StringUtils.join(new String[]{}));
        assertEquals("abc", StringUtils.join(new String[]{"a", "b", "c"}));
        assertEquals("a,b,c", StringUtils.join(new String[]{"a", "b", "c"}, ','));
        assertEquals("a--b--c", StringUtils.join(new String[]{"a", "b", "c"}, "--"));
        assertEquals("a null c", StringUtils.join(new String[]{"a", null, "c"}, ' '));
    }
}