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
        assertNotNull("Formatter should not be null", formatter);
        assertTrue("Formatter should be a printer", formatter.isPrinter());
        assertTrue("Formatter should be a parser", formatter.isParser());

        DateTime date = new DateTime(2023, 10, 5, 0, 0);
        String formatted = formatter.print(date);
        assertEquals("Formatted string should match", "Date: 2023-10-05", formatted);

        DateTime parsed = formatter.parseDateTime("Date: 2023-10-05");
        assertEquals("Parsed year should match", 2023, parsed.getYear());
        assertEquals("Parsed month should match", 10, parsed.getMonthOfYear());
        assertEquals("Parsed day should match", 5, parsed.getDayOfMonth());
    }

    @Test
    public void testAppendDecimalField() {
        builder.appendDecimal(DateTimeFieldType.year(), 4, 4)
               .appendLiteral('-')
               .appendDecimal(DateTimeFieldType.monthOfYear(), 2, 2);

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime date = new DateTime(2023, 5, 1, 0, 0);
        String formatted = formatter.print(date);
        assertEquals("Formatted year-month should match", "2023-05", formatted);

        DateTime parsed = formatter.parseDateTime("2023-05");
        assertEquals("Parsed year should be 2023", 2023, parsed.getYear());
        assertEquals("Parsed month should be 5", 5, parsed.getMonthOfYear());
    }

    @Test
    public void testAppendTextFields() {
        builder.appendMonthOfYearText()
               .appendLiteral(' ')
               .appendYear(4, 4);

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime date = new DateTime(2023, 10, 1, 0, 0);
        String formatted = formatter.print(date);
        assertTrue("Formatted string should contain month text", formatted.startsWith("October"));
        assertTrue("Formatted string should contain year", formatted.endsWith("2023"));

        DateTime parsed = formatter.parseDateTime("October 2023");
        assertEquals("Parsed year should be 2023", 2023, parsed.getYear());
        assertEquals("Parsed month should be 10", 10, parsed.getMonthOfYear());
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
        assertTrue("Should be able to build formatter", builder.canBuildFormatter());

        builder.clear();
        assertFalse("After clear, should not build formatter", builder.canBuildFormatter());

        builder.appendMonthOfYear(2);
        DateTimeFormatter formatter = builder.toFormatter();
        assertNotNull("New formatter should be created", formatter);
        DateTime date = new DateTime(2023, 5, 1, 0, 0);
        assertEquals("Formatted month should be 05", "05", formatter.print(date));
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
        DateTime date1 = formatter.parseDateTime("2023-05");
        assertEquals("Parsed year with month should be 2023", 2023, date1.getYear());
        assertEquals("Parsed month should be 5", 5, date1.getMonthOfYear());

        DateTime date2 = formatter.parseDateTime("2023");
        assertEquals("Parsed year without month should be 2023", 2023, date2.getYear());
        assertEquals("Parsed month should default to 1", 1, date2.getMonthOfYear());
    }
}