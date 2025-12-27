package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.charset.StandardCharsets;
import java.nio.charset.CharsetEncoder;

public class ParserTest {
    
    @Test
    public void testEscapeWithXhtmlMode() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        String input = "<>&\"'";
        String result = Entities.escape(input, encoder, Entities.EscapeMode.xhtml);
        
        assertEquals("&lt;&gt;&amp;&quot;&apos;", result);
        
        // Test with characters that should not be escaped in XHTML mode
        String nonXhtmlInput = "©®";
        String nonXhtmlResult = Entities.escape(nonXhtmlInput, encoder, Entities.EscapeMode.xhtml);
        assertEquals("&#169;&#174;", nonXhtmlResult);
    }
    
    @Test
    public void testEscapeWithBaseMode() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        String input = "<>&\"áéíóú";
        String result = Entities.escape(input, encoder, Entities.EscapeMode.base);
        
        assertTrue(result.contains("&lt;"));
        assertTrue(result.contains("&gt;"));
        assertTrue(result.contains("&amp;"));
        assertTrue(result.contains("&quot;"));
        assertTrue(result.contains("&aacute;"));
        assertTrue(result.contains("&eacute;"));
    }
    
    @Test
    public void testUnescapeBasicEntities() {
        String input = "&lt;&gt;&amp;&quot;&apos;";
        String result = Entities.unescape(input);
        
        assertEquals("<>&\"'", result);
        
        // Test numeric entities
        String numericInput = "&#65;&#x41;";
        String numericResult = Entities.unescape(numericInput);
        assertEquals("AA", numericResult);
    }
    
    @Test
    public void testUnescapeFullEntities() {
        String input = "&alpha;&beta;&copy;&reg;";
        String result = Entities.unescape(input);
        
        assertEquals("αβ©®", result);
        
        // Test with mixed content
        String mixedInput = "The &pi; is approximately 3.14 and &euro; is €";
        String mixedResult = Entities.unescape(mixedInput);
        assertTrue(mixedResult.contains("π"));
        assertTrue(mixedResult.contains("€"));
    }
    
    @Test
    public void testUnescapeEdgeCases() {
        // Test string without ampersand
        String noAmpersand = "Hello World";
        String result1 = Entities.unescape(noAmpersand);
        assertEquals("Hello World", result1);
        
        // Test malformed entities
        String malformed = "&unknown;&";
        String result2 = Entities.unescape(malformed);
        assertEquals("&unknown;&", result2);
        
        // Test empty string
        String empty = "";
        String result3 = Entities.unescape(empty);
        assertEquals("", result3);
    }
}