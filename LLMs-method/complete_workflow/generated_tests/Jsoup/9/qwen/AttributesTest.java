package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

public class AttributesTest {

    @Test
    public void testEscapeXhtmlMode() {
        CharsetEncoder encoder = StandardCharsets.US_ASCII.newEncoder();
        String result = Entities.escape("<>", encoder, Entities.EscapeMode.xhtml);
        assertEquals("&lt;&gt;", result);
        
        result = Entities.escape("Hello & World", encoder, Entities.EscapeMode.xhtml);
        assertEquals("Hello &amp; World", result);
        
        result = Entities.escape("'\"", encoder, encoder, Entities.EscapeMode.xhtml);
        assertEquals("&apos;&quot;", result);
    }

    @Test
    public void testEscapeBaseMode() {
        CharsetEncoder encoder = StandardCharsets.US_ASCII.newEncoder();
        String result = Entities.escape("é©", encoder, Entities.EscapeMode.base);
        assertEquals("&#233;&#169;", result);
        
        result = Entities.escape("áéíóú", encoder, Entities.EscapeMode.base);
        assertNotEquals("áéíóú", result);
        assertTrue(result.contains("&#"));
        
        result = Entities.escape("&copy;", encoder, Entities.EscapeMode.base);
        assertEquals("&amp;copy;", result);
    }

    @Test
    public void testUnescapeBasicEntities() {
        String result = Entities.unescape("&lt;&gt;&amp;&quot;&apos;");
        assertEquals("<>&\"'", result);
        
        result = Entities.unescape("Copyright &copy; 2023");
        assertEquals("Copyright © 2023", result);
        
        result = Entities.unescape("Trademark &trade; symbol");
        assertEquals("Trademark ™ symbol", result);
    }

    @Test
    public void testUnescapeNumericEntities() {
        String result = Entities.unescape("&#65;&#66;&#67;");
        assertEquals("ABC", result);
        
        result = Entities.unescape("&#x41;&#x42;&#x43;");
        assertEquals("ABC", result);
        
        result = Entities.unescape("Mixed &#65; normal &#x42; text");
        assertEquals("Mixed A normal B text", result);
    }

    @Test
    public void testUnescapeNoAmpersand() {
        String original = "This string has no entities";
        String result = Entities.unescape(original);
        assertEquals(original, result);
        
        original = "Normal text with spaces and punctuation.";
        result = Entities.unescape(original);
        assertEquals(original, result);
        
        // Test with invalid entity that shouldn't be processed
        original = "This is not an entity: &invalidxxx";
        result = Entities.unescape(original);
        assertEquals(original, result);
    }
}