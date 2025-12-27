package org.joda.time.format;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;

public class DateTimeZoneTest {

    @Test
    public void testAppendLiteral() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendLiteral('A');
        builder.appendLiteral("BC");
        
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        assertTrue(builder.canBuildFormatter());
        
        DateTimeFormatter formatter = builder.toFormatter();
        assertEquals(true, formatter.isPrinter());
        assertEquals(true, formatter.isParser());
    }
    
    @Test
    public void testAppendDecimal() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendDecimal(DateTimeFieldType.hourOfDay(), 1, 2);
        builder.appendDecimal(DateTimeFieldType.minuteOfHour(), 2, 2);
        
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        DateTimeFormatter formatter = builder.toFormatter();
        assertNotNull(formatter);
        
        // Test normal case
        LocalDateTime dt = new LocalDateTime(2023, 6, 15, 9, 30);
        String result = formatter.print(dt);
        assertTrue(result.length() > 0);
    }
    
    @Test
    public void testClearAndRebuild() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendLiteral('X');
        assertTrue(builder.canBuildFormatter());
        
        builder.clear();
        assertEquals(0, builder.canBuildPrinter() ? 1 : 0);
        assertEquals(0, builder.canBuildParser() ? 1 : 0);
        
        // Rebuild after clear
        builder.appendText(DateTimeFieldType.dayOfWeek());
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        DateTimeFormatter formatter = builder.toFormatter();
        assertNotNull(formatter);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAppendDecimalWithNullFieldType() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendDecimal(null, 1, 2);
    }
    
    @Test
    public void testAppendTimeZoneNameWithLookup() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        Map<String, DateTimeZone> lookup = new HashMap<>();
        lookup.put("UTC", DateTimeZone.UTC);
        
        builder.appendTimeZoneName(lookup);
        
        assertTrue(builder.canBuildPrinter());
        assertTrue(builder.canBuildParser());
        
        DateTimeFormatter formatter = builder.toFormatter();
        assertNotNull(formatter);
        
        // Verify the parser/printer can be built
        DateTimePrinter printer = builder.toPrinter();
        DateTimeParser parser = builder.toParser();
        assertNotNull(printer);
        assertNotNull(parser);
    }
}