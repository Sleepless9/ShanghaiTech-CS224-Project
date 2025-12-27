package org.joda.time.format;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DateTimePrinter;
import java.util.HashMap;
import java.util.Map;

public class LocalDateTimeTest {

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
        assertNotNull("Formatter should not be null", formatter);
        assertTrue("Formatter should be a printer", formatter.isPrinter());
        assertTrue("Formatter should be a parser", formatter.isParser());

        DateTime dateTime = new DateTime(2023, 5, 15, 0, 0);
        String formatted = formatter.print(dateTime);
        assertEquals("Formatted string should match", "Date: 2023-05-15", formatted);

        DateTime parsed = formatter.parseDateTime("Date: 2023-05-15");
        assertEquals("Parsed year should match", 2023, parsed.getYear());
        assertEquals("Parsed month should match", 5, parsed.getMonthOfYear());
        assertEquals("Parsed day should match", 15, parsed.getDayOfMonth());
    }

    @Test
    public void testAppendDecimalField() {
        builder.appendDecimal(DateTimeFieldType.year(), 4, 9);
        DateTimeFormatter formatter = builder.toFormatter();

        DateTime dateTime = new DateTime(2023, 1, 1, 0, 0);
        String formatted = formatter.print(dateTime);
        assertEquals("Year should be formatted with 4 digits", "2023", formatted);

        DateTime parsed = formatter.parseDateTime("2023");
        assertEquals("Parsed year should match", 2023, parsed.getYear());

        parsed = formatter.parseDateTime("0002023");
        assertEquals("Parsed year should handle leading zeros", 2023, parsed.getYear());
    }

    @Test
    public void testAppendTextAndShortText() {
        builder.appendMonthOfYearText()
               .appendLiteral(' ')
               .appendMonthOfYearShortText();

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 5, 1, 0, 0);
        String formatted = formatter.print(dateTime);
        assertTrue("Formatted string should contain month text", formatted.contains("May"));
        assertTrue("Formatted string should contain short month", formatted.contains("May"));

        DateTime parsed = formatter.parseDateTime("May May");
        assertEquals("Parsed month should be May", 5, parsed.getMonthOfYear());
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
    public void testAppendTimeZoneOffset() {
        builder.appendHourOfDay(2)
               .appendLiteral(':')
               .appendMinuteOfHour(2)
               .appendLiteral(' ')
               .appendTimeZoneOffset("Z", true, 2, 4);

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 5, 15, 14, 30, DateTimeZone.forOffsetHours(3));
        String formatted = formatter.print(dateTime);
        assertTrue("Formatted string should contain timezone offset", formatted.contains("+03:00"));

        DateTime parsed = formatter.parseDateTime("14:30 +03:00");
        assertEquals("Parsed hour should match", 14, parsed.getHourOfDay());
        assertEquals("Parsed minute should match", 30, parsed.getMinuteOfHour());
        assertEquals("Parsed zone offset should be +03:00", DateTimeZone.forOffsetHours(3), parsed.getZone());
    }

    @Test
    public void testAppendTimeZoneNameWithLookup() {
        Map<String, DateTimeZone> lookup = new HashMap<>();
        lookup.put("EST", DateTimeZone.forID("America/New_York"));
        lookup.put("PST", DateTimeZone.forID("America/Los_Angeles"));

        builder.appendLiteral("Zone: ")
               .appendTimeZoneName(lookup);

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 5, 15, 14, 30, DateTimeZone.forID("America/New_York"));
        String formatted = formatter.print(dateTime);
        assertTrue("Formatted string should contain timezone name", formatted.contains("Eastern"));

        DateTime parsed = formatter.parseDateTime("Zone: EST");
        assertEquals("Parsed zone should be America/New_York", DateTimeZone.forID("America/New_York"), parsed.getZone());
    }

    @Test
    public void testClearAndReuseBuilder() {
        builder.appendYear(4, 4);
        DateTimeFormatter formatter1 = builder.toFormatter();
        assertNotNull("First formatter should not be null", formatter1);

        builder.clear();
        builder.appendMonthOfYear(2);
        DateTimeFormatter formatter2 = builder.toFormatter();
        assertNotNull("Second formatter should not be null", formatter2);

        DateTime dateTime = new DateTime(2023, 5, 15, 0, 0);
        String formatted1 = formatter1.print(dateTime);
        String formatted2 = formatter2.print(dateTime);
        assertEquals("First formatter should print year", "2023", formatted1);
        assertEquals("Second formatter should print month", "05", formatted2);
    }
}