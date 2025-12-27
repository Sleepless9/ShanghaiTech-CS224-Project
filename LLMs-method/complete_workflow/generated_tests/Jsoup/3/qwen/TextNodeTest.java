package org.jsoup.nodes;

import org.apache.commons.lang.StringUtils;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TextNodeTest {

    @Test
    public void testConstructorAndBasicProperties() {
        Tag tag = Tag.valueOf("div");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");

        Element element = new Element(tag, baseUri, attributes);

        assertEquals("div", element.tagName());
        assertEquals("div", element.nodeName());
        assertEquals(tag, element.tag());
        assertEquals(baseUri, element.baseUri());
        assertEquals("test", element.attr("id"));
        assertTrue(element.attributes().hasKey("id"));
    }

    @Test
    public void testChildOperations() {
        Element parent = new Element(Tag.valueOf("div"), "http://example.com");
        Element child1 = new Element(Tag.valueOf("p"), "http://example.com");
        Element child2 = new Element(Tag.valueOf("span"), "http://example.com");

        // Test appendChild
        parent.appendChild(child1);
        parent.appendChild(child2);

        Elements children = parent.children();
        assertEquals(2, children.size());
        assertEquals("p", children.get(0).tagName());
        assertEquals("span", children.get(1).tagName());

        // Test child() by index
        assertEquals(child1, parent.child(0));
        assertEquals(child2, parent.child(1));

        // Test prependChild
        Element child3 = new Element(Tag.valueOf("h1"), "http://example.com");
        parent.prependChild(child3);
        assertEquals("h1", parent.child(0).tagName());
        assertEquals("p", parent.child(1).tagName());
        assertEquals(3, parent.children().size());
    }

    @Test
    public void testTextOperations() {
        Element element = new Element(Tag.valueOf("p"), "http://example.com");

        // Test text() and hasText()
        assertFalse(element.hasText());
        assertEquals("", element.text());

        // Test appendText and text()
        element.appendText("Hello");
        assertTrue(element.hasText());
        assertEquals("Hello", element.text());

        // Test setting text (replaces content)
        element.text("New text");
        assertEquals("New text", element.text());
        assertEquals(1, element.children().size()); // Should have one TextNode
    }

    @Test
    public void testClassOperations() {
        Element element = new Element(Tag.valueOf("div"), "http://example.com");

        // Test initial state
        assertFalse(element.hasClass("active"));
        assertEquals(0, element.classNames().size());
        assertEquals("", element.className());

        // Test addClass
        element.addClass("active");
        assertTrue(element.hasClass("active"));
        Set<String> expectedClasses = new HashSet<>(Arrays.asList("active"));
        assertEquals(expectedClasses, element.classNames());
        assertEquals("active", element.className());

        // Test add multiple classes
        element.addClass("highlight");
        assertTrue(element.hasClass("highlight"));
        expectedClasses.add("highlight");
        assertEquals(expectedClasses, element.classNames());
        assertTrue(StringUtils.contains(element.className(), "active"));
        assertTrue(StringUtils.contains(element.className(), "highlight"));

        // Test removeClass
        element.removeClass("active");
        assertFalse(element.hasClass("active"));
        assertFalse(element.classNames().contains("active"));
        assertTrue(element.hasClass("highlight"));

        // Test toggleClass
        element.toggleClass("active"); // should add back
        assertTrue(element.hasClass("active"));
        element.toggleClass("active"); // should remove
        assertFalse(element.hasClass("active"));
    }

    @Test
    public void testExceptionCases() {
        Element element = new Element(Tag.valueOf("div"), "http://example.com");

        // Test null child in appendChild
        try {
            element.appendChild(null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test null child in prependChild
        try {
            element.prependChild(null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test null className operations
        try {
            element.addClass(null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            element.removeClass(null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test empty operations
        element.empty();
        assertEquals(0, element.children().size());
        assertFalse(element.hasText());
    }
}