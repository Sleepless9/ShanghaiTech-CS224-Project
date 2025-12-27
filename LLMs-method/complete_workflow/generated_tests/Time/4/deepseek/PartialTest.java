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

public class PartialTest {

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

        DateTime dateTime = new DateTime(2023, 10, 5, 0, 0);
        String formatted = formatter.print(dateTime);
        assertEquals("Date: 2023-10-05", formatted);

        DateTime parsed = formatter.parseDateTime("Date: 2023-10-05");
        assertEquals(2023, parsed.getYear());
        assertEquals(10, parsed.getMonthOfYear());
        assertEquals(5, parsed.getDayOfMonth());
    }

    @Test
    public void testAppendDecimalField() {
        builder.appendDecimal(DateTimeFieldType.year(), 2, 4);

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 1, 1, 0, 0);
        String formatted = formatter.print(dateTime);
        assertEquals("2023", formatted);

        DateTime parsed = formatter.parseDateTime("99");
        assertEquals(1999, parsed.getYear());
    }

    @Test
    public void testAppendTimeZoneOffset() {
        builder.appendHourOfDay(2)
               .appendLiteral(':')
               .appendMinuteOfHour(2)
               .appendLiteral(' ')
               .appendTimeZoneOffset("Z", true, 2, 2);

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 10, 5, 14, 30, DateTimeZone.forOffsetHours(2));
        String formatted = formatter.print(dateTime);
        assertTrue(formatted.contains("+02:00") || formatted.contains("Z"));

        DateTime parsed = formatter.parseDateTime("14:30 +03:00");
        assertEquals(14, parsed.getHourOfDay());
        assertEquals(30, parsed.getMinuteOfHour());
        assertEquals(DateTimeZone.forOffsetHours(3), parsed.getZone());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendLiteralNullThrowsException() {
        builder.appendLiteral(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendDecimalFieldTypeNullThrowsException() {
        builder.appendDecimal(null, 1, 2);
    }

    @Test
    public void testClearAndReuse() {
        builder.appendYear(4, 4);
        DateTimeFormatter formatter1 = builder.toFormatter();
        assertNotNull(formatter1);

        builder.clear();
        builder.appendMonthOfYear(2);
        DateTimeFormatter formatter2 = builder.toFormatter();
        assertNotNull(formatter2);

        DateTime dateTime = new DateTime(2023, 10, 5, 0, 0);
        String formatted1 = formatter1.print(dateTime);
        String formatted2 = formatter2.print(dateTime);
        assertEquals("2023", formatted1);
        assertEquals("10", formatted2);
    }

    @Test
    public void testAppendOptionalParser() {
        builder.appendYear(4, 4)
               .appendOptional(builder.getFormatter().getParser());

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 1, 1, 0, 0);
        String formatted = formatter.print(dateTime);
        assertEquals("2023", formatted);

        DateTime parsed = formatter.parseDateTime("2023");
        assertEquals(2023, parsed.getYear());
    }

    @Test
    public void testAppendTimeZoneNameWithLookup() {
        Map<String, DateTimeZone> lookup = new HashMap<>();
        lookup.put("Europe/Paris", DateTimeZone.forID("Europe/Paris"));
        lookup.put("America/New_York", DateTimeZone.forID("America/New_York"));

        builder.appendHourOfDay(2)
               .appendLiteral(':')
               .appendMinuteOfHour(2)
               .appendLiteral(' ')
               .appendTimeZoneName(lookup);

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 10, 5, 14, 30, DateTimeZone.forID("Europe/Paris"));
        String formatted = formatter.print(dateTime);
        assertTrue(formatted.contains("14:30"));

        DateTime parsed = formatter.parseDateTime("14:30 Europe/Paris");
        assertEquals(DateTimeZone.forID("Europe/Paris"), parsed.getZone());
    }
}