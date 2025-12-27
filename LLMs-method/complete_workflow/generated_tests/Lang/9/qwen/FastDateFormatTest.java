package org.apache.commons.lang3;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class FastDateFormatTest {

    @Test
    public void testIsEmpty() {
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty("abc"));
        assertFalse(StringUtils.isEmpty("  abc  "));
        
        assertThat(StringUtils.EMPTY, is(""));
        assertThat(StringUtils.INDEX_NOT_FOUND, is(-1));
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertTrue(StringUtils.isBlank("\t\n\r"));
        assertFalse(StringUtils.isBlank("abc"));
        assertFalse(StringUtils.isBlank("  abc  "));
        
        assertThat(StringUtils.isNotBlank(null), is(false));
        assertThat(StringUtils.isNotBlank(""), is(false));
        assertThat(StringUtils.isNotBlank(" "), is(false));
        assertThat(StringUtils.isNotBlank("abc"), is(true));
    }

    @Test
    public void testTrim() {
        assertThat(StringUtils.trim(null), nullValue());
        assertThat(StringUtils.trim(""), is(""));
        assertThat(StringUtils.trim("     "), is(""));
        assertThat(StringUtils.trim("abc"), is("abc"));
        assertThat(StringUtils.trim("    abc    "), is("abc"));
        
        assertThat(StringUtils.trimToNull(null), nullValue());
        assertThat(StringUtils.trimToNull(""), nullValue());
        assertThat(StringUtils.trimToNull("     "), nullValue());
        assertThat(StringUtils.trimToNull("abc"), is("abc"));
    }

    @Test
    public void testEquals() {
        assertTrue(StringUtils.equals(null, null));
        assertFalse(StringUtils.equals(null, "abc"));
        assertFalse(StringUtils.equals("abc", null));
        assertTrue(StringUtils.equals("abc", "abc"));
        assertFalse(StringUtils.equals("abc", "ABC"));
        
        assertTrue(StringUtils.equalsIgnoreCase("abc", "ABC"));
        assertFalse(StringUtils.equalsIgnoreCase("abc", "ABD"));
        assertTrue(StringUtils.equalsIgnoreCase(null, null));
        assertFalse(StringUtils.equalsIgnoreCase("abc", null));
    }

    @Test
    public void testContains() {
        assertFalse(StringUtils.contains(null, 'a'));
        assertFalse(StringUtils.contains("", 'a'));
        assertTrue(StringUtils.contains("abc", 'a'));
        assertTrue(StringUtils.contains("abc", 'b'));
        assertFalse(StringUtils.contains("abc", 'z'));
        
        assertFalse(StringUtils.containsIgnoreCase(null, "abc"));
        assertFalse(StringUtils.containsIgnoreCase("abc", null));
        assertTrue(StringUtils.containsIgnoreCase("abc", "A"));
        assertTrue(StringUtils.containsIgnoreCase("abc", "BC"));
        assertFalse(StringUtils.containsIgnoreCase("abc", "ZZ"));
    }

    @Test
    public void testSubstring() {
        assertThat(StringUtils.substring(null, 0), nullValue());
        assertThat(StringUtils.substring("", 0), is(""));
        assertThat(StringUtils.substring("abc", 0), is("abc"));
        assertThat(StringUtils.substring("abc", 2), is("c"));
        assertThat(StringUtils.substring("abc", 4), is(""));
        assertThat(StringUtils.substring("abc", -2), is("bc"));
        
        assertThat(StringUtils.substring(null, 0, 2), nullValue());
        assertThat(StringUtils.substring("", 0, 2), is(""));
        assertThat(StringUtils.substring("abc", 0, 2), is("ab"));
        assertThat(StringUtils.substring("abc", 2, 0), is(""));
        assertThat(StringUtils.substring("abc", 2, 4), is("c"));
    }

    @Test
    public void testReplace() {
        assertThat(StringUtils.replace(null, "a", "b"), nullValue());
        assertThat(StringUtils.replace("", "a", "b"), is(""));
        assertThat(StringUtils.replace("abc", null, "b"), is("abc"));
        assertThat(StringUtils.replace("abc", "a", null), is("abc"));
        assertThat(StringUtils.replace("aba", "a", "z"), is("zbz"));
        assertThat(StringUtils.replace("aba", "a", ""), is("b"));
        
        assertThat(StringUtils.replaceOnce("aba", "a", "z"), is("zba"));
        assertThat(StringUtils.replaceOnce("aba", "x", "z"), is("aba"));
    }

    @Test
    public void testJoin() {
        assertThat(StringUtils.join((Object[]) null), nullValue());
        assertThat(StringUtils.join(new String[]{}), is(""));
        assertThat(StringUtils.join(new String[]{null}), is(""));
        assertThat(StringUtils.join(new String[]{"a", "b", "c"}), is("abc"));
        assertThat(StringUtils.join(new Object[]{null, "", "a"}), is("a"));
        
        assertThat(StringUtils.join(new String[]{"a", "b", "c"}, ';'), is("a;b;c"));
        assertThat(StringUtils.join(new String[]{null, "", "a"}, ';'), is(";;a"));
    }

    @Test
    public void testSplit() {
        assertThat(StringUtils.split(null), nullValue());
        assertThat(StringUtils.split(""), is(emptyArray()));
        assertThat(StringUtils.split("abc def"), is(arrayContaining("abc", "def")));
        assertThat(StringUtils.split("abc  def"), is(arrayContaining("abc", "def")));
        assertThat(StringUtils.split(" abc "), is(arrayContaining("abc")));
        
        assertThat(StringUtils.split(null, ' '), nullValue());
        assertThat(StringUtils.split("", ' '), is(emptyArray()));
        assertThat(StringUtils.split("a.b.c", '.'), is(arrayContaining("a", "b", "c")));
        assertThat(StringUtils.split("a..b.c", '.'), is(arrayContaining("a", "b", "c")));
    }

    @Test
    public void testLeftRightPad() {
        assertThat(StringUtils.leftPad(null, 5), nullValue());
        assertThat(StringUtils.leftPad("", 3), is("   "));
        assertThat(StringUtils.leftPad("bat", 3), is("bat"));
        assertThat(StringUtils.leftPad("bat", 5), is("  bat"));
        assertThat(StringUtils.leftPad("bat", 1), is("bat"));
        
        assertThat(StringUtils.rightPad(null, 5), nullValue());
        assertThat(StringUtils.rightPad("", 3), is("   "));
        assertThat(StringUtils.rightPad("bat", 3), is("bat"));
        assertThat(StringUtils.rightPad("bat", 5), is("bat  "));
        assertThat(StringUtils.rightPad("bat", 1), is("bat"));
    }

    @Test
    public void testStartsWithEndsWith() {
        assertTrue(StringUtils.startsWith(null, null));
        assertFalse(StringUtils.startsWith(null, "abc"));
        assertFalse(StringUtils.startsWith("abcdef", null));
        assertTrue(StringUtils.startsWith("abcdef", "abc"));
        assertFalse(StringUtils.startsWith("ABCDEF", "abc"));
        
        assertTrue(StringUtils.endsWith(null, null));
        assertFalse(StringUtils.endsWith(null, "def"));
        assertFalse(StringUtils.endsWith("abcdef", null));
        assertTrue(StringUtils.endsWith("abcdef", "def"));
        assertFalse(StringUtils.endsWith("ABCDEF", "def"));
        
        assertTrue(StringUtils.startsWithIgnoreCase("ABCDEF", "abc"));
        assertTrue(StringUtils.endsWithIgnoreCase("ABCDEF", "def"));
    }

    @Test
    public void testRemove() {
        assertThat(StringUtils.remove(null, "a"), nullValue());
        assertThat(StringUtils.remove("", "a"), is(""));
        assertThat(StringUtils.remove("queued", "ue"), is("qd"));
        assertThat(StringUtils.remove("queued", "zz"), is("queued"));
        
        assertThat(StringUtils.removeStart("www.domain.com", "www."), is("domain.com"));
        assertThat(StringUtils.removeStart("domain.com", "www."), is("domain.com"));
        
        assertThat(StringUtils.removeEnd("www.domain.com", ".com"), is("www.domain"));
        assertThat(StringUtils.removeEnd("www.domain.com", "domain"), is("www.domain.com"));
    }

    @Test
    public void testReverse() {
        assertThat(StringUtils.reverse(null), nullValue());
        assertThat(StringUtils.reverse(""), is(""));
        assertThat(StringUtils.reverse("bat"), is("tab"));
        assertThat(StringUtils.reverse("hello"), is("olleh"));
        
        assertThat(StringUtils.reverseDelimited(null, '.'), nullValue());
        assertThat(StringUtils.reverseDelimited("", '.'), is(""));
        assertThat(StringUtils.reverseDelimited("a.b.c", '.'), is("c.b.a"));
        assertThat(StringUtils.reverseDelimited("a.b.c", 'x'), is("a.b.c"));
    }

    @Test
    public void testAbbreviate() {
        assertThat(StringUtils.abbreviate(null, 4), nullValue());
        assertThat(StringUtils.abbreviate("", 4), is(""));
        assertThat(StringUtils.abbreviate("abcdefg", 6), is("abc..."));
        assertThat(StringUtils.abbreviate("abcdefg", 7), is("abcdefg"));
        assertThat(StringUtils.abbreviate("abcdefg", 8), is("abcdefg"));
        assertThat(StringUtils.abbreviate("abcdefg", 4), is("a..."));
    }
}