package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.charset.StandardCharsets;
import java.nio.charset.CharsetEncoder;

public class SelectorTest {

    @Test
    public void testEscapeBaseMode() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        String input = "Hello & World <>";
        String result = Entities.escape(input, encoder, Entities.EscapeMode.base);
        
        // Should escape &, <, > using base entities
        assertTrue(result.contains("&amp;"));
        assertTrue(result.contains("&lt;"));
        assertTrue(result.contains("&gt;"));
        assertFalse(result.contains("Hello & World <>"));
        
        // Test with special characters that need numeric escaping
        String specialInput = "Euro: €";
        String specialResult = Entities.escape(specialInput, encoder, Entities.EscapeMode.base);
        assertTrue(specialResult.contains("&#"));
        assertTrue(specialResult.contains("€"));
    }
    
    @Test
    public void testEscapeExtendedMode() {
        CharsetEncoder encoder = StandardCharsets.US_ASCII.newEncoder(); // ASCII can't encode non-ASCII chars
        String input = "Copyright © and registered ®";
        String result = Entities.escape(input, encoder, Entities.EscapeMode.extended);
        
        // In extended mode, should use full entity map
        assertTrue(result.contains("&copy;"));
        assertTrue(result.contains("&reg;"));
        
        // Characters not in the entity map should be numerically escaped
        assertTrue(result.contains("&#174;")); // for ® if not found in base
        
        // Test with a simple case that shouldn't change
        String noEntities = "Simple text";
        String noEntitiesResult = Entities.escape(noEntities, encoder, Entities.EscapeMode.base);
        assertEquals(noEntities, noEntitiesResult);
    }
    
    @Test
    public void testUnescapeBasic() {
        String input = "&amp; &lt; &gt; &quot; &apos;";
        String result = Entities.unescape(input);
        
        assertEquals("& < > \" \'", result);
        
        // Test with mixed content
        String mixed = "The &quot;quick&quot; brown fox &lt;jumps&gt; over the lazy dog &copy;";
        String mixedResult = Entities.unescape(mixed);
        assertTrue(mixedResult.contains("\"quick\""));
        assertTrue(mixedResult.contains("<jumps>"));
        assertTrue(mixedResult.contains("©"));
        
        // Test with no ampersands (should return original)
        String noAmp = "No entities here";
        String noAmpResult = Entities.unescape(noAmp);
        assertEquals(noAmp, noAmpResult);
    }
    
    @Test
    public void testUnescapeNumeric() {
        // Test decimal numeric references
        String decimal = "&#65;&#66;&#67;";
        String decimalResult = Entities.unescape(decimal);
        assertEquals("ABC", decimalResult);
        
        // Test hexadecimal numeric references
        String hex = "&#x41;&#x42;&#x43;";
        String hexResult = Entities.unescape(hex);
        assertEquals("ABC", hexResult);
        
        // Test mixed numeric and named
        String mixed = "&amp;#65; &#x41; &copy;";
        String mixedResult = Entities.unescape(mixed);
        assertTrue(mixedResult.contains("A A ©"));
        
        // Test invalid numeric reference (should be preserved)
        String invalid = "&#999999;"; // out of range
        String invalidResult = Entities.unescape(invalid);
        assertEquals(invalid, invalidResult);
    }
    
    @Test
    public void testUnescapeEdgeCases() {
        // Test empty string
        assertEquals("", Entities.unescape(""));
        
        // Test null-like cases (though method doesn't accept null)
        String singleAmp = "&";
        assertEquals(singleAmp, Entities.unescape(singleAmp));
        
        // Test incomplete entity
        String incomplete = "&incomplete";
        assertEquals(incomplete, Entities.unescape(incomplete));
        
        // Test entity without semicolon (should still work for base entities)
        String noSemicolon = "&amp &lt &gt";
        String noSemicolonResult = Entities.unescape(noSemicolon);
        assertEquals("& < >", noSemicolonResult);
        
        // Test malformed numeric
        String malformed = "&#xyz;";
        String malformedResult = Entities.unescape(malformed);
        assertEquals(malformed, malformedResult);
    }
}