package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

public class EntitiesTest {
    private CharsetEncoder encoder;
    private Document.OutputSettings outputSettings;

    @Before
    public void setUp() {
        encoder = StandardCharsets.UTF_8.newEncoder();
        outputSettings = new Document.OutputSettings();
        outputSettings.encoder(encoder);
    }

    @Test
    public void testEscapeXhtmlMode() {
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        
        String input = "<div>\"test\" & 'sample'</div>";
        String escaped = Entities.escape(input, outputSettings);
        
        assertEquals("&lt;div&gt;&quot;test&quot; &amp; &apos;sample&apos;&lt;/div&gt;", escaped);
        assertFalse(escaped.contains("<"));
        assertFalse(escaped.contains("\""));
    }

    @Test
    public void testEscapeBaseMode() {
        outputSettings.escapeMode(Entities.EscapeMode.base);
        
        String input = "© 2023 <test> & \"quotes\"";
        String escaped = Entities.escape(input, outputSettings);
        
        assertTrue(escaped.contains("&copy;"));
        assertTrue(escaped.contains("&lt;"));
        assertTrue(escaped.contains("&gt;"));
        assertTrue(escaped.contains("&quot;"));
        assertFalse(escaped.contains("<"));
    }

    @Test
    public void testEscapeNonEncodableCharacter() {
        outputSettings.escapeMode(Entities.EscapeMode.base);
        
        // Create a character that might not be encodable in ASCII
        String input = "normal" + Character.toString((char) 0xFFFF) + "text";
        String escaped = Entities.escape(input, outputSettings);
        
        assertTrue(escaped.contains("&#65535;"));
        assertTrue(escaped.startsWith("normal"));
        assertTrue(escaped.endsWith("text"));
    }

    @Test
    public void testUnescapeNumericEntities() {
        String input = "&#65; &#x41; &#x61;";
        String unescaped = Entities.unescape(input);
        
        assertEquals("A A a", unescaped);
        assertEquals(5, unescaped.length());
        assertTrue(unescaped.contains("A"));
        assertTrue(unescaped.contains("a"));
    }

    @Test
    public void testUnescapeNamedEntities() {
        String input = "&lt;div&gt; &amp; &quot;test&quot; &copy;";
        String unescaped = Entities.unescape(input);
        
        assertEquals("<div> & \"test\" ©", unescaped);
        assertFalse(unescaped.contains("&lt;"));
        assertFalse(unescaped.contains("&quot;"));
        assertTrue(unescaped.contains("<"));
        assertTrue(unescaped.contains("\""));
    }

    @Test
    public void testUnescapeMixedEntities() {
        String input = "Price: &pound;100 &#x20AC;200";
        String unescaped = Entities.unescape(input);
        
        assertEquals("Price: £100 €200", unescaped);
        assertFalse(unescaped.contains("&pound;"));
        assertFalse(unescaped.contains("&#x20AC;"));
        assertTrue(unescaped.contains("£"));
        assertTrue(unescaped.contains("€"));
    }

    @Test
    public void testUnescapeInvalidEntity() {
        String input = "test &invalid; &another &";
        String unescaped = Entities.unescape(input);
        
        assertEquals("test &invalid; &another &", unescaped);
        assertEquals(input, unescaped);
    }

    @Test
    public void testEscapeModeMaps() {
        assertNotNull(Entities.EscapeMode.xhtml.getMap());
        assertNotNull(Entities.EscapeMode.base.getMap());
        assertNotNull(Entities.EscapeMode.extended.getMap());
        
        assertTrue(Entities.EscapeMode.xhtml.getMap().size() > 0);
        assertTrue(Entities.EscapeMode.base.getMap().size() > Entities.EscapeMode.xhtml.getMap().size());
        assertTrue(Entities.EscapeMode.extended.getMap().size() > Entities.EscapeMode.base.getMap().size());
    }

    @Test
    public void testNoEscapeNeeded() {
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        
        String input = "plain text without special characters";
        String escaped = Entities.escape(input, outputSettings);
        
        assertEquals(input, escaped);
        assertSame(input, escaped);
    }

    @Test
    public void testUnescapeNoAmpersand() {
        String input = "plain text without ampersand";
        String unescaped = Entities.unescape(input);
        
        assertEquals(input, unescaped);
        assertSame(input, unescaped);
    }
}