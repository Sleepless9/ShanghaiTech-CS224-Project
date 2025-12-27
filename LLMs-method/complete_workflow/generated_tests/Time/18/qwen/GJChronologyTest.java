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
import org.joda.time.field.SkipUndoDateTimeField;
import org.joda.time.format.DateTimeFormatterBuilder.CharacterLiteral;
import org.joda.time.format.DateTimeFormatterBuilder.Composite;
import org.joda.time.format.DateTimeFormatterBuilder.FixedNumber;
import org.joda.time.format.DateTimeFormatterBuilder.PaddedNumber;
import org.joda.time.format.DateTimeFormatterBuilder.StringLiteral;
import org.joda.time.format.DateTimeFormatterBuilder.TextField;
import org.joda.time.format.DateTimeFormatterBuilder.TwoDigitYear;
import org.joda.time.format.DateTimeFormatterBuilder.UnpaddedNumber;
import org.joda.time.tz.CachedDateTimeZone;
import org.joda.time.tz.FixedDateTimeZone;
import org.junit.Test;

public class GJChronologyTest {

    @Test
    public void testConstructorAndClear() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        assertNotNull(builder);
        assertTrue(builder.canBuildPrinter());
        assertFalse(builder.canBuildParser());
        
        builder.appendLiteral("test");
        builder.clear();
        assertEquals(0, builder.canBuildPrinter());
        assertEquals(0, builder.canBuildParser());
        assertEquals(0, builder.iElementPairs.size());
    }

    @Test
    public void testAppendLiteral() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test character literal
        builder.appendLiteral('A');
        assertEquals(1, builder.iElementPairs.size());
        assertTrue(builder.iElementPairs.get(0) instanceof CharacterLiteral);
        
        // Test string literal
        builder.clear();
        builder.appendLiteral("Hello");
        assertEquals(1, builder.iElementPairs.size());
        assertTrue(builder.iElementPairs.get(0) instanceof StringLiteral);
        
        // Test empty string
        builder.clear();
        builder.appendLiteral("");
        assertEquals(0, builder.iElementPairs.size());
    }

    @Test
    public void testAppendDecimal() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test normal decimal field
        builder.appendDecimal(DateTimeFieldType.hourOfDay(), 2, 4);
        assertEquals(1, builder.iElementPairs.size());
        assertTrue(builder.iElementPairs.get(0) instanceof PaddedNumber);
        
        // Test unpadded number (minDigits <= 1)
        builder.clear();
        builder.appendDecimal(DateTimeFieldType.minuteOfHour(), 1, 3);
        assertEquals(1, builder.iElementPairs.size());
        assertTrue(builder.iElementPairs.get(0) instanceof UnpaddedNumber);
        
        // Verify can build printer and parser
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
    }

    @Test
    public void testAppendFixedDecimal() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test fixed decimal field
        builder.appendFixedDecimal(DateTimeFieldType.dayOfMonth(), 2);
        assertEquals(1, builder.iElementPairs.size());
        assertTrue(builder.iElementPairs.get(0) instanceof FixedNumber);
        
        // Verify can build formatter
        assertTrue(builder.canBuildFormatter());
        
        // Test exception for invalid digits
        try {
            builder.appendFixedDecimal(DateTimeFieldType.year(), 0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAppendText() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test text field
        builder.appendText(DateTimeFieldType.monthOfYear());
        assertEquals(1, builder.iElementPairs.size());
        assertTrue(builder.iElementPairs.get(0) instanceof TextField);
        
        // Test short text field
        builder.clear();
        builder.appendShortText(DateTimeFieldType.dayOfWeek());
        assertEquals(1, builder.iElementPairs.size());
        assertTrue(builder.iElementPairs.get(0) instanceof TextField);
        
        // Verify can build both printer and parser
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
    }

    @Test
    public void testToFormatter() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Build a simple formatter with month and year
        builder.appendMonthOfYear(2)
               .appendLiteral('-')
               .appendYear(4, 4);
        
        // Create formatter
        DateTimeFormatter formatter = builder.toFormatter();
        assertNotNull(formatter);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        
        // Test that subsequent changes don't affect the built formatter
        builder.appendLiteral("extra");
        DateTimeFormatter formatter2 = builder.toFormatter();
        assertNotSame(formatter, formatter2);
    }

    @Test
    public void testAppendTwoDigitYear() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test two digit year with pivot
        builder.appendTwoDigitYear(2000);
        assertEquals(1, builder.iElementPairs.size());
        assertTrue(builder.iElementPairs.get(0) instanceof TwoDigitYear);
        
        // Verify can build formatter
        assertTrue(builder.canBuildFormatter());
        
        // Test with lenient parsing
        builder.clear();
        builder.appendTwoDigitYear(2000, true);
        assertEquals(1, builder.iElementPairs.size());
        assertTrue(builder.iElementPairs.get(0) instanceof TwoDigitYear);
    }

    @Test
    public void testCompositeBuilder() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Build composite formatter with multiple elements
        builder.appendLiteral("Start")
               .appendHourOfDay(2)
               .appendLiteral(":")
               .appendMinuteOfHour(2)
               .appendLiteral("End");
        
        // Verify element count (each element has printer and parser)
        assertEquals(8, builder.iElementPairs.size());
        
        // Test can build methods
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        assertTrue(builder.canBuildFormatter());
    }
}