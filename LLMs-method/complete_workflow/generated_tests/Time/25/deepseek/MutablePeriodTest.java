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

public class MutablePeriodTest {

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
        builder.appendDecimal(DateTimeFieldType.year(), 4, 4)
               .appendLiteral('-')
               .appendDecimal(DateTimeFieldType.monthOfYear(), 2, 2)
               .appendLiteral('-')
               .appendDecimal(DateTimeFieldType.dayOfMonth(), 2, 2);

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(1999, 12, 31, 0, 0);
        String formatted = formatter.print(dateTime);
        assertEquals("Formatted date should match", "1999-12-31", formatted);

        DateTime parsed = formatter.parseDateTime("2025-01-01");
        assertEquals("Parsed year should match", 2025, parsed.getYear());
        assertEquals("Parsed month should match", 1, parsed.getMonthOfYear());
        assertEquals("Parsed day should match", 1, parsed.getDayOfMonth());
    }

    @Test
    public void testAppendTextFields() {
        builder.appendMonthOfYearText()
               .appendLiteral(' ')
               .appendDayOfMonth(1)
               .appendLiteral(", ")
               .appendYear(4, 4);

        DateTimeFormatter formatter = builder.toFormatter();
        DateTime dateTime = new DateTime(2023, 8, 4, 0, 0);
        String formatted = formatter.print(dateTime);
        assertTrue("Formatted string should contain month text", formatted.startsWith("August"));
        assertTrue("Formatted string should contain year", formatted.endsWith("2023"));

        DateTime parsed = formatter.parseDateTime("March 15, 2020");
        assertEquals("Parsed month should match", 3, parsed.getMonthOfYear());
        assertEquals("Parsed day should match", 15, parsed.getDayOfMonth());
        assertEquals("Parsed year should match", 2020, parsed.getYear());
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
        assertNotNull("First formatter should not be null", formatter1);

        builder.clear();
        builder.appendMonthOfYear(2);
        DateTimeFormatter formatter2 = builder.toFormatter();
        assertNotNull("Second formatter should not be null", formatter2);

        DateTime dateTime = new DateTime(2023, 5, 1, 0, 0);
        String formatted1 = formatter1.print(dateTime);
        String formatted2 = formatter2.print(dateTime);
        assertEquals("First formatter should print year", "2023", formatted1);
        assertEquals("Second formatter should print month", "05", formatted2);
    }

    @Test
    public void testCanBuildMethods() {
        assertFalse("Empty builder should not build formatter", builder.canBuildFormatter());
        assertFalse("Empty builder should not build printer", builder.canBuildPrinter());
        assertFalse("Empty builder should not build parser", builder.canBuildParser());

        builder.appendYear(4, 4);
        assertTrue("Builder with field should build formatter", builder.canBuildFormatter());
        assertTrue("Builder with field should build printer", builder.canBuildPrinter());
        assertTrue("Builder with field should build parser", builder.canBuildParser());

        builder.clear();
        builder.append((DateTimePrinter) new DateTimeFormatterBuilder().appendYear(4, 4).toPrinter());
        assertTrue("Builder with only printer should build printer", builder.canBuildPrinter());
        assertFalse("Builder with only printer should not build parser", builder.canBuildParser());
    }
}