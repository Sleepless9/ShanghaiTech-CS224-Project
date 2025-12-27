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
import java.util.HashMap;
import java.util.Map;

public class GJChronologyTest {

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
    public void testAppendDecimalFields() {
        builder.appendHourOfDay(2)
               .appendLiteral(':')
               .appendMinuteOfHour(2)
               .appendLiteral(':')
               .appendSecondOfMinute(2);

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 5, 15, 14, 30, 45);
        String formatted = formatter.print(dateTime);
        assertEquals("14:30:45", formatted);

        DateTime parsed = formatter.parseDateTime("09:05:07");
        assertEquals(9, parsed.getHourOfDay());
        assertEquals(5, parsed.getMinuteOfHour());
        assertEquals(7, parsed.getSecondOfMinute());
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

        DateTime parsed = formatter.parseDateTime("January 1, 2024");
        assertEquals(2024, parsed.getYear());
        assertEquals(1, parsed.getMonthOfYear());
        assertEquals(1, parsed.getDayOfMonth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNullFormatterThrowsException() {
        builder.append((DateTimeFormatter) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNullLiteralThrowsException() {
        builder.appendLiteral(null);
    }

    @Test
    public void testAppendTimeZoneOffset() {
        builder.appendHourOfDay(2)
               .appendLiteral(':')
               .appendMinuteOfHour(2)
               .appendTimeZoneOffset("Z", true, 2, 2);

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 5, 15, 14, 30, 0);
        String formatted = formatter.withZoneUTC().print(dateTime);
        assertTrue(formatted.contains("Z") || formatted.contains("+00:00"));

        DateTime parsed = formatter.parseDateTime("14:30+05:30");
        assertEquals(14, parsed.getHourOfDay());
        assertEquals(30, parsed.getMinuteOfHour());
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

        DateTime dateTime = new DateTime(2023, 5, 15, 0, 0);
        String formatted1 = formatter1.print(dateTime);
        String formatted2 = formatter2.print(dateTime);
        assertEquals("2023", formatted1);
        assertEquals("05", formatted2);
    }
}