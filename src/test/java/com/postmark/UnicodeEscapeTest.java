package com.postmark;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.postmark.util.UnicodeEscapeFilterWriter;

public class UnicodeEscapeTest {
	
	public static final String TEST = "Some test String with non-ASCII chars here. \néèàùìòÿ";
	public static final String TEST_RESULT = "Some test String with non-ASCII chars here. \n\\u00e9\\u00e8\\u00e0\\u00f9\\u00ec\\u00f2\\u00ff";

	@Test
	public void testEscape() throws IOException {
		assertEquals(TEST_RESULT, UnicodeEscapeFilterWriter.escape(TEST));
	}

}
