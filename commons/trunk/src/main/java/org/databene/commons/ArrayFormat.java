/*
 * (c) Copyright 2007-2011 by Volker Bergmann. All rights reserved.
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

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

import org.databene.commons.converter.AnyConverter;
import org.databene.commons.converter.ToStringConverter;

/**
 * java.lang.text.Format implementation for formatting and parsing arrays.<br/>
 * <br/>
 * Created: 20.06.2007 07:04:37
 * @author Volker Bergmann
 */
public class ArrayFormat extends Format {

    // defaults --------------------------------------------------------------------------------------------------------

    private static final long serialVersionUID = 290320869220307493L;
    
	private static final String DEFAULT_SEPARATOR = ", ";
    private static final Converter<Object, String> DEFAULT_ITEM_FORMATTER = new ToStringConverter();

    // attributes ------------------------------------------------------------------------------------------------------

    private Converter<Object, String> itemFormatter;
    private String separator;

    // constructors ----------------------------------------------------------------------------------------------------

    public ArrayFormat() {
        this(DEFAULT_ITEM_FORMATTER, DEFAULT_SEPARATOR);
    }

    public ArrayFormat(String separator) {
        this(DEFAULT_ITEM_FORMATTER, separator);
    }

    public ArrayFormat(Converter<Object, String> itemFormat) {
        this(itemFormat, DEFAULT_SEPARATOR);
    }

    public ArrayFormat(Converter<Object, String> itemFormatter, String separator) {
        this.itemFormatter = itemFormatter;
        this.separator = separator;
    }

    // java.text.Format interface implementation -----------------------------------------------------------------------

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        return formatPart(toAppendTo, itemFormatter, separator, 0, Array.getLength(obj), obj);
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        String[] result = parse(source, separator, String.class);
        pos.setIndex(source.length());
		return result;
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

    public static <T> String format(Converter<Object,String> formatter, String separator, T ... items) {
        return formatPart(formatter, separator, 0, items.length, items);
    }

    public static <T> String formatPart(int offset, int length, T ... items) {
        return formatPart(null, DEFAULT_SEPARATOR, offset, length, items);
    }

    public static <T> String formatPart(String separator, int offset, int length, T ... items) {
        return formatPart(null, separator, offset, length, items);
    }

    public static <T> String formatPart(Converter<Object,String> formatter, String separator, int offset, int length, T ... items) {
        if (items.length == 0)
            return "";
        return formatPart(new StringBuilder(), formatter, separator, offset, length, items).toString();
    }

    public static <T, E extends Appendable> E formatPart(E toAppendTo, Converter<Object,String> formatter, String separator,
                                                         int offset, int length, Object items) {
        if (Array.getLength(items) == 0)
            return toAppendTo;
        try {
            if (formatter == null)
                formatter = DEFAULT_ITEM_FORMATTER;
            toAppendTo.append(formatter.convert(Array.get(items, offset)));
            for (int i = 1; i < length; i++)
                toAppendTo.append(separator).append(formatter.convert(Array.get(items, offset + i)));
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

    public static String formatBytes(String separator, byte ... items) {
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
    
    // parse methods ---------------------------------------------------------------------------------------------------

    public static <T> T[] parse(String source, String separator, Class<T> componentType) {
        ArrayBuilder<T> builder = new ArrayBuilder<T>(componentType);
        int i = 0;
        int sepIndex;
        while ((sepIndex = source.indexOf(separator, i)) >= 0) {
            String token = source.substring(i, sepIndex);
            builder.add(AnyConverter.convert(token, componentType));
            i = sepIndex + separator.length();
        }
        builder.add(AnyConverter.convert(source.substring(i, source.length()), componentType));
        return builder.toArray();
    }

}
