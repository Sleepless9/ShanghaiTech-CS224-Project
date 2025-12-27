package org.jsoup.nodes;

import org.apache.commons.lang.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ParserTest {

    @Test
    public void testConstructorAndBasicProperties() {
        Tag tag = Tag.valueOf("div");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");

        Element element = new Element(tag, baseUri, attributes);

        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("div", element.nodeName());
        assertEquals(tag, element.tag());
        assertEquals(baseUri, element.baseUri());
        assertEquals("test", element.attr("id"));
        assertTrue(element.attributes().hasKey("id"));
    }

    @Test
    public void testChildOperations() {
        Tag tag = Tag.valueOf("div");
        Element parent = new Element(tag, "http://example.com");
        Element child1 = new Element(Tag.valueOf("p"), "http://example.com");
        Element child2 = new Element(Tag.valueOf("span"), "http://example.com");

        // Test appendChild
        parent.appendChild(child1);
        parent.appendChild(child2);

        Elements children = parent.children();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));

        // Test child() by index
        assertEquals(child1, parent.child(0));
        assertEquals(child2, parent.child(1));

        // Test prependChild
        Element child3 = new Element(Tag.valueOf("a"), "http://example.com");
        parent.prependChild(child3);
        assertEquals(child3, parent.child(0));
        assertEquals(child1, parent.child(1));
        assertEquals(3, parent.children().size());
    }

    @Test
    public void testTextAndContentMethods() {
        Tag tag = Tag.valueOf("div");
        Element element = new Element(tag, "http://example.com");

        // Test text operations
        element.appendText("Hello");
        assertEquals("Hello", element.text());
        assertTrue(element.hasText());

        // Test setting text directly
        element.text("New text");
        assertEquals("New text", element.text());
        assertEquals(1, element.childNodes().size());
        assertTrue(element.childNode(0) instanceof TextNode);

        // Test empty content
        element.empty();
        assertEquals("", element.text());
        assertFalse(element.hasText());
        assertEquals(0, element.childNodes().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullChildValidation() {
        Tag tag = Tag.valueOf("div");
        Element element = new Element(tag, "http://example.com");
        
        try {
            element.appendChild(null);
        } catch (IllegalArgumentException e) {
            // Apache Commons Lang's Validate throws IllegalArgumentException
            throw e;
        }
    }

    @Test
    public void testClassNamesOperations() {
        Tag tag = Tag.valueOf("div");
        Element element = new Element(tag, "http://example.com");
        
        // Test initial state
        assertEquals("", element.className());
        assertTrue(element.classNames().isEmpty());
        assertFalse(element.hasClass("active"));

        // Test addClass
        element.addClass("active");
        assertTrue(element.hasClass("active"));
        assertEquals("active", element.className());
        assertTrue(element.classNames().contains("active"));

        // Test add multiple classes
        element.addClass("highlight");
        assertTrue(element.hasClass("highlight"));
        Set<String> expectedClasses = new LinkedHashSet<>();
        expectedClasses.add("active");
        expectedClasses.add("highlight");
        assertEquals(expectedClasses, element.classNames());

        // Test removeClass
        element.removeClass("active");
        assertFalse(element.hasClass("active"));
        assertTrue(element.hasClass("highlight"));
        assertEquals("highlight", element.className());

        // Test toggleClass
        element.toggleClass("new-class"); // adds it
        assertTrue(element.hasClass("new-class"));
        element.toggleClass("new-class"); // removes it
        assertFalse(element.hasClass("new-class"));
    }
}