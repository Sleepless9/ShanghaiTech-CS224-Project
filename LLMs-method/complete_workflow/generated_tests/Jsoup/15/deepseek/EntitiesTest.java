package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
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
    public void testIsNamedEntity() {
        assertTrue("Should recognize 'lt' as named entity", Entities.isNamedEntity("lt"));
        assertTrue("Should recognize 'amp' as named entity", Entities.isNamedEntity("amp"));
        assertFalse("Should not recognize unknown entity", Entities.isNamedEntity("unknown"));
        assertFalse("Should not recognize null as named entity", Entities.isNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull("Should return null for unknown entity", Entities.getCharacterByName("unknown"));
        assertNull("Should return null for null input", Entities.getCharacterByName(null));
    }

    @Test
    public void testEscapeWithDifferentModes() {
        String input = "<\"Hello & 'World'>";
        
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        String xhtmlEscaped = Entities.escape(input, outputSettings);
        assertEquals("&lt;&quot;Hello &amp; &apos;World&apos;&gt;", xhtmlEscaped);
        
        outputSettings.escapeMode(Entities.EscapeMode.base);
        String baseEscaped = Entities.escape(input, outputSettings);
        assertTrue("Should contain &lt;", baseEscaped.contains("&lt;"));
        assertTrue("Should contain &amp;", baseEscaped.contains("&amp;"));
        
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        String extendedEscaped = Entities.escape(input, outputSettings);
        assertTrue("Should contain &lt;", extendedEscaped.contains("&lt;"));
        assertTrue("Should contain &amp;", extendedEscaped.contains("&amp;"));
    }

    @Test
    public void testUnescape() {
        assertEquals("Simple unescape", "<>", Entities.unescape("&lt;&gt;"));
        assertEquals("Mixed content", "A < B & C > D", Entities.unescape("A &lt; B &amp; C &gt; D"));
        assertEquals("Numeric decimal", "@", Entities.unescape("&#64;"));
        assertEquals("Numeric hex", "@", Entities.unescape("&#x40;"));
        assertEquals("No ampersand", "Hello World", Entities.unescape("Hello World"));
        
        assertEquals("Strict mode requires semicolon", "&amp", Entities.unescape("&amp", true));
        assertEquals("Non-strict mode doesn't require semicolon", "&", Entities.unescape("&amp", false));
    }

    @Test
    public void testEscapeUnescapeRoundTrip() {
        String[] testStrings = {
            "Hello World",
            "A < B & C > D",
            "Special chars: \"'&<>",
            "Unicode: © € ∑",
            "Mixed: A&B < C > D"
        };
        
        for (String original : testStrings) {
            outputSettings.escapeMode(Entities.EscapeMode.extended);
            String escaped = Entities.escape(original, outputSettings);
            String unescaped = Entities.unescape(escaped);
            assertEquals("Round trip should return original", original, unescaped);
        }
    }
}