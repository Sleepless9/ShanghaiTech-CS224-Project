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
import org.joda.time.field.MillisDurationField;
import org.joda.time.field.PreciseDateTimeField;
import org.joda.time.format.DateTimeFormatterBuilder.Composite;
import org.joda.time.format.DateTimeFormatterBuilder.Fraction;
import org.joda.time.format.DateTimeFormatterBuilder.TimeZoneOffset;
import org.joda.time.format.DateTimeFormatterBuilder.TwoDigitYear;
import org.joda.time.format.DateTimeParserBucket;
import org.junit.Test;

public class PeriodTest {

    @Test
    public void testConstructorAndClear() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        assertNotNull(builder);
        
        // Test clear method
        builder.appendLiteral("test");
        assertEquals(true, builder.canBuildFormatter());
        
        builder.clear();
        assertEquals(false, builder.canBuildFormatter());
        assertEquals(0, builder.toString().length()); // toString not overridden, but should be safe
    }

    @Test
    public void testAppendMethods() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test appendLiteral with char
        builder.appendLiteral('A');
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test appendLiteral with string
        builder.appendLiteral("BC");
        assertTrue(builder.canBuildFormatter());
        
        // Test appendText
        builder.appendText(DateTimeFieldType.monthOfYear());
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test appendDecimal
        builder.appendDecimal(DateTimeFieldType.year(), 4, 4);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
    }

    @Test
    public void testToFormatterAndCapabilities() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test with only printer
        DateTimePrinter mockPrinter = mock(DateTimePrinter.class);
        when(mockPrinter.estimatePrintedLength()).thenReturn(5);
        builder.append(mockPrinter);
        
        assertTrue(builder.canBuildPrinter());
        assertFalse(builder.canBuildParser());
        assertTrue(builder.canBuildFormatter());
        
        // Verify toPrinter works
        DateTimePrinter resultPrinter = builder.toPrinter();
        assertNotNull(resultPrinter);
        
        // Test formatter creation
        DateTimeFormatter formatter = builder.toFormatter();
        assertNotNull(formatter);
        assertTrue(formatter.isPrinter());
        assertFalse(formatter.isParser());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNullFormatter() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.append((DateTimeFormatter) null);
    }

    @Test
    public void testAppendTwoDigitYear() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test appendTwoDigitYear with pivot
        builder.appendTwoDigitYear(2000);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test the TwoDigitYear implementation directly
        TwoDigitYear twoDigitYear = new TwoDigitYear(DateTimeFieldType.year(), 2000, false);
        assertEquals(2, twoDigitYear.estimatePrintedLength());
        assertEquals(2, twoDigitYear.estimateParsedLength());
        
        // Test fraction field
        Fraction fraction = new Fraction(DateTimeFieldType.secondOfDay(), 1, 3);
        assertEquals(3, fraction.estimateParsedLength());
        assertEquals(3, fraction.estimatePrintedLength());
    }

    @Test
    public void testTimeZoneOffset() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test timeZoneOffset with various parameters
        builder.appendTimeZoneOffset("Z", true, 1, 4);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test the TimeZoneOffset implementation
        TimeZoneOffset tzOffset = new TimeZoneOffset("Z", "Z", true, 1, 4);
        assertEquals(7, tzOffset.estimatePrintedLength()); // +hh:mm:ss.SSS
        
        // Test composite builder
        Composite composite = new Composite(java.util.Arrays.asList(new Object[]{tzOffset, tzOffset}));
        assertEquals(14, composite.estimatePrintedLength());
        assertEquals(14, composite.estimateParsedLength());
    }
}