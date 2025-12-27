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
import org.joda.time.format.DateTimeFormatterBuilder.FixedNumber;
import org.joda.time.format.DateTimeFormatterBuilder.StringLiteral;
import org.joda.time.format.DateTimeFormatterBuilder.TextField;
import org.joda.time.format.DateTimeFormatterBuilder.TwoDigitYear;
import org.joda.time.format.DateTimeFormatterBuilder.UnpaddedNumber;
import org.joda.time.format.DateTimeFormatterBuilder.PaddedNumber;
import org.joda.time.format.DateTimeFormatterBuilder.Fraction;
import org.joda.time.format.DateTimeFormatterBuilder.TimeZoneOffset;
import org.joda.time.format.DateTimeFormatterBuilder.TimeZoneName;
import org.joda.time.format.DateTimeFormatterBuilder.TimeZoneId;
import org.joda.time.format.DateTimeFormatterBuilder.MatchingParser;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.SkipUndoDateTimeField;
import org.joda.time.format.DateTimeFormat.FieldFormatter;
import org.joda.time.tz.CachedDateTimeZone;
import org.junit.Test;

public class DateTimeFormatterTest {

    @Test
    public void testConstructorAndClear() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        assertNotNull(builder);
        assertTrue(builder.canBuildFormatter());
        
