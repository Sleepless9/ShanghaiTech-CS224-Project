package org.jsoup.nodes;

import org.junit.Test;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsNamedEntity() {
        // Test known named entities
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        
        // Test unknown named entities
        assertFalse(Entities.isNamedEntity("unknown"));
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        // Test retrieval of valid named entities
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        
        // Test retrieval of invalid named entities
        assertNull(Entities.getCharacterByName("unknown"));
        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        
        // Test escaping with base mode
        String input1 = "<div>Hello & World</div>";
        String expected1 = "&lt;div&gt;Hello &amp; World&lt;/div&gt;";
        assertEquals(expected1, Entities.escape(input1, encoder, Entities.EscapeMode.base));
        
        // Test escaping with xhtml mode (restricted entities)
        String input2 = "\"test'";
        String expected2 = "&quot;test&apos;";
        assertEquals(expected2, Entities.escape(input2, encoder, Entities.EscapeMode.xhtml));
        
        // Test escaping characters not in encoder range
        // Note: This test depends on the encoder's capabilities
        String input3 = "normal text";
        assertEquals(input3, Entities.escape(input3, encoder, Entities.EscapeMode.base));
    }

    @Test
    public void testUnescape() {
        // Test unescape with default (non-strict) mode
        String input1 = "&lt;div&gt;Hello &amp; World&lt;/div&gt;";
        String expected1 = "<div>Hello & World</div>";
        assertEquals(expected1, Entities.unescape(input1));
        
        // Test unescape with numeric entities
        String input2 = "&#65;&#x42;&#x43;";
        String expected2 = "ABC";
        assertEquals(expected2, Entities.unescape(input2));
        
        // Test unescape with mixed entities
        String input3 = "&lt;&#65;&amp;&#x42;";
        String expected3 = "<A&B";
        assertEquals(expected3, Entities.unescape(input3));
        
        // Test unescape with no entities
        String input4 = "Plain text without entities";
        assertEquals(input4, Entities.unescape(input4));
    }

    @Test
    public void testUnescapeStrictMode() {
        // Test strict mode requires semicolon
        String input1 = "&lt;div&gt;";
        String expected1 = "<div>";
        assertEquals(expected1, Entities.unescape(input1, true));
        
        // Test strict mode doesn't match without semicolon
        String input2 = "&ltdiv&gt";
        assertEquals(input2, Entities.unescape(input2, true));
        
        // Test numeric entities in strict mode
        String input3 = "&#65;&#x42;";
        String expected3 = "AB";
        assertEquals(expected3, Entities.unescape(input3, true));
        
        // Test invalid entities in strict mode
        String input4 = "&invalid;";
        assertEquals(input4, Entities.unescape(input4, true));
    }
}