package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

public class ElementTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("nonexistent"));
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("nonexistent"));
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscapeModes() {
        // Test xhtml escape mode
        Entities.EscapeMode xhtmlMode = Entities.EscapeMode.xhtml;
        Map<Character, String> xhtmlMap = xhtmlMode.getMap();
        assertTrue(xhtmlMap.containsKey('<'));
        assertTrue(xhtmlMap.containsKey('>'));
        assertTrue(xhtmlMap.containsKey('&'));
        assertTrue(xhtmlMap.containsKey('"'));
        assertTrue(xhtmlMap.containsKey('\''));
        assertEquals("lt", xhtmlMap.get('<'));
        assertEquals("gt", xhtmlMap.get('>'));
        
        // Test base escape mode
        Entities.EscapeMode baseMode = Entities.EscapeMode.base;
        Map<Character, String> baseMap = baseMode.getMap();
        assertTrue(baseMap.containsKey('<'));
        assertTrue(baseMap.containsKey('>'));
        assertTrue(baseMap.containsKey('&'));
        assertTrue(baseMap.size() > xhtmlMap.size());
        
        // Test extended escape mode
        Entities.EscapeMode extendedMode = Entities.EscapeMode.extended;
        Map<Character, String> fullMap = extendedMode.getMap();
        assertTrue(fullMap.size() > baseMap.size());
        assertTrue(fullMap.containsKey('€')); // euro symbol should be in extended
    }

    @Test
    public void testUnescape() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.escapeMode(Entities.EscapeMode.base);
        
        // Test normal unescaping
        String escaped = "&lt;&gt;&amp;&quot;";
        String unescaped = Entities.unescape(escaped);
        assertEquals("<>&\"", unescaped);
        
        // Test numeric entities
        String numericEscaped = "&#60;&#x3C;";
        String numericUnescaped = Entities.unescape(numericEscaped);
        assertEquals("<<", numericUnescaped);
        
        // Test mixed entities
        String mixed = "Hello &lt;World&gt; &amp; Universe &#33;";
        String result = Entities.unescape(mixed);
        assertEquals("Hello <World> & Universe !", result);
    }

    @Test
    public void testStrictUnescape() {
        // Test strict vs non-strict unescape
        String withSemicolon = "&lt;";
        String withoutSemicolon = "&lt";
        
        // Non-strict should handle both
        assertEquals("<", Entities.unescape(withSemicolon, false));
        assertEquals("<", Entities.unescape(withoutSemicolon, false));
        
        // Strict should only handle with semicolon
        assertEquals("<", Entities.unescape(withSemicolon, true));
        assertEquals("&lt", Entities.unescape(withoutSemicolon, true)); // unchanged
        
        // Test invalid entity handling
        String invalid = "&invalidxxx;";
        assertEquals(invalid, Entities.unescape(invalid, true));
        assertEquals(invalid, Entities.unescape(invalid, false));
    }
}