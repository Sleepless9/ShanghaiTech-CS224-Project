package org.joda.time.format;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatterBuilder.CharacterLiteral;
import org.joda.time.format.DateTimeFormatterBuilder.StringLiteral;
import org.joda.time.format.DateTimeFormatterBuilder.TextField;
import org.joda.time.format.DateTimeFormatterBuilder.TwoDigitYear;
import org.joda.time.format.DateTimeFormatterBuilder.UnpaddedNumber;
import org.joda.time.format.DateTimeFormatterBuilder.PaddedNumber;
import org.joda.time.format.DateTimeFormatterBuilder.FixedNumber;
import org.joda.time.format.DateTimeFormatterBuilder.Fraction;
import org.joda.time.format.DateTimeFormatterBuilder.TimeZoneOffset;
import org.joda.time.format.DateTimeFormatterBuilder.TimeZoneName;
import org.joda.time.format.DateTimeFormatterBuilder.Composite;
import org.joda.time.format.DateTimeFormatterBuilder.MatchingParser;
import org.joda.time.tz.ZoneInfoProvider;
import org.joda.time.DateTimeZone;
import org.junit.Test;

public class DateTimeFormatterBuilderTest {

    @Test
    public void testConstructorAndClear() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        assertNotNull(builder);
        
        // Test clear on empty builder
        builder.clear();
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Add an element then clear
        builder.appendLiteral("test");
        builder.clear();
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Verify internal state is cleared
        assertEquals(0, builder.getClass().getDeclaredFields().length);
    }

    @Test
    public void testAppendMethods() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test append with formatter
        DateTimeFormatter mockFormatter = mock(DateTimeFormatter.class);
        when(mockFormatter.getPrinter()).thenReturn(mock(DateTimePrinter.class));
        when(mockFormatter.getParser()).thenReturn(mock(DateTimeParser.class));
        
        DateTimeFormatterBuilder result = builder.append(mockFormatter);
        assertSame(builder, result);
        
        // Test append with printer only
        DateTimePrinter mockPrinter = mock(DateTimePrinter.class);
        result = builder.append(mockPrinter);
        assertSame(builder, result);
        
        // Test append with parser only
        DateTimeParser mockParser = mock(DateTimeParser.class);
        result = builder.append(mockParser);
        assertSame(builder, result);
        
        // Test append with printer and parser
        result = builder.append(mockPrinter, mockParser);
        assertSame(builder, result);
    }

    @Test
    public void testAppendLiteral() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test char literal
        DateTimeFormatterBuilder result = builder.appendLiteral('a');
        assertSame(builder, result);
        
        // Test string literal
        result = builder.appendLiteral("test");
        assertSame(builder, result);
        
        // Test empty string
        result = builder.appendLiteral("");
        assertSame(builder, result);
        
        // Test null string - should throw exception
        try {
            builder.appendLiteral((String) null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAppendDecimal() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test normal case
        DateTimeFormatterBuilder result = builder.appendDecimal(DateTimeFieldType.year(), 4, 4);
        assertSame(builder, result);
        
        // Test minDigits <= 1 case
        result = builder.appendDecimal(DateTimeFieldType.monthOfYear(), 1, 2);
        assertSame(builder, result);
        
        // Test null fieldType - should throw exception
        try {
            builder.appendDecimal(null, 4, 4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test invalid digit ranges
        try {
            builder.appendDecimal(DateTimeFieldType.year(), -1, 4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testToFormatter() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test building formatter with both printer and parser
        builder.appendLiteral("test");
        DateTimeFormatter formatter = builder.toFormatter();
        assertNotNull(formatter);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        
        // Test building formatter with only printer
        builder.clear();
        builder.append(new CharacterLiteral('a'));
        formatter = builder.toFormatter();
        assertNotNull(formatter);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        
        // Test canBuild methods
        assertTrue(builder.canBuildFormatter());
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
    }

    @Test
    public void testAppendText() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test appendText
        DateTimeFormatterBuilder result = builder.appendText(DateTimeFieldType.monthOfYear());
        assertSame(builder, result);
        
        // Test appendShortText
        result = builder.appendShortText(DateTimeFieldType.dayOfWeek());
        assertSame(builder, result);
        
        // Test appendDayOfWeekText
        result = builder.appendDayOfWeekText();
        assertSame(builder, result);
        
        // Test null fieldType - should throw exception
        try {
            builder.appendText(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAppendFraction() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test appendFraction
        DateTimeFormatterBuilder result = builder.appendFraction(DateTimeFieldType.secondOfDay(), 1, 3);
        assertSame(builder, result);
        
        // Test appendFractionOfSecond
        result = builder.appendFractionOfSecond(1, 3);
        assertSame(builder, result);
        
        // Test appendFractionOfMinute
        result = builder.appendFractionOfMinute(1, 3);
        assertSame(builder, result);
        
        // Test null fieldType - should throw exception
        try {
            builder.appendFraction(null, 1, 3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAppendFixedDecimal() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test normal case
        DateTimeFormatterBuilder result = builder.appendFixedDecimal(DateTimeFieldType.year(), 4);
        assertSame(builder, result);
        
        // Test null fieldType - should throw exception
        try {
            builder.appendFixedDecimal(null, 4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test invalid numDigits
        try {
            builder.appendFixedDecimal(DateTimeFieldType.year(), 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAppendTwoDigitYear() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test normal case
        DateTimeFormatterBuilder result = builder.appendTwoDigitYear(2000);
        assertSame(builder, result);
        
        // Test lenient parse case
        result = builder.appendTwoDigitYear(2000, true);
        assertSame(builder, result);
        
        // Verify TwoDigitYear creation
        assertTrue(builder.toString().contains("TwoDigitYear"));
    }

    @Test
    public void testAppendTimeZone() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test appendTimeZoneId
        DateTimeFormatterBuilder result = builder.appendTimeZoneId();
        assertSame(builder, result);
        
        // Test appendTimeZoneOffset
        result = builder.appendTimeZoneOffset("Z", false, 1, 3);
        assertSame(builder, result);
        
        // Test appendTimeZoneOffset with separate print/parse texts
        result = builder.appendTimeZoneOffset("UTC", "Z", true, 1, 4);
        assertSame(builder, result);
        
        // Test invalid field parameters
        try {
            builder.appendTimeZoneOffset("Z", false, 0, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAppendOptional() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Create a mock parser
        DateTimeParser mockParser = mock(DateTimeParser.class);
        when(mockParser.estimateParsedLength()).thenReturn(1);
        
        // Test appendOptional
        DateTimeFormatterBuilder result = builder.appendOptional(mockParser);
        assertSame(builder, result);
        
        // Verify the MatchingParser was created with proper array
        assertTrue(builder.toString().contains("MatchingParser"));
    }

    @Test
    public void testAppendWithParsersArray() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Create mock parsers
        DateTimeParser mockParser1 = mock(DateTimeParser.class);
        DateTimeParser mockParser2 = mock(DateTimeParser.class);
        when(mockParser1.estimateParsedLength()).thenReturn(1);
        when(mockParser2.estimateParsedLength()).thenReturn(1);
        
        DateTimeParser[] parsers = {mockParser1, mockParser2};
        
        // Test append with printers and parsers array
        DateTimeFormatterBuilder result = builder.append(null, parsers);
        assertSame(builder, result);
        
        // Test null parsers array
        try {
            builder.append(null, (DateTimeParser[]) null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test incomplete parser array (null in middle)
        try {
            DateTimeParser[] incompleteParsers = {null, mockParser1};
            builder.append(null, incompleteParsers);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCompositeBuilder() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Add multiple elements to create a composite
        builder.appendLiteral("test");
        builder.appendLiteral("ing");
        
        // Build the formatter
        DateTimeFormatter formatter = builder.toFormatter();
        assertNotNull(formatter);
        
        // Verify it's a composite structure
        assertTrue(formatter.toString().contains("Composite"));
        
        // Test canBuild methods after composition
        assertTrue(builder.canBuildFormatter());
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
    }
}