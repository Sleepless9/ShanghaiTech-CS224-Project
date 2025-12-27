package org.joda.time.format;

import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DateTimeFieldType;
import java.util.HashMap;
import java.util.Map;

public class MutableDateTimeTest {

    @Test
    public void testToFormatter_NormalCase() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendYear(4, 4);
        builder.appendLiteral('-');
        builder.appendMonthOfYear(2);
        
        DateTimeFormatter formatter = builder.toFormatter();
        
        assertNotNull(formatter);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertTrue(builder.canBuildFormatter());
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
    }
    
    @Test
    public void testAppendLiteral_BoundaryCases() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test empty string
        builder.appendLiteral("");
        assertFalse(builder.canBuildPrinter());
        assertFalse(builder.canBuildParser());
        
        // Clear and test single character
        builder.clear();
        builder.appendLiteral('A');
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test multi-character string
        builder.clear();
        builder.appendLiteral("Hello");
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test null handling - expect exception
        try {
            builder.appendLiteral((String) null);
            fail("Expected IllegalArgumentException for null literal");
        } catch (IllegalArgumentException e) {
            assertEquals("Literal must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testAppendDecimal_ExceptionCases() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test null field type
        try {
            builder.appendDecimal(null, 1, 2);
            fail("Expected IllegalArgumentException for null field type");
        } catch (IllegalArgumentException e) {
            assertEquals("Field type must not be null", e.getMessage());
        }
        
        // Test invalid digit parameters
        try {
            builder.appendDecimal(DateTimeFieldType.year(), -1, 0);
            fail("Expected IllegalArgumentException for negative minDigits");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test valid case
        builder.clear();
        DateTimeFormatterBuilder result = builder.appendDecimal(DateTimeFieldType.dayOfMonth(), 2, 2);
        assertSame(builder, result);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
    }
    
    @Test
    public void testAppendOptional_ParserOnly() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Create a simple parser
        DateTimeParser mockParser = new CharacterLiteral('X');
        
        // Append optional parser
        builder.appendOptional(mockParser);
        
        // Should be able to build parser but not printer
        assertTrue(builder.canBuildParser());
        assertFalse(builder.canBuildPrinter());
        
        // toPrinter should throw exception
        try {
            builder.toPrinter();
            fail("Expected UnsupportedOperationException for missing printer");
        } catch (UnsupportedOperationException e) {
            assertEquals("Printing is not supported", e.getMessage());
        }
        
        // toParser should work
        DateTimeParser parser = builder.toParser();
        assertNotNull(parser);
    }
    
    @Test
    public void testTimeZoneName_WithLookup() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Create lookup map
        Map<String, DateTimeZone> lookup = new HashMap<>();
        lookup.put("UTC", DateTimeZone.UTC);
        lookup.put("GMT", DateTimeZone.forOffsetHours(0));
        
        // Append with lookup
        builder.appendTimeZoneName(lookup);
        
        // Should support both printing and parsing
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Clear should reset state
        builder.clear();
        assertFalse(builder.canBuildPrinter());
        assertFalse(builder.canBuildParser());
        
        // Verify builder is reusable after clear
        builder.appendYear(4, 4);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
    }
}