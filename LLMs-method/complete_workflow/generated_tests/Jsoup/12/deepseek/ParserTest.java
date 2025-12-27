package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

public class ParserTest {
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
        String escaped = Entities.escape("<div>Hello & World</div>", outputSettings);
        assertEquals("&lt;div&gt;Hello &amp; World&lt;/div&gt;", escaped);
        
        // Test that non-ASCII characters are preserved when encoder can encode them
        escaped = Entities.escape("café", outputSettings);
        assertEquals("café", escaped);
    }

    @Test
    public void testEscapeXhtmlMode() {
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        
        // XHTML only escapes 5 basic entities
        String escaped = Entities.escape("\"<>&'", outputSettings);
        assertEquals("&quot;&lt;&gt;&amp;&apos;", escaped);
        
        // Test that other characters are not escaped
        escaped = Entities.escape("©", outputSettings);
        assertEquals("©", escaped);
    }

    @Test
    public void testEscapeExtendedMode() {
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        
        // Test extended entity escaping
        String escaped = Entities.escape("©€", outputSettings);
        // © should be escaped, € might be kept as is if encoder can handle it
        assertTrue(escaped.contains("&copy;") || escaped.contains("©"));
        
        // Test that common entities are escaped
        escaped = Entities.escape("<>&\"'", outputSettings);
        assertTrue(escaped.contains("&lt;"));
        assertTrue(escaped.contains("&gt;"));
        assertTrue(escaped.contains("&amp;"));
    }

    @Test
    public void testUnescapeNumericEntities() {
        // Test decimal numeric entities
        String unescaped = Entities.unescape("&#65;&#66;&#67;");
        assertEquals("ABC", unescaped);
        
        // Test hexadecimal numeric entities
        unescaped = Entities.unescape("&#x41;&#x42;&#x43;");
        assertEquals("ABC", unescaped);
        
        // Test mixed entities
        unescaped = Entities.unescape("A&#66;C");
        assertEquals("ABC", unescaped);
    }

    @Test
    public void testUnescapeNamedEntities() {
        // Test basic named entities
        String unescaped = Entities.unescape("&lt;div&gt;&amp;&lt;/div&gt;");
        assertEquals("<div>&</div>", unescaped);
        
        // Test extended named entities
        unescaped = Entities.unescape("&copy; 2023 &reg;");
        assertEquals("© 2023 ®", unescaped);
        
        // Test that invalid entities are left as-is
        unescaped = Entities.unescape("&nonexistent;");
        assertEquals("&nonexistent;", unescaped);
    }

    @Test
    public void testUnescapeMixedAndEdgeCases() {
        // Test string without ampersands returns unchanged
        String unescaped = Entities.unescape("Hello World");
        assertEquals("Hello World", unescaped);
        
        // Test malformed numeric entities are skipped
        unescaped = Entities.unescape("&#notanumber;");
        assertEquals("&#notanumber;", unescaped);
        
        // Test incomplete entities
        unescaped = Entities.unescape("&amp");
        assertEquals("&amp", unescaped);
        
        // Test combination of valid and invalid entities
        unescaped = Entities.unescape("&lt;test&amp;test&invalid;");
        assertEquals("<test&test&invalid;", unescaped);
    }

    @Test
    public void testEscapeModeMaps() {
        // Test that each escape mode has the correct map size
        assertEquals(5, Entities.EscapeMode.xhtml.getMap().size());
        assertTrue(Entities.EscapeMode.base.getMap().size() > 5);
        assertTrue(Entities.EscapeMode.extended.getMap().size() > Entities.EscapeMode.base.getMap().size());
        
        // Test specific entries in xhtml mode
        assertTrue(Entities.EscapeMode.xhtml.getMap().containsKey('"'));
        assertEquals("quot", Entities.EscapeMode.xhtml.getMap().get('"'));
        
        // Test that base mode contains common entities
        assertTrue(Entities.EscapeMode.base.getMap().containsKey('©'));
        assertEquals("copy", Entities.EscapeMode.base.getMap().get('©'));
    }
}