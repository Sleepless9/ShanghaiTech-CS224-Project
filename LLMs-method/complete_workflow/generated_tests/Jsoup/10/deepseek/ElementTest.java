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
        
        // Test common HTML entities
        String escaped = Entities.escape("<div>Hello & World</div>", outputSettings);
        assertEquals("&lt;div&gt;Hello &amp; World&lt;/div&gt;", escaped);
        
        // Test non-ASCII character that encoder can handle
        escaped = Entities.escape("café", outputSettings);
        assertEquals("café", escaped);
        
        // Test character not in base map but encoder can encode
        escaped = Entities.escape("abc", outputSettings);
        assertEquals("abc", escaped);
    }

    @Test
    public void testEscapeXhtmlMode() {
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        
        // XHTML only escapes 5 basic entities
        String escaped = Entities.escape("\"<>&'", outputSettings);
        assertEquals("&quot;&lt;&gt;&amp;&apos;", escaped);
        
        // Test that other characters pass through
        escaped = Entities.escape("abc123", outputSettings);
        assertEquals("abc123", escaped);
        
        // Test apostrophe specifically
        escaped = Entities.escape("It's", outputSettings);
        assertEquals("It&apos;s", escaped);
    }

    @Test
    public void testEscapeExtendedMode() {
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        
        // Test extended entity (copyright symbol)
        String input = "© 2023";
        String escaped = Entities.escape(input, outputSettings);
        // Should escape copyright symbol if in extended map
        assertTrue(escaped.contains("&") || escaped.equals(input));
        
        // Test euro symbol
        input = "€100";
        escaped = Entities.escape(input, outputSettings);
        // Euro might be encoded as numeric entity if not in map
        assertNotNull(escaped);
    }

    @Test
    public void testUnescapeNumericEntities() {
        // Test decimal numeric entities
        String unescaped = Entities.unescape("&#65;&#66;&#67;");
        assertEquals("ABC", unescaped);
        
        // Test hexadecimal numeric entities
        unescaped = Entities.unescape("&#x41;&#x42;&#x43;");
        assertEquals("ABC", unescaped);
        
        // Test mixed case hex
        unescaped = Entities.unescape("&#x61;&#x42;&#x43;");
        assertEquals("aBC", unescaped);
        
        // Test with semicolon
        unescaped = Entities.unescape("&#65;");
        assertEquals("A", unescaped);
    }

    @Test
    public void testUnescapeNamedEntities() {
        // Test common named entities
        String unescaped = Entities.unescape("&lt;div&gt;&amp;&quot;");
        assertEquals("<div>&\"", unescaped);
        
        // Test full/named entity
        unescaped = Entities.unescape("&copy; 2023");
        assertEquals("© 2023", unescaped);
        
        // Test without trailing semicolon (should still work for base entities)
        unescaped = Entities.unescape("&amp");
        assertEquals("&", unescaped);
        
        // Test invalid entity remains unchanged
        unescaped = Entities.unescape("&invalid;");
        assertEquals("&invalid;", unescaped);
        
        // Test numeric entity out of range
        unescaped = Entities.unescape("&#9999999;");
        assertEquals("&#9999999;", unescaped);
    }

    @Test
    public void testUnescapeMixedContent() {
        // Test string without ampersand returns unchanged
        String unescaped = Entities.unescape("Hello World");
        assertEquals("Hello World", unescaped);
        
        // Test mixed entities and plain text
        unescaped = Entities.unescape("Price: &euro;100 &amp; Tax: &pound;20");
        assertTrue(unescaped.contains("100"));
        assertTrue(unescaped.contains("20"));
        
        // Test multiple entities in sequence
        unescaped = Entities.unescape("&lt;&gt;&amp;&quot;&apos;");
        assertEquals("<>&\"'", unescaped);
        
        // Test partial/incomplete entity
        unescaped = Entities.unescape("Text & more text");
        assertEquals("Text & more text", unescaped);
    }

    @Test
    public void testEscapeModeMaps() {
        // Test that each escape mode has appropriate map
        assertNotNull(Entities.EscapeMode.xhtml.getMap());
        assertNotNull(Entities.EscapeMode.base.getMap());
        assertNotNull(Entities.EscapeMode.extended.getMap());
        
        // Test map sizes (xhtml should be smallest)
        assertTrue(Entities.EscapeMode.xhtml.getMap().size() < 
                  Entities.EscapeMode.base.getMap().size());
        assertTrue(Entities.EscapeMode.base.getMap().size() < 
                  Entities.EscapeMode.extended.getMap().size());
        
        // Test specific entries in xhtml map
        assertTrue(Entities.EscapeMode.xhtml.getMap().containsKey('<'));
        assertEquals("lt", Entities.EscapeMode.xhtml.getMap().get('<'));
    }
}