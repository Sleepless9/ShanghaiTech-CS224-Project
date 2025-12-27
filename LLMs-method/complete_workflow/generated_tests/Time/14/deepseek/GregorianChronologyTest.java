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

public class GregorianChronologyTest {

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
    public void testAppendDecimalAndBoundaryCases() {
        builder.appendDecimal(DateTimeFieldType.year(), 2, 4);

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(5, 1, 1, 0, 0); // Year 5
        String formatted = formatter.print(dateTime);
        assertEquals("Two-digit year should be printed", "0005", formatted);

        dateTime = new DateTime(2023, 1, 1, 0, 0);
        formatted = formatter.print(dateTime);
        assertEquals("Four-digit year should be printed", "2023", formatted);

        DateTime parsed = formatter.parseDateTime("99");
        assertEquals("Parsed two-digit year should be 99", 99, parsed.getYear());

        parsed = formatter.parseDateTime("2023");
        assertEquals("Parsed four-digit year should be 2023", 2023, parsed.getYear());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendDecimalWithNullFieldType() {
        builder.appendDecimal(null, 1, 2);
    }

    @Test
    public void testAppendTimeZoneOffset() {
        builder.appendHourOfDay(2)
               .appendLiteral(':')
               .appendMinuteOfHour(2)
               .appendLiteral(' ')
               .appendTimeZoneOffset("Z", true, 2, 2);

        DateTimeFormatter formatter = builder.toFormatter();
        assertNotNull("Formatter should not be null", formatter);

        DateTime dateTime = new DateTime(2023, 5, 15, 14, 30, 0, 0);
        String formatted = formatter.print(dateTime);
        assertTrue("Formatted string should contain timezone offset", formatted.contains("+") || formatted.contains("Z"));

        DateTime parsed = formatter.parseDateTime("14:30 +02:00");
        assertEquals("Parsed hour should match", 14, parsed.getHourOfDay());
        assertEquals("Parsed minute should match", 30, parsed.getMinuteOfHour());
    }

    @Test
    public void testAppendOptionalParser() {
        builder.appendLiteral("T")
               .appendOptional(
                   new DateTimeFormatterBuilder()
                       .appendHourOfDay(2)
                       .appendLiteral(':')
                       .appendMinuteOfHour(2)
                       .toParser()
               );

        DateTimeFormatter formatter = builder.toFormatter();
        assertTrue("Formatter should be a parser", formatter.isParser());

        DateTime parsedWithTime = formatter.parseDateTime("T14:30");
        assertEquals("Parsed hour should match", 14, parsedWithTime.getHourOfDay());
        assertEquals("Parsed minute should match", 30, parsedWithTime.getMinuteOfHour());

        DateTime parsedWithoutTime = formatter.parseDateTime("T");
        assertNotNull("Parsing without time should succeed", parsedWithoutTime);
    }

    @Test
    public void testClearAndReuse() {
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