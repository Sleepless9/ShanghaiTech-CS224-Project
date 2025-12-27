package org.joda.time.format;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.SkipUndoDateTimeField;
import org.joda.time.format.DateTimeFormatterBuilder.CharacterLiteral;
import org.joda.time.format.DateTimeFormatterBuilder.Composite;
import org.joda.time.format.DateTimeFormatterBuilder.FixedNumber;
import org.joda.time.format.DateTimeFormatterBuilder.PaddedNumber;
import org.joda.time.format.DateTimeFormatterBuilder.TextField;
import org.joda.time.format.DateTimeFormatterBuilder.UnpaddedNumber;
import org.joda.time.format.DateTimeFormatterBuilder.TwoDigitYear;
import org.joda.time.format.DateTimeFormatterBuilder.Fraction;
import org.joda.time.format.DateTimeFormatterBuilder.TimeZoneOffset;
import org.joda.time.format.DateTimeFormatterBuilder.TimeZoneName;
import org.joda.time.format.DateTimeFormatterBuilder.MatchingParser;
import org.joda.time.tz.UTCProvider;
import org.joda.time.zone.ZoneInfoProvider;
import org.junit.Test;

public class GregorianChronologyTest {

    @Test
    public void testConstructorAndClear() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        assertNotNull(builder);
        
        // Test clear method
        builder.appendLiteral("test");
        assertTrue(builder.canBuildFormatter());
        builder.clear();
        assertFalse(builder.canBuildFormatter());
        assertEquals(0, builder.canBuildPrinter());
        assertEquals(0, builder.canBuildParser());
    }

    @Test
    public void testAppendLiteral() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test single character literal
        builder.appendLiteral('a');
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test string literal
        builder.appendLiteral("test");
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test empty string literal (should not add anything)
        builder.appendLiteral("");
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Verify formatter can be created
        assertNotNull(builder.toFormatter());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAppendLiteralNull() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendLiteral((String) null);
    }
    
    @Test
    public void testAppendDecimal() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test normal case
        builder.appendDecimal(DateTimeFieldType.year(), 4, 4);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test minDigits > 1 (should create PaddedNumber)
        builder.clear();
        builder.appendDecimal(DateTimeFieldType.monthOfYear(), 2, 2);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test boundary cases
        builder.clear();
        builder.appendDecimal(DateTimeFieldType.dayOfMonth(), 1, 3);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Verify formatter creation
        assertNotNull(builder.toFormatter());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAppendDecimalNullFieldType() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendDecimal(null, 1, 3);
    }
    
    @Test
    public void testAppendFixedDecimal() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test normal case
        builder.appendFixedDecimal(DateTimeFieldType.year(), 4);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Verify formatter creation
        assertNotNull(builder.toFormatter());
        
        // Test invalid digit count
        try {
            builder.clear();
            builder.appendFixedDecimal(DateTimeFieldType.year(), 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testAppendText() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test normal text field
        builder.appendText(DateTimeFieldType.monthOfYear());
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test short text field
        builder.clear();
        builder.appendShortText(DateTimeFieldType.dayOfWeek());
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Verify formatter creation
        assertNotNull(builder.toFormatter());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAppendTextNullFieldType() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendText(null);
    }
    
    @Test
    public void testAppendFraction() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test fraction of second
        builder.appendFractionOfSecond(3, 3);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test fraction of minute
        builder.clear();
        builder.appendFractionOfMinute(2, 2);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test general fraction
        builder.clear();
        builder.appendFraction(DateTimeFieldType.secondOfDay(), 3, 3);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Verify formatter creation
        assertNotNull(builder.toFormatter());
    }
    
    @Test
    public void testAppendTwoDigitYear() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test two digit year with pivot
        builder.appendTwoDigitYear(2000);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test two digit year with lenient parsing
        builder.clear();
        builder.appendTwoDigitYear(2000, true);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Verify formatter creation
        assertNotNull(builder.toFormatter());
    }
    
    @Test
    public void testAppendTimeZoneOffset() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test time zone offset with zero offset text
        builder.appendTimeZoneOffset("Z", true, 1, 4);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test time zone offset with separate parse and print texts
        builder.clear();
        builder.appendTimeZoneOffset("Z", "UTC", true, 1, 4);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Verify formatter creation
        assertNotNull(builder.toFormatter());
    }
    
    @Test
    public void testAppendOptional() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Create a mock parser for testing
        DateTimeParser mockParser = mock(DateTimeParser.class);
        when(mockParser.estimateParsedLength()).thenReturn(1);
        
        // Test optional parser
        builder.appendOptional(mockParser);
        assertTrue(builder.canBuildParser());
        assertFalse(builder.canBuildPrinter()); // No printer provided
        
        // Verify parser can be created
        assertNotNull(builder.toParser());
    }
    
    @Test
    public void testToFormatterExceptions() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test that toFormatter throws exception when neither printer nor parser is supported
        try {
            builder.toFormatter();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
        
        // Test that toPrinter throws exception when printing is not supported
        try {
            builder.toPrinter();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
        
        // Test that toParser throws exception when parsing is not supported
        try {
            builder.toParser();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testCanBuildMethods() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Initially should not be able to build anything
        assertFalse(builder.canBuildFormatter());
        assertFalse(builder.canBuildPrinter());
        assertFalse(builder.canBuildParser());
        
        // After adding a literal, should be able to build both
        builder.appendLiteral('a');
        assertTrue(builder.canBuildFormatter());
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Clear and test with only a printer
        builder.clear();
        builder.append(new CharacterLiteral('a'));
        assertTrue(builder.canBuildPrinter());
        assertFalse(builder.canBuildParser());
        assertTrue(builder.canBuildFormatter());
        
        // Clear and test with only a parser
        builder.clear();
        builder.append((DateTimePrinter) null, new CharacterLiteral('a'));
        assertFalse(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        assertTrue(builder.canBuildFormatter());
    }
}