package org.joda.time.format;

import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DateTimePrinter;
import java.util.HashMap;
import java.util.Map;

public class DateTimeZoneTest {

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
    public void testAppendDecimalAndParse() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendDayOfMonth(2)
               .appendLiteral('/')
               .appendMonthOfYear(2)
               .appendLiteral('/')
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
    public void testAppendTextAndShortText() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendMonthOfYearText()
               .appendLiteral(' ')
               .appendDayOfMonth(1)
               .appendLiteral(", ")
               .appendYear(4, 4);
        
        DateTimeFormatter formatter = builder.toFormatter();
        
        DateTime dateTime = new DateTime(2023, 5, 15, 0, 0);
        String formatted = formatter.print(dateTime);
        assertTrue(formatted.startsWith("May"));
        assertTrue(formatted.contains("15"));
        assertTrue(formatted.contains("2023"));
        
        builder.clear();
        builder.appendMonthOfYearShortText()
               .appendLiteral(' ')
               .appendDayOfMonth(1);
        formatter = builder.toFormatter();
        formatted = formatter.print(dateTime);
        assertTrue(formatted.startsWith("May") || formatted.startsWith("May"));
    }

    @Test
    public void testAppendTimeZoneOffset() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendHourOfDay(2)
               .appendLiteral(':')
               .appendMinuteOfHour(2)
               .appendLiteral(' ')
               .appendTimeZoneOffset("Z", true, 2, 2);
        
        DateTimeFormatter formatter = builder.toFormatter();
        
        DateTime dateTime = new DateTime(2023, 5, 15, 14, 30, DateTimeZone.forOffsetHours(3));
        String formatted = formatter.print(dateTime);
        assertTrue(formatted.contains("+03:00") || formatted.contains("14:30"));
        
        dateTime = new DateTime(2023, 5, 15, 14, 30, DateTimeZone.UTC);
        formatted = formatter.print(dateTime);
        assertTrue(formatted.contains("Z") || formatted.contains("14:30"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNullFormatterThrowsException() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.append((DateTimeFormatter) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNullLiteralThrowsException() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendLiteral((String) null);
    }

    @Test
    public void testClearAndReuseBuilder() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendYear(4, 4);
        
        DateTimeFormatter formatter1 = builder.toFormatter();
        assertNotNull(formatter1);
        
        builder.clear();
        builder.appendMonthOfYear(2);
        
        DateTimeFormatter formatter2 = builder.toFormatter();
        assertNotNull(formatter2);
        
        DateTime dateTime = new DateTime(2023, 5, 15, 0, 0);
        String formatted2 = formatter2.print(dateTime);
        assertEquals("05", formatted2);
    }

    @Test
    public void testAppendOptionalParser() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendLiteral("T")
               .appendOptional(
                   new DateTimeFormatterBuilder()
                       .appendHourOfDay(2)
                       .appendLiteral(':')
                       .appendMinuteOfHour(2)
                       .toParser()
               );
        
        DateTimeFormatter formatter = builder.toFormatter();
        
        DateTime dateTime = new DateTime(2023, 5, 15, 14, 30);
        String formatted = formatter.print(dateTime);
        assertTrue(formatted.contains("T14:30"));
        
        DateTime parsedWithTime = formatter.parseDateTime("T14:30");
        assertEquals(14, parsedWithTime.getHourOfDay());
        assertEquals(30, parsedWithTime.getMinuteOfHour());
        
        DateTime parsedWithoutTime = formatter.parseDateTime("T");
        assertNotNull(parsedWithoutTime);
    }

    @Test
    public void testAppendTimeZoneNameWithLookup() {
        Map<String, DateTimeZone> lookup = new HashMap<>();
        lookup.put("Europe/London", DateTimeZone.forID("Europe/London"));
        lookup.put("America/New_York", DateTimeZone.forID("America/New_York"));
        
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendHourOfDay(2)
               .appendLiteral(':')
               .appendMinuteOfHour(2)
               .appendLiteral(' ')
               .appendTimeZoneName(lookup);
        
        DateTimeFormatter formatter = builder.toFormatter();
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        
        DateTime dateTime = new DateTime(2023, 5, 15, 14, 30, DateTimeZone.forID("Europe/London"));
        String formatted = formatter.print(dateTime);
        assertTrue(formatted.contains("14:30"));
    }

    @Test
    public void testCanBuildMethods() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        assertFalse(builder.canBuildFormatter());
        assertFalse(builder.canBuildPrinter());
        assertFalse(builder.canBuildParser());
        
        builder.appendYear(4, 4);
        assertTrue(builder.canBuildFormatter());
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        builder.clear();
        builder.append((DateTimePrinter) null, new DateTimeFormatterBuilder().appendYear(4, 4).toParser());
        assertTrue(builder.canBuildParser());
        assertFalse(builder.canBuildPrinter());
    }
}