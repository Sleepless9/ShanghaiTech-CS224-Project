package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Map;

public class EntitiesTest {
    
    @Test
    public void testEscapeModeMaps() {
        // Test that EscapeMode maps are properly initialized and contain expected values
        Map<Character, String> xhtmlMap = Entities.EscapeMode.xhtml.getMap();
        Map<Character, String> baseMap = Entities.EscapeMode.base.getMap();
        Map<Character, String> extendedMap = Entities.EscapeMode.extended.getMap();
        
        // XHTML should have exactly 5 entries
        assertEquals(5, xhtmlMap.size());
        assertTrue(xhtmlMap.containsKey('<'));
        assertTrue(xhtmlMap.containsKey('>'));
        assertTrue(xhtmlMap.containsKey('&'));
        assertTrue(xhtmlMap.containsKey('\''));
        assertTrue(xhtmlMap.containsKey('"'));
        
        // Base should have more entries than XHTML
        assertTrue(baseMap.size() > xhtmlMap.size());
        assertTrue(baseMap.containsKey('©'));
        assertTrue(baseMap.containsKey('®'));
        
        // Extended should have the most entries
        assertTrue(extendedMap.size() > baseMap.size());
        assertTrue(extendedMap.containsKey('∑'));
        assertTrue(extendedMap.containsKey('∫'));
    }
    
    @Test
    public void testEscapeNormalCases() {
        // Test normal escaping with different modes
        CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
        
        // Test basic HTML entities with xhtml mode
        String result1 = Entities.escape("<script>&\"'</script>", null, Entities.EscapeMode.xhtml);
        assertEquals("&lt;script&gt;&amp;&quot;'&lt;/script&gt;", result1);
        
        // Test base mode with additional entities
        String result2 = Entities.escape("Café ©", null, Entities.EscapeMode.base);
        assertEquals("Caf&eacute; &copy;", result2);
        
        // Test that characters encodable by UTF-8 are left as-is
        String result3 = Entities.escape("Hello World", encoder, Entities.EscapeMode.xhtml);
        assertEquals("Hello World", result3);
    }
    
    @Test
    public void testUnescapeNormalCases() {
        // Test normal unescaping cases
        String result1 = Entities.unescape("&lt;div&gt;Hello&lt;/div&gt;");
        assertEquals("<div>Hello</div>", result1);
        
        String result2 = Entities.unescape("&copy; 2023 &amp; &reg;");
        assertEquals("© 2023 & ®", result2);
        
        String result3 = Entities.unescape("&#65;&#x41;"); // A in decimal and hex
        assertEquals("AA", result3);
        
        // Test mixed content
        String result4 = Entities.unescape("Price: &pound;10 &lt; $15");
        assertEquals("Price: £10 < $15", result4);
    }
    
    @Test
    public void testUnescapeBoundaryCases() {
        // Test boundary cases for unescape
        String result1 = Entities.unescape("No ampersands here");
        assertEquals("No ampersands here", result1);
        
        String result2 = Entities.unescape("");
        assertEquals("", result2);
        
        // Test incomplete entities (should be preserved)
        String result3 = Entities.unescape("Incomplete: &lt &invalid; &");
        assertEquals("Incomplete: &lt &invalid; &", result3);
        
        // Test numeric entities at boundaries
        String result4 = Entities.unescape("&#0;&#65535;");
        assertTrue(result4.length() == 2);
        assertEquals('\u0000', result4.charAt(0));
        assertEquals('\uFFFF', result4.charAt(1));
    }
    
    @Test
    public void testEscapeSpecialCharacters() {
        CharsetEncoder encoder = Charset.forName("US-ASCII").newEncoder(); // ASCII can't encode non-ASCII
        
        // Test that non-encodable characters are escaped numerically
        String result1 = Entities.escape("€", encoder, Entities.EscapeMode.xhtml);
        assertEquals("&#8364;", result1);
        
        // Test a character not in any named entity map
        String result2 = Entities.escape("☺", encoder, Entities.EscapeMode.xhtml);
        assertTrue(result2.startsWith("&#"));
        assertTrue(result2.endsWith(";"));
        
        // Test multiple special characters
        String result3 = Entities.escape(" naïve résumé ", encoder, Entities.EscapeMode.base);
        assertTrue(result3.contains("&#"));
        assertFalse(result3.contains("naïve"));
        assertFalse(result3.contains("résumé"));
    }
}