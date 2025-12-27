package org.joda.time.format;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Locale;

import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatterBuilder.CharacterLiteral;
import org.joda.time.format.DateTimeFormatterBuilder.Composite;
import org.joda.time.format.DateTimeFormatterBuilder.TextField;
import org.joda.time.format.DateTimeFormatterBuilder.TwoDigitYear;
import org.joda.time.format.DateTimeFormatterBuilder.UnpaddedNumber;
import org.joda.time.format.DateTimeParserBucket;
import org.junit.Test;

public class GJChronologyTest {

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
        assertNotNull(builder.iElementPairs);
        
        // Test appendLiteral with char and string
        builder.clear();
        result = builder.appendLiteral('A');
        assertSame(builder, result);
        assertEquals(2, builder.iElementPairs.size());
        
        builder.clear();
        result = builder.appendLiteral("Hello");
        assertSame(builder, result);
        assertEquals(2, builder.iElementPairs.size());
        
        // Test appendDecimal
        builder.clear();
        result = builder.appendDecimal(DateTimeFieldType.year(), 4, 4);
        assertSame(builder, result);
        assertTrue(builder.iElementPairs.get(0) instanceof UnpaddedNumber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendLiteralNull() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendLiteral((String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendDecimalNullFieldType() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendDecimal(null, 1, 2);
    }

    @Test
    public void testToFormatterAndCapabilities() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test canBuild methods before any appends
        assertFalse(builder.canBuildFormatter());
        assertFalse(builder.canBuildPrinter());
        assertFalse(builder.canBuildParser());
        
        // Add a printer element
        builder.appendLiteral('A');
        
        assertTrue(builder.canBuildFormatter());
        assertTrue(builder.canBuildPrinter());
        assertFalse(builder.canBuildParser());
        
        // Test toFormatter
        DateTimeFormatter formatter = builder.toFormatter();
        assertNotNull(formatter);
        assertTrue(formatter.isPrinter());
        assertFalse(formatter.isParser());
        
        // Test toPrinter
        DateTimePrinter printer = builder.toPrinter();
        assertNotNull(printer);
    }

    @Test
    public void testAppendTextAndTwoDigitYear() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test appendText
        DateTimeFormatterBuilder result = builder.appendText(DateTimeFieldType.monthOfYear());
        assertSame(builder, result);
        assertTrue(builder.iElementPairs.get(0) instanceof TextField);
        
        builder.clear();
        
        // Test appendTwoDigitYear
        result = builder.appendTwoDigitYear(2000);
        assertSame(builder, result);
        assertTrue(builder.iElementPairs.get(0) instanceof TwoDigitYear);
        
        // Test the TwoDigitYear functionality
        TwoDigitYear twoDigitYear = new TwoDigitYear(DateTimeFieldType.year(), 2000, false);
        assertEquals(2, twoDigitYear.estimatePrintedLength());
        assertEquals(2, twoDigitYear.estimateParsedLength());
    }
}