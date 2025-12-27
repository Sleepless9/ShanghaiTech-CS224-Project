package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;

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
        assertTrue(StringUtils.isBlank("  \t\n\r"));
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
        assertEquals("test", StringUtils.strip("xxtestxx", "x"));
        assertEquals("  test  ", StringUtils.strip("x  test  x", "x"));
        assertEquals("test", StringUtils.strip("xyxytestxyxy", "xy"));
    }

    @Test
    public void testEqualsAndCompare() {
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
        assertFalse(StringUtils.equalsIgnoreCase("test", "best"));
        
        // Test contains
        assertFalse(StringUtils.contains(null, 'a'));
        assertFalse(StringUtils.contains("", 'a'));
        assertTrue(StringUtils.contains("abc", 'a'));
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
        assertTrue(StringUtils.containsIgnoreCase("ABC", "bc"));
        assertFalse(StringUtils.containsIgnoreCase("abc", "ac"));
    }

    @Test
    public void testSubstringMethods() {
        // Test substring
        assertNull(StringUtils.substring(null, 0));
        assertEquals("", StringUtils.substring("", 0));
        assertEquals("test", StringUtils.substring("test", 0));
        assertEquals("est", StringUtils.substring("test", 1));
        assertEquals("st", StringUtils.substring("test", 2));
        assertEquals("", StringUtils.substring("test", 4));
        assertEquals("test", StringUtils.substring("test", -4));
        assertEquals("st", StringUtils.substring("test", -2));
        
        // Test substring with start and end
        assertNull(StringUtils.substring(null, 0, 2));
        assertEquals("", StringUtils.substring("", 0, 2));
        assertEquals("te", StringUtils.substring("test", 0, 2));
        assertEquals("es", StringUtils.substring("test", 1, 3));
        assertEquals("", StringUtils.substring("test", 2, 2));
        assertEquals("", StringUtils.substring("test", 2, 1));
        assertEquals("te", StringUtils.substring("test", -4, 2));
        
        // Test left, right, mid
        assertNull(StringUtils.left(null, 2));
        assertEquals("", StringUtils.left("", 2));
        assertEquals("te", StringUtils.left("test", 2));
        assertEquals("test", StringUtils.left("test", 10));
        assertEquals("", StringUtils.left("test", -1));
        
        assertNull(StringUtils.right(null, 2));
        assertEquals("", StringUtils.right("", 2));
        assertEquals("st", StringUtils.right("test", 2));
        assertEquals("test", StringUtils.right("test", 10));
        assertEquals("", StringUtils.right("test", -1));
        
        assertNull(StringUtils.mid(null, 0, 2));
        assertEquals("", StringUtils.mid("", 0, 2));
        assertEquals("es", StringUtils.mid("test", 1, 2));
        assertEquals("est", StringUtils.mid("test", 1, 10));
        assertEquals("", StringUtils.mid("test", 10, 2));
        assertEquals("te", StringUtils.mid("test", -1, 2));
    }

    @Test
    public void testCaseConversionAndPadding() {
        // Test upper/lower case
        assertNull(StringUtils.upperCase(null));
        assertEquals("", StringUtils.upperCase(""));
        assertEquals("TEST", StringUtils.upperCase("test"));
        assertEquals("TEST", StringUtils.upperCase("TEST"));
        assertEquals("T EST", StringUtils.upperCase("t est"));
        
        assertNull(StringUtils.lowerCase(null));
        assertEquals("", StringUtils.lowerCase(""));
        assertEquals("test", StringUtils.lowerCase("TEST"));
        assertEquals("test", StringUtils.lowerCase("test"));
        assertEquals("t est", StringUtils.lowerCase("T EST"));
        
        // Test with locale
        assertEquals("TEST", StringUtils.upperCase("test", Locale.ENGLISH));
        assertEquals("test", StringUtils.lowerCase("TEST", Locale.ENGLISH));
        
        // Test capitalize and uncapitalize
        assertNull(StringUtils.capitalize(null));
        assertEquals("", StringUtils.capitalize(""));
        assertEquals("Test", StringUtils.capitalize("test"));
        assertEquals("TEST", StringUtils.capitalize("TEST"));
        assertEquals(" Test", StringUtils.capitalize(" test"));
        
        assertNull(StringUtils.uncapitalize(null));
        assertEquals("", StringUtils.uncapitalize(""));
        assertEquals("test", StringUtils.uncapitalize("Test"));
        assertEquals("tEST", StringUtils.uncapitalize("TEST"));
        assertEquals(" test", StringUtils.uncapitalize(" test"));
        
        // Test padding
        assertNull(StringUtils.leftPad(null, 5));
        assertEquals("     ", StringUtils.leftPad("", 5));
        assertEquals("  test", StringUtils.leftPad("test", 6));
        assertEquals("test", StringUtils.leftPad("test", 3));
        assertEquals("00test", StringUtils.leftPad("test", 6, '0'));
        
        assertNull(StringUtils.rightPad(null, 5));
        assertEquals("     ", StringUtils.rightPad("", 5));
        assertEquals("test  ", StringUtils.rightPad("test", 6));
        assertEquals("test", StringUtils.rightPad("test", 3));
        assertEquals("test00", StringUtils.rightPad("test", 6, '0'));
        
        // Test center
        assertNull(StringUtils.center(null, 5));
        assertEquals("  test ", StringUtils.center("test", 7));
        assertEquals(" test  ", StringUtils.center("test", 7, ' '));
        assertEquals("xxtestxx", StringUtils.center("test", 8, 'x'));
        assertEquals("test", StringUtils.center("test", 3));
    }
}