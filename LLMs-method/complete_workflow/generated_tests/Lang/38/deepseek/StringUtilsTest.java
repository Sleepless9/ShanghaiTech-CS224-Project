package org.apache.commons.lang3;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class StringUtilsTest {

    @Test
    public void testIsEmptyAndIsBlank() {
        // Test isEmpty
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty("test"));
        
        // Test isBlank
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertTrue(StringUtils.isBlank("  \t\n\r"));
        assertFalse(StringUtils.isBlank("test"));
        assertFalse(StringUtils.isBlank(" test "));
    }

    @Test
    public void testTrimAndStrip() {
        // Test trim
        assertNull(StringUtils.trim(null));
        assertEquals("", StringUtils.trim(""));
        assertEquals("", StringUtils.trim("   "));
        assertEquals("test", StringUtils.trim("test"));
        assertEquals("test", StringUtils.trim("  test  "));
        
        // Test strip
        assertNull(StringUtils.strip(null));
        assertEquals("", StringUtils.strip(""));
        assertEquals("", StringUtils.strip("   "));
        assertEquals("test", StringUtils.strip("test"));
        assertEquals("test", StringUtils.strip("  test  "));
        assertEquals("test", StringUtils.strip("\t\ntest\r"));
        
        // Test strip with specific characters
        assertEquals("test", StringUtils.strip("xyxtestyyx", "xy"));
        assertEquals("  test  ", StringUtils.strip("  test  ", ""));
    }

    @Test
    public void testEqualsAndContains() {
        // Test equals
        assertTrue(StringUtils.equals(null, null));
        assertFalse(StringUtils.equals(null, "test"));
        assertFalse(StringUtils.equals("test", null));
        assertTrue(StringUtils.equals("test", "test"));
        assertFalse(StringUtils.equals("test", "TEST"));
        
        // Test equalsIgnoreCase
        assertTrue(StringUtils.equalsIgnoreCase(null, null));
        assertFalse(StringUtils.equalsIgnoreCase(null, "test"));
        assertFalse(StringUtils.equalsIgnoreCase("test", null));
        assertTrue(StringUtils.equalsIgnoreCase("test", "test"));
        assertTrue(StringUtils.equalsIgnoreCase("test", "TEST"));
        
        // Test contains
        assertFalse(StringUtils.contains(null, 'a'));
        assertFalse(StringUtils.contains("", 'a'));
        assertTrue(StringUtils.contains("abc", 'a'));
        assertFalse(StringUtils.contains("abc", 'z'));
        
        // Test contains with String
        assertFalse(StringUtils.contains(null, "ab"));
        assertFalse(StringUtils.contains("abc", null));
        assertTrue(StringUtils.contains("abc", "ab"));
        assertFalse(StringUtils.contains("abc", "z"));
    }

    @Test
    public void testSubstringMethods() {
        // Test substring
        assertNull(StringUtils.substring(null, 0));
        assertEquals("", StringUtils.substring("", 0));
        assertEquals("test", StringUtils.substring("test", 0));
        assertEquals("st", StringUtils.substring("test", 2));
        assertEquals("", StringUtils.substring("test", 4));
        assertEquals("est", StringUtils.substring("test", -3));
        
        // Test substring with start and end
        assertNull(StringUtils.substring(null, 0, 2));
        assertEquals("", StringUtils.substring("", 0, 2));
        assertEquals("te", StringUtils.substring("test", 0, 2));
        assertEquals("", StringUtils.substring("test", 2, 2));
        assertEquals("es", StringUtils.substring("test", 1, 3));
        assertEquals("te", StringUtils.substring("test", -4, 2));
        
        // Test substringBefore and substringAfter
        assertEquals("test", StringUtils.substringBefore("test.txt", "."));
        assertEquals("txt", StringUtils.substringAfter("test.txt", "."));
        assertEquals("test.txt", StringUtils.substringBefore("test.txt", ","));
        assertEquals("", StringUtils.substringAfter("test.txt", ","));
    }

    @Test
    public void testJoinAndSplit() {
        // Test join with array
        assertNull(StringUtils.join((Object[]) null, ","));
        assertEquals("", StringUtils.join(new String[]{}, ","));
        assertEquals("a,b,c", StringUtils.join(new String[]{"a", "b", "c"}, ","));
        assertEquals("a,,c", StringUtils.join(new String[]{"a", null, "c"}, ","));
        
        // Test split
        assertNull(StringUtils.split(null, ","));
        assertEquals(0, StringUtils.split("", ",").length);
        assertArrayEquals(new String[]{"a", "b", "c"}, StringUtils.split("a,b,c", ","));
        assertArrayEquals(new String[]{"a", "b", "c"}, StringUtils.split("a,b,c,", ","));
        assertArrayEquals(new String[]{"a", "b", "c"}, StringUtils.split("a b c", " "));
        
        // Test split with max parameter
        assertArrayEquals(new String[]{"a", "b,c"}, StringUtils.split("a,b,c", ",", 2));
        assertArrayEquals(new String[]{"a", "b", "c"}, StringUtils.split("a,b,c", ",", 3));
    }
}