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
        builder.appendDecimal(DateTimeFieldType.year(), 1, 4)
               .appendLiteral('-')
               .appendDecimal(DateTimeFieldType.monthOfYear(), 2, 2);

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 5, 1, 0, 0);
        String formatted = formatter.print(dateTime);
        assertEquals("2023-05", formatted);

        DateTime parsed = formatter.parseDateTime("2023-05");
        assertEquals(2023, parsed.getYear());
        assertEquals(5, parsed.getMonthOfYear());
    }

    @Test
    public void testAppendOptionalParser() {
        builder.appendYear(4, 4)
               .appendOptional(
                   new DateTimeFormatterBuilder()
                       .appendLiteral('-')
                       .appendMonthOfYear(2)
                       .toParser()
               );

        DateTimeFormatter formatter = builder.toFormatter();
        
        DateTime parsedWithMonth = formatter.parseDateTime("2023-05");
        assertEquals(2023, parsedWithMonth.getYear());
        assertEquals(5, parsedWithMonth.getMonthOfYear());
        
        DateTime parsedWithoutMonth = formatter.parseDateTime("2023");
        assertEquals(2023, parsedWithoutMonth.getYear());
        assertEquals(1, parsedWithoutMonth.getMonthOfYear());
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
    public void testClearAndReuseBuilder() {
        builder.appendYear(4, 4);
        DateTimeFormatter formatter1 = builder.toFormatter();
        assertNotNull(formatter1);
        
        builder.clear();
        builder.appendMonthOfYear(2);
        DateTimeFormatter formatter2 = builder.toFormatter();
        assertNotNull(formatter2);
        
        DateTime dateTime = new DateTime(2023, 5, 1, 0, 0);
        assertEquals("05", formatter2.print(dateTime));
        assertNotEquals(formatter1, formatter2);
    }

    @Test
    public void testCanBuildMethods() {
        assertFalse(builder.canBuildFormatter());
        assertFalse(builder.canBuildPrinter());
        assertFalse(builder.canBuildParser());
        
        builder.appendYear(4, 4);
        assertTrue(builder.canBuildFormatter());
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        builder.clear();
        builder.append((DateTimePrinter) null, new DateTimeFormatterBuilder().appendYear(4, 4).toParser());
        assertFalse(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
    }
}