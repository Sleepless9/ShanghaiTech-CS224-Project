package org.joda.time.format;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatterBuilder.CharacterLiteral;
import org.joda.time.format.DateTimeFormatterBuilder.Composite;
import org.joda.time.format.DateTimeFormatterBuilder.StringLiteral;
import org.joda.time.format.DateTimeFormatterBuilder.TextField;
import org.joda.time.format.DateTimeFormatterBuilder.TwoDigitYear;
import org.joda.time.format.DateTimeFormatterBuilder.UnpaddedNumber;
import org.joda.time.format.DateTimeFormatterBuilder.PaddedNumber;
import org.joda.time.format.DateTimeFormatterBuilder.FixedNumber;
import org.joda.time.format.DateTimeFormatterBuilder.Fraction;
import org.joda.time.format.DateTimeFormatterBuilder.TimeZoneOffset;
import org.joda.time.format.DateTimeFormatterBuilder.TimeZoneName;
import org.joda.time.format.DateTimeFormatterBuilder.TimeZoneId;
import org.joda.time.format.DateTimeFormatterBuilder.MatchingParser;
import org.joda.time.MockBucket;
import org.joda.time.TestDateTimeUtils;
import org.joda.time.field.SkipUndoDateTimeField;
import org.joda.time.tz.CachedDateTimeZone;
import org.joda.time.tz.ZoneInfoProvider;
import org.junit.Before;
import org.junit.Test;

public class DateTimeZoneTest {

    private DateTimeFormatterBuilder builder;
    private MockBucket bucket;

    @Before
    public void setUp() {
        builder = new DateTimeFormatterBuilder();
        bucket = new MockBucket(0L, null, null, null);
    }

    @Test
    public void testConstructorAndClear() {
        assertNotNull(builder);
        assertTrue(builder.canBuildFormatter());
        
        // Test clear method
        builder.appendLiteral("test");
        assertFalse(builder.iElementPairs.isEmpty());
        
        builder.clear();
        assertTrue(builder.iElementPairs.isEmpty());
        assertNull(builder.iFormatter);
    }

