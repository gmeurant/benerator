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

/**
 * Formats a Composite as a String.<br/><br/>
 * Created: 14.03.2008 22:47:57
 * @author Volker Bergmann
 */
public class CompositeFormatter {
    
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private static final String INDENT_DELTA = "    ";
    
    // interface -------------------------------------------------------------------------------------------------------

    public static <T> String render(String head, Composite composite, String tail, boolean flat, boolean renderNames) {
        if (flat)
            return renderFlat(head, composite, tail, renderNames);
        else
            return renderHierarchical(head, composite, tail, renderNames);
    }
    
    public static String renderHierarchical(String head, Composite composite, String tail, String indent, boolean renderNames) {
        return head + renderComponentsHierarchical(composite, indent, renderNames) + tail;
    }
    
    // private helpers -------------------------------------------------------------------------------------------------
    
    private static String renderFlat(String head, Composite composite, String tail, boolean renderNames) {
        return head + renderComponentsFlat(composite, renderNames) + tail;
    }
    
    private static String renderHierarchical(String head, Composite composite, String tail, boolean renderNames) {
        return renderHierarchical(head, composite, tail, "", renderNames);
    }
    
    private static <T> String renderComponentsFlat(Composite composite, boolean renderNames) {
        StringBuilder builder = new StringBuilder();
        Map<String, T> components = composite.getComponents();
        Iterator<Map.Entry<String, T>> iterator = components.entrySet().iterator();
        if (iterator.hasNext())
            renderComponent(builder, true, "", renderNames, iterator.next());
        while (iterator.hasNext()) {
            builder.append(", ");
            renderComponent(builder, true, "", renderNames, iterator.next());
        }
        return builder.toString();
    }

    private static <T> String renderComponentsHierarchical(Composite composite, String indent, boolean renderNames) {
        StringBuilder builder = new StringBuilder();
        indent += INDENT_DELTA;
        Map<String, T> components = composite.getComponents();
        Iterator<Map.Entry<String, T>> iterator = components.entrySet().iterator();
        while (iterator.hasNext()) {
            builder.append(SystemInfo.lineSeparator());
            renderComponent(builder, false, indent, renderNames, iterator.next());
        }
        indent = indent.substring(0, indent.length() - INDENT_DELTA.length());
        if (builder.length() > 1)
            builder.append(SystemInfo.lineSeparator());
        return builder.toString();
    }

    private static void renderComponent(StringBuilder builder, boolean flat, String indent, boolean renderName, Map.Entry<String, ? extends Object> component) {
        builder.append(indent);
        if (renderName)
            builder.append(component.getKey()).append('=');
        Object value = component.getValue();
        if (value == null) {
            value = "[null]";
        } else if (value instanceof Date) {
            Date date = (Date) value;
            TimeZone timeZone = TimeZone.getDefault();
            long timeInMillis = date.getTime();
            timeInMillis += timeZone.getRawOffset();
            if (timeInMillis % 86400000L == 0L)
                value = DATE_FORMAT.format((Date) value);
            else
                value = DATETIME_FORMAT.format((Date) value);
        } else if (value.getClass().isArray()) {
            value = ArrayFormat.format(", ", (Object[]) value);
        } else if (value instanceof Composite) {
            value = render("[", (Composite) value, "]", flat, renderName) ;
        }
        builder.append(value);
    }


}
