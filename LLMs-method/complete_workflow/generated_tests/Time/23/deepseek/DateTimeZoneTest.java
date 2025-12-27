package org.joda.time.format;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DateTimePrinter;

public class DateTimeZoneTest {

    private DateTimeFormatterBuilder builder;

    @Before
    public void setUp() {
        builder = new DateTimeFormatterBuilder();
    }

    @Test
    public void testAppendLiteralAndToFormatter() {
        builder.appendLiteral("Date: ")
               .appendYear(4, 4)
               .appendLiteral('-')
               .appendMonthOfYear(2)
               .appendLiteral('-')
               .appendDayOfMonth(2);

        DateTimeFormatter formatter = builder.toFormatter();
        assertNotNull(formatter);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());

        DateTime dateTime = new DateTime(2023, 5, 15, 0, 0);
        String formatted = formatter.print(dateTime);
        assertEquals("Date: 2023-05-15", formatted);

        DateTime parsed = formatter.parseDateTime("Date: 2023-05-15");
        assertEquals(2023, parsed.getYear());
        assertEquals(5, parsed.getMonthOfYear());
        assertEquals(15, parsed.getDayOfMonth());
    }

    @Test
    public void testAppendDecimalField() {
        builder.appendDecimal(DateTimeFieldType.hourOfDay(), 1, 2)
               .appendLiteral(':')
               .appendDecimal(DateTimeFieldType.minuteOfHour(), 2, 2);

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 1, 1, 9, 5);
        String formatted = formatter.print(dateTime);
        assertEquals("9:05", formatted);

        DateTime parsed = formatter.parseDateTime("09:05");
        assertEquals(9, parsed.getHourOfDay());
        assertEquals(5, parsed.getMinuteOfHour());
    }

    @Test
    public void testAppendTextFields() {
        builder.appendMonthOfYearText()
               .appendLiteral(' ')
               .appendDayOfMonth(1)
               .appendLiteral(", ")
               .appendYear(4, 4);

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 12, 25, 0, 0);
        String formatted = formatter.print(dateTime);
        assertTrue(formatted.startsWith("December 25, 2023"));

        DateTime parsed = formatter.parseDateTime("December 25, 2023");
        assertEquals(2023, parsed.getYear());
        assertEquals(12, parsed.getMonthOfYear());
        assertEquals(25, parsed.getDayOfMonth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNullFormatterThrowsException() {
        builder.append((DateTimeFormatter) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNullFieldTypeThrowsException() {
        builder.appendDecimal(null, 1, 2);
    }

    @Test
    public void testClearAndReuseBuilder() {
        builder.appendYear(4, 4);
        DateTimeFormatter formatter1 = builder.toFormatter();
        assertNotNull(formatter1);

        builder.clear();
        builder.appendMonthOfYear(2);
        DateTimeFormatter formatter2 = builder.toFormatter();
        assertNotNull(formatter2);

        DateTime dateTime = new DateTime(2023, 5, 1, 0, 0);
        String formatted1 = formatter1.print(dateTime);
        String formatted2 = formatter2.print(dateTime);
        assertEquals("2023", formatted1);
        assertEquals("05", formatted2);
    }

    @Test
    public void testAppendOptionalParser() {
        builder.appendLiteral("T")
               .appendHourOfDay(2)
               .appendOptional(
                   new DateTimeFormatterBuilder()
                       .appendLiteral(':')
                       .appendMinuteOfHour(2)
                       .toParser()
               );

        DateTimeFormatter formatter = builder.toFormatter();
        
        DateTime parsedWithMinutes = formatter.parseDateTime("T14:30");
        assertEquals(14, parsedWithMinutes.getHourOfDay());
        assertEquals(30, parsedWithMinutes.getMinuteOfHour());
        
        DateTime parsedWithoutMinutes = formatter.parseDateTime("T14");
        assertEquals(14, parsedWithoutMinutes.getHourOfDay());
        assertEquals(0, parsedWithoutMinutes.getMinuteOfHour());
    }
}