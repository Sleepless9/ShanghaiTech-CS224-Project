package org.joda.time.format;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;

public class LocalDateTimeTest {

    @Test
    public void testAppendLiteral() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test single character literal
        builder.appendLiteral('X');
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test string literal
        builder.clear();
        builder.appendLiteral("Test");
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test empty string literal
        builder.clear();
        builder.appendLiteral("");
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test null literal throws exception
        try {
            builder.appendLiteral((String) null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAppendDecimal() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test normal case
        builder.appendDecimal(DateTimeFieldType.year(), 4, 4);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test with minDigits <= maxDigits
        builder.clear();
        builder.appendDecimal(DateTimeFieldType.dayOfMonth(), 3, 5);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test null field type throws exception
        try {
            builder.appendDecimal(null, 1, 2);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test invalid digit values
        try {
            builder.appendDecimal(DateTimeFieldType.hourOfDay(), -1, 2);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testToFormatterAndCapabilities() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test building formatter after appending elements
        builder.appendText(DateTimeFieldType.monthOfYear())
               .appendLiteral(' ')
               .appendYear(4, 4);
        
        assertTrue(builder.canBuildFormatter());
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test toFormatter creates a valid formatter
        DateTimeFormatter formatter = builder.toFormatter();
        assertNotNull(formatter);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        
        // Test clear functionality
        builder.clear();
        assertFalse(builder.canBuildPrinter());
        assertFalse(builder.canBuildParser());
        assertFalse(builder.canBuildFormatter());
    }

    @Test
    public void testAppendOptional() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Create a parser for optional element
        DateTimeParser parser = new DateTimeFormatterBuilder()
            .appendLiteral("Optional").toParser();
        
        // Append as optional
        builder.appendOptional(parser);
        assertTrue(builder.canBuildParser());
        
        // Verify the parser is properly wrapped
        Object formatter = ((DateTimeFormatterBuilderAccessor) builder).getFormatter();
        assertTrue(formatter instanceof DateTimeFormatterBuilder.Composite);
        
        // Test that required elements work normally
        builder.clear();
        builder.append(parser);
        assertTrue(builder.canBuildParser());
    }

    @Test
    public void testAppendTimeZoneName() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test appendTimeZoneName without lookup
        builder.appendTimeZoneName();
        assertTrue(builder.canBuildPrinter());
        assertFalse(builder.canBuildParser()); // Can't parse without lookup
        
        // Test appendTimeZoneName with lookup
        builder.clear();
        Map<String, DateTimeZone> lookup = new HashMap<>();
        lookup.put("UTC", DateTimeZone.UTC);
        builder.appendTimeZoneName(lookup);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test appendTimeZoneShortName with lookup
        builder.clear();
        builder.appendTimeZoneShortName(lookup);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test appendTimeZoneId (bidirectional)
        builder.clear();
        builder.appendTimeZoneId();
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
    }
    
    // Helper interface to access private methods for testing
    private interface DateTimeFormatterBuilderAccessor {
        Object getFormatter();
    }
}