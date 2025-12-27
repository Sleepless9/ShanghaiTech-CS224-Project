package org.joda.time.format;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.SkipUndoDateTimeField;
import org.joda.time.format.DateTimeFormatterBuilder.CharacterLiteral;
import org.joda.time.format.DateTimeFormatterBuilder.Composite;
import org.joda.time.format.DateTimeFormatterBuilder.FixedNumber;
import org.joda.time.format.DateTimeFormatterBuilder.TextField;
import org.joda.time.format.DateTimeFormatterBuilder.TwoDigitYear;
import org.joda.time.format.DateTimeFormatterBuilder.UnpaddedNumber;
import org.joda.time.tz.CachedDateTimeZone;
import org.joda.time.tz.ZoneInfoProvider;
import org.junit.Test;

public class PartialTest {

    @Test
    public void testConstructorAndClear() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        assertNotNull(builder);
        
        // Test clear on empty builder
        builder.clear();
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // Add an element and clear again
        builder.appendLiteral('a');
        builder.clear();
        assertEquals(0, builder.canBuildPrinter());
        assertEquals(0, builder.canBuildParser());
    }

    @Test
    public void testAppendLiteral() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test character literal
        builder.appendLiteral('a');
        Object formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        Composite composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        assertEquals(1, composite.iParsers.length);
        
        CharacterLiteral literal = (CharacterLiteral) composite.iPrinters[0];
        assertEquals('a', literal.iValue);
        
        // Test string literal
        builder.clear();
        builder.appendLiteral("test");
        formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        
        DateTimeFormatterBuilder.StringLiteral stringLiteral = (DateTimeFormatterBuilder.StringLiteral) composite.iPrinters[0];
        assertEquals("test", stringLiteral.iValue);
        
        // Test null string literal throws exception
        try {
            builder.appendLiteral(null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAppendDecimalAndFixedDecimal() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test appendDecimal
        builder.appendDecimal(DateTimeFieldType.year(), 4, 4);
        Object formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        Composite composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        
        FixedNumber fixedNumber = (FixedNumber) composite.iPrinters[0];
        assertEquals(DateTimeFieldType.year(), fixedNumber.iFieldType);
        assertEquals(4, fixedNumber.iMaxParsedDigits);
        assertEquals(true, fixedNumber.iSigned);
        
        // Test appendFixedDecimal
        builder.clear();
        builder.appendFixedDecimal(DateTimeFieldType.monthOfYear(), 2);
        formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        
        FixedNumber fixedNumber2 = (FixedNumber) composite.iPrinters[0];
        assertEquals(DateTimeFieldType.monthOfYear(), fixedNumber2.iFieldType);
        assertEquals(2, fixedNumber2.iMaxParsedDigits);
        assertEquals(false, fixedNumber2.iSigned);
        
        // Test exceptions
        try {
            builder.appendDecimal(null, 1, 1);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        try {
            builder.appendFixedDecimal(DateTimeFieldType.dayOfMonth(), 0);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAppendTextAndTwoDigitYear() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Test appendText
        builder.appendText(DateTimeFieldType.monthOfYear());
        Object formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        Composite composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        
        TextField textField = (TextField) composite.iPrinters[0];
        assertEquals(DateTimeFieldType.monthOfYear(), textField.iFieldType);
        assertEquals(false, textField.iShort);
        
        // Test appendTwoDigitYear
        builder.clear();
        builder.appendTwoDigitYear(2000);
        formatter = builder.getFormatter();
        assertTrue(formatter instanceof Composite);
        composite = (Composite) formatter;
        assertEquals(1, composite.iPrinters.length);
        
        TwoDigitYear twoDigitYear = (TwoDigitYear) composite.iPrinters[0];
        assertEquals(DateTimeFieldType.year(), twoDigitYear.iType);
        assertEquals(2000, twoDigitYear.iPivot);
        assertEquals(false, twoDigitYear.iLenientParse);
        
        // Test append with lenient parse
        builder.clear();
        builder.appendTwoDigitYear(2000, true);
        formatter = builder.getFormatter();
        composite = (Composite) formatter;
        twoDigitYear = (TwoDigitYear) composite.iPrinters[0];
        assertEquals(true, twoDigitYear.iLenientParse);
    }

    @Test
    public void testCanBuildMethods() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        
        // Initially can build both
        assertTrue(builder.canBuildFormatter());
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        // After adding only a parser, can't build printer
        builder.append(new MockDateTimeParser());
        assertFalse(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        assertTrue(builder.canBuildFormatter());
        
        // Clear and add only a printer
        builder.clear();
        builder.append(new MockDateTimePrinter());
        assertTrue(builder.canBuildPrinter());
        assertFalse(builder.canBuildParser());
        assertTrue(builder.canBuildFormatter());
        
        // Clear and test empty state
        builder.clear();
        assertTrue(builder.canBuildFormatter());
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
    }
    
    // Mock classes for testing
    private static class MockDateTimePrinter implements DateTimePrinter {
        public int estimatePrintedLength() { return 0; }
        public void printTo(StringBuffer buf, long instant, Chronology chrono, int displayOffset, DateTimeZone displayZone, Locale locale) {}
        public void printTo(Writer out, long instant, Chronology chrono, int displayOffset, DateTimeZone displayZone, Locale locale) throws IOException {}
        public void printTo(StringBuffer buf, ReadablePartial partial, Locale locale) {}
        public void printTo(Writer out, ReadablePartial partial, Locale locale) throws IOException {}
    }
    
    private static class MockDateTimeParser implements DateTimeParser {
        public int estimateParsedLength() { return 0; }
        public int parseInto(DateTimeParserBucket bucket, String text, int position) { return 0; }
    }
}