package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.HashSet;
import java.util.Set;

public class CharacterReaderTest {
    private Element divElement;
    private Element parentElement;
    private Element childElement1;
    private Element childElement2;
    
    @Before
    public void setUp() {
        divElement = new Element(Tag.valueOf("div"), "http://example.com");
        parentElement = new Element(Tag.valueOf("div"), "http://example.com");
        childElement1 = new Element(Tag.valueOf("span"), "http://example.com");
        childElement2 = new Element(Tag.valueOf("p"), "http://example.com");
    }
    
    @Test
    public void testElementCreationAndBasicProperties() {
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        
        assertEquals("div", element.tagName());
        assertEquals("http://example.com", element.baseUri());
        assertTrue(element.isBlock());
        assertEquals("", element.id());
        
        Element spanElement = new Element(Tag.valueOf("span"), "http://example.com");
        assertEquals("span", spanElement.tagName());
        assertFalse(spanElement.isBlock());
    }
    
    @Test
    public void testAttributeOperations() {
        divElement.attr("id", "main");
        divElement.attr("class", "container");
        
        assertEquals("main", divElement.id());
        assertEquals("container", divElement.attr("class"));
        assertEquals("main", divElement.attr("id"));
        
        divElement.attr("data-test", "value");
        assertEquals("value", divElement.attr("data-test"));
    }
    
    @Test
    public void testChildManipulation() {
        parentElement.appendChild(childElement1);
        parentElement.appendChild(childElement2);
        
        assertEquals(2, parentElement.children().size());
        assertEquals(childElement1, parentElement.child(0));
        assertEquals(childElement2, parentElement.child(1));
        
        Element firstChild = parentElement.children().first();
        assertEquals(childElement1, firstChild);
        
        parentElement.empty();
        assertEquals(0, parentElement.children().size());
        assertTrue(parentElement.children().isEmpty());
    }
    
    @Test
    public void testClassOperations() {
        divElement.attr("class", "header main active");
        
        assertTrue(divElement.hasClass("header"));
        assertTrue(divElement.hasClass("main"));
        assertTrue(divElement.hasClass("active"));
        assertFalse(divElement.hasClass("inactive"));
        
        Set<String> classes = divElement.classNames();
        assertEquals(3, classes.size());
        assertTrue(classes.contains("header"));
        
        divElement.addClass("new-class");
        assertTrue(divElement.hasClass("new-class"));
        
        divElement.removeClass("main");
        assertFalse(divElement.hasClass("main"));
        
        divElement.toggleClass("active");
        assertFalse(divElement.hasClass("active"));
        
        divElement.toggleClass("inactive");
        assertTrue(divElement.hasClass("inactive"));
    }
    
    @Test
    public void testTextAndHtmlOperations() {
        divElement.appendText("Hello ");
        divElement.appendText("World");
        
        assertEquals("Hello World", divElement.text());
        assertTrue(divElement.hasText());
        
        divElement.text("Replaced text");
        assertEquals("Replaced text", divElement.text());
        
        divElement.html("<span>Inner HTML</span>");
        assertTrue(divElement.html().contains("span"));
        assertEquals("Inner HTML", divElement.text());
        
        Element textarea = new Element(Tag.valueOf("textarea"), "http://example.com");
        textarea.text("Text area content");
        assertEquals("Text area content", textarea.val());
        
        textarea.val("New value");
        assertEquals("New value", textarea.val());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullTagThrowsException() {
        new Element(null, "http://example.com");
    }
    
    @Test
    public void testElementSiblings() {
        Element parent = new Element(Tag.valueOf("div"), "http://example.com");
        Element child1 = new Element(Tag.valueOf("p"), "http://example.com");
        Element child2 = new Element(Tag.valueOf("span"), "http://example.com");
        Element child3 = new Element(Tag.valueOf("div"), "http://example.com");
        
        parent.appendChild(child1);
        parent.appendChild(child2);
        parent.appendChild(child3);
        
        assertEquals(child2, child1.nextElementSibling());
        assertEquals(child3, child2.nextElementSibling());
        assertNull(child3.nextElementSibling());
        
        assertEquals(child1, child2.previousElementSibling());
        assertEquals(child2, child3.previousElementSibling());
        assertNull(child1.previousElementSibling());
        
        assertEquals(child1, parent.firstElementSibling());
        assertEquals(child3, parent.lastElementSibling());
    }
    
    @Test
    public void testAppendAndPrependElements() {
        Element container = new Element(Tag.valueOf("div"), "http://example.com");
        
        Element appended = container.appendElement("p");
        appended.attr("id", "first");
        assertEquals("p", appended.tagName());
        assertEquals("first", appended.attr("id"));
        assertEquals(1, container.children().size());
        
        Element prepended = container.prependElement("h1");
        prepended.text("Title");
        assertEquals("h1", prepended.tagName());
        assertEquals("Title", prepended.text());
        assertEquals(2, container.children().size());
        assertEquals(prepended, container.child(0));
    }
}