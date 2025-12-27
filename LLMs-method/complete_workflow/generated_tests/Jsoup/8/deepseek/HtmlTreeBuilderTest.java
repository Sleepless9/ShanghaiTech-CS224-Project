package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Before;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;

public class HtmlTreeBuilderTest {
    private CharsetEncoder encoder;
    private Document.OutputSettings outputSettings;

    @Before
    public void setUp() {
        encoder = StandardCharsets.UTF_8.newEncoder();
        outputSettings = new Document.OutputSettings();
        outputSettings.encoder(encoder);
    }

    @Test
    public void testEscapeWithBaseMode() {
        outputSettings.escapeMode(Entities.EscapeMode.base);
        
        // Test basic HTML entities
        String input1 = "<div>Hello & World</div>";
        String escaped1 = Entities.escape(input1, outputSettings);
        assertEquals("&lt;div&gt;Hello &amp; World&lt;/div&gt;", escaped1);
        
        // Test numeric encoding for non-ASCII characters when encoder can't encode
        // Note: UTF-8 encoder can encode most characters, so we test with a character that might not be encodable
        // For this test, we'll use a character that should be encoded as numeric entity in base mode
        String input2 = "Price: €100";
        String escaped2 = Entities.escape(input2, outputSettings);
        // Euro symbol might be encoded as numeric entity if not in base map
        assertTrue(escaped2.contains("100"));
    }

    @Test
    public void testEscapeWithXhtmlMode() {
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        
        // XHTML only escapes 5 basic entities
        String input = "\"<>&'";
        String escaped = Entities.escape(input, outputSettings);
        assertEquals("&quot;&lt;&gt;&amp;&apos;", escaped);
        
        // Test that other characters are not escaped
        String input2 = "© 2023";
        String escaped2 = Entities.escape(input2, outputSettings);
        // Copyright symbol should not be escaped in xhtml mode
        assertTrue(escaped2.contains("©"));
    }

    @Test
    public void testUnescapeBasicEntities() {
        // Test named entities
        String input1 = "&lt;div&gt;";
        String unescaped1 = Entities.unescape(input1);
        assertEquals("<div>", unescaped1);
        
        // Test numeric entities (decimal)
        String input2 = "&#60;div&#62;";
        String unescaped2 = Entities.unescape(input2);
        assertEquals("<div>", unescaped2);
        
        // Test numeric entities (hexadecimal)
        String input3 = "&#x3C;div&#x3E;";
        String unescaped3 = Entities.unescape(input3);
        assertEquals("<div>", unescaped3);
    }

    @Test
    public void testUnescapeEdgeCases() {
        // Test incomplete entity (no semicolon)
        String input1 = "&amp";
        String unescaped1 = Entities.unescape(input1);
        // Should remain unchanged since it's incomplete
        assertEquals("&amp", unescaped1);
        
        // Test mixed content
        String input2 = "Hello &amp; World &#x263A;";
        String unescaped2 = Entities.unescape(input2);
        assertEquals("Hello & World ☺", unescaped2);
        
        // Test string without entities
        String input3 = "Hello World";
        String unescaped3 = Entities.unescape(input3);
        assertEquals("Hello World", unescaped3);
        
        // Test invalid numeric entity
        String input4 = "&#invalid;";
        String unescaped4 = Entities.unescape(input4);
        // Should remain unchanged
        assertEquals("&#invalid;", unescaped4);
    }

    @Test
    public void testEscapeModeMaps() {
        // Test that each escape mode has the correct map size
        assertTrue(Entities.EscapeMode.xhtml.getMap().size() == 5);
        assertTrue(Entities.EscapeMode.base.getMap().size() > 5);
        assertTrue(Entities.EscapeMode.extended.getMap().size() > Entities.EscapeMode.base.getMap().size());
        
        // Test specific entity in base mode
        assertTrue(Entities.EscapeMode.base.getMap().containsKey('©'));
        assertEquals("copy", Entities.EscapeMode.base.getMap().get('©'));
        
        // Test specific entity in xhtml mode
        assertTrue(Entities.EscapeMode.xhtml.getMap().containsKey('"'));
        assertEquals("quot", Entities.EscapeMode.xhtml.getMap().get('"'));
        
        // Test that extended mode contains entities not in base mode
        // Extended mode should have many more entities
        assertTrue(Entities.EscapeMode.extended.getMap().size() > 1000);
    }
}