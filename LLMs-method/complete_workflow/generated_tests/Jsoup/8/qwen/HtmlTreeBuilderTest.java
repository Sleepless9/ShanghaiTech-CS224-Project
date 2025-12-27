package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HtmlTreeBuilderTest {

    @Test
    public void testEscapeModeMapsContainExpectedEntities() {
        // Test XHTML mode contains only the 5 restricted entities
        Map<Character, String> xhtmlMap = Entities.EscapeMode.xhtml.getMap();
        assertEquals(5, xhtmlMap.size());
        assertTrue(xhtmlMap.containsKey('<'));
        assertTrue(xhtmlMap.containsKey('>'));
        assertTrue(xhtmlMap.containsKey('&'));
        assertTrue(xhtmlMap.containsKey('\''));
        assertTrue(xhtmlMap.containsKey('"'));

        // Test base mode contains common entities including lt, gt, amp
        Map<Character, String> baseMap = Entities.EscapeMode.base.getMap();
        assertFalse(baseMap.isEmpty());
        assertTrue(baseMap.containsKey('<'));
        assertTrue(baseMap.containsKey('>'));
        assertTrue(baseMap.containsKey('&'));

        // Test extended mode has more entities than base mode
        Map<Character, String> extendedMap = Entities.EscapeMode.extended.getMap();
        assertTrue(extendedMap.size() > baseMap.size());
    }

    @Test
    public void testEscapeWithSpecialCharacters() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        
        // Test basic HTML escaping with base mode
        String result1 = Entities.escape("<script>alert('XSS')</script>", encoder, Entities.EscapeMode.base);
        assertEquals("&lt;script&gt;alert(&#x27;XSS&#x27;)&lt;/script&gt;", result1);
        
        // Test xhtml mode escaping
        String result2 = Entities.escape("a < b & c > d", encoder, Entities.EscapeMode.xhtml);
        assertEquals("a &lt; b &amp; c &gt; d", result2);
        
        // Test that characters not in the map but encodable are preserved
        String result3 = Entities.escape("normal text", encoder, Entities.EscapeMode.xhtml);
        assertEquals("normal text", result3);
    }

    @Test
    public void testUnescapeBasicEntities() {
        // Test unescaping of basic named entities
        String result1 = Entities.unescape("&lt;&gt;&amp;&quot;");
        assertEquals("<>&\"", result1);
        
        // Test unescaping of numeric entities (decimal)
        String result2 = Entities.unescape("&#65;&#66;&#67;");
        assertEquals("ABC", result2);
        
        // Test unescaping of numeric entities (hexadecimal)
        String result3 = Entities.unescape("&#x41;&#x42;&#x43;");
        assertEquals("ABC", result3);
        
        // Test mixed entities
        String result4 = Entities.unescape("The &quot;A&quot; is &#65; and &#x41;");
        assertEquals("The \"A\" is A and A", result4);
    }

    @Test
    public void testUnescapeEdgeCases() {
        // Test string without any ampersands returns original
        String result1 = Entities.unescape("no entities here");
        assertEquals("no entities here", result1);
        
        // Test malformed entities - should remain unchanged
        String result2 = Entities.unescape("malformed &unknownentity and &;");
        assertEquals("malformed &unknownentity and &;", result2);
        
        // Test incomplete numeric entity
        String result3 = Entities.unescape("incomplete &# and &#x;");
        assertEquals("incomplete &# and &#x;", result3);
        
        // Test empty string
        String result4 = Entities.unescape("");
        assertEquals("", result4);
    }
}