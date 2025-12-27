package org.apache.commons.lang3;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testIsEmpty() {
        // Normal cases
        assertThat(StringUtils.isEmpty(""), is(true));
        assertThat(StringUtils.isEmpty("hello"), is(false));
        assertThat(StringUtils.isEmpty(" "), is(false));
        
        // Boundary cases
        assertThat(StringUtils.isEmpty((CharSequence) null), is(true));
        assertThat(StringUtils.isEmpty("   "), is(false));
        
        // Empty string vs null
        String emptyStr = "";
        String nullStr = null;
        assertThat(StringUtils.isEmpty(emptyStr), is(true));
        assertThat(StringUtils.isEmpty(nullStr), is(true));
    }
    
    @Test
    public void testIsBlank() {
        // Normal cases
        assertThat(StringUtils.isBlank("  \t\n\r  "), is(true));
        assertThat(StringUtils.isBlank("hello"), is(false));
        assertThat(StringUtils.isBlank(" hello "), is(false));
        
        // Boundary cases
        assertThat(StringUtils.isBlank((CharSequence) null), is(true));
        assertThat(StringUtils.isBlank(""), is(true));
        assertThat(StringUtils.isBlank(" \t \n "), is(true));
        
        // Edge case with mixed whitespace and text
        assertThat(StringUtils.isBlank("a \t \n "), is(false));
        assertThat(StringUtils.isBlank(" \t \n a"), is(false));
    }
    
    @Test
    public void testTrim() {
        // Normal cases
        assertThat(StringUtils.trim("  hello  "), is("hello"));
        assertThat(StringUtils.trim("hello"), is("hello"));
        assertThat(StringUtils.trim("  hello"), is("hello"));
        
        // Boundary cases
        assertThat(StringUtils.trim((String) null), is(nullValue()));
        assertThat(StringUtils.trim(""), is(""));
        assertThat(StringUtils.trim("     "), is(""));
        assertThat(StringUtils.trim("\t\n\r\f"), is(""));
        
        // Multiple whitespace characters
        assertThat(StringUtils.trim("\t  hello \n  "), is("hello"));
    }
    
    @Test
    public void testEquals() {
        // Normal cases
        assertThat(StringUtils.equals("hello", "hello"), is(true));
        assertThat(StringUtils.equals("hello", "world"), is(false));
        assertThat(StringUtils.equals("", ""), is(true));
        
        // Boundary cases
        assertThat(StringUtils.equals((CharSequence) null, null), is(true));
        assertThat(StringUtils.equals("hello", null), is(false));
        assertThat(StringUtils.equals(null, "hello"), is(false));
        
        // Case sensitivity
        assertThat(StringUtils.equals("Hello", "hello"), is(false));
        assertThat(StringUtils.equals("HELLO", "hello"), is(false));
    }
    
    @Test
    public void testContains() {
        // Normal cases
        assertThat(StringUtils.contains("hello world", "world"), is(true));
        assertThat(StringUtils.contains("hello world", "earth"), is(false));
        assertThat(StringUtils.contains("abc", "ab"), is(true));
        
        // Boundary cases
        assertThat(StringUtils.contains((CharSequence) null, "test"), is(false));
        assertThat(StringUtils.contains("test", (CharSequence) null), is(false));
        assertThat(StringUtils.contains("", ""), is(true));
        assertThat(StringUtils.contains("test", ""), is(true));
        
        // Overlapping cases
        assertThat(StringUtils.contains("aaaa", "aa"), is(true));
        assertThat(StringUtils.contains("abcabc", "cab"), is(true));
    }
}