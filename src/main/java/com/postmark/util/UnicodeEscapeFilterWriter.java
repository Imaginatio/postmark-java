// The MIT License
//
// Copyright (c) 2010 Jared Holdcroft
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

/* Postmark for Java / Spring
 * <p/>
 * This library can be used to leverage the postmarkapp.com functionality from a Java client
 * <p/>
 *
 * https://github.com/Imaginatio/postmark-java
 * forked from https://github.com/bitformed/postmark-java
 */

package com.postmark.util;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * FilterWriter escaping non-ASCII characters to their <tt>\\uXXXX</tt> Unicode escape
 * sequence.
 * <p>
 * Processes the buffer char by char.
 * <p>
 * A faster way for heavy usage would be to pre-build the 65K entries table of escape
 * sequences, but that would mean keeping <tt>65K*4*2 = 512KB</tt> in memory just for
 * this feature, which should be fast enough as it is for most uses.
 * 
 * @see FilterWriter
 */
public class UnicodeEscapeFilterWriter extends FilterWriter {
	
	protected UnicodeEscapeFilterWriter(Writer out) {
		super(out);
	}

	private static char[] prefix = { '\\', 'u', '0', '0', '0' };
	

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
    	for (int i = 0; i < len; i++) {
            if ((cbuf[i] > '\u007f')) {
                String hx = Integer.toHexString(cbuf[i]);
                out.write(prefix, 0, 6 - hx.length());
                out.write(hx);
            } else
                out.write(cbuf[i]);
        }
    }

    @Override
    public void write(int c) throws IOException {
        write(new char[] {(char)c}, 0, 1);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        write(str.toCharArray(), off, len);
    }
    
    
    /**
     * Escapes <tt>str</tt> using this Filter.
     * <p>
     * Default estimate for non-ASCII chars ratio is 3%
     * 
     * @param str the original String
     * @return the escaped String
     * @throws IOException
     */
    public static String escape(String str) throws IOException {
		return escape(str, .03f);
    }

    /**
     * Escapes <tt>str</tt> using this Filter.
     * 
     * @param str the original String
     * @param estimatedNonASCIIRatio an estimation of the ratio of non-ASCII chars in <tt>str</tt>.
     * It is used for the initial allocation size of the new char array.
     * @return the escaped String
     * @throws IOException
     */
    public static String escape(String str, float estimatedNonASCIIRatio) throws IOException {
		StringWriter out = new StringWriter(Math.round(str.length() * (1 + estimatedNonASCIIRatio * 5)));
		UnicodeEscapeFilterWriter fw = new UnicodeEscapeFilterWriter(out);
		fw.write(str);
		return out.toString();

    }
}