    @Test
    public void testAppendLiteral() {
        // Test char literal
        builder.appendLiteral('A');
        assertEquals(2, builder.iElementPairs.size());
        Object element = builder.iElementPairs.get(0);
        assertTrue(element instanceof CharacterLiteral);
        assertEquals('A', ((CharacterLiteral) element).iValue);
        
        // Test string literal
        builder.clear();
        builder.appendLiteral("ABC");
        assertEquals(2, builder.iElementPairs.size());
        element = builder.iElementPairs.get(0);
        assertTrue(element instanceof StringLiteral);
        assertEquals("ABC", ((StringLiteral) element).iValue);
        
        // Test empty string
        builder.clear();
        builder.appendLiteral("");
        assertEquals(0, builder.iElementPairs.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendLiteralNullString() {
        builder.appendLiteral((String) null);
    }

    @Test
    public void testAppendDecimalAndFixedDecimal() {
        // Test appendDecimal
        builder.appendDecimal(DateTimeFieldType.year(), 1, 4);
        assertEquals(2, builder.iElementPairs.size());
        Object printer = builder.iElementPairs.get(0);
        assertTrue(printer instanceof UnpaddedNumber);
        assertEquals(DateTimeFieldType.year(), ((UnpaddedNumber) printer).iFieldType);
        
        // Test appendFixedDecimal
        builder.clear();
        builder.appendFixedDecimal(DateTimeFieldType.monthOfYear(), 2);
        assertEquals(2, builder.iElementPairs.size());
        printer = builder.iElementPairs.get(0);
        assertTrue(printer instanceof FixedNumber);
        assertEquals(DateTimeFieldType.monthOfYear(), ((FixedNumber) printer).iFieldType);
        
        // Verify min/max digits handling
        builder.clear();
        builder.appendDecimal(DateTimeFieldType.dayOfMonth(), 3, 2); // max < min
        printer = builder.iElementPairs.get(0);
        assertEquals(2, ((PaddedNumber) printer).iMaxParsedDigits);
    }

    @Test
    public void testAppendTextFields() {
        // Test text field
        builder.appendText(DateTimeFieldType.monthOfYear());
        assertEquals(2, builder.iElementPairs.size());
        Object element = builder.iElementPairs.get(0);
        assertTrue(element instanceof TextField);
        assertFalse(((TextField) element).iShort);
        
        // Test short text field
        builder.clear();
        builder.appendShortText(DateTimeFieldType.dayOfWeek());
        element = builder.iElementPairs.get(0);
        assertTrue(element instanceof TextField);
        assertTrue(((TextField) element).iShort);
    }

    @Test
    public void testToFormatterCapabilities() {
        // Builder with only printer should not support parsing
        builder.appendLiteral("test");
        assertTrue(builder.canBuildPrinter());
        assertFalse(builder.canBuildParser());
        
        // Builder with only parser should not support printing
        builder.clear();
        builder.append(new MockDateTimeParser());
        assertFalse(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Builder with both should support both
        builder.clear();
        builder.appendLiteral("test").append(new MockDateTimeParser());
        assertTrue(builder.canBuildFormatter());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToParserWithoutParser() {
        builder.appendLiteral("test");
        builder.toParser();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToPrinterWithoutPrinter() {
        builder.append(new MockDateTimeParser());
        builder.toPrinter();
    }

    @Test
    public void testAppendTwoDigitYear() {
        builder.appendTwoDigitYear(2000);
        assertEquals(2, builder.iElementPairs.size());
        Object element = builder.iElementPairs.get(0);
        assertTrue(element instanceof TwoDigitYear);
        assertEquals(DateTimeFieldType.year(), ((TwoDigitYear) element).iType);
        assertEquals(2000, ((TwoDigitYear) element).iPivot);
        
        // Test lenient parse version
        builder.clear();
        builder.appendTwoDigitYear(1975, true);
        element = builder.iElementPairs.get(0);
        assertTrue(element instanceof TwoDigitYear);
        assertTrue(((TwoDigitYear) element).iLenientParse);
    }

    @Test
    public void testAppendFraction() {
        builder.appendFraction(DateTimeFieldType.secondOfDay(), 1, 3);
        assertEquals(2, builder.iElementPairs.size());
        Object element = builder.iElementPairs.get(0);
        assertTrue(element instanceof Fraction);
        assertEquals(1, ((Fraction) element).iMinDigits);
        assertEquals(3, ((Fraction) element).iMaxDigits);
    }

    @Test
    public void testAppendTimeZoneMethods() {
        // Test time zone offset
        builder.appendTimeZoneOffset("Z", true, 1, 4);
        assertEquals(2, builder.iElementPairs.size());
        Object element = builder.iElementPairs.get(0);
        assertTrue(element instanceof TimeZoneOffset);
        assertEquals("Z", ((TimeZoneOffset) element).iZeroOffsetPrintText);
        
        // Test time zone name with lookup
        Map<String, DateTimeZone> lookup = new HashMap<>();
        lookup.put("UTC", DateTimeZone.UTC);
        builder.clear();
        builder.appendTimeZoneName(lookup);
        element = builder.iElementPairs.get(0);
        assertTrue(element instanceof TimeZoneName);
        assertSame(lookup, ((TimeZoneName) element).iParseLookup);
        
        // Test time zone ID
        builder.clear();
        builder.appendTimeZoneId();
        element = builder.iElementPairs.get(0);
        assertSame(TimeZoneId.INSTANCE, element);
    }

    @Test
    public void testAppendOptional() {
        MockDateTimeParser mockParser = new MockDateTimeParser();
        builder.appendOptional(mockParser);
        assertEquals(2, builder.iElementPairs.size());
        Object element = builder.iElementPairs.get(1);
        assertTrue(element instanceof MatchingParser);
        MatchingParser matchingParser = (MatchingParser) element;
        assertEquals(2, matchingParser.iParsers.length);
        assertSame(mockParser, matchingParser.iParsers[0]);
        assertNull(matchingParser.iParsers[1]);
    }

    @Test
    public void testAppendWithMultipleParsers() {
        DateTimeParser[] parsers = {new MockDateTimeParser(), new MockDateTimeParser()};
        builder.append(null, parsers);
        assertEquals(2, builder.iElementPairs.size());
        Object parser = builder.iElementPairs.get(1);
        assertTrue(parser instanceof MatchingParser);
        assertEquals(2, ((MatchingParser) parser).iParsers.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNullFormatter() {
        builder.append((DateTimeFormatter) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNullPrinter() {
        builder.append((DateTimePrinter) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNullParser() {
        builder.append((DateTimeParser) null);
    }

    @Test
    public void testCanBuildMethods() {
        // Empty builder can build formatter (but it won't do much)
        assertTrue(builder.canBuildFormatter());
        
        // Add printer only
        builder.appendLiteral("test");
        assertTrue(builder.canBuildPrinter());
        assertFalse(builder.canBuildParser());
        
        // Clear and add parser only
        builder.clear();
        builder.append(new MockDateTimeParser());
        assertFalse(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
    }

    @Test
    public void testCompositeFormatter() {
        builder.appendLiteral("A").appendLiteral("B");
        DateTimeFormatter formatter = builder.toFormatter();
        assertNotNull(formatter);
        assertTrue(formatter.isPrinter());
        assertFalse(formatter.isParser()); // because second append was just a literal printer
        
        // Test that formatter is immutable after creation
        builder.appendLiteral("C");
        DateTimeFormatter formatter2 = builder.toFormatter();
        assertNotSame(formatter, formatter2);
    }
}