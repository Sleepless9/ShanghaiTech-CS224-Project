package org.joda.time.format;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatterBuilder.CharacterLiteral;
import org.joda.time.format.DateTimeFormatterBuilder.Composite;
import org.joda.time.format.DateTimeFormatterBuilder.FixedNumber;
import org.joda.time.format.DateTimeFormatterBuilder.PaddedNumber;
import org.joda.time.format.DateTimeFormatterBuilder.TextField;
import org.joda.time.format.DateTimeFormatterBuilder.UnpaddedNumber;
import org.joda.time.format.DateTimeFormatterBuilder.TwoDigitYear;
import org.joda.time.tz.DateTimeZoneBuilder;
import org.junit.Test;

public class DateTimeFormatterTest {

    @Test
    public void testConstructorAndClear() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        assertNotNull(builder);
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Test clear method
        builder.appendLiteral("test");
        builder.clear();
        assertEquals(0, getFieldPairsSize(builder));
        assertNull(getCachedFormatter(builder));
    }

    @Test
    public void testAppendMethods() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test append with null
        try {
            builder.append((DateTimeFormatter) null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test appendLiteral
        builder.appendLiteral('a');
        builder.appendLiteral("test");
        builder.appendLiteral("");
        
        // Test appendDecimal
        builder.appendDecimal(DateTimeFieldType.year(), 4, 4);
        builder.appendDecimal(DateTimeFieldType.monthOfYear(), 1, 2);
        
        // Test appendFixedDecimal
        builder.appendFixedDecimal(DateTimeFieldType.dayOfMonth(), 2);
        
        // Test text fields
        builder.appendText(DateTimeFieldType.monthOfYear());
        builder.appendShortText(DateTimeFieldType.dayOfWeek());
        
        // Verify the builder can create formatter
        assertTrue(builder.canBuildFormatter());
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
    }

    @Test
    public void testToFormatter() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Build a simple formatter for month and year
        builder.appendMonthOfYearText()
               .appendLiteral(' ')
               .appendYear(4, 4);
        
        DateTimeFormatter formatter = builder.toFormatter();
        assertNotNull(formatter);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        
        // Test that subsequent changes don't affect the formatter
        builder.appendLiteral("!");
        String formatted1 = formatter.print(new LocalDateTime());
        String formatted2 = builder.toFormatter().print(new LocalDateTime());
        assertFalse(formatted1.equals(formatted2) || 
                   (formatted1.length() == formatted2.length() - 1 && 
                    formatted2.startsWith(formatted1)));
    }

    @Test
    public void testAppendOptionalAndMatchingParser() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        DateTimeParser parser = mock(DateTimeParser.class);
        
        when(parser.estimateParsedLength()).thenReturn(3);
        when(parser.parseInto(any(), anyString(), anyInt())).thenReturn(3);
        
        // Test append with multiple parsers
        DateTimeParser[] parsers = {parser, null};
        builder.append(null, parsers);
        
        Object formatter = getPrivateField(builder, "iFormatter");
        assertTrue(formatter instanceof Composite);
        Composite composite = (Composite) formatter;
        assertNotNull(composite.iParsers);
        assertEquals(1, composite.iParsers.length);
        
        // Test appendOptional
        DateTimeFormatterBuilder builder2 = new DateTimeFormatterBuilder();
        builder2.appendOptional(parser);
        
        Object formatter2 = getPrivateField(builder2, "iFormatter");
        assertTrue(formatter2 instanceof Composite);
        Composite composite2 = (Composite) formatter2;
        assertNotNull(composite2.iParsers);
        assertEquals(1, composite2.iParsers.length);
    }

    @Test
    public void testCanBuildMethods() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Initially should be able to build both
        assertTrue(builder.canBuildFormatter());
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Add only a printer
        DateTimePrinter printer = mock(DateTimePrinter.class);
        when(printer.estimatePrintedLength()).thenReturn(5);
        
        builder.clear();
        builder.append(printer);
        
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildFormatter());
        assertFalse(builder.canBuildParser());
        
        // Add only a parser
        builder.clear();
        DateTimeParser parser = mock(DateTimeParser.class);
        when(parser.estimateParsedLength()).thenReturn(5);
        builder.append(parser);
        
        assertTrue(builder.canBuildParser());
        assertTrue(builder.canBuildFormatter());
        assertFalse(builder.canBuildPrinter());
    }

    // Helper methods to access private fields
    private int getFieldPairsSize(DateTimeFormatterBuilder builder) {
        return ((java.util.ArrayList<?>) getPrivateField(builder, "iElementPairs")).size();
    }
    
    private Object getCachedFormatter(DateTimeFormatterBuilder builder) {
        return getPrivateField(builder, "iFormatter");
    }
    
    private Object getPrivateField(Object instance, String fieldName) {
        try {
            java.lang.reflect.Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}