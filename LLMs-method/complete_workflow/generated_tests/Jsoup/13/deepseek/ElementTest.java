package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

public class ElementTest {
    private CharsetEncoder encoder;

    @Before
    public void setUp() {
        encoder = StandardCharsets.UTF_8.newEncoder();
    }

    @Test
    public void testIsNamedEntity() {
        assertTrue("amp should be a known entity", Entities.isNamedEntity("amp"));
        assertTrue("lt should be a known entity", Entities.isNamedEntity("lt"));
        assertFalse("unknown should not be a known entity", Entities.isNamedEntity("unknown"));
        assertFalse("empty string should not be a known entity", Entities.isNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull("Unknown entity should return null", Entities.getCharacterByName("unknown"));
        assertNull("Empty string should return null", Entities.getCharacterByName(""));
    }

    @Test
    public void testEscapeWithDifferentModes() {
        String input = "<\"Hello & 'World'>";
        
        String xhtmlEscaped = Entities.escape(input, encoder, Entities.EscapeMode.xhtml);
        assertEquals("&lt;&quot;Hello &amp; &apos;World&apos;&gt;", xhtmlEscaped);
        
        String baseEscaped = Entities.escape(input, encoder, Entities.EscapeMode.base);
        assertTrue("Should contain &lt;", baseEscaped.contains("&lt;"));
        assertTrue("Should contain &amp;", baseEscaped.contains("&amp;"));
        
        String extendedEscaped = Entities.escape(input, encoder, Entities.EscapeMode.extended);
        assertTrue("Extended mode should escape characters", extendedEscaped.contains("&"));
    }

    @Test
    public void testUnescapeStrictVsNonStrict() {
        String strictEntity = "&amp;";
        String nonStrictEntity = "&amp";
        
        assertEquals("&", Entities.unescape(strictEntity, true));
        assertEquals("&", Entities.unescape(nonStrictEntity, false));
        
        assertEquals("&amp", Entities.unescape(nonStrictEntity, true));
        assertEquals("&", Entities.unescape(strictEntity, false));
    }

    @Test
    public void testUnescapeNumericAndHexEntities() {
        assertEquals("<", Entities.unescape("&#60;", false));
        assertEquals("<", Entities.unescape("&#x3c;", false));
        assertEquals("<", Entities.unescape("&#x3C;", false));
        assertEquals("A", Entities.unescape("&#65;", false));
        assertEquals("A", Entities.unescape("&#x41;", false));
        
        String mixed = "Test &#60;&#x3c;&lt;";
        assertEquals("Test <<<", Entities.unescape(mixed, false));
    }
}