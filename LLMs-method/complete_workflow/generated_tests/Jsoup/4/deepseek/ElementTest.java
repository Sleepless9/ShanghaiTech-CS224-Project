package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Before;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;

public class ElementTest {
    private CharsetEncoder encoder;
    private Document.OutputSettings outputSettings;

    @Before
    public void setUp() {
        encoder = StandardCharsets.UTF_8.newEncoder();
        outputSettings = new Document.OutputSettings();
        outputSettings.encoder(encoder);
    }

    @Test
    public void testEscapeBaseMode() {
        outputSettings.escapeMode(Entities.EscapeMode.base);
        
        // Test basic HTML entities
        String input = "<div>&\"'";
        String escaped = Entities.escape(input, outputSettings);
        assertEquals("&lt;div&gt;&amp;&quot;&apos;", escaped);
        
        // Test characters that should not be escaped
        String normalText = "Hello World";
        String normalEscaped = Entities.escape(normalText, outputSettings);
        assertEquals("Hello World", normalEscaped);
    }

    @Test
    public void testEscapeExtendedMode() {
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        
        // Test extended entities
        String input = "©®";
        String escaped = Entities.escape(input, outputSettings);
        assertTrue(escaped.contains("&copy;") || escaped.contains("&reg;"));
        
        // Test non-encodable characters
        String specialChar = "€"; // Euro symbol
        String specialEscaped = Entities.escape(specialChar, outputSettings);
        assertTrue(specialEscaped.contains("&#"));
    }

    @Test
    public void testUnescapeNumericEntities() {
        // Test decimal numeric entities
        String decimalEntity = "&#65;&#66;&#67;";
        String unescaped = Entities.unescape(decimalEntity);
        assertEquals("ABC", unescaped);
        
        // Test hexadecimal numeric entities
        String hexEntity = "&#x41;&#x42;&#x43;";
        String hexUnescaped = Entities.unescape(hexEntity);
        assertEquals("ABC", hexUnescaped);
    }

    @Test
    public void testUnescapeNamedEntities() {
        // Test common named entities
        String namedEntities = "&lt;&gt;&amp;&quot;&apos;";
        String unescaped = Entities.unescape(namedEntities);
        assertEquals("<>&\"'", unescaped);
        
        // Test extended named entities
        String extendedEntities = "&copy;&reg;";
        String extendedUnescaped = Entities.unescape(extendedEntities);
        assertTrue(extendedUnescaped.contains("©") || extendedUnescaped.contains("®"));
    }

    @Test
    public void testUnescapeMixedAndInvalid() {
        // Test mixed valid entities and plain text
        String mixed = "Hello &lt;world&gt; &amp; friends";
        String unescaped = Entities.unescape(mixed);
        assertEquals("Hello <world> & friends", unescaped);
        
        // Test invalid entities (should remain unchanged)
        String invalid = "&invalid; &123";
        String invalidUnescaped = Entities.unescape(invalid);
        assertEquals("&invalid; &123", invalidUnescaped);
        
        // Test string without ampersands
        String noAmpersands = "Hello World";
        String noAmpUnescaped = Entities.unescape(noAmpersands);
        assertEquals("Hello World", noAmpUnescaped);
    }

    @Test
    public void testEscapeUnescapeRoundTrip() {
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        
        // Test round trip for special characters
        String original = "<div id=\"test\">© 2023 & beyond</div>";
        String escaped = Entities.escape(original, outputSettings);
        String unescaped = Entities.unescape(escaped);
        assertEquals(original, unescaped);
        
        // Test round trip for non-ASCII characters
        String unicodeText = "Hello 世界 €100";
        String escapedUnicode = Entities.escape(unicodeText, outputSettings);
        String unescapedUnicode = Entities.unescape(escapedUnicode);
        assertEquals(unicodeText, unescapedUnicode);
    }
}