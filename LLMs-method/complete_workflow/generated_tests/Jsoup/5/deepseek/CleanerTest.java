package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

public class CleanerTest {
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
        
        String input = "<Hello> & 'World'";
        String escaped = Entities.escape(input, outputSettings);
        
        assertEquals("&lt;Hello&gt; &amp; &apos;World&apos;", escaped);
        assertFalse(escaped.contains("&quot;"));
        
        String input2 = "a > b & c < d";
        String escaped2 = Entities.escape(input2, outputSettings);
        assertTrue(escaped2.contains("&gt;"));
        assertTrue(escaped2.contains("&lt;"));
        assertTrue(escaped2.contains("&amp;"));
    }

    @Test
    public void testEscapeExtendedMode() {
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        
        String input = "© € ±";
        String escaped = Entities.escape(input, outputSettings);
        
        assertTrue(escaped.contains("&copy;"));
        assertTrue(escaped.contains("&plusmn;"));
        
        String input2 = "α β γ";
        String escaped2 = Entities.escape(input2, outputSettings);
        assertTrue(escaped2.contains("&#"));
        assertTrue(escaped2.contains("945"));
        assertTrue(escaped2.contains("946"));
    }

    @Test
    public void testUnescapeNumericEntities() {
        String input = "&#65;&#x42;&#x43;";
        String unescaped = Entities.unescape(input);
        
        assertEquals("ABC", unescaped);
        
        String input2 = "&#65;&#x42;test&#x43;";
        String unescaped2 = Entities.unescape(input2);
        assertEquals("ABtestC", unescaped2);
    }

    @Test
    public void testUnescapeNamedEntities() {
        String input = "&lt;&gt;&amp;&quot;&apos;";
        String unescaped = Entities.unescape(input);
        
        assertEquals("<>&\"'", unescaped);
        
        String input2 = "&copy;&reg;&nbsp;";
        String unescaped2 = Entities.unescape(input2);
        assertEquals("©®\u00A0", unescaped2);
    }

    @Test
    public void testUnescapeMixedAndInvalid() {
        String input = "Test &lt;div&gt; &invalid; &amp; &#65;";
        String unescaped = Entities.unescape(input);
        
        assertEquals("Test <div> &invalid; & A", unescaped);
        
        String input2 = "&#; &x; &xyz;";
        String unescaped2 = Entities.unescape(input2);
        assertEquals("&#; &x; &xyz;", unescaped2);
    }

    @Test
    public void testEscapeUnescapeRoundTrip() {
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        
        String original = "<Hello> & 'World' © € ± α";
        String escaped = Entities.escape(original, outputSettings);
        String unescaped = Entities.unescape(escaped);
        
        assertEquals(original, unescaped);
        
        String original2 = "Test &quot;quotes&quot; and &apos;apos&apos;";
        String escaped2 = Entities.escape(original2, outputSettings);
        String unescaped2 = Entities.unescape(escaped2);
        assertEquals(original2, unescaped2);
    }
}