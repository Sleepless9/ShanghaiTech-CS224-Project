package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Before;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;

public class SelectorTest {
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
        String escaped = Entities.escape("<div>\"test\" & 'sample'</div>", outputSettings);
        assertEquals("&lt;div&gt;&quot;test&quot; &amp; &apos;sample&apos;&lt;/div&gt;", escaped);
        
        // Test numeric encoding for non-ASCII characters
        String unicode = Entities.escape("© €", outputSettings);
        assertTrue(unicode.contains("&#169;")); // ©
        assertTrue(unicode.contains(" ")); // space remains
    }

    @Test
    public void testEscapeExtendedMode() {
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        
        // Test extended entities
        String escaped = Entities.escape("← →", outputSettings);
        // Should use named entities for arrows in extended mode
        assertTrue(escaped.contains("&larr;") || escaped.contains("&rarr;") || 
                   escaped.contains("&#")); // fallback to numeric
        
        // Test that common entities still work
        String basic = Entities.escape("<>&\"", outputSettings);
        assertTrue(basic.contains("&lt;"));
        assertTrue(basic.contains("&gt;"));
        assertTrue(basic.contains("&amp;"));
        assertTrue(basic.contains("&quot;"));
    }

    @Test
    public void testUnescapeNamedEntities() {
        // Test basic named entities
        String unescaped = Entities.unescape("&lt;div&gt;Hello &amp; World&lt;/div&gt;");
        assertEquals("<div>Hello & World</div>", unescaped);
        
        // Test numeric entities
        String numeric = Entities.unescape("&#65;&#x42;&#x43;");
        assertEquals("ABC", numeric);
        
        // Test mixed entities
        String mixed = Entities.unescape("&quot;Test&#39;s &amp; more&quot;");
        assertEquals("\"Test's & more\"", mixed);
    }

    @Test
    public void testUnescapeEdgeCases() {
        // Test incomplete entity (no semicolon)
        String incomplete = Entities.unescape("&amp");
        assertEquals("&", incomplete); // Should still decode
        
        // Test invalid entity
        String invalid = Entities.unescape("&invalid;");
        assertEquals("&invalid;", invalid); // Should remain unchanged
        
        // Test empty string
        String empty = Entities.unescape("");
        assertEquals("", empty);
        
        // Test string without entities
        String noEntities = Entities.unescape("Hello World");
        assertEquals("Hello World", noEntities);
    }

    @Test
    public void testUnescapeNumericEntities() {
        // Test decimal entities
        String decimal = Entities.unescape("&#65;&#66;&#67;");
        assertEquals("ABC", decimal);
        
        // Test hexadecimal entities (lowercase)
        String hexLower = Entities.unescape("&#x61;&#x62;&#x63;");
        assertEquals("abc", hexLower);
        
        // Test hexadecimal entities (uppercase)
        String hexUpper = Entities.unescape("&#X41;&#X42;&#X43;");
        assertEquals("ABC", hexUpper);
        
        // Test invalid numeric entity
        String invalidNum = Entities.unescape("&#invalid;");
        assertEquals("&#invalid;", invalidNum);
        
        // Test out of range character
        String outOfRange = Entities.unescape("&#9999999;");
        assertEquals("&#9999999;", outOfRange); // Should remain unchanged
    }
}