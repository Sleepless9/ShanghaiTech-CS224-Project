package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class NumberUtilsTest {

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
        assertTrue(StringUtils.isBlank("  \t\n\r  "));
        assertFalse(StringUtils.isBlank("test"));
        assertFalse(StringUtils.isBlank("  test  "));
        
        // Test isNotEmpty and isNotBlank
        assertFalse(StringUtils.isNotEmpty(null));
        assertFalse(StringUtils.isNotEmpty(""));
        assertTrue(StringUtils.isNotEmpty(" "));
        assertTrue(StringUtils.isNotEmpty("test"));
        
        assertFalse(StringUtils.isNotBlank(null));
        assertFalse(StringUtils.isNotBlank(""));
        assertFalse(StringUtils.isNotBlank(" "));
        assertTrue(StringUtils.isNotBlank("test"));
        assertTrue(StringUtils.isNotBlank("  test  "));
    }

    @Test
    public void testTrimAndStrip() {
        // Test trim
        assertNull(StringUtils.trim(null));
        assertEquals("", StringUtils.trim(""));
        assertEquals("", StringUtils.trim("   "));
        assertEquals("test", StringUtils.trim("test"));
        assertEquals("test", StringUtils.trim("  test  "));
        assertEquals("test", StringUtils.trim("\ttest\n"));
        
        // Test trimToNull and trimToEmpty
        assertNull(StringUtils.trimToNull(null));
        assertNull(StringUtils.trimToNull(""));
        assertNull(StringUtils.trimToNull("   "));
        assertEquals("test", StringUtils.trimToNull("test"));
        assertEquals("test", StringUtils.trimToNull("  test  "));
        
        assertEquals("", StringUtils.trimToEmpty(null));
        assertEquals("", StringUtils.trimToEmpty(""));
        assertEquals("", StringUtils.trimToEmpty("   "));
        assertEquals("test", StringUtils.trimToEmpty("test"));
        assertEquals("test", StringUtils.trimToEmpty("  test  "));
        
        // Test strip
        assertNull(StringUtils.strip(null));
        assertEquals("", StringUtils.strip(""));
        assertEquals("", StringUtils.strip("   "));
        assertEquals("test", StringUtils.strip("test"));
        assertEquals("test", StringUtils.strip("  test  "));
        assertEquals("test", StringUtils.strip("\ttest\n"));
        
        // Test strip with specific characters
        assertEquals("test", StringUtils.strip("xxxtestxxx", "x"));
        assertEquals("test", StringUtils.strip("xyztestzyx", "xyz"));
        assertEquals("  test  ", StringUtils.strip("  test  ", null));
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
        assertTrue(StringUtils.equalsIgnoreCase("TeSt", "tEsT"));
        
        // Test contains
        assertFalse(StringUtils.contains(null, 'a'));
        assertFalse(StringUtils.contains("", 'a'));
        assertTrue(StringUtils.contains("abc", 'a'));
        assertTrue(StringUtils.contains("abc", 'b'));
        assertFalse(StringUtils.contains("abc", 'z'));
        
        assertFalse(StringUtils.contains(null, "ab"));
        assertFalse(StringUtils.contains("abc", null));
        assertTrue(StringUtils.contains("abc", ""));
        assertTrue(StringUtils.contains("abc", "a"));
        assertTrue(StringUtils.contains("abc", "bc"));
        assertFalse(StringUtils.contains("abc", "ac"));
        
        // Test containsIgnoreCase
        assertFalse(StringUtils.containsIgnoreCase(null, "ab"));
        assertFalse(StringUtils.containsIgnoreCase("abc", null));
        assertTrue(StringUtils.containsIgnoreCase("abc", ""));
        assertTrue(StringUtils.containsIgnoreCase("abc", "A"));
        assertTrue(StringUtils.containsIgnoreCase("ABC", "b"));
        assertTrue(StringUtils.containsIgnoreCase("abc", "BC"));
        assertFalse(StringUtils.containsIgnoreCase("abc", "AC"));
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
        assertEquals("abc", StringUtils.substring("abc", -4));
        
        // Test substring with start and end
        assertNull(StringUtils.substring(null, 0, 2));
        assertEquals("", StringUtils.substring("", 0, 2));
        assertEquals("ab", StringUtils.substring("abc", 0, 2));
        assertEquals("", StringUtils.substring("abc", 2, 0));
        assertEquals("c", StringUtils.substring("abc", 2, 4));
        assertEquals("", StringUtils.substring("abc", 4, 6));
        assertEquals("b", StringUtils.substring("abc", -2, -1));
        assertEquals("ab", StringUtils.substring("abc", -4, 2));
        
        // Test left, right, mid
        assertNull(StringUtils.left(null, 2));
        assertEquals("", StringUtils.left("", 2));
        assertEquals("", StringUtils.left("abc", -1));
        assertEquals("", StringUtils.left("abc", 0));
        assertEquals("ab", StringUtils.left("abc", 2));
        assertEquals("abc", StringUtils.left("abc", 4));
        
        assertNull(StringUtils.right(null, 2));
        assertEquals("", StringUtils.right("", 2));
        assertEquals("", StringUtils.right("abc", -1));
        assertEquals("", StringUtils.right("abc", 0));
        assertEquals("bc", StringUtils.right("abc", 2));
        assertEquals("abc", StringUtils.right("abc", 4));
        
        assertNull(StringUtils.mid(null, 0, 2));
        assertEquals("", StringUtils.mid("", 0, 2));
        assertEquals("", StringUtils.mid("abc", 0, -1));
        assertEquals("ab", StringUtils.mid("abc", 0, 2));
        assertEquals("abc", StringUtils.mid("abc", 0, 4));
        assertEquals("c", StringUtils.mid("abc", 2, 4));
        assertEquals("", StringUtils.mid("abc", 4, 2));
        assertEquals("ab", StringUtils.mid("abc", -2, 2));
    }

    @Test
    public void testSplitAndJoin() {
        // Test split
        assertNull(StringUtils.split(null));
        assertEquals(0, StringUtils.split("").length);
        assertArrayEquals(new String[]{"abc", "def"}, StringUtils.split("abc def"));
        assertArrayEquals(new String[]{"abc", "def"}, StringUtils.split("abc  def"));
        assertArrayEquals(new String[]{"abc"}, StringUtils.split(" abc "));
        
        // Test split with separator
        assertNull(StringUtils.split(null, '.'));
        assertEquals(0, StringUtils.split("", '.').length);
        assertArrayEquals(new String[]{"a", "b", "c"}, StringUtils.split("a.b.c", '.'));
        assertArrayEquals(new String[]{"a", "b", "c"}, StringUtils.split("a..b.c", '.'));
        assertArrayEquals(new String[]{"a:b:c"}, StringUtils.split("a:b:c", '.'));
        
        // Test join
        assertNull(StringUtils.join((Object[])null));
        assertEquals("", StringUtils.join());
        assertEquals("", StringUtils.join(new Object[]{null}));
        assertEquals("abc", StringUtils.join(new Object[]{"a", "b", "c"}));
        assertEquals("anullb", StringUtils.join(new Object[]{"a", null, "b"}));
        
        // Test join with separator
        assertNull(StringUtils.join((Object[])null, ','));
        assertEquals("", StringUtils.join(new Object[]{}, ','));
        assertEquals("", StringUtils.join(new Object[]{null}, ','));
        assertEquals("a,b,c", StringUtils.join(new Object[]{"a", "b", "c"}, ','));
        assertEquals("a,,b", StringUtils.join(new Object[]{"a", null, "b"}, ','));
        
        // Test join with separator string
        assertNull(StringUtils.join((Object[])null, "--"));
        assertEquals("", StringUtils.join(new Object[]{}, "--"));
        assertEquals("", StringUtils.join(new Object[]{null}, "--"));
        assertEquals("a--b--c", StringUtils.join(new Object[]{"a", "b", "c"}, "--"));
        assertEquals("a--null--b", StringUtils.join(new Object[]{"a", null, "b"}, "--"));
    }
}