package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class TokenQueueTest {
    
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
        Character amp = Entities.getCharacterByName("amp");
        Character lt = Entities.getCharacterByName("lt");
        Character gt = Entities.getCharacterByName("gt");
        
        assertNotNull(amp);
        assertNotNull(lt);
        assertNotNull(gt);
        assertEquals('&', amp.charValue());
        assertEquals('<', lt.charValue());
        assertEquals('>', gt.charValue());
        
        assertNull(Entities.getCharacterByName("unknown"));
    }
    
    @Test
    public void testEscapeModesMapContents() {
        Map<Character, String> xhtmlMap = Entities.EscapeMode.xhtml.getMap();
        Map<Character, String> baseMap = Entities.EscapeMode.base.getMap();
        Map<Character, String> extendedMap = Entities.EscapeMode.extended.getMap();
        
        // XHTML mode should have only 5 entities
        assertEquals(5, xhtmlMap.size());
        assertTrue(xhtmlMap.containsKey('&'));
        assertTrue(xhtmlMap.containsKey('<'));
        assertTrue(xhtmlMap.containsKey('>'));
        assertTrue(xhtmlMap.containsKey('\''));
        assertTrue(xhtmlMap.containsKey('"'));
        
        // Base mode should include common HTML entities
        assertTrue(baseMap.size() > xhtmlMap.size());
        assertTrue(baseMap.containsKey('&'));
        assertTrue(baseMap.containsKey('<'));
        assertTrue(baseMap.containsKey('>'));
        
        // Extended mode should be largest
        assertTrue(extendedMap.size() > baseMap.size());
        assertTrue(extendedMap.containsKey('€')); // Euro symbol
    }
    
    @Test
    public void testEscapeAndUnescapeBasic() {
        CharsetEncoder encoder = StandardCharsets.US_ASCII.newEncoder();
        String input = "Hello <world> & me";
        String escaped = Entities.escape(input, encoder, Entities.EscapeMode.xhtml);
        String unescaped = Entities.unescape(escaped);
        
        assertEquals("Hello &lt;world&gt; &amp; me", escaped);
        assertEquals(input, unescaped);
        
        // Test with strict unescape
        String strictUnescaped = Entities.unescape(escaped, true);
        assertEquals(input, strictUnescaped);
    }
    
    @Test
    public void testUnescapeEdgeCases() {
        // Test unescape with malformed entities
        String malformed = "Normal &amp; but &unknown; and &invalid and &#1234; and &#x12AB;";
        String result = Entities.unescape(malformed, false);
        assertTrue(result.contains("&unknown;"));
        assertTrue(result.contains("&invalid"));
        assertTrue(result.contains("&#1234;"));
        assertTrue(result.contains("&#x12AB;"));
        
        // Test with no ampersand
        String noAmp = "This has no entities";
        assertEquals(noAmp, Entities.unescape(noAmp));
        assertEquals(noAmp, Entities.unescape(noAmp, true));
        
        // Test numeric entities
        String numeric = "&#65;&#x41;&#x0041;";
        String unescaped = Entities.unescape(numeric);
        assertEquals("AAAAAA", unescaped); // Should decode to A three times (3 entities)
    }
}