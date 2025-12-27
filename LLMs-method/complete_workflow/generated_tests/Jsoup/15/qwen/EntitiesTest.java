package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.charset.StandardCharsets;
import java.nio.charset.CharsetEncoder;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertFalse(Entities.isNamedEntity("unknown"));
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertNull(Entities.getCharacterByName("unknown"));
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscapeWithDifferentModes() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        String input = "<Hello & World>";
        
        String xhtmlResult = Entities.escape(input, encoder, Entities.EscapeMode.xhtml);
        String baseResult = Entities.escape(input, encoder, Entities.EscapeMode.base);
        String extendedResult = Entities.escape(input, encoder, Entities.EscapeMode.extended);
        
        assertTrue(xhtmlResult.contains("&lt;"));
        assertTrue(xhtmlResult.contains("&gt;"));
        assertTrue(xhtmlResult.contains("&amp;"));
        assertFalse(xhtmlResult.contains("&quot;"));
        
        assertTrue(baseResult.contains("&lt;"));
        assertTrue(baseResult.contains("&gt;"));
        assertTrue(baseResult.contains("&amp;"));
        
        assertTrue(extendedResult.contains("&lt;"));
        assertTrue(extendedResult.contains("&gt;"));
        assertTrue(extendedResult.contains("&amp;"));
    }

    @Test
    public void testUnescapeNormalCases() {
        String input = "&amp;&lt;&gt;&quot;";
        String result = Entities.unescape(input);
        
        assertEquals("&<>\"", result);
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
    }

    @Test
    public void testUnescapeStrictMode() {
        String inputWithSemicolon = "&amp;&lt;&gt;";
        String inputWithoutSemicolon = "&amp&lt&gt";
        
        String strictResult = Entities.unescape(inputWithoutSemicolon, true);
        String nonStrictResult = Entities.unescape(inputWithoutSemicolon, false);
        
        // Strict mode should not unescape without semicolon
        assertEquals(inputWithoutSemicolon, strictResult);
        // Non-strict mode should unescape even without semicolon
        assertNotEquals(inputWithoutSemicolon, nonStrictResult);
        assertEquals("&<>", nonStrictResult);
        
        // Both modes should handle semicolon properly
        assertEquals("&<>", Entities.unescape(inputWithSemicolon, true));
        assertEquals("&<>", Entities.unescape(inputWithSemicolon, false));
    }
}