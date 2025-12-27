package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ParserTest {

    @Test
    public void testEscapeModeMapsInitialized() {
        EscapeMode xhtmlMode = EscapeMode.xhtml;
        EscapeMode baseMode = EscapeMode.base;
        EscapeMode extendedMode = EscapeMode.extended;
        
        Map<Character, String> xhtmlMap = xhtmlMode.getMap();
        Map<Character, String> baseMap = baseMode.getMap();
        Map<Character, String> fullMap = extendedMode.getMap();
        
        assertNotNull("XHTML map should not be null", xhtmlMap);
        assertNotNull("Base map should not be null", baseMap);
        assertNotNull("Extended map should not be null", fullMap);
        
        // Verify XHTML has only 5 entities
        assertEquals("XHTML mode should have 5 entities", 5, xhtmlMap.size());
        assertTrue("XHTML map should contain &amp;", xhtmlMap.containsKey('&'));
        assertTrue("XHTML map should contain &lt;", xhtmlMap.containsKey('<'));
        assertTrue("XHTML map should contain &gt;", xhtmlMap.containsKey('>'));
        assertTrue("XHTML map should contain &quot;", xhtmlMap.containsKey('\"'));
        assertTrue("XHTML map should contain &apos;", xhtmlMap.containsKey('\''));
        
        // Verify base is larger than XHTML
        assertTrue("Base mode should have more entities than XHTML", baseMap.size() > xhtmlMap.size());
        assertTrue("Base map should contain &amp;", baseMap.containsKey('&'));
    }

    @Test
    public void testEscapeNormalCases() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.escapeMode(EscapeMode.xhtml);
        settings.encoder(encoder);
        
        // Test basic HTML escaping with XHTML mode
        String result1 = Entities.escape("<div>Hello & World</div>", settings);
        assertEquals("&lt;div&gt;Hello &amp; World&lt;/div&gt;", result1);
        
        // Test with quotes in XHTML mode
        String result2 = Entities.escape("He said \"Hello\" and 'World'", settings);
        assertEquals("He said &quot;Hello&quot; and &#39;World&#39;", result2);
        
        // Test with base mode (more entities)
        settings.escapeMode(EscapeMode.base);
        String result3 = Entities.escape("Copyright © 2023", settings);
        assertEquals("Copyright &copy; 2023", result3);
    }

    @Test
    public void testUnescapeNormalCases() {
        // Test unescaping common named entities
        String result1 = Entities.unescape("&lt;div&gt;Hello &amp; World&lt;/div&gt;");
        assertEquals("<div>Hello & World</div>", result1);
        
        // Test unescaping numeric entities (decimal)
        String result2 = Entities.unescape("Copyright &#169; 2023");
        assertEquals("Copyright © 2023", result2);
        
        // Test unescaping numeric entities (hexadecimal)
        String result3 = Entities.unescape("Euro: &#x20AC;");
        assertEquals("Euro: €", result3);
        
        // Test mixed entities
        String result4 = Entities.unescape("&quot;Hello&quot; &lt; &copy; &amp; &reg;");
        assertEquals("\"Hello\" < © & ®", result4);
    }

    @Test
    public void testUnescapeBoundaryCases() {
        // Test string without any ampersands (should return original)
        String result1 = Entities.unescape("No entities here");
        assertEquals("No entities here", result1);
        
        // Test incomplete entity (should preserve as is)
        String result2 = Entities.unescape("Incomplete &entity;");
        assertEquals("Incomplete &entity;", result2);
        
        // Test malformed numeric entity
        String result3 = Entities.unescape("Malformed &#xyz;");
        assertEquals("Malformed &#xyz;", result3);
        
        // Test empty string
        String result4 = Entities.unescape("");
        assertEquals("", result4);
    }

    @Test
    public void testEscapeBoundaryCases() {
        CharsetEncoder encoder = StandardCharsets.US_ASCII.newEncoder();
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.escapeMode(EscapeMode.xhtml);
        settings.encoder(encoder);
        
        // Test empty string
        String result1 = Entities.escape("", settings);
        assertEquals("", result1);
        
        // Test string with characters that can't be encoded
        String result2 = Entities.escape("Unicode: €", settings);
        assertEquals("Unicode: &#8364;", result2);
        
        // Test null character handling
        String result3 = Entities.escape("\u0000", settings);
        assertEquals("&#0;", result3);
    }
}