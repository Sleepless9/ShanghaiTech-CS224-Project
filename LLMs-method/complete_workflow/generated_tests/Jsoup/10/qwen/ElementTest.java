package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

public class ElementTest {

    @Test
    public void testEscapeWithXhtmlMode() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        
        // Test basic XHTML entities
        String result1 = Entities.escape("<>", outputSettings);
        assertEquals("&lt;&gt;", result1);
        
        String result2 = Entities.escape("Hello & World", outputSettings);
        assertEquals("Hello &amp; World", result2);
        
        // Test mixed with non-entity characters
        String result3 = Entities.escape("\"Hello\" <world>", outputSettings);
        assertEquals("&quot;Hello&quot; &lt;world&gt;", result3);
    }

    @Test
    public void testEscapeWithBaseMode() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.base);
        
        CharsetEncoder encoder = StandardCharsets.US_ASCII.newEncoder();
        
        // Test base entities including accented characters
        String result1 = Entities.escape("Café & café", encoder, Entities.EscapeMode.base);
        assertTrue(result1.contains("&eacute;"));
        assertTrue(result1.contains("&amp;"));
        
        // Test copyright symbol
        String result2 = Entities.escape("Copyright © 2023", encoder, Entities.EscapeMode.base);
        assertTrue(result2.contains("&copy;"));
        
        // Test non-breaking space
        String result3 = Entities.escape("Hello World", encoder, Entities.EscapeMode.base);
        assertTrue(result3.contains("&nbsp;"));
    }

    @Test
    public void testUnescapeBasicEntities() {
        // Test standard named entities
        String result1 = Entities.unescape("&lt;&gt;&amp;&quot;&apos;");
        assertEquals("<>&\"'", result1);
        
        // Test case sensitivity and mixed content
        String result2 = Entities.unescape("The &AMP; symbol is &lt; than other things.");
        assertEquals("The & symbol is < than other things.", result2);
        
        // Test numeric entities (decimal)
        String result3 = Entities.unescape("&#65;&#66;&#67;");
        assertEquals("ABC", result3);
        
        // Test numeric entities (hexadecimal)
        String result4 = Entities.unescape("&#x41;&#x42;&#x43;");
        assertEquals("ABC", result4);
    }

    @Test
    public void testUnescapeAdvancedFeatures() {
        // Test hexadecimal with uppercase X
        String result1 = Entities.unescape("&#X41;&#X42;&#X43;");
        assertEquals("ABC", result1);
        
        // Test entities without semicolon (should still work for base entities)
        String result2 = Entities.unescape("&lt &gt &amp");
        assertEquals("< > &", result2);
        
        // Test mixed named and numeric entities
        String result3 = Entities.unescape("A &amp; B = &#67; or &#x43;");
        assertEquals("A & B = C or C", result3);
        
        // Test string without entities (performance shortcut)
        String result4 = Entities.unescape("Plain text with no entities");
        assertEquals("Plain text with no entities", result4);
    }

    @Test
    public void testEscapeUnescapeRoundtrip() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        
        // Test roundtrip for various characters
        String original = "<p>© 2023 Café & Restaurant – \"It's great!\"</p>";
        String escaped = Entities.escape(original, encoder, Entities.EscapeMode.base);
        String unescaped = Entities.unescape(escaped);
        
        assertEquals(original, unescaped);
        
        // Test with more complex entities
        String original2 = "Math: α + β = γ & ∑_{i=1}^n i = n(n+1)/2";
        String escaped2 = Entities.escape(original2, encoder, Entities.EscapeMode.extended);
        String unescaped2 = Entities.unescape(escaped2);
        
        assertEquals(original2, unescaped2);
    }
}