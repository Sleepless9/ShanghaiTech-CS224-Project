package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.parser.Tag;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.*;

public class TextNodeTest {

    @Test
    public void testElementCreationAndBasicProperties() {
        // Test normal element creation
        Element div = new Element(Tag.valueOf("div"), "http://example.com");
        assertEquals("div", div.tagName());
        assertEquals("http://example.com", div.baseUri());
        assertTrue(div.isBlock());
        
        // Test element with attributes
        Attributes attrs = new Attributes();
        attrs.put("id", "testId");
        attrs.put("class", "header main");
        Element p = new Element(Tag.valueOf("p"), "http://example.com", attrs);
        assertEquals("testId", p.id());
        assertEquals("header main", p.className());
        assertFalse(p.isBlock());
    }

    @Test
    public void testChildManipulation() {
        Element parent = new Element(Tag.valueOf("div"), "http://example.com");
        
        // Test appendChild
        Element child1 = new Element(Tag.valueOf("span"), "http://example.com");
        parent.appendChild(child1);
        assertEquals(1, parent.children().size());
        assertEquals(child1, parent.child(0));
        assertEquals(parent, child1.parent());
        
        // Test prependChild
        Element child2 = new Element(Tag.valueOf("p"), "http://example.com");
        parent.prependChild(child2);
        assertEquals(2, parent.children().size());
        assertEquals(child2, parent.child(0));
        assertEquals(child1, parent.child(1));
        
        // Test appendText and prependText
        parent.appendText(" appended");
        parent.prependText("prepended ");
        assertTrue(parent.hasText());
        assertTrue(parent.text().contains("prepended"));
        assertTrue(parent.text().contains("appended"));
    }

    @Test
    public void testClassManipulation() {
        Element elem = new Element(Tag.valueOf("div"), "http://example.com");
        elem.attr("class", "header main");
        
        // Test classNames retrieval
        Set<String> classes = elem.classNames();
        assertEquals(2, classes.size());
        assertTrue(classes.contains("header"));
        assertTrue(classes.contains("main"));
        
        // Test hasClass
        assertTrue(elem.hasClass("header"));
        assertFalse(elem.hasClass("footer"));
        
        // Test addClass and removeClass
        elem.addClass("new-class");
        assertTrue(elem.hasClass("new-class"));
        elem.removeClass("main");
        assertFalse(elem.hasClass("main"));
        assertTrue(elem.hasClass("header"));
        assertTrue(elem.hasClass("new-class"));
        
        // Test toggleClass
        elem.toggleClass("header");
        assertFalse(elem.hasClass("header"));
        elem.toggleClass("header");
        assertTrue(elem.hasClass("header"));
    }

    @Test
    public void testElementSelectionMethods() {
        Element root = new Element(Tag.valueOf("div"), "http://example.com");
        root.attr("id", "root");
        
        Element child1 = new Element(Tag.valueOf("p"), "http://example.com");
        child1.attr("class", "paragraph first");
        child1.appendText("First paragraph");
        root.appendChild(child1);
        
        Element child2 = new Element(Tag.valueOf("p"), "http://example.com");
        child2.attr("class", "paragraph");
        child2.attr("id", "second");
        child2.appendText("Second paragraph");
        root.appendChild(child2);
        
        // Test getElementById
        Element foundById = root.getElementById("second");
        assertNotNull(foundById);
        assertEquals(child2, foundById);
        assertNull(root.getElementById("nonexistent"));
        
        // Test getElementsByTag
        Elements paragraphs = root.getElementsByTag("p");
        assertEquals(2, paragraphs.size());
        assertTrue(paragraphs.contains(child1));
        assertTrue(paragraphs.contains(child2));
        
        // Test getElementsByClass
        Elements firstClass = root.getElementsByClass("first");
        assertEquals(1, firstClass.size());
        assertEquals(child1, firstClass.first());
        
        Elements paragraphClass = root.getElementsByClass("paragraph");
        assertEquals(2, paragraphClass.size());
    }

    @Test
    public void testSiblingNavigation() {
        Element parent = new Element(Tag.valueOf("div"), "http://example.com");
        
        Element child1 = new Element(Tag.valueOf("span"), "http://example.com");
        child1.attr("id", "first");
        parent.appendChild(child1);
        
        Element child2 = new Element(Tag.valueOf("p"), "http://example.com");
        child2.attr("id", "second");
        parent.appendChild(child2);
        
        Element child3 = new Element(Tag.valueOf("div"), "http://example.com");
        child3.attr("id", "third");
        parent.appendChild(child3);
        
        // Test nextElementSibling
        assertEquals(child2, child1.nextElementSibling());
        assertEquals(child3, child2.nextElementSibling());
        assertNull(child3.nextElementSibling());
        
        // Test previousElementSibling
        assertEquals(child2, child3.previousElementSibling());
        assertEquals(child1, child2.previousElementSibling());
        assertNull(child1.previousElementSibling());
        
        // Test elementSiblingIndex
        assertEquals(Integer.valueOf(0), child1.elementSiblingIndex());
        assertEquals(Integer.valueOf(1), child2.elementSiblingIndex());
        assertEquals(Integer.valueOf(2), child3.elementSiblingIndex());
        
        // Test firstElementSibling and lastElementSibling
        assertEquals(child1, parent.firstElementSibling());
        assertEquals(child3, parent.lastElementSibling());
    }

    @Test
    public void testHtmlAndTextManipulation() {
        Element div = new Element(Tag.valueOf("div"), "http://example.com");
        
        // Test html() and text() on empty element
        assertEquals("", div.html());
        assertEquals("", div.text());
        
        // Test setting and getting HTML
        div.html("<p>First</p><p>Second</p>");
        assertEquals(2, div.children().size());
        assertEquals("FirstSecond", div.text());
        
        // Test append and prepend HTML
        div.append("<span>appended</span>");
        div.prepend("<h1>prepended</h1>");
        assertEquals(4, div.children().size());
        assertTrue(div.html().contains("prepended"));
        assertTrue(div.html().contains("appended"));
        
        // Test empty()
        div.empty();
        assertEquals(0, div.children().size());
        assertEquals("", div.html());
        
        // Test val() for form elements
        Element input = new Element(Tag.valueOf("input"), "http://example.com");
        input.attr("value", "test value");
        assertEquals("test value", input.val());
        
        Element textarea = new Element(Tag.valueOf("textarea"), "http://example.com");
        textarea.text("textarea content");
        assertEquals("textarea content", textarea.val());
    }
}