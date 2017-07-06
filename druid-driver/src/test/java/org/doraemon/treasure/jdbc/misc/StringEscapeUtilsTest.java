package org.doraemon.treasure.jdbc.misc;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class StringEscapeUtilsTest {

    @Test
    public void escape() throws Exception {
//        assertThat(
//                StringEscapeUtils.escape("首012abc\0\r\n\\mn56中'\b\f\txyz789尾"),
//                is("首012abc\\0\\r\\n\\\\mn56中\\'\\b\\f\\txyz789尾")
//        );
        assertThat(
                StringEscapeUtils.escape("首012abc\\mn56中'xyz789尾"),
                is("首012abc\\\\mn56中\\'xyz789尾")
        );
        assertThat(StringEscapeUtils.escape("首012abc尾"), is("首012abc尾"));
    }

    @Test
    public void needEscape() throws Exception {
        assertThat(StringEscapeUtils.needEscape("abc123\\xyz"), is(true));
        assertThat(StringEscapeUtils.needEscape("abc123'xyz"), is(true));
        assertThat(StringEscapeUtils.needEscape("abc123"), is(false));
    }

    @Test
    public void singleQuote() throws Exception {
        System.out.println("\'");
        System.out.println("'");

    }
}
