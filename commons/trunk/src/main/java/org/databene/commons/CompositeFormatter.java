/*
 * (c) Copyright 2006 by Volker Bergmann. All rights reserved.
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import org.databene.commons.converter.ToStringConverter;

/**
 * Formats a Composite as a String.<br/><br/>
 * Created: 14.03.2008 22:47:57
 * @author Volker Bergmann
 */
public class CompositeFormatter {
    
    private static final String DEFAULT_TIMESTAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
	private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

	private static final String INDENT_DELTA = "    ";
    
	boolean flat;
	boolean renderNames;
    private String datePattern;
    private String timestampPattern;
    private String nullValue;

    public CompositeFormatter() {
    	this(true, true);
    }
    
    public CompositeFormatter(boolean flat, boolean renderNames) {
    	this(flat, renderNames, DEFAULT_DATE_PATTERN, DEFAULT_TIMESTAMP_PATTERN);
    }
    
    public CompositeFormatter(boolean flat, boolean renderNames, String datePattern, String timestampPattern) {
    	this.flat = flat;
    	this.renderNames = renderNames;
    	this.datePattern = datePattern;
    	this.timestampPattern = timestampPattern;
    	this.nullValue = "[null]";
    }
    
    // interface -------------------------------------------------------------------------------------------------------

   	public <T> String render(String head, Composite<T> composite, String tail) {
        if (flat)
            return renderFlat(head, composite, tail);
        else
            return renderHierarchical(head, composite, tail);
    }
    
    public <T> String renderHierarchical(String head, Composite<T> composite, String tail, String indent) {
        return head + renderComponentsHierarchical(composite, indent) + tail;
    }
    
    public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public String getTimestampPattern() {
		return timestampPattern;
	}

	public void setTimestampPattern(String timestampPattern) {
		this.timestampPattern = timestampPattern;
	}

    // private helpers -------------------------------------------------------------------------------------------------
    
	private <T> String renderFlat(String head, Composite<T> composite, String tail) {
        return head + renderComponentsFlat(composite) + tail;
    }
    
    private <T> String renderHierarchical(String head, Composite<T> composite, String tail) {
        return renderHierarchical(head, composite, tail, "");
    }
    
    private <T> String renderComponentsFlat(Composite<T> composite) {
        StringBuilder builder = new StringBuilder();
        Map<String, T> components = composite.getComponents();
        Iterator<Map.Entry<String, T>> iterator = components.entrySet().iterator();
        if (iterator.hasNext())
            renderComponent(builder, "", iterator.next());
        while (iterator.hasNext()) {
            builder.append(", ");
            renderComponent(builder, "", iterator.next());
        }
        return builder.toString();
    }

    private <T> String renderComponentsHierarchical(Composite<T> composite, String indent) {
        StringBuilder builder = new StringBuilder();
        indent += INDENT_DELTA;
        Map<String, T> components = composite.getComponents();
        Iterator<Map.Entry<String, T>> iterator = components.entrySet().iterator();
        while (iterator.hasNext()) {
            builder.append(SystemInfo.lineSeparator());
            renderComponent(builder, indent, iterator.next());
        }
        indent = indent.substring(0, indent.length() - INDENT_DELTA.length());
        if (builder.length() > 1)
            builder.append(SystemInfo.lineSeparator());
        return builder.toString();
    }

    void renderComponent(StringBuilder builder, String indent, Map.Entry<String, ? extends Object> component) {
        builder.append(indent);
        if (renderNames)
            builder.append(component.getKey()).append('=');
        Object value = component.getValue();
        if (value == null) {
            value = nullValue;
        } else if (value instanceof Date) {
            Date date = (Date) value;
            TimeZone timeZone = TimeZone.getDefault();
            long timeInMillis = date.getTime();
            timeInMillis += timeZone.getRawOffset();
            if (timeInMillis % 86400000L == 0L)
                value = new SimpleDateFormat(datePattern).format((Date) value);
            else
                value = new SimpleDateFormat(timestampPattern).format((Date) value);
        } else if (value instanceof Composite) {
            value = render("[", (Composite) value, "]") ;
        } else {
            value = ToStringConverter.convert(value, "null");
        }
        builder.append(value);
    }

}
