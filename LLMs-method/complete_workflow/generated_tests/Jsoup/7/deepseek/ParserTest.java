package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Before;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;

public class ParserTest {
    private CharsetEncoder utf8Encoder;
    private Document.OutputSettings outputSettings;

    @Before
    public void setUp() {
        utf8Encoder = StandardCharsets.UTF_8.newEncoder();
        outputSettings = new Document.OutputSettings();
        outputSettings.charset(StandardCharsets.UTF_8);
    }

    @Test
    public void testEscapeXhtmlMode() {
        String input = "<div>\"test\" & 'sample'</div>";
        String escaped = Entities.escape(input, outputSettings.escapeMode(Entities.EscapeMode.xhtml));
        
        assertNotNull(escaped);
        assertTrue(escaped.contains("&lt;"));
        assertTrue(escaped.contains("&gt;"));
        assertTrue(escaped.contains("&quot;"));
        assertTrue(escaped.contains("&amp;"));
        assertFalse(escaped.contains("&apos;"));
        assertTrue(escaped.contains("'"));
    }

    @Test
    public void testEscapeBaseMode() {
        String input = "© 2022 & <test> €";
        outputSettings.escapeMode(Entities.EscapeMode.base);
        String escaped = Entities.escape(input, outputSettings);
        
        assertNotNull(escaped);
        assertTrue(escaped.contains("&copy;"));
        assertTrue(escaped.contains("&amp;"));
        assertTrue(escaped.contains("&lt;"));
        assertTrue(escaped.contains("&gt;"));
        assertFalse(escaped.contains("&euro;"));
        assertTrue(escaped.contains("€"));
    }

    @Test
    public void testEscapeExtendedMode() {
        String input = "∑ ∫ ∂x";
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        String escaped = Entities.escape(input, outputSettings);
        
        assertNotNull(escaped);
        assertTrue(escaped.contains("&sum;"));
        assertTrue(escaped.contains("&int;"));
        assertTrue(escaped.contains("&part;"));
        assertTrue(escaped.contains("x"));
    }

    @Test
    public void testUnescapeNumericEntities() {
        String input = "&#65; &#x42; &#x43;";
        String unescaped = Entities.unescape(input);
        
        assertEquals("A B C", unescaped);
        
        String input2 = "&#65;&#66;&#67;";
        String unescaped2 = Entities.unescape(input2);
        assertEquals("ABC", unescaped2);
    }

    @Test
    public void testUnescapeNamedEntities() {
        String input = "&lt;div&gt; &amp; &quot;test&quot; &copy;";
        String unescaped = Entities.unescape(input);
        
        assertEquals("<div> & \"test\" ©", unescaped);
        
        String input2 = "&nbsp;&amp;&nbsp;";
        String unescaped2 = Entities.unescape(input2);
        assertEquals("\u00A0&\u00A0", unescaped2);
    }

    @Test
    public void testUnescapeMixedEntities() {
        String input = "&lt;&#65;&gt; &amp;&#x42; &quot;&#67;&quot;";
        String unescaped = Entities.unescape(input);
        
        assertEquals("<A> &B \"C\"", unescaped);
        
        String input2 = "test&#32;string&#32;here";
        String unescaped2 = Entities.unescape(input2);
        assertEquals("test string here", unescaped2);
    }

    @Test
    public void testUnescapeInvalidEntities() {
        String input = "&invalid; &123abc; &";
        String unescaped = Entities.unescape(input);
        
        assertEquals("&invalid; &123abc; &", unescaped);
        
        String input2 = "test &amp incomplete";
        String unescaped2 = Entities.unescape(input2);
        assertEquals("test &amp incomplete", unescaped2);
    }

    @Test
    public void testEscapeUnescapeRoundtrip() {
        String original = "<div>\"test\" & 'sample' © ∑";
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        String escaped = Entities.escape(original, outputSettings);
        String unescaped = Entities.unescape(escaped);
        
        assertEquals(original, unescaped);
        
        String original2 = "A B C D E F G";
        String escaped2 = Entities.escape(original2, outputSettings);
        String unescaped2 = Entities.unescape(escaped2);
        assertEquals(original2, unescaped2);
    }

    @Test
    public void testEscapeNonEncodableCharacters() {
        String input = "normal \uD83D\uDE00 \uFFFF";
        String escaped = Entities.escape(input, utf8Encoder, Entities.EscapeMode.base);
        
        assertNotNull(escaped);
        assertTrue(escaped.contains("normal"));
        assertTrue(escaped.contains("&#"));
        assertFalse(escaped.contains("\uFFFF"));
    }

    @Test
    public void testNoEscapeNeeded() {
        String input = "Hello World";
        String escaped = Entities.escape(input, outputSettings);
        
        assertEquals(input, escaped);
        
        String input2 = "123 abc";
        String escaped2 = Entities.escape(input2, outputSettings);
        assertEquals(input2, escaped2);
    }

    @Test
    public void testUnescapeNoAmpersand() {
        String input = "Hello World";
        String unescaped = Entities.unescape(input);
        
        assertEquals(input, unescaped);
        
        String input2 = "";
        String unescaped2 = Entities.unescape(input2);
        assertEquals(input2, unescaped2);
    }
}