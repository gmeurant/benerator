/*
 * (c) Copyright 2007 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License.
 *
 * For redistributing this software or a derivative work under a license other
 * than the GPL-compatible Free Software License as defined by the Free
 * Software Foundation or approved by OSI, you must first obtain a commercial
 * license to this software product from Volker Bergmann.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED CONDITIONS,
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE
 * HEREBY EXCLUDED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.databene.commons;

import java.text.Format;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.io.IOException;

/**
 * java.lang.text.Format implementation for formatting arrays.<br/>
 * <br/>
 * Created: 20.06.2007 07:04:37
 */
public class ArrayFormat extends Format {

    // defaults --------------------------------------------------------------------------------------------------------

    private static final String DEFAULT_SEPARATOR = ", ";
    private static final Format DEFAULT_ITEM_FORMAT = ToStringFormat.getDefault();

    // attributes ------------------------------------------------------------------------------------------------------

    private Format itemFormat;
    private String separator;

    // constructors ----------------------------------------------------------------------------------------------------

    public ArrayFormat() {
        this(DEFAULT_ITEM_FORMAT, DEFAULT_SEPARATOR);
    }

    public ArrayFormat(String separator) {
        this(DEFAULT_ITEM_FORMAT, separator);
    }

    public ArrayFormat(Format itemFormat) {
        this(itemFormat, DEFAULT_SEPARATOR);
    }

    public ArrayFormat(Format itemFormat, String separator) {
        this.itemFormat = itemFormat;
        this.separator = separator;
    }

    // java.text.Format interface implementation -----------------------------------------------------------------------

    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        Object[] array = (Object[]) obj;
        return formatPart(toAppendTo, itemFormat, separator, 0, array.length, array);
    }

    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException("Not implemented"); // TODO v0.3 implement
    }

    // publicly available utility methods ------------------------------------------------------------------------------

    /**
     * formats an array's content as comma separated list
     * @param items the objects to format
     * @return a String with the comma-separated-list representation of the array
     */
    public static <T> String format(T ... items) {
        return format(", ", items);
    }

    public static <T> String format(String separator, T ... items) {
        if (items == null)
            return "";
        return formatPart(null, separator, 0, items.length, items);
    }

    public static <T> String format(Format format, String separator, T ... items) {
        return formatPart(format, separator, 0, items.length, items);
    }

    public static <T> String formatPart(int offset, int length, T ... items) {
        return formatPart(null, DEFAULT_SEPARATOR, offset, length, items);
    }

    public static <T> String formatPart(String separator, int offset, int length, T ... items) {
        return formatPart(null, separator, offset, length, items);
    }

    public static <T> String formatPart(Format format, String separator, int offset, int length, T ... items) {
        if (items.length == 0)
            return "";
        return formatPart(new StringBuilder(), format, separator, offset, length, items).toString();
    }

    public static <T, E extends Appendable> E formatPart(E toAppendTo, Format format, String separator,
                                                         int offset, int length, T ... items) {
        if (items.length == 0)
            return toAppendTo;
        try {
            if (format == null)
                format = ToStringFormat.getDefault();
            toAppendTo.append(format.format(items[offset]));
            for (int i = 1; i < length; i++)
                toAppendTo.append(separator).append(format.format(items[offset + i]));
            return toAppendTo;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String formatInts(String separator, int ... items) {
        if (items.length == 0)
            return "";
        StringBuilder builder = new StringBuilder();
        builder.append(items[0]);
        for (int i = 1; i < items.length; i++)
            builder.append(separator).append(items[i]);
        return builder.toString();
    }

    public static String formatChars(String separator, char ... items) {
        if (items.length == 0)
            return "";
        StringBuilder builder = new StringBuilder();
        builder.append(items[0]);
        for (int i = 1; i < items.length; i++)
            builder.append(separator).append(items[i]);
        return builder.toString();
    }

    public static String formatStrings(String separator, String ... items) {
        if (items == null)
            return "";
        return formatPart(null, separator, 0, items.length, items);
    }

}
