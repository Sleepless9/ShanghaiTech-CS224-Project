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

public class MutableDateTimeTest {

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
        DateTime dateTime = new DateTime(2023, 5, 15, 9, 7);
        String formatted = formatter.print(dateTime);
        assertEquals("9:07", formatted);

        DateTime parsed = formatter.parseDateTime("09:07");
        assertEquals(9, parsed.getHourOfDay());
        assertEquals(7, parsed.getMinuteOfHour());
    }

    @Test
    public void testAppendTextAndShortText() {
        builder.appendMonthOfYearText()
               .appendLiteral(' ')
               .appendDayOfMonth(2)
               .appendLiteral(", ")
               .appendYear(4, 4);

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 5, 15, 0, 0);
        String formatted = formatter.print(dateTime);
        assertTrue(formatted.startsWith("May 15, 2023"));

        builder.clear();
        builder.appendMonthOfYearShortText()
               .appendLiteral(' ')
               .appendDayOfMonth(2);
        formatter = builder.toFormatter();
        formatted = formatter.print(dateTime);
        assertEquals("May 15", formatted);
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
               .appendLiteral(' ')
               .appendTimeZoneOffset("Z", true, 2, 2);

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 5, 15, 14, 30, 0, 0);
        String formatted = formatter.print(dateTime);
        assertTrue(formatted.contains("14:30"));
        assertTrue(formatted.contains("+") || formatted.contains("Z"));

        DateTime parsed = formatter.parseDateTime("14:30 +02:00");
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