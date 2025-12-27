package org.joda.time.format;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.SkipUndoDateTimeField;
import org.joda.time.format.DateTimeFormatterBuilder.Composite;
import org.joda.time.format.DateTimeFormatterBuilder.Fraction;
import org.joda.time.format.DateTimeFormatterBuilder.TimeZoneOffset;
import org.joda.time.format.DateTimeFormatterBuilder.TwoDigitYear;
import org.joda.time.format.DateTimeFormatterBuilder.UnpaddedNumber;
import org.joda.time.tz.FixedDateTimeZone;
import org.junit.Test;

public class DateTimeZoneTest {

    @Test
    public void testConstructorAndClear() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        assertNotNull(builder);
        
        // Test clear method
        builder.appendLiteral("test");
        assertEquals(2, builder.iElementPairs.size());
        
        builder.clear();
        assertEquals(0, builder.iElementPairs.size());
        assertNull(builder.iFormatter);
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
        assertEquals(2, builder.iElementPairs.size());
        
        // Test append with printer only
        builder.clear();
        DateTimePrinter mockPrinter = mock(DateTimePrinter.class);
        result = builder.append(mockPrinter);
        assertSame(builder, result);
        assertEquals(2, builder.iElementPairs.size());
        assertSame(mockPrinter, builder.iElementPairs.get(0));
        assertNull(builder.iElementPairs.get(1));
        
        // Test append with parser only
        builder.clear();
        DateTimeParser mockParser = mock(DateTimeParser.class);
        result = builder.append(mockParser);
        assertSame(builder, result);
        assertEquals(2, builder.iElementPairs.size());
        assertNull(builder.iElementPairs.get(0));
        assertSame(mockParser, builder.iElementPairs.get(1));
    }

    @Test
    public void testAppendLiteral() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test char literal
        DateTimeFormatterBuilder result = builder.appendLiteral('a');
        assertSame(builder, result);
        assertTrue(builder.iElementPairs.get(0) instanceof DateTimeFormatterBuilder.CharacterLiteral);
        
        builder.clear();
        
        // Test string literal
        result = builder.appendLiteral("test");
        assertSame(builder, result);
        assertTrue(builder.iElementPairs.get(0) instanceof DateTimeFormatterBuilder.StringLiteral);
        assertEquals("test", ((DateTimeFormatterBuilder.StringLiteral) builder.iElementPairs.get(0)).iValue);
        
        // Test empty string
        builder.clear();
        result = builder.appendLiteral("");
        assertSame(builder, result);
        assertEquals(0, builder.iElementPairs.size());
        
        // Test null string should throw exception
        try {
            builder.appendLiteral((String) null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testToFormatter() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test with no elements - should throw exception
        try {
            builder.toFormatter();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
        
        // Test with printer only
        builder.appendLiteral('a');
        assertTrue(builder.canBuildFormatter());
        assertTrue(builder.canBuildPrinter());
        assertFalse(builder.canBuildParser());
        
        DateTimeFormatter formatter = builder.toFormatter();
        assertNotNull(formatter);
        assertTrue(formatter.isPrinter());
        assertFalse(formatter.isParser());
        
        // Test with both printer and parser
        builder.clear();
        builder.append(new UnpaddedNumber(DateTimeFieldType.year(), 4, false), 
                      new UnpaddedNumber(DateTimeFieldType.year(), 4, false));
        assertTrue(builder.canBuildFormatter());
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        formatter = builder.toFormatter();
        assertNotNull(formatter);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
    }

    @Test
    public void testAppendDecimal() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test normal case
        DateTimeFormatterBuilder result = builder.appendDecimal(DateTimeFieldType.year(), 4, 4);
        assertSame(builder, result);
        assertTrue(builder.iElementPairs.get(0) instanceof DateTimeFormatterBuilder.PaddedNumber);
        
        // Test with minDigits <= 1 - should create UnpaddedNumber
        builder.clear();
        result = builder.appendDecimal(DateTimeFieldType.year(), 1, 4);
        assertTrue(builder.iElementPairs.get(0) instanceof DateTimeFormatterBuilder.UnpaddedNumber);
        
        // Test null field type
        try {
            builder.appendDecimal(null, 4, 4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test invalid digit counts
        try {
            builder.appendDecimal(DateTimeFieldType.year(), -1, 4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAppendFraction() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test normal case
        DateTimeFormatterBuilder result = builder.appendFraction(DateTimeFieldType.secondOfDay(), 3, 6);
        assertSame(builder, result);
        assertTrue(builder.iElementPairs.get(0) instanceof Fraction);
        assertEquals(3, ((Fraction) builder.iElementPairs.get(0)).iMinDigits);
        assertEquals(6, ((Fraction) builder.iElementPairs.get(0)).iMaxDigits);
        
        // Test null field type
        try {
            builder.appendFraction(null, 3, 6);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test maxDigits < minDigits - should adjust maxDigits
        builder.clear();
        result = builder.appendFraction(DateTimeFieldType.secondOfDay(), 6, 3);
        assertEquals(3, ((Fraction) builder.iElementPairs.get(0)).iMaxDigits);
    }

    @Test
    public void testAppendTwoDigitYear() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test normal case
        DateTimeFormatterBuilder result = builder.appendTwoDigitYear(2000);
        assertSame(builder, result);
        assertTrue(builder.iElementPairs.get(0) instanceof TwoDigitYear);
        assertEquals(2000, ((TwoDigitYear) builder.iElementPairs.get(0)).iPivot);
        assertFalse(((TwoDigitYear) builder.iElementPairs.get(0)).iLenientParse);
        
        // Test lenient parse version
        builder.clear();
        result = builder.appendTwoDigitYear(2000, true);
        assertTrue(((TwoDigitYear) builder.iElementPairs.get(0)).iLenientParse);
    }

    @Test
    public void testAppendTimeZoneOffset() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test normal case
        DateTimeFormatterBuilder result = builder.appendTimeZoneOffset("Z", true, 1, 4);
        assertSame(builder, result);
        assertTrue(builder.iElementPairs.get(0) instanceof TimeZoneOffset);
        assertEquals("Z", ((TimeZoneOffset) builder.iElementPairs.get(0)).iZeroOffsetPrintText);
        assertTrue(((TimeZoneOffset) builder.iElementPairs.get(0)).iShowSeparators);
        
        // Test overloaded version
        builder.clear();
        result = builder.appendTimeZoneOffset("Z", "UTC", true, 1, 4);
        assertEquals("Z", ((TimeZoneOffset) builder.iElementPairs.get(0)).iZeroOffsetPrintText);
        assertEquals("UTC", ((TimeZoneOffset) builder.iElementPairs.get(0)).iZeroOffsetParseText);
    }
}