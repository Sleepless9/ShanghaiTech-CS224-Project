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

public class DateTimeFormatterTest {

    private DateTimeFormatterBuilder builder;

    @Before
    public void setUp() {
        builder = new DateTimeFormatterBuilder();
    }

    @Test
    public void testAppendLiteralAndDecimal() {
        builder.appendLiteral("Date: ")
               .appendYear(4, 4)
               .appendLiteral('-')
               .appendMonthOfYear(2)
               .appendLiteral('-')
               .appendDayOfMonth(2);

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 5, 15, 0, 0);
        String formatted = formatter.print(dateTime);
        
        assertEquals("Date: 2023-05-15", formatted);
        
        DateTime parsed = formatter.parseDateTime("Date: 2023-05-15");
        assertEquals(2023, parsed.getYear());
        assertEquals(5, parsed.getMonthOfYear());
        assertEquals(15, parsed.getDayOfMonth());
    }

    @Test
    public void testAppendTextFields() {
        builder.appendDayOfWeekText()
               .appendLiteral(", ")
               .appendMonthOfYearText()
               .appendLiteral(' ')
               .appendDayOfMonth(1)
               .appendLiteral(", ")
               .appendYear(4, 4);

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 5, 15, 0, 0); // Monday, May 15, 2023
        
        String formatted = formatter.print(dateTime);
        assertTrue(formatted.startsWith("Monday, May 15, 2023"));
        
        DateTime parsed = formatter.parseDateTime("Monday, May 15, 2023");
        assertEquals(2023, parsed.getYear());
        assertEquals(5, parsed.getMonthOfYear());
        assertEquals(15, parsed.getDayOfMonth());
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
        
        DateTime parsed = formatter.parseDateTime("14:30 +02:00");
        assertEquals(14, parsed.getHourOfDay());
        assertEquals(30, parsed.getMinuteOfHour());
    }

    @Test
    public void testAppendOptionalParser() {
        builder.appendYear(4, 4)
               .appendOptional(builder.getFormatter().getParser());

        DateTimeFormatter formatter = builder.toFormatter();
        
        // Should parse with or without the optional part
        DateTime parsed1 = formatter.parseDateTime("2023");
        assertEquals(2023, parsed1.getYear());
        
        DateTime parsed2 = formatter.parseDateTime("20232023");
        assertEquals(2023, parsed2.getYear());
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
    public void testClearMethodResetsBuilder() {
        builder.appendYear(4, 4)
               .appendLiteral('-')
               .appendMonthOfYear(2);
        
        builder.clear();
        
        builder.appendDayOfMonth(2);
        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 5, 15, 0, 0);
        
        String formatted = formatter.print(dateTime);
        assertEquals("15", formatted);
    }

    @Test
    public void testAppendPattern() {
        builder.appendPattern("yyyy-MM-dd HH:mm:ss");
        
        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 5, 15, 14, 30, 45, 0);
        
        String formatted = formatter.print(dateTime);
        assertEquals("2023-05-15 14:30:45", formatted);
        
        DateTime parsed = formatter.parseDateTime("2023-05-15 14:30:45");
        assertEquals(2023, parsed.getYear());
        assertEquals(5, parsed.getMonthOfYear());
        assertEquals(15, parsed.getDayOfMonth());
        assertEquals(14, parsed.getHourOfDay());
        assertEquals(30, parsed.getMinuteOfHour());
        assertEquals(45, parsed.getSecondOfMinute());
    }

    @Test
    public void testCanBuildMethods() {
        assertTrue(builder.canBuildFormatter());
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        builder.appendYear(4, 4);
        
        assertTrue(builder.canBuildFormatter());
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
    }
}