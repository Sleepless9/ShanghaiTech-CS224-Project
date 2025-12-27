package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.charset.StandardCharsets;
import java.nio.charset.CharsetEncoder;

public class ElementTest {

    @Test
    public void testEscapeBaseMode() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        String input = "Hello & World < > \" '";
        String result = Entities.escape(input, encoder, Entities.EscapeMode.base);
        
        assertEquals("Hello &amp; World &lt; &gt; &quot; '", result);
        
        // Test with characters that don't need escaping
        String noEscapeInput = "Normal text without entities";
        String noEscapeResult = Entities.escape(noEscapeInput, encoder, Entities.EscapeMode.base);
        assertEquals("Normal text without entities", noEscapeResult);
    }
    
    @Test
    public void testEscapeExtendedMode() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        String input = "Copyright © Registered ®";
        String result = Entities.escape(input, encoder, Entities.EscapeMode.extended);
        
        assertTrue(result.contains("&copy;"));
        assertTrue(result.contains("&reg;"));
        
        // Test with special characters that require numeric escape
        String specialChars = "©®™";
        String specialResult = Entities.escape(specialChars, encoder, Entities.EscapeMode.base);
        
        assertTrue(specialResult.contains("&copy;"));
        assertTrue(specialResult.contains("&reg;"));
    }
    
    @Test
    public void testUnescapeBasicEntities() {
        String input = "Hello &amp; World &lt; &gt; &quot;";
        String result = Entities.unescape(input);
        
        assertEquals("Hello & World < > \"", result);
        
        // Test with common base entities
        String baseEntities = "&AElig; &AMP; &Aacute; &Acirc; &Agrave;";
        String baseResult = Entities.unescape(baseEntities);
        
        assertTrue(baseResult.contains("Æ"));
        assertTrue(baseResult.contains("&"));
        assertTrue(baseResult.contains("Á"));
        assertTrue(baseResult.contains("Â"));
        assertTrue(baseResult.contains("À"));
    }
    
    @Test
    public void testUnescapeNumericEntities() {
        String decimalInput = "Euro: &#8364; Cent: &#162;";
        String decimalResult = Entities.unescape(decimalInput);
        
        assertTrue(decimalResult.contains("€"));
        assertTrue(decimalResult.contains("¢"));
        
        String hexInput = "Euro: &#x20AC; Cent: &#xA2;";
        String hexResult = Entities.unescape(hexInput);
        
        assertTrue(hexResult.contains("€"));
        assertTrue(hexResult.contains("¢"));
        
        // Test mixed entities
        String mixedInput = "Quote: &quot; Number: &#34; Hex: &#x22;";
        String mixedResult = Entities.unescape(mixedInput);
        
        assertEquals("Quote: \" Number: \" Hex: \"", mixedResult);
    }
    
    @Test
    public void testUnescapeEdgeCases() {
        // Test string without ampersand
        String noAmpersand = "Normal string without entities";
        String noAmpResult = Entities.unescape(noAmpersand);
        assertEquals("Normal string without entities", noAmpResult);
        
        // Test malformed entities (should remain unchanged)
        String malformed = "This is &notAnEntity and &invalid; but &amp; works";
        String malformedResult = Entities.unescape(malformed);
        
        assertTrue(malformedResult.contains("&notAnEntity"));
        assertTrue(malformedResult.contains("&invalid;"));
        assertTrue(malformedResult.contains("&"));
        assertTrue(malformedResult.contains("works"));
        
        // Test empty string
        String emptyResult = Entities.unescape("");
        assertEquals("", emptyResult);
    }
}