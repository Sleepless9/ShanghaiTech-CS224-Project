package org.jsoup.nodes;

import org.junit.Test;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testEscapeXhtmlMode() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        String input = "<div>\"test\" & 'sample'</div>";
        String escaped = Entities.escape(input, encoder, Entities.EscapeMode.xhtml);
        
        assertEquals("&lt;div&gt;&quot;test&quot; &amp; &apos;sample&apos;&lt;/div&gt;", escaped);
        assertFalse(escaped.contains("<"));
        assertFalse(escaped.contains("\""));
    }

    @Test
    public void testEscapeBaseModeWithNonAscii() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        String input = "© 2023 café";
        String escaped = Entities.escape(input, encoder, Entities.EscapeMode.base);
        
        assertTrue(escaped.contains("&copy;"));
        assertTrue(escaped.contains("café") || escaped.contains("caf&#"));
        assertFalse(escaped.contains("©"));
    }

    @Test
    public void testEscapeExtendedMode() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        String input = "∑ α β";
        String escaped = Entities.escape(input, encoder, Entities.EscapeMode.extended);
        
        assertTrue(escaped.contains("&sum;"));
        assertTrue(escaped.contains("&alpha;") || escaped.contains("&#945;"));
        assertTrue(escaped.contains("&beta;") || escaped.contains("&#946;"));
    }

    @Test
    public void testUnescapeNumericReferences() {
        String input1 = "&#65;&#x42;&#x43;";
        String result1 = Entities.unescape(input1);
        assertEquals("ABC", result1);
        
        String input2 = "&#x3C;div&#x3E;";
        String result2 = Entities.unescape(input2);
        assertEquals("<div>", result2);
        
        assertNotEquals(input1, result1);
        assertNotEquals(input2, result2);
    }

    @Test
    public void testUnescapeNamedReferences() {
        String input1 = "&lt;&gt;&amp;&quot;&apos;";
        String result1 = Entities.unescape(input1);
        assertEquals("<>&\"'", result1);
        
        String input2 = "&copy;&reg;&trade;";
        String result2 = Entities.unescape(input2);
        assertTrue(result2.contains("©"));
        assertTrue(result2.contains("®"));
        assertTrue(result2.contains("™"));
        
        String input3 = "test &amp test"; // missing semicolon
        String result3 = Entities.unescape(input3);
        assertEquals("test &amp test", result3);
    }

    @Test
    public void testUnescapeMixedAndInvalid() {
        String input = "Price: &pound;100 &#x26; $150 &invalid;";
        String result = Entities.unescape(input);
        
        assertTrue(result.contains("£"));
        assertTrue(result.contains("&"));
        assertTrue(result.contains("&invalid;"));
        assertEquals("Price: £100 & $150 &invalid;", result);
    }
}