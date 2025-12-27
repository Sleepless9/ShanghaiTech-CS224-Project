package org.joda.time.format;

import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DateTimePrinter;

public class PeriodTest {

    @Test
    public void testAppendLiteralAndToFormatter() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendLiteral("Year: ")
               .appendYear(4, 4)
               .appendLiteral("-")
               .appendMonthOfYear(2);
        
        DateTimeFormatter formatter = builder.toFormatter();
        assertNotNull(formatter);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        
        DateTime dateTime = new DateTime(2023, 5, 15, 0, 0);
        String formatted = formatter.print(dateTime);
        assertEquals("Year: 2023-05", formatted);
        
        DateTime parsed = formatter.parseDateTime("Year: 2023-05");
        assertEquals(2023, parsed.getYear());
        assertEquals(5, parsed.getMonthOfYear());
    }

    @Test
    public void testAppendDecimalField() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendDayOfMonth(2)
               .appendLiteral("/")
               .appendMonthOfYear(2)
               .appendLiteral("/")
               .appendYear(4, 4);
        
        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 5, 15, 0, 0);
        String formatted = formatter.print(dateTime);
        assertEquals("15/05/2023", formatted);
        
        DateTime parsed = formatter.parseDateTime("15/05/2023");
        assertEquals(15, parsed.getDayOfMonth());
        assertEquals(5, parsed.getMonthOfYear());
        assertEquals(2023, parsed.getYear());
    }

    @Test
    public void testAppendOptionalParser() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendYear(4, 4)
               .appendOptional(
                   new DateTimeFormatterBuilder()
                       .appendLiteral("-")
                       .appendMonthOfYear(2)
                       .toParser()
               );
        
        DateTimeFormatter formatter = builder.toFormatter();
        
        // Parse with optional part
        DateTime parsedWithMonth = formatter.parseDateTime("2023-05");
        assertEquals(2023, parsedWithMonth.getYear());
        assertEquals(5, parsedWithMonth.getMonthOfYear());
        
        // Parse without optional part
        DateTime parsedWithoutMonth = formatter.parseDateTime("2023");
        assertEquals(2023, parsedWithoutMonth.getYear());
        assertEquals(1, parsedWithoutMonth.getMonthOfYear()); // Default month
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNullFormatterThrowsException() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.append((DateTimeFormatter) null);
    }

    @Test
    public void testClearAndReuseBuilder() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendYear(4, 4);
        
        assertTrue(builder.canBuildFormatter());
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        builder.clear();
        
        // After clear, should still be able to build (empty formatter)
        assertTrue(builder.canBuildFormatter());
        
        // Reuse builder
        builder.appendMonthOfYear(2);
        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 5, 15, 0, 0);
        String formatted = formatter.print(dateTime);
        assertEquals("05", formatted);
    }

    @Test
    public void testAppendTextFields() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendMonthOfYearText()
               .appendLiteral(" ")
               .appendDayOfMonth(1)
               .appendLiteral(", ")
               .appendYear(4, 4);
        
        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 5, 15, 0, 0);
        String formatted = formatter.print(dateTime);
        
        // Check basic structure
        assertTrue(formatted.contains("May"));
        assertTrue(formatted.contains("15"));
        assertTrue(formatted.contains("2023"));
        
        // Parse back
        DateTime parsed = formatter.parseDateTime("May 15, 2023");
        assertEquals(2023, parsed.getYear());
        assertEquals(5, parsed.getMonthOfYear());
        assertEquals(15, parsed.getDayOfMonth());
    }
}