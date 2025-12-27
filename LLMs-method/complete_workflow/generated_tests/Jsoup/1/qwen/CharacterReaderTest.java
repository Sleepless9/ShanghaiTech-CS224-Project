package org.jsoup.nodes;

import org.apache.commons.lang.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CharacterReaderTest {

    @Test
    public void testConstructorAndBasicProperties() {
        Tag tag = Tag.valueOf("div");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testId");

        Element element = new Element(tag, baseUri, attributes);

        assertEquals("div", element.tagName());
        assertEquals("div", element.nodeName());
        assertEquals(tag, element.tag());
        assertEquals(baseUri, element.baseUri());
        assertEquals("testId", element.attr("id"));
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

        assertEquals(2, parent.children().size());
        assertEquals(child1, parent.child(0));
        assertEquals(child2, parent.child(1));

        // Test prependChild
        Element child0 = new Element(Tag.valueOf("h1"), "http://example.com");
        parent.prependChild(child0);

        assertEquals(3, parent.children().size());
        assertEquals(child0, parent.child(0));
        assertEquals(child1, parent.child(1));
        assertEquals(child2, parent.child(2));
    }

    @Test
    public void testTextOperations() {
        Tag tag = Tag.valueOf("p");
        Element element = new Element(tag, "http://example.com");

        // Test appendText and text()
        element.appendText("Hello");
        assertEquals("Hello", element.text());
        assertTrue(element.hasText());

        // Test text(String) - should replace content
        element.text("New text");
        assertEquals("New text", element.text());
        assertEquals(1, element.childNodes().size());
        assertTrue(element.childNode(0) instanceof TextNode);

        // Test prependText
        element.empty();
        element.appendText("world");
        element.prependText("Hello ");
        assertEquals("Hello world", element.text());
    }

    @Test
    public void testClassOperations() {
        Tag tag = Tag.valueOf("div");
        Element element = new Element(tag, "http://example.com");

        // Test className and classNames
        assertTrue(element.className().isEmpty());
        assertTrue(element.classNames().isEmpty());
        assertFalse(element.hasClass("test"));

        // Test addClass
        element.addClass("test");
        assertEquals("test", element.className());
        assertTrue(element.hasClass("test"));
        assertEquals(1, element.classNames().size());

        // Test add multiple classes
        element.addClass("active");
        Set<String> expectedClasses = new LinkedHashSet<>();
        expectedClasses.add("test");
        expectedClasses.add("active");
        assertEquals(expectedClasses, element.classNames());
        assertTrue(element.hasClass("active"));

        // Test removeClass
        element.removeClass("test");
        assertFalse(element.hasClass("test"));
        assertTrue(element.hasClass("active"));
        assertEquals(1, element.classNames().size());

        // Test toggleClass
        element.toggleClass("test"); // adds it back
        assertTrue(element.hasClass("test"));
        element.toggleClass("test"); // removes it
        assertFalse(element.hasClass("test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullTag() {
        try {
            new Element(null, "http://example.com");
        } catch (IllegalArgumentException e) {
            // Apache Commons Lang 2.x throws IllegalArgumentException for Validate.notNull
            throw e;
        }
    }
}