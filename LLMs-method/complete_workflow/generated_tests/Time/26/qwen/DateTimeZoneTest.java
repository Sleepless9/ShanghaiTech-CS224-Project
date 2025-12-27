package org.joda.time.format;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.SkipUndoDateTimeField;
import org.joda.time.format.DateTimeFormatterBuilder.CharacterLiteral;
import org.joda.time.format.DateTimeFormatterBuilder.Composite;
import org.joda.time.format.DateTimeFormatterBuilder.FixedNumber;
import org.joda.time.format.DateTimeFormatterBuilder.PaddedNumber;
import org.joda.time.format.DateTimeFormatterBuilder.TextField;
import org.joda.time.format.DateTimeFormatterBuilder.TwoDigitYear;
import org.joda.time.format.DateTimeFormatterBuilder.UnpaddedNumber;
import org.joda.time.format.FormatUtils;
import org.junit.Test;

public class DateTimeZoneTest {

    @Test
    public void testAppendLiteral_char() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        DateTimeFormatterBuilder result = builder.appendLiteral('X');
        
        assertNotNull(result);
        assertEquals(builder, result);
        
        Object formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        Composite composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        assertEquals(1, composite.iParsers.length);
        
        CharacterLiteral literal = (CharacterLiteral) composite.iPrinters[0];
        assertEquals('X', literal.iValue);
    }

    @Test
    public void testAppendLiteral_string() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        DateTimeFormatterBuilder result = builder.appendLiteral("Hello");
        
        assertNotNull(result);
        assertEquals(builder, result);
        
        Object formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        Composite composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        assertEquals(1, composite.iParsers.length);
        
        DateTimeFormatterBuilder.StringLiteral literal = (DateTimeFormatterBuilder.StringLiteral) composite.iPrinters[0];
        assertEquals("Hello", literal.iValue);
        
        // Test empty string
        builder.clear();
        builder.appendLiteral("");
        Object emptyFormatter = builder.getFormatter();
        assertTrue(emptyFormatter instanceof Composite);
        Composite emptyComposite = (Composite) emptyFormatter;
        assertEquals(0, emptyComposite.iPrinters.length);
        assertEquals(0, emptyComposite.iParsers.length);
    }

    @Test
    public void testAppendDecimal() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        DateTimeFormatterBuilder result = builder.appendDecimal(DateTimeFieldType.year(), 4, 4);
        
        assertNotNull(result);
        assertEquals(builder, result);
        
        Object formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        Composite composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        assertEquals(1, composite.iParsers.length);
        
        PaddedNumber number = (PaddedNumber) composite.iPrinters[0];
        assertEquals(DateTimeFieldType.year(), number.iFieldType);
        assertEquals(4, number.iMinPrintedDigits);
        assertEquals(4, number.iMaxParsedDigits);
        
        // Test unpadded case
        builder.clear();
        builder.appendDecimal(DateTimeFieldType.dayOfMonth(), 1, 2);
        Object formatter2 = builder.getFormatter();
        Composite composite2 = (Composite) formatter2;
        UnpaddedNumber unpadded = (UnpaddedNumber) composite2.iPrinters[0];
        assertEquals(DateTimeFieldType.dayOfMonth(), unpadded.iFieldType);
        assertEquals(2, unpadded.iMaxParsedDigits);
    }

    @Test
    public void testAppendFixedDecimal() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        DateTimeFormatterBuilder result = builder.appendFixedDecimal(DateTimeFieldType.hourOfDay(), 2);
        
        assertNotNull(result);
        assertEquals(builder, result);
        
        Object formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        Composite composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        assertEquals(1, composite.iParsers.length);
        
        FixedNumber fixed = (FixedNumber) composite.iPrinters[0];
        assertEquals(DateTimeFieldType.hourOfDay(), fixed.iFieldType);
        assertEquals(2, fixed.iMaxParsedDigits);
        assertEquals(2, fixed.iMinPrintedDigits);
        
        // Test exception cases
        try {
            builder.appendFixedDecimal(null, 2);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        try {
            builder.appendFixedDecimal(DateTimeFieldType.minuteOfHour(), 0);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAppendText() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        DateTimeFormatterBuilder result = builder.appendText(DateTimeFieldType.monthOfYear());
        
        assertNotNull(result);
        assertEquals(builder, result);
        
        Object formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        Composite composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        assertEquals(1, composite.iParsers.length);
        
        TextField text = (TextField) composite.iPrinters[0];
        assertEquals(DateTimeFieldType.monthOfYear(), text.iFieldType);
        assertFalse(text.iShort);
        
        // Test short text
        builder.clear();
        builder.appendShortText(DateTimeFieldType.dayOfWeek());
        Object formatter2 = builder.getFormatter();
        Composite composite2 = (Composite) formatter2;
        TextField shortText = (TextField) composite2.iPrinters[0];
        assertEquals(DateTimeFieldType.dayOfWeek(), shortText.iFieldType);
        assertTrue(shortText.iShort);
    }

    @Test
    public void testToFormatter_canBuild() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Initially cannot build anything
        assertFalse(builder.canBuildFormatter());
        assertFalse(builder.canBuildPrinter());
        assertFalse(builder.canBuildParser());
        
        // Add a printer - can now build formatter and printer
        builder.appendLiteral('X');
        assertTrue(builder.canBuildFormatter());
        assertTrue(builder.canBuildPrinter());
        assertFalse(builder.canBuildParser());
        
        DateTimeFormatter formatter = builder.toFormatter();
        assertNotNull(formatter);
        assertTrue(formatter.isPrinter());
        assertFalse(formatter.isParser());
        
        DateTimePrinter printer = builder.toPrinter();
        assertNotNull(printer);
        
        // Clear and add only a parser
        builder.clear();
        builder.append(new MockDateTimeParser());
        assertTrue(builder.canBuildFormatter());
        assertFalse(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        DateTimeParser parser = builder.toParser();
        assertNotNull(parser);
    }

    @Test
    public void testAppendTwoDigitYear() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        DateTimeFormatterBuilder result = builder.appendTwoDigitYear(2000);
        
        assertNotNull(result);
        assertEquals(builder, result);
        
        Object formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        Composite composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        assertEquals(1, composite.iParsers.length);
        
        TwoDigitYear twoDigit = (TwoDigitYear) composite.iPrinters[0];
        assertEquals(DateTimeFieldType.year(), twoDigit.iType);
        assertEquals(2000, twoDigit.iPivot);
        assertFalse(twoDigit.iLenientParse);
        
        // Test lenient parse version
        builder.clear();
        builder.appendTwoDigitYear(1975, true);
        Object formatter2 = builder.getFormatter();
        Composite composite2 = (Composite) formatter2;
        TwoDigitYear twoDigitLenient = (TwoDigitYear) composite2.iPrinters[0];
        assertEquals(1975, twoDigitLenient.iPivot);
        assertTrue(twoDigitLenient.iLenientParse);
    }

    @Test
    public void testClear() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendLiteral('X');
        builder.appendDecimal(DateTimeFieldType.year(), 4, 4);
        
        assertNotNull(builder.getFormatter());
        
        builder.clear();
        
        assertNull(builder.iFormatter);
        assertEquals(0, builder.iElementPairs.size());
        
        // After clear, should not be able to build anything
        assertFalse(builder.canBuildFormatter());
        assertFalse(builder.canBuildPrinter());
        assertFalse(builder.canBuildParser());
    }

    @Test
    public void testAppend_optional() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        DateTimeParser mockParser = mock(DateTimeParser.class);
        when(mockParser.estimateParsedLength()).thenReturn(3);
        
        DateTimeFormatterBuilder result = builder.appendOptional(mockParser);
        
        assertNotNull(result);
        assertEquals(builder, result);
        
        Object formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        Composite composite = (Composite) formatter;
        assertEquals(0, composite.iPrinters.length);
        assertEquals(1, composite.iParsers.length);
        
        DateTimeFormatterBuilder.MatchingParser matching = (DateTimeFormatterBuilder.MatchingParser) composite.iParsers[0];
        assertEquals(2, matching.iParsers.length);
        assertEquals(mockParser, matching.iParsers[0]);
        assertNull(matching.iParsers[1]);
    }
    
    // Mock class for testing
    private static class MockDateTimeParser implements DateTimeParser {
        @Override
        public int estimateParsedLength() {
            return 5;
        }
        
        @Override
        public int parseInto(DateTimeParserBucket bucket, String text, int position) {
            return position + 1;
        }
    }
}