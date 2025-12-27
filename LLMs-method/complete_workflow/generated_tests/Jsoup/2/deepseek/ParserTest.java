package org.jsoup.nodes;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class ParserTest {
    private Element divElement;
    private Element parentElement;
    
    @Before
    public void setUp() {
        divElement = new Element(Tag.valueOf("div"), "http://example.com");
        parentElement = new Element(Tag.valueOf("div"), "http://example.com");
    }
    
    @Test
    public void testElementCreationAndBasicProperties() {
        Element p = new Element(Tag.valueOf("p"), "http://test.com");
        
        assertEquals("p", p.tagName());
        assertEquals("p", p.nodeName());
        assertTrue(p.isBlock());
        
        Element span = new Element(Tag.valueOf("span"), "http://test.com");
        assertFalse(span.isBlock());
        
        assertNotNull(p.attributes());
        assertEquals("http://test.com", p.baseUri());
    }
    
    @Test
    public void testChildManipulation() {
        Element child1 = new Element(Tag.valueOf("span"), "http://example.com");
        Element child2 = new Element(Tag.valueOf("p"), "http://example.com");
        
        divElement.appendChild(child1);
        divElement.appendChild(child2);
        
        assertEquals(2, divElement.children().size());
        assertEquals(child1, divElement.child(0));
        assertEquals(child2, divElement.child(1));
        
        assertThat(divElement.children(), hasItems(child1, child2));
        assertEquals(divElement, child1.parent());
        assertEquals(divElement, child2.parent());
    }
    
    @Test
    public void testTextAndHtmlMethods() {
        divElement.text("Hello World");
        
        assertEquals("Hello World", divElement.text());
        assertTrue(divElement.hasText());
        
        divElement.html("<span>Test</span>");
        assertEquals("<span>Test</span>", divElement.html().trim());
        assertEquals(1, divElement.children().size());
        
        divElement.empty();
        assertEquals(0, divElement.children().size());
        assertFalse(divElement.hasText());
    }
    
    @Test
    public void testClassManipulation() {
        divElement.attr("class", "header main");
        
        assertTrue(divElement.hasClass("header"));
        assertTrue(divElement.hasClass("main"));
        assertEquals(2, divElement.classNames().size());
        
        divElement.addClass("highlight");
        assertTrue(divElement.hasClass("highlight"));
        
        divElement.removeClass("main");
        assertFalse(divElement.hasClass("main"));
        
        divElement.toggleClass("active");
        assertTrue(divElement.hasClass("active"));
        
        divElement.toggleClass("active");
        assertFalse(divElement.hasClass("active"));
    }
    
    @Test
    public void testAttributeAndIdMethods() {
        divElement.attr("id", "mainContent");
        divElement.attr("data-custom", "value123");
        
        assertEquals("mainContent", divElement.id());
        assertEquals("value123", divElement.attr("data-custom"));
        
        divElement.attr("id", "updatedId");
        assertEquals("updatedId", divElement.id());
        
        Element noIdElement = new Element(Tag.valueOf("span"), "http://test.com");
        assertEquals("", noIdElement.id());
    }
    
    @Test
    public void testSiblingNavigation() {
        Element parent = new Element(Tag.valueOf("div"), "http://example.com");
        Element child1 = parent.appendElement("p");
        Element child2 = parent.appendElement("span");
        Element child3 = parent.appendElement("div");
        
        assertEquals(child2, child1.nextElementSibling());
        assertEquals(child3, child2.nextElementSibling());
        assertNull(child3.nextElementSibling());
        
        assertEquals(child2, child3.previousElementSibling());
        assertEquals(child1, child2.previousElementSibling());
        assertNull(child1.previousElementSibling());
        
        assertEquals(child1, parent.firstElementSibling());
        assertEquals(child3, parent.lastElementSibling());
        
        assertEquals(Integer.valueOf(0), child1.elementSiblingIndex());
        assertEquals(Integer.valueOf(2), child3.elementSiblingIndex());
    }
    
    @Test
    public void testAppendAndPrependMethods() {
        divElement.appendText("End Text");
        divElement.prependText("Start Text");
        
        assertEquals("Start TextEnd Text", divElement.text());
        
        Element appended = divElement.appendElement("span");
        appended.attr("class", "appended");
        
        Element prepended = divElement.prependElement("p");
        prepended.attr("class", "prepended");
        
        assertEquals(4, divElement.children().size());
        assertEquals("p", divElement.child(0).tagName());
        assertEquals("span", divElement.child(3).tagName());
        
        divElement.append("<b>Bold</b>");
        assertEquals(5, divElement.children().size());
        assertEquals("b", divElement.child(4).tagName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullChildAppendThrowsException() {
        divElement.appendChild(null);
    }
    
    @Test
    public void testElementEquality() {
        Element el1 = new Element(Tag.valueOf("div"), "http://test.com");
        el1.attr("id", "test");
        
        Element el2 = new Element(Tag.valueOf("div"), "http://test.com");
        el2.attr("id", "test");
        
        Element el3 = new Element(Tag.valueOf("span"), "http://test.com");
        el3.attr("id", "test");
        
        assertEquals(el1, el1);
        assertNotEquals(el1, el2);
        assertNotEquals(el1, el3);
        assertNotEquals(el1, null);
        assertNotEquals(el1, "string");
        
        assertNotEquals(el1.hashCode(), el2.hashCode());
    }
    
    @Test
    public void testFormElementValueMethods() {
        Element input = new Element(Tag.valueOf("input"), "http://example.com");
        input.attr("value", "initial");
        
        assertEquals("initial", input.val());
        
        input.val("updated");
        assertEquals("updated", input.attr("value"));
        
        Element textarea = new Element(Tag.valueOf("textarea"), "http://example.com");
        textarea.text("Text content");
        
        assertEquals("Text content", textarea.val());
        
        textarea.val("New content");
        assertEquals("New content", textarea.text());
    }
}