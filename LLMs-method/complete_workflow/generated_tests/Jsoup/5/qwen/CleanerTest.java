package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import org.jsoup.nodes.Entities.EscapeMode;

public class CleanerTest {

    @Test
    public void testEscapeWithBaseMode() {
        CharsetEncoder encoder = Charset.forName("US-ASCII").newEncoder();
        String input = "Hello & World <>";
        String result = Entities.escape(input, encoder, EscapeMode.base);
        
        assertNotNull(result);
        assertNotEquals(input, result);
        assertTrue(result.contains("&amp;"));
        assertTrue(result.contains("&lt;"));
        assertTrue(result.contains("&gt;"));
        assertFalse(result.contains("&#"));
    }

    @Test
    public void testEscapeWithExtendedMode() {
        CharsetEncoder encoder = Charset.forName("US-ASCII").newEncoder();
        String input = "Copyright © and Euro €";
        String result = Entities.escape(input, encoder, EscapeMode.extended);
        
        assertNotNull(result);
        assertTrue(result.contains("&copy;"));
        assertTrue(result.contains("&#8364;")); // Euro symbol not in base, so numeric
        assertEquals(2, result.split("&").length - 1); // Two entities
    }

    @Test
    public void testUnescapeBasicEntities() {
        String input = "Hello &amp; World &lt;&gt; &quot;test&quot;";
        String result = Entities.unescape(input);
        
        assertNotNull(result);
        assertEquals("Hello & World <> \"test\"", result);
        assertFalse(result.contains("&amp;"));
        assertFalse(result.contains("&lt;"));
        assertFalse(result.contains("&quot;"));
    }

    @Test
    public void testUnescapeNumericEntities() {
        String input = "Euro: &#8364; or &#x20AC;; Null: &#0;";
        String result = Entities.unescape(input);
        
        assertNotNull(result);
        assertTrue(result.contains("€"));
        assertTrue(result.contains("\u0000"));
        assertEquals(3, result.codePointCount(0, result.length()));
    }

    @Test
    public void testUnescapeMixedAndEdgeCases() {
        String input = "No entities here";
        String same = Entities.unescape(input);
        assertEquals(input, same);
        
        String partial = "Apos: &apos; &invalid; &amp";
        String result = Entities.unescape(partial);
        
        assertEquals("Apos: ' &invalid; &", result);
        assertTrue(result.contains("&invalid;"));
        assertFalse(result.contains("&apos;"));
        assertTrue(result.contains("&"));
    }
}