        // Test clear method
        builder.appendLiteral("test");
        builder.clear();
        assertEquals(0, builder.canBuildPrinter());
        assertEquals(0, builder.canBuildParser());
    }
    
    @Test
    public void testAppendLiteral() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test character literal
        builder.appendLiteral('A');
        Object formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        Composite composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        assertEquals(1, composite.iParsers.length);
        assertTrue(composite.iPrinters[0] instanceof CharacterLiteral);
        assertTrue(composite.iParsers[0] instanceof CharacterLiteral);
        
        // Test string literal
        builder.clear();
        builder.appendLiteral("ABC");
        formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        assertEquals(1, composite.iParsers.length);
        assertTrue(composite.iPrinters[0] instanceof StringLiteral);
        assertTrue(composite.iParsers[0] instanceof StringLiteral);
        
        // Test empty string literal
        builder.clear();
        builder.appendLiteral("");
        formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        composite = (Composite) formatter;
        assertEquals(0, composite.iPrinters.length);
        assertEquals(0, composite.iParsers.length);
    }
    
    @Test
    public void testAppendDecimalAndFixedDecimal() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test unpadded decimal
        builder.appendDecimal(DateTimeFieldType.year(), 1, 4);
        Object formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        Composite composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        assertTrue(composite.iPrinters[0] instanceof UnpaddedNumber);
        
        // Test padded decimal
        builder.clear();
        builder.appendDecimal(DateTimeFieldType.year(), 2, 4);
        formatter = builder.getFormatter();
        composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        assertTrue(composite.iPrinters[0] instanceof PaddedNumber);
        
        // Test fixed decimal
        builder.clear();
        builder.appendFixedDecimal(DateTimeFieldType.year(), 4);
        formatter = builder.getFormatter();
        composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        assertTrue(composite.iPrinters[0] instanceof FixedNumber);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAppendDecimalNullFieldType() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendDecimal(null, 1, 4);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAppendFixedDecimalInvalidDigits() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendFixedDecimal(DateTimeFieldType.year(), 0);
    }
    
    @Test
    public void testAppendText() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test full text
        builder.appendText(DateTimeFieldType.monthOfYear());
        Object formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        Composite composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        assertTrue(composite.iPrinters[0] instanceof TextField);
        
        // Test short text
        builder.clear();
        builder.appendShortText(DateTimeFieldType.monthOfYear());
        formatter = builder.getFormatter();
        composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        assertTrue(composite.iPrinters[0] instanceof TextField);
    }
    
    @Test
    public void testAppendFraction() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendFraction(DateTimeFieldType.secondOfDay(), 1, 3);
        
        Object formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        Composite composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        assertTrue(composite.iPrinters[0] instanceof Fraction);
        
        // Test fraction shortcuts
        builder.clear();
        builder.appendFractionOfSecond(1, 3);
        formatter = builder.getFormatter();
        composite = (Composite) formatter;
        assertTrue(composite.iPrinters[0] instanceof Fraction);
    }
    
    @Test
    public void testAppendTwoDigitYear() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendTwoDigitYear(2000);
        
        Object formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        Composite composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        assertTrue(composite.iPrinters[0] instanceof TwoDigitYear);
        
        builder.clear();
        builder.appendTwoDigitYear(2000, true);
        formatter = builder.getFormatter();
        composite = (Composite) formatter;
        assertTrue(composite.iPrinters[0] instanceof TwoDigitYear);
    }
    
    @Test
    public void testAppendTimeZoneMethods() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test time zone offset
        builder.appendTimeZoneOffset("Z", true, 1, 4);
        Object formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        Composite composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        assertTrue(composite.iPrinters[0] instanceof TimeZoneOffset);
        
        // Test time zone name with lookup
        Map<String, DateTimeZone> lookup = new HashMap<>();
        lookup.put("UTC", DateTimeZone.UTC);
        builder.clear();
        builder.appendTimeZoneName(lookup);
        formatter = builder.getFormatter();
        composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        assertTrue(composite.iPrinters[0] instanceof TimeZoneName);
        
        // Test time zone id
        builder.clear();
        builder.appendTimeZoneId();
        formatter = builder.getFormatter();
        composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        assertTrue(composite.iPrinters[0] instanceof TimeZoneId);
    }
    
    @Test
    public void testCanBuildMethods() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Initially can build both
        assertTrue(builder.canBuildFormatter());
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // After appending only a parser, can still build formatter and parser but not printer
        builder.clear();
        builder.append(new MockDateTimeParser());
        assertFalse(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        assertTrue(builder.canBuildFormatter());
        
        // After appending only a printer, can still build formatter and printer but not parser
        builder.clear();
        builder.append(new MockDateTimePrinter());
        assertTrue(builder.canBuildPrinter());
        assertFalse(builder.canBuildParser());
        assertTrue(builder.canBuildFormatter());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testToPrinterWithoutPrinter() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.append(new MockDateTimeParser());
        builder.toPrinter();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testToParserWithoutParser() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.append(new MockDateTimePrinter());
        builder.toParser();
    }
    
    @Test
    public void testAppendOptional() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendOptional(new MockDateTimeParser());
        
        Object formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        Composite composite = (Composite) formatter;
        assertEquals(1, composite.iParsers.length);
        assertTrue(composite.iParsers[0] instanceof MatchingParser);
    }
    
    @Test
    public void testAppendWithArray() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        DateTimeParser[] parsers = {new MockDateTimeParser(), null};
        builder.append(new MockDateTimePrinter(), parsers);
        
        Object formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        Composite composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        assertEquals(1, composite.iParsers.length);
        assertTrue(composite.iParsers[0] instanceof MatchingParser);
    }
    
    // Mock classes for testing
    private static class MockDateTimePrinter implements DateTimePrinter {
        @Override
        public int estimatePrintedLength() {
            return 10;
        }
        
        @Override
        public void printTo(StringBuffer buf, long instant, Chronology chrono,
                int displayOffset, DateTimeZone displayZone, Locale locale) {
            buf.append("test");
        }
        
        @Override
        public void printTo(Writer out, long instant, Chronology chrono,
                int displayOffset, DateTimeZone displayZone, Locale locale) throws IOException {
            out.write("test");
        }
        
        @Override
        public void printTo(StringBuffer buf, ReadablePartial partial, Locale locale) {
            buf.append("test");
        }
        
        @Override
        public void printTo(Writer out, ReadablePartial partial, Locale locale) throws IOException {
            out.write("test");
        }
    }
    
    private static class MockDateTimeParser implements DateTimeParser {
        @Override
        public int estimateParsedLength() {
            return 10;
        }
        
        @Override
        public int parseInto(DateTimeParserBucket bucket, String text, int position) {
            return position + 1;
        }
    }
